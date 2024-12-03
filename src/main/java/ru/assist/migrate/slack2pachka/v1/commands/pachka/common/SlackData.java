package ru.assist.migrate.slack2pachka.v1.commands.pachka.common;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;
import ru.assist.migrate.slack2pachka.ImportRuntimeException;
import ru.assist.migrate.slack2pachka.slack.channel.SlackChannel;
import ru.assist.migrate.slack2pachka.slack.message.SlackMessage;
import ru.assist.migrate.slack2pachka.slack.user.SlackUser;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

@Component
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SlackData {
    private static final ObjectMapper MAPPER = getObjectMapper();
    Path dataFolderPath;
    final Map<String, SlackUser> users = new HashMap<>();
    final Map<String, Path> files = new HashMap<>();
    final Map<String, SlackChannel> channels = new HashMap<>();

    private static ObjectMapper getObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.configure(DeserializationFeature.USE_JAVA_ARRAY_FOR_JSON_ARRAY, true);
        return objectMapper;
    }

    public void set(Path dataFolderPath) {
        this.dataFolderPath = dataFolderPath;

        clearAll();

        setUsers();
        setChannels();
        setFiles();
    }

    public void set(Path files, Path users, SlackChannel[] channels) {
        clearAll();
        setUsers(users);
        setChannels(channels);
        setFiles(files);
    }

    public void set(Path[] files, Path[] users, SlackChannel[] channels) {
        clearAll();
        setUsers(users);
        setChannels(channels);
        setFiles(files);
    }

    private void clearAll() {
        users.clear();
        files.clear();
        channels.clear();
    }

    private void setFiles() {
        Path filesFolder = dataFolderPath.resolve("IMAGES");
        setFiles(filesFolder);
    }

    private void setFiles(Path[] filesFolders) {
        for (Path filesFolder : filesFolders) {
            setFiles(filesFolder);
        }
    }

    private void setFiles(Path filesFolder) {
        if (!filesFolder.toFile().exists()) return;

        Map<String, Path> result = new HashMap<>();
        try (Stream<Path> stream = Files.walk(filesFolder, 1)) {
            stream
                    .filter(Files::isRegularFile)
                    .forEach(path -> {
                        String slackFileId = getSlackFileIdFromFileString(path.toString());
                        result.put(slackFileId, path);
                    });

        } catch (IOException e) {
            throw new ImportRuntimeException(e);
        }
        files.putAll(result);
    }

    private void setUsers() {
        Path usersDataPath = dataFolderPath.resolve("users.json");
        setUsers(usersDataPath);
    }

    private void setUsers(Path usersDataPath) {
        try {
            SlackUser[] slackUsers = MAPPER.readValue(usersDataPath.toFile(), SlackUser[].class);
            setUsers(slackUsers);
        } catch (IOException e) {
            throw new ImportRuntimeException(e);
        }
    }

    private void setUsers(Path[] usersData) {
        for (Path userData : usersData) {
            setUsers(userData);
        }
    }

    private void setUsers(SlackUser[] slackUsers) {
        for (SlackUser slackUser : slackUsers) {
            users.put(slackUser.getId(), slackUser);
        }
    }

    private void setChannels() {
        Path channelsDataPath = dataFolderPath.resolve("channels.json");
        setChannels(channelsDataPath);
    }

    private void setChannels(Path channelsDataPath) {
        try {
            SlackChannel[] slackChannels = MAPPER.readValue(channelsDataPath.toFile(), SlackChannel[].class);
            setChannels(slackChannels);
        } catch (IOException e) {
            throw new ImportRuntimeException(e);
        }
    }

    private void setChannels(SlackChannel[] slackChannels) {
        for (SlackChannel slackChannel : slackChannels) {
            channels.put(slackChannel.getId(), slackChannel);
        }
    }

    public String getSlackFileIdFromFileString(String fileString) {
        Pattern pattern = Pattern.compile("(F[A-Z0-9]+)");
        Matcher matcher = pattern.matcher(fileString);

        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }

    public static String getCreated(SlackMessage slackMessage) {
        String ts = slackMessage.getTs();
        double time = Double.parseDouble(ts) * 1000.0;
        long longValue = (long) time;

        ZonedDateTime zdt = Instant
                .ofEpochMilli(longValue)
                .atOffset(ZoneOffset.UTC)
                .toZonedDateTime();

        final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.000'Z'");

        return zdt.format(formatter);
    }

    public SlackChannel[] getChannelsArray() {
        return getChannels().values().toArray(new SlackChannel[0]);
    }

    public Map<String, SlackChannel> getName2Channel() {
        Map<String, SlackChannel> result = new HashMap<>();
        SlackChannel[] slackChannels = getChannelsArray();
        for (SlackChannel slackChannel : slackChannels) {
            result.put(slackChannel.getName(), slackChannel);
        }
        return result;
    }
}
