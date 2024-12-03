package ru.assist.migrate.slack2pachka.v1.commands.slack;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.shell.command.CommandRegistration;
import org.springframework.shell.command.annotation.Command;
import org.springframework.shell.command.annotation.Option;
import ru.assist.migrate.slack2pachka.ImportRuntimeException;
import ru.assist.migrate.slack2pachka.v1.commands.pachka.common.AggregateObject;
import ru.assist.migrate.slack2pachka.slack.channel.SlackChannel;
import ru.assist.migrate.slack2pachka.slack.user.SlackUser;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static ru.assist.migrate.slack2pachka.v1.commands.test.utils.SlackUtils.*;

@Slf4j
@Command(command = "analyze", description = "Slack import utilities", group = "analyze")
@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Analyze {
    @Value("${slack.data.alternate.root.folder}")
    String altRootFolder;
    @Value("${slack.data.alternate.root.file}")
    String rootFile;
    @Value("${slack.data.root.folder}")
    String slackFolder;

    AggregateObject root;

    @Autowired
    public Analyze() {
    }

    @Command(command = "messages", description = "aggregate slack messages to get compact field representation")
    public void analyze(
            @Option(required = true,
                    arity = CommandRegistration.OptionArity.EXACTLY_ONE,
                    longNames = "fields",
                    shortNames = 'f',
                    description = "build all message fields map",
                    defaultValue = "true"
            ) boolean fields
    ) {
        if (fields) {
            root = new AggregateObject();
            Path[] backups = getBackupFolders(slackFolder);
            for (Path backup : backups) {
                Path[] folders = getMessageFolders(backup);
                for (Path path : folders) {
                    Path[] files = getMessageFiles(path);
                    for (Path file : files) {
                        readFile(file);
                    }
                }
            }

            String text = root.toString();
            try (PrintWriter out = new PrintWriter("aggregate.txt")) {
                out.print(text);
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void readFile(Path path) {
        ObjectMapper mapper = new ObjectMapper();

        try {
            JsonNode jsonNode = mapper.readTree(path.toFile());
            root.put("", jsonNode);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    private static final ObjectMapper MAPPER = getObjectMapper();

    private static ObjectMapper getObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.configure(DeserializationFeature.USE_JAVA_ARRAY_FOR_JSON_ARRAY, true);
        return objectMapper;
    }

    @Command(command = "owners", description = "dump slack channel owners list")
    public void owners() {
        StringBuilder sb = new StringBuilder();
        Path[] backups = getBackupFolders(slackFolder);
        for (Path backup : backups) {
            Map<String, SlackUser> users = loadUsers(backup);
            sb.append(backup.toFile().getName()).append("\n");
            try {
                Path channelsDataPath = backup.resolve("channels.json");
                SlackChannel[] slackChannels = MAPPER.readValue(channelsDataPath.toFile(), SlackChannel[].class);
                for (SlackChannel slackChannel : slackChannels) {
                    String creatorId = slackChannel.getCreator();
                    SlackUser slackUser = users.get(creatorId);
                    String nick = slackUser.getProfile().getDisplay_name();
                    sb.append(nick).append(" : ")
                            .append(slackChannel.getName()).append("\n");
                }
            } catch (IOException e) {
                throw new ImportRuntimeException(e);
            }
            sb.append("\n");
        }

        try (PrintWriter out = new PrintWriter("owners.txt")) {
            out.print(sb);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Command(command = "archived", description = "dump archived slack channels, with owners")
    public void archived() {
        StringBuilder sb = new StringBuilder();
        Path[] backups = getBackupFolders(slackFolder);
        for (Path backup : backups) {
            Map<String, SlackUser> users = loadUsers(backup);
            sb.append(backup.toFile().getName()).append("\n");
            try {
                Path channelsDataPath = backup.resolve("channels.json");
                SlackChannel[] slackChannels = MAPPER.readValue(channelsDataPath.toFile(), SlackChannel[].class);
                for (SlackChannel slackChannel : slackChannels) {
                    String creatorId = slackChannel.getCreator();
                    SlackUser slackUser = users.get(creatorId);
                    if (slackChannel.is_archived()) {
                        String nick = slackUser.getProfile().getDisplay_name();
                        sb.append(nick).append(" : ")
                                .append(slackChannel.getName()).append("\n");
                    }
                }
            } catch (IOException e) {
                throw new ImportRuntimeException(e);
            }
            sb.append("\n");
        }

        try (PrintWriter out = new PrintWriter("archived.txt")) {
            out.print(sb);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private Map<String, SlackUser> loadUsers(Path backup) {
        Map<String, SlackUser> result = new HashMap<>();
        try {
            Path usersDataPath = backup.resolve("users.json");
            SlackUser[] users = MAPPER.readValue(usersDataPath.toFile(), SlackUser[].class);
            for (SlackUser slackUser : users) {
                result.put(slackUser.getId(), slackUser);
            }
        } catch (IOException e) {
            throw new ImportRuntimeException(e);
        }
        return result;
    }

    private Map<String, SlackChannel> loadChannels(Path backup) {
        Map<String, SlackChannel> result = new HashMap<>();
        try {
            Path channelsDataPath = backup.resolve("channels.json");
            SlackChannel[] channels = MAPPER.readValue(channelsDataPath.toFile(), SlackChannel[].class);
            for (SlackChannel slackChannel : channels) {
                result.put(slackChannel.getId(), slackChannel);
            }
        } catch (IOException e) {
            throw new ImportRuntimeException(e);
        }
        return result;
    }

    @Command(command = "alt", description = "dump archived slack channels, with owners")
    public void alternate() {
        Map<String, SlackChannel> channels = new HashMap<>();
        Path[] backups = getBackupFolders(slackFolder);
        for (Path backup : backups) {
            Map<String, SlackChannel> backupChannels = loadChannels(backup);
            channels.putAll(backupChannels);
        }
        log.info("total: {}", channels.size());

        for (Map.Entry<String, SlackChannel> entry : channels.entrySet()) {
            if (entry.getValue().is_archived()) {
                log.info(entry.getValue().getName());
            }
        }

        StringBuilder sb = new StringBuilder();

        try (Stream<Path> stream = Files.walk(Paths.get(altRootFolder), 1)) {
            log.info(altRootFolder);
            Set<String> set = stream
                    .filter(Files::isRegularFile)
                    .filter(p -> p.toFile().getName().endsWith(".json"))
                    .filter(p -> {
                        String name = p.toFile().getName();
                        String[] splitted = name.split("\\.");
                        return !channels.containsKey(splitted[0]);
                    })
                    .map(p -> {
                        String name = p.toFile().getName();
                        String[] splitted = name.split("\\.");
                        return splitted[0];
                    })
                    .collect(Collectors.toSet());

            log.info("total: {}", set.size());

            try (Stream<String> lines = Files.lines(Paths.get(altRootFolder).resolve(rootFile))) {
                lines
                        .filter(line -> {
                            ChannelInfo channelInfo = getChannelInfo(line);
                            if (channelInfo != null) {
                                return set.contains(channelInfo.getId());
                            }
                            return false;
                        })
                        .forEach(line -> sb.append(line).append("\n"));
            }
        } catch (IOException e) {
            throw new ImportRuntimeException(e);
        }

        try (PrintWriter out = new PrintWriter("new-channels.txt")) {
            out.print(sb);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private ChannelInfo getChannelInfo(String line) {
        Pattern pattern = Pattern.compile("([CDG][A-Z\\d]+)\\s+(arch|-)\\s+(-|saved)\\s+([(#]|\uD83D\uDD12 )([\\w-]+)");
        Matcher matcher = pattern.matcher(line);
        if (matcher.find()) {
            return ChannelInfo.builder()
                    .name(matcher.group(5))
                    .id(matcher.group(1))
                    .archived(matcher.group(2).equals("arch"))
                    .isPrivate(!matcher.group(4).equals("#"))
                    .build();
        }
        return null;
    }

    @Builder
    @Data
    @FieldDefaults(level = AccessLevel.PRIVATE)
    private static class ChannelInfo {
        String id;
        boolean archived;
        boolean isPrivate;
        String name;
    }
}