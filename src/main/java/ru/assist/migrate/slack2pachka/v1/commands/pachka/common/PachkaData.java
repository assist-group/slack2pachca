package ru.assist.migrate.slack2pachka.v1.commands.pachka.common;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.assist.migrate.slack2pachka.ImportRuntimeException;
import ru.assist.migrate.slack2pachka.slack.alternate.AlternateChannel;
import ru.assist.migrate.slack2pachka.slack.channel.SlackChannel;
import ru.assist.migrate.slack2pachka.slack.message.File;
import ru.assist.migrate.slack2pachka.slack.message.SlackMessage;
import ru.assist.migrate.slack2pachka.slack.user.Profile;
import ru.assist.migrate.slack2pachka.slack.user.SlackUser;
import ru.assist.migrate.slack2pachka.v1.commands.test.utils.SlackUtils;
import ru.assist.migrate.slack2pachka.v1.model.channels.Channel;
import ru.assist.migrate.slack2pachka.v1.model.messages.FileMeta;
import ru.assist.migrate.slack2pachka.v1.model.messages.Message;
import ru.assist.migrate.slack2pachka.v1.model.messages.MessageFile;
import ru.assist.migrate.slack2pachka.v1.model.messages.Thread;
import ru.assist.migrate.slack2pachka.v1.model.users.User;
import ru.assist.migrate.slack2pachka.v1.service.channel.ChannelService;
import ru.assist.migrate.slack2pachka.v1.service.message.FileService;
import ru.assist.migrate.slack2pachka.v1.service.message.MessageService;
import ru.assist.migrate.slack2pachka.v1.service.message.ThreadService;
import ru.assist.migrate.slack2pachka.v1.service.user.UserService;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PachkaData {
    final UserService userService;
    final ChannelService channelService;
    final MessageService messageService;
    final FileService fileService;
    final ThreadService threadService;
    final SlackData slackData;

    @Value("${users.create.skip_email_notify}")
    boolean skip_email_notify;

    final Map<String, User> emailUserMap = new HashMap<>();

    final List<User> users = new ArrayList<>();
    final List<Channel> channels = new ArrayList<>();
    final Map<String, Long> channelName2Id = new HashMap<>();

    MessageContent messageContent;

    @Autowired
    public PachkaData(UserService userService, ChannelService channelService, MessageService messageService, FileService fileService, ThreadService threadService, SlackData slackData) {
        this.userService = userService;
        this.channelService = channelService;
        this.messageService = messageService;
        this.fileService = fileService;
        this.threadService = threadService;
        this.slackData = slackData;
    }

    public void set(Path dataPath) {
        clearAll();
        slackData.set(dataPath);
        messageContent = new MessageContent(slackData);
    }

    public void set(Path files, Path users, SlackChannel[] channels) {
        clearAll();
        slackData.set(files, users, channels);
        messageContent = new MessageContent(slackData);
    }

    public void set(Path[] files, Path[] users, SlackChannel[] channels) {
        clearAll();
        slackData.set(files, users, channels);
        messageContent = new MessageContent(slackData);
    }

    private void clearAll() {
        emailUserMap.clear();
        users.clear();
        channels.clear();
        channelName2Id.clear();
    }

    private Long getChannelId(String name) {
        return channelName2Id.get(name);
    }

    public void syncUsers() {
        log.info("user list loading... ");
        User[] currentUsers = userService.list();
        users.addAll(List.of(currentUsers));
        log.info("user list loaded");

        for (User user : users) {
            emailUserMap.put(user.getEmail(), user);
        }

        Map<String, SlackUser> slackUsers = slackData.getUsers();
        for (SlackUser slackUser : slackUsers.values()) {
            String slackUserEmail = slackUser.getProfile().getEmail();
            if (slackUserEmail == null) continue;
            if (emailUserMap.containsKey(slackUserEmail)) continue;
            addNewUser(slackUser);
        }
    }

    private void addNewUser(SlackUser slackUser) {
        if (slackUser.isDeleted()) {
            return;
        }
        if (slackUser.is_bot()) {
            return;
        }
        if (slackUser.is_app_user()) {
            return;
        }
        if (slackUser.is_restricted()) {
            return;
        }
        if (slackUser.is_ultra_restricted()) {
            return;
        }

        Profile profile = slackUser.getProfile();
        if (profile.getEmail() == null || profile.getEmail().isEmpty()) {
            return;
        }

        String fullName = slackUser.getProfile().getReal_name();
        String nickname = slackUser.getName() == null || slackUser.getName().isEmpty() ? profile.getEmail().split("@")[0] : slackUser.getName();
        String role = slackUser.is_admin() ? "admin" : "user";
        String email = profile.getEmail();
        String phone_number = profile.getPhone();

        String[] name = fullName.split(" ");
        String first_name = name.length > 0 ? name[0] : null;
        String last_name = name.length > 1 ? name[1] : null;
        User.Request request = User.Request.builder()
                .skip_email_notify(skip_email_notify)
                .user(User.builder()
                        .first_name(first_name)
                        .last_name(last_name)
                        .email(email)
                        .bot(false)
                        .phone_number(phone_number)
                        .nickname(nickname)
                        .role(role)
                        .suspended(false)
                        .build())
                .build();
        User user = userService.create(request);
        users.add(user);
        emailUserMap.put(user.getEmail(), user);
        log.info("+{} : {} : {}", email, fullName, nickname);
    }

    public void syncChannels() {
        loadChannels();

        Map<String, Channel> nameChannelMap = new HashMap<>();
        for (Channel channel : channels) {
            nameChannelMap.put(channel.getName(), channel);
        }

        SlackChannel[] slackChannels = slackData.getChannelsArray();
        for (SlackChannel slackChannel : slackChannels) {
            String channelName = slackChannel.getName();
            if (nameChannelMap.containsKey(channelName)) continue;
            addNewChannel(slackChannel);
            log.info("+{}", channelName);
        }

        for (Channel channel : channels) {
            String channelName = channel.getName();
            channelName2Id.put(channelName, channel.getId());
        }
    }

    private void loadChannels() {
        log.info("channel list loading... ");
        Channel[] currentChannels = channelService.list();
        for (Channel channel : currentChannels) {
            if (channel.isPublic()) channels.add(channel);
        }
        log.info("channel list loaded");
    }

    private void addNewChannel(SlackChannel slackChannel) {
        Channel.Request request = Channel.Request.builder()
                .chat(Channel.builder()
                        .isPublic(true)
                        .channel(false)
                        .name(slackChannel.getName())
                        .build())
                .build();

        Channel channel = channelService.create(request);
        channels.add(channel);
    }

    public void syncChannelMembers() {
        loadChannels();

        log.info("channel member list modification started");
        Map<String, SlackChannel> slackChannelMap = slackData.getName2Channel();
        for (Channel channel : channels) {
            String channelName = channel.getName();
            SlackChannel slackChannel = slackChannelMap.get(channelName);
            if (slackChannel == null) continue;

            // merge members
            long[] currentMemberIds = channel.getMember_ids();
            long[] memberIds = slackUserIds2UserIds(slackChannel.getMembers());
            long[] merged = mergeArrays(currentMemberIds, memberIds);

            if (memberIds.length == 0) continue;

            Channel.Members channelMembers = Channel.Members.builder()
                    .silent(true)
                    .member_ids(merged)
                    .build();

            channelService.addMembers(channel.getId(), channelMembers);
            log.info("+{}", channelName);
        }
        log.info("channel member list modification completed");
    }

    private long[] mergeArrays(long[] first, long[] second) {
        Set<Long> merged = new HashSet<>();
        merged.addAll(Arrays.stream(first).boxed().toList());
        merged.addAll(Arrays.stream(second).boxed().toList());
        return merged.stream().mapToLong(Number::longValue).toArray();
    }

    private long[] slackUserIds2UserIds(String[] slackUserIds) {
        List<Long> userIds = new ArrayList<>();
        Map<String, SlackUser> id2SlackUser = slackData.getUsers();
        for (String slackUserId : slackUserIds) {
            SlackUser slackUser = id2SlackUser.get(slackUserId);
            String email = slackUser.getProfile().getEmail();
            if (email == null || email.isEmpty()) continue;

            User user = emailUserMap.get(email);
            if (user != null) {
                userIds.add(user.getId());
            }
        }
        return toPrimitives(userIds.toArray(new Long[0]));
    }

    private static long[] toPrimitives(Long... objects) {

        long[] primitives = new long[objects.length];
        for (int i = 0; i < objects.length; i++)
            primitives[i] = objects[i];

        return primitives;
    }

    @Value("${executor.thread.pool.size}")
    private int threadPoolSize;

    public void syncMessages() {
        Path root = slackData.getDataFolderPath();
        Path[] dirs = SlackUtils.getMessageFolders(root);

        List<Callable<String>> tasks = new ArrayList<>();
        for (Path dir : dirs) {
            Callable<String> task = () -> runImport(dir);
            tasks.add(task);
        }
        processTasks(tasks);
    }

    public void syncMessages(List<AlternateChannel> channels) {

        List<Callable<String>> tasks = new ArrayList<>();
        for (AlternateChannel channel : channels) {
            Callable<String> task = () -> runImport(channel);
            tasks.add(task);
        }
        processTasks(tasks);
    }

    private void processTasks(List<Callable<String>> tasks) {
        log.info("INFO - added {} tasks to execute, {}", tasks.size(), Instant.now());

        final ExecutorService executor = Executors.newFixedThreadPool(threadPoolSize);
        List<Future<String>> futures;
        try {
            futures = executor.invokeAll(tasks);
        } catch (InterruptedException e) {
            java.lang.Thread.currentThread().interrupt();
            throw new ImportRuntimeException(e);
        }

        log.info("INFO - await termination {}", Instant.now());
        executor.shutdown();
        try {
            boolean terminationComleted = executor.awaitTermination(7, TimeUnit.DAYS);
            if (!terminationComleted)
                log.error("ERROR - Submitted tasks not completed before time-out. {}", Instant.now());
        } catch (InterruptedException e) {
            throw new ImportRuntimeException(e);
        }


        log.info("INFO - all submitted tasks are done, and the executor service has ended.. {}", Instant.now());
        for (Future<String> future : futures) {
            if (future.isCancelled()) {
                log.warn("Oops, task was cancelled. ");
            } else {
                try {
                    log.info("completed : {}", future.get());
                } catch (InterruptedException | ExecutionException e) {
                    throw new ImportRuntimeException(e);
                }
            }
        }
    }

    private String runImport(AlternateChannel channel) {
        String name = channel.getName();

        if (channelName2Id.isEmpty()) return String.format("%s : %s", name, "channel list is empty");
        Long channelId = getChannelId(name);
        if (channelId == null) return String.format("channel '%s' not found", name);

        SlackMessage[] messages = channel.getMessages();
        if (messages == null) return String.format("messages from channel '%s' not found", name);

        return processMessages(name, channelId, Arrays.asList(messages));
    }

    private String runImport(Path dir) {
        String name = dir.toFile().getName();

        if (channelName2Id.isEmpty()) return String.format("%s : %s", name, "channel list is empty");
        Long channelId = getChannelId(name);
        if (channelId == null) return String.format("channel '%s' not found", name);

        Path[] files = SlackUtils.getMessageFiles(dir);
        List<SlackMessage> messages = new ArrayList<>();
        for (Path file : files) {
            SlackMessage[] slackMessages = SlackUtils.getSlackMessages(file);
            messages.addAll(Arrays.asList(slackMessages));
        }
        return processMessages(name, channelId, messages);
    }

    private String processMessages(String name, long channelId, List<SlackMessage> messages) {
        log.info("{} : {}", name, messages.size());

        final Map<String, SlackMessage> ts2SlackMessage = new HashMap<>();
        for (SlackMessage slackMessage : messages) {
            ts2SlackMessage.put(slackMessage.getTs(), slackMessage);
        }

        messages = messages.stream()
                .filter(p -> (p.getTs() != null && p.getText() != null))
                .collect(Collectors.toList());

        messages.sort((lhs, rhs) -> {
            LocalDateTime lhsTime = getCreatedAsLocalDateTime(lhs);
            LocalDateTime rhsTime = getCreatedAsLocalDateTime(rhs);
            // -1 - less than, 1 - greater than, 0 - equal
            return lhsTime.isBefore(rhsTime) ? -1 : (lhsTime.isAfter(rhsTime)) ? 1 : 0;
        });

        if (!channelName2Id.containsKey(name)) {
            if (channelName2Id.isEmpty()) return String.format("%s : %s", name, "channel does not exist");
        }

        Channel channel = channelService.read(channelId);
        LocalDateTime lastMessageTime = LocalDateTime.parse(channel.getLast_message_at(), DateTimeFormatter.ISO_DATE_TIME);
        boolean isEmpty = messageService.isEmpty(channelId);

        messages = messages.stream()
                .filter(p -> {
                    if (isEmpty) return true;
                    LocalDateTime time = getCreatedAsLocalDateTime(p);
                    time = time.truncatedTo(ChronoUnit.SECONDS);
                    return time.isAfter(lastMessageTime);
                })
                .collect(Collectors.toList());

        Map<String, Thread> threads = new HashMap<>();
        for (SlackMessage slackMessage : messages) {
            if (java.lang.Thread.currentThread().isInterrupted()) java.lang.Thread.currentThread().interrupt();
            processMessage(name, channelId, threads, slackMessage);
        }

        log.info("done : {}", name);
        return String.format("%s : %d", name, messages.size());

    }

    private void processMessage(String name, long channelId, Map<String, Thread> threads, SlackMessage slackMessage) {
        Message response = toMessage("discussion", getChannelId(name), slackMessage);
        if (response == null) return;
        Long messageId = response.getId();

        if (slackMessage.getReply_count() > 0) {
            Thread thread = threadService.create(messageId);
            threads.put(slackMessage.getThread_ts(), thread);
        }
        String threadTs = slackMessage.getThread_ts();
        if (threadTs != null && threads.containsKey(threadTs)) {
            Thread thread = threads.get(threadTs);
            toMessage("thread", thread.getId(), slackMessage);
        }
        SlackMessage[] slackdump_thread_replies = slackMessage.getSlackdump_thread_replies();
        if (slackdump_thread_replies != null && slackdump_thread_replies.length > 0) {
            Thread thread = threads.get(threadTs);
            for (SlackMessage threadMessage : slackdump_thread_replies) {
                toMessage("thread", thread.getId(), threadMessage);
            }
        }
    }

    public Message toMessage(String entity, long id, SlackMessage slackMessage) {
        String thread_ts = slackMessage.getThread_ts();
        if (thread_ts != null && "discussion".equals(entity)
                && (!slackMessage.getTs().equals(thread_ts))
        ) return null;

        if (thread_ts != null && "thread".equals(entity)
                && slackMessage.getTs().equals(slackMessage.getThread_ts())
        ) return null;

        MessageFile[] files = toMessageFiles(slackMessage);
        String content = toMessageContent(slackMessage);
        String created = SlackData.getCreated(slackMessage);
        Message message = Message.builder()
                .content(content)
                .created_at(created)
                .files(files)
                .entity_type(entity)
                .entity_id(id)
                .build();

        Message.Request request = Message.Request.builder()
                .message(message)
                .build();

        return messageService.create(request);
    }

    public String toMessageContent(SlackMessage slackMessage) {
        String result = messageContent.getContentExtended(slackMessage);
        if (result == null || result.trim().isEmpty()) {
            throw new ImportRuntimeException("message content is null or empty");
        }
        return result;
    }

    public MessageFile[] toMessageFiles(SlackMessage slackMessage) {
        List<MessageFile> list = new ArrayList<>();
        File[] slackMessageFiles = slackMessage.getFiles();
        if (slackMessageFiles == null) return list.toArray(new MessageFile[0]);
        for (File slackFile : slackMessageFiles) {
            if ("tombstone".equals(slackFile.getMode())) continue;
            if (slackFile.getName() == null) continue;
            String filename = slackFile.getName();
            String key = createAttachment(filename, slackFile);
            String filetype = detectFileType(slackFile);
            MessageFile mFile = MessageFile.builder()
                    .file_type(filetype)
                    .name(filename)
                    .size(slackFile.getSize())
                    .key(key)
                    .build();
            list.add(mFile);
        }
        return list.toArray(new MessageFile[0]);
    }

    @Value("${tmp.dir}")
    String tmpDirName;

    private String createAttachment(String filename, File slackFile) {
        Map<String, Path> filesMap = slackData.getFiles();

        if (filesMap == null) return createFakeAttachment();
        Path path = filesMap.get(slackFile.getId());
        if (path == null) return createFakeAttachment();

        Path tmpdir;
        Path copied;
        try {
            tmpdir = Files.createTempDirectory(Path.of(tmpDirName), "slack2pachka");
            tmpdir.toFile().deleteOnExit();
            copied = tmpdir.resolve(filename);
            Files.copy(path, copied, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new ImportRuntimeException(e);
        }

        FileMeta fileMeta = fileService.upload(copied.toFile());
        try {
            Files.delete(copied);
            Files.delete(tmpdir);
        } catch (IOException e) {
            throw new ImportRuntimeException(e);
        }

        String key = fileMeta.getKey() == null ? "${filename}" : fileMeta.getKey();

        return key.replace("${filename}", copied.toFile().getName());
    }

    private String createFakeAttachment() {
        ClassLoader classloader = java.lang.Thread.currentThread().getContextClassLoader();
        InputStream is = classloader.getResourceAsStream("empty.file");

        try {
            Path tempDirWithPrefix = Files.createTempDirectory("test");
            java.io.File file = new java.io.File(tempDirWithPrefix.toFile(), "empty.file");
            Files.copy(Objects.requireNonNull(is), file.toPath(), StandardCopyOption.REPLACE_EXISTING);
            FileMeta fileMeta = fileService.upload(file);
            String key = fileMeta.getKey() == null ? "${filename}" : fileMeta.getKey();
            return key.replace("${filename}", file.getName());

        } catch (IOException e) {
            throw new ImportRuntimeException(e);
        }
    }

    private static String detectFileType(File slackFile) {
        String mimetype = slackFile.getMimetype();
        if (mimetype == null) {
            return "file";
        }

        if (mimetype.startsWith("image/")) {
            return "image";
        }
        return "file";
    }

    public static LocalDateTime getCreatedAsLocalDateTime(SlackMessage slackMessage) {
        String ts = slackMessage.getTs();
        double time = Double.parseDouble(ts) * 1000.0;
        long longValue = (long) time;

        return Instant
                .ofEpochMilli(longValue)
                .atOffset(ZoneOffset.UTC)
                .toLocalDateTime();
    }
}
