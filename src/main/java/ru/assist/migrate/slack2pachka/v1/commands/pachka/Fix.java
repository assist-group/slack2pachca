package ru.assist.migrate.slack2pachka.v1.commands.pachka;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.shell.command.CommandRegistration;
import org.springframework.shell.command.annotation.Command;
import org.springframework.shell.command.annotation.Option;
import ru.assist.migrate.slack2pachka.v1.commands.pachka.common.Handler;
import ru.assist.migrate.slack2pachka.v1.commands.pachka.common.PachkaData;
import ru.assist.migrate.slack2pachka.v1.commands.test.utils.SlackUtils;
import ru.assist.migrate.slack2pachka.v1.model.channels.Channel;
import ru.assist.migrate.slack2pachka.slack.alternate.AlternateChannel;
import ru.assist.migrate.slack2pachka.slack.message.SlackMessage;
import ru.assist.migrate.slack2pachka.v1.model.users.User;
import ru.assist.migrate.slack2pachka.v1.service.channel.ChannelService;
import ru.assist.migrate.slack2pachka.v1.service.message.MessageService;
import ru.assist.migrate.slack2pachka.v1.service.user.UserService;

import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;

import static ru.assist.migrate.slack2pachka.v1.commands.pachka.common.PachkaData.getCreatedAsLocalDateTime;
import static ru.assist.migrate.slack2pachka.v1.commands.test.utils.SlackUtils.getBackupFolders;
import static ru.assist.migrate.slack2pachka.v1.commands.test.utils.SlackUtils.getMessageFolders;

@Slf4j
@Command(command = "fix", description = "fix imported data utilities", group = "fix")
@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Fix {
    final PachkaData pachkaData;
    final UserService userService;
    final MessageService messageService;
    final ChannelService channelService;

    @Value("${slack.data.root.folder}")
    private String slackFolder;

    @Autowired
    public Fix(PachkaData pachkaData, UserService userService, MessageService messageService, ChannelService channelService) {
        this.pachkaData = pachkaData;
        this.userService = userService;
        this.messageService = messageService;
        this.channelService = channelService;
    }

    private Map<String, SlackMessage> time2SlackMessage(Path dir) {
        Map<String, SlackMessage> result = new HashMap<>();
        if (!dir.toFile().exists()) return result;

        Path[] files = SlackUtils.getMessageFiles(dir);
        for (Path file : files) {
            SlackMessage[] slackMessages = SlackUtils.getSlackMessages(file);
            for (SlackMessage message : slackMessages) {
                if (message.getTs() == null || message.getTs().isEmpty()) continue;
                LocalDateTime time = getCreatedAsLocalDateTime(message);
                time = time.truncatedTo(ChronoUnit.SECONDS);
                String ts = time.format(DateTimeFormatter.ISO_DATE_TIME);
                result.put(ts, message);
            }
        }
        return result;
    }

    @Value("${slack.data.alternate.root.folder}")
    private String altRootFolder;

    @Value("${slack.data.alternate.channels}")
    private Set<String> toImport;

    @Command(command = "threads", description = "fix message threads")
    public void threads() {
        List<AlternateChannel> altChannels = Import.setPackaData(altRootFolder, toImport, pachkaData);
        Handler handler = new Handler(false, false, true);
        handler.setPachkaData(pachkaData);
        handler.setMessageService(messageService);

        Channel[] currentChannels = channelService.list();
        for (AlternateChannel alternateChannel : altChannels) {
            for (Channel channel : currentChannels) {
                if (!channel.getName().equals(alternateChannel.getName())) continue;
                long chatId = channel.getId();
                SlackMessage[] slackMessages = alternateChannel.getMessages();
                if (slackMessages == null || slackMessages.length == 0) continue;
                Map<String, SlackMessage> ts2SlackMessage = ts2SlackMessage(alternateChannel.getMessages());
                handler.setTs2SlackMessage(ts2SlackMessage);
                messageService.list(chatId, ts2SlackMessage.size(), handler);
            }
        }
    }

    private Map<String, SlackMessage> ts2SlackMessage(SlackMessage[] messages) {
        Map<String, SlackMessage> result = new HashMap<>();
        for (SlackMessage message : messages) {
            result.put(message.getTs(), message);
        }
        return result;
    }

//    @Command(command = "messages", description = "fix messages")
    public void messages(
            @Option(
                    arity = CommandRegistration.OptionArity.EXACTLY_ONE,
                    longNames = "all",
                    shortNames = 'a',
                    description = "fix all - content, attachments and threads",
                    defaultValue = "false"
            ) boolean all,
            @Option(
                    arity = CommandRegistration.OptionArity.EXACTLY_ONE,
                    longNames = "content",
                    shortNames = 'c',
                    description = "fix content",
                    defaultValue = "false"
            ) boolean content,
            @Option(
                    arity = CommandRegistration.OptionArity.EXACTLY_ONE,
                    longNames = "files",
                    shortNames = 'f',
                    description = "fix attachments",
                    defaultValue = "false"
            ) boolean files,
            @Option(
                    arity = CommandRegistration.OptionArity.EXACTLY_ONE,
                    longNames = "threads",
                    shortNames = 't',
                    description = "fix threads",
                    defaultValue = "false"
            ) boolean threads
    ) {
        Handler handler;
        if (all) {
            handler = new Handler();
        } else {
            handler = new Handler(content, files, threads);
        }


    }

    @Command(command = "attachments", description = "fix message attachments")
    public void attachments() {
        Path[] backups = getBackupFolders(slackFolder);
        for (Path backup : backups) {
            pachkaData.set(backup);
            Set<String> folderSet = getMessageFolderSet(backup);
            log.info("total channel count {}", folderSet.size());
            Channel[] channels = channelService.list();
            for (Channel channel : channels) {
                String channelName = channel.getName();
                if (!folderSet.contains(channelName)) continue;
                long chatId = channel.getId();
                Map<String, SlackMessage> time2SlackMessage = time2SlackMessage(
                        Path.of(slackFolder, backup.toFile().getName(), channelName));
                log.info("{} : {}", channelName, time2SlackMessage.size());
                Handler handler = new Handler();
                messageService.list(chatId, time2SlackMessage.size(), handler);
            }
        }
    }

    private Set<String> getMessageFolderSet(Path backup) {
        Set<String> result = new HashSet<>();
        Path[] folders = getMessageFolders(backup);
        for (Path folder : folders) {
            result.add(folder.toFile().getName());
        }
        return result;
    }

    @Command(command = "user", description = "change user data")
    public void user(
            @Option(required = true,
                    arity = CommandRegistration.OptionArity.EXACTLY_ONE,
                    longNames = "current-email",
                    shortNames = 'c',
                    description = "user current email"
            ) String currentEmail,
            @Option(
                    arity = CommandRegistration.OptionArity.EXACTLY_ONE,
                    longNames = "email",
                    shortNames = 'e',
                    description = "email"
            ) String email,
            @Option(
                    arity = CommandRegistration.OptionArity.EXACTLY_ONE,
                    longNames = "role",
                    shortNames = 'r',
                    description = "role"
            ) String role,
            @Option(
                    arity = CommandRegistration.OptionArity.EXACTLY_ONE,
                    longNames = "phone_number",
                    shortNames = 'p',
                    description = "phone_number"
            ) String phone_number,
            @Option(
                    arity = CommandRegistration.OptionArity.EXACTLY_ONE,
                    longNames = "first_name",
                    shortNames = 'f',
                    description = "first_name"
            ) String first_name,
            @Option(
                    arity = CommandRegistration.OptionArity.EXACTLY_ONE,
                    longNames = "last_name",
                    shortNames = 'l',
                    description = "last_name"
            ) String last_name,
            @Option(
                    arity = CommandRegistration.OptionArity.EXACTLY_ONE,
                    longNames = "nickname",
                    shortNames = 'n',
                    description = "nickname"
            ) String nickname,
            @Option(
                    arity = CommandRegistration.OptionArity.EXACTLY_ONE,
                    longNames = "suspended",
                    shortNames = 's',
                    description = "suspended",
                    defaultValue = "false"
            ) boolean suspended
    ) {
        User[] users = userService.list();
        for (User user : users) {
            if (!user.getEmail().equals(currentEmail)) continue;
            User.UserBuilder userBuilder = Create.getUserBuilder(
                    email, role, phone_number, first_name, last_name, nickname, suspended);

            User.Request request = User.Request.builder()
                    .user(userBuilder.build())
                    .skip_email_notify(true)
                    .build();
            userService.update(request, user.getId());
        }
    }
}
