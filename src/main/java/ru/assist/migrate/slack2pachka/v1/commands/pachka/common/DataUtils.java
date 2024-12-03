package ru.assist.migrate.slack2pachka.v1.commands.pachka.common;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.assist.migrate.slack2pachka.ImportRuntimeException;
import ru.assist.migrate.slack2pachka.slack.alternate.AlternateChannel;
import ru.assist.migrate.slack2pachka.slack.channel.SlackChannel;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import static ru.assist.migrate.slack2pachka.v1.commands.test.utils.SlackUtils.getBackupFolders;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DataUtils {
    private DataUtils() {
    }

    static String slackFolder;
    static Set<String> toImport;
    static String altRootFolder;

    @Value("${slack.data.root.folder}")
    private void setSlackFolder(String name) {
        slackFolder = name;
    }

    @Value("${slack.data.alternate.root.folder}")
    private void setAltRootFolder(String name) {
        altRootFolder = name;
    }

    @Value("${slack.data.alternate.channels}")
    private void setToImport(Set<String> set) {
        toImport = set;
    }


    public static SlackChannel[] toChannels(List<AlternateChannel> alternateChannels) {
        List<SlackChannel> result = new ArrayList<>();
        for (AlternateChannel alternateChannel : alternateChannels) {
            SlackChannel channel = SlackChannel.builder()
                    .id(alternateChannel.getChannel_id())
                    .name(alternateChannel.getName())
                    .build();
            result.add(channel);
        }
        return result.toArray(new SlackChannel[0]);
    }

    public static Path[] toUsers() {
        List<Path> result = new ArrayList<>();
        Path[] backups = getBackupFolders(slackFolder);
        for (Path backup : backups) {
            Path usersDataPath = backup.resolve("users.json");
            result.add(usersDataPath);
        }
        return result.toArray(new Path[0]);
    }

    public static Path[] toFiles() {
        try (Stream<Path> files = Files.walk(Path.of(altRootFolder), 1)) {
            return files
                    .filter(Files::isDirectory)
                    .filter(p -> toImport.contains(p.toFile().getName())).distinct().toArray(Path[]::new);
        } catch (IOException e) {
            throw new ImportRuntimeException(e);
        }
    }

    public static List<AlternateChannel> toAlternateChannels(Set<Path> jsonSet) {
        List<AlternateChannel> result = new ArrayList<>();
        for (Path json : jsonSet) {
            result.add(getAlternateChannel(json));
        }
        return result;
    }

    private static final ObjectMapper MAPPER = getObjectMapper();

    private static ObjectMapper getObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.configure(DeserializationFeature.USE_JAVA_ARRAY_FOR_JSON_ARRAY, true);
        return objectMapper;
    }

    public static AlternateChannel getAlternateChannel(Path file) {
        try {
            return MAPPER.readValue(Files.newInputStream(file), AlternateChannel.class);
        } catch (IOException e) {
            throw new ImportRuntimeException(e);
        }
    }
}
