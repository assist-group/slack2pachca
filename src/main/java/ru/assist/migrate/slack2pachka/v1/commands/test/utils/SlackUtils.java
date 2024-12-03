package ru.assist.migrate.slack2pachka.v1.commands.test.utils;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.assist.migrate.slack2pachka.ImportRuntimeException;
import ru.assist.migrate.slack2pachka.slack.message.SlackMessage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class SlackUtils {
    private static final ObjectMapper MAPPER = getObjectMapper();

    private static ObjectMapper getObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.configure(DeserializationFeature.USE_JAVA_ARRAY_FOR_JSON_ARRAY, true);
        return objectMapper;
    }

    private static String prefix;

    @Value("${backup.folder.prefix}")
    private void setPrefix(String name) {
        prefix = name;
    }

    private static String regexp;

    @Value("${backup.folder.date.regex.regexp}")
    private void setRegexp(String name) {
        regexp = name;
    }

    private static String format;

    @Value("${backup.folder.date.format}")
    private void setFormat(String name) {
        format = name;
    }

    public static Path[] getBackupFolders(String rootFolder) {
        try (Stream<Path> stream = Files.walk(Paths.get(rootFolder), 1)) {
            Path[] result = stream.filter(Files::isDirectory).filter(path -> !Path.of(rootFolder).equals(path)).filter(path -> path.toFile().getName().startsWith(prefix)).collect(Collectors.toSet()).toArray(Path[]::new);

            Arrays.sort(result, (o1, o2) -> {
                String name1 = o1.toFile().getName();
                String name2 = o2.toFile().getName();

                String stringDate1 = getDateStringFromFileName(name1);
                String stringDate2 = getDateStringFromFileName(name2);

                if (stringDate1 == null || stringDate2 == null) {
                    throw new ImportRuntimeException("backup folder bad format");
                }

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
                LocalDate date1 = LocalDate.parse(stringDate1, formatter);
                LocalDate date2 = LocalDate.parse(stringDate2, formatter);
                return date1.compareTo(date2);
            });

            return result;
        } catch (IOException e) {
            throw new ImportRuntimeException(e);
        }
    }

    private static String getDateStringFromFileName(String in) {
        Pattern pattern = Pattern.compile(regexp);
        Matcher matcher = pattern.matcher(in);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }

    public static Path[] getMessageFolders(Path rootFolder) {

        try (Stream<Path> stream = Files.walk(rootFolder, 1)) {
            Set<String> excludeSet = new HashSet<>();
            excludeSet.add("IMAGES");

            return stream.filter(Files::isDirectory).filter(path -> !rootFolder.equals(path)).filter(path -> {
                        String folder = path.toFile().getName();
                        return !excludeSet.contains(folder);
                    })
//                     .filter(path -> path.endsWith())
                    .collect(Collectors.toSet()).toArray(Path[]::new);
        } catch (IOException e) {
            throw new ImportRuntimeException(e);
        }
    }

    public static Path[] getMessageFiles(Path dir) {

        try (Stream<Path> stream = Files.walk(dir, 1)) {
            return stream.filter(file -> !Files.isDirectory(file)).toArray(Path[]::new);
        } catch (IOException e) {
            throw new ImportRuntimeException(e);
        }
    }

    public static SlackMessage[] getSlackMessages(Path file) {
        try {
            return MAPPER.readValue(Files.newInputStream(file), SlackMessage[].class);
        } catch (IOException e) {
            throw new ImportRuntimeException(e);
        }
    }

}
