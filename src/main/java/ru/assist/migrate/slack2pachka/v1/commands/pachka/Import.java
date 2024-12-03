package ru.assist.migrate.slack2pachka.v1.commands.pachka;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.shell.command.annotation.Command;
import org.springframework.shell.standard.AbstractShellComponent;
import ru.assist.migrate.slack2pachka.ImportRuntimeException;
import ru.assist.migrate.slack2pachka.v1.commands.pachka.common.PachkaData;
import ru.assist.migrate.slack2pachka.slack.alternate.AlternateChannel;
import ru.assist.migrate.slack2pachka.slack.channel.SlackChannel;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static ru.assist.migrate.slack2pachka.v1.commands.pachka.common.DataUtils.*;
import static ru.assist.migrate.slack2pachka.v1.commands.test.utils.SlackUtils.getBackupFolders;

@Slf4j
@Command(command = "import", description = "Slack import utilities", group = "import")
@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Import extends AbstractShellComponent {

    final PachkaData pachkaData;

    @Value("${slack.data.root.folder}")
    private String slackFolder;

    @Autowired
    public Import(PachkaData pachkaData) {
        this.pachkaData = pachkaData;
    }

    @Command(command = "messages", description = "(stage 1) import messages")
    private void messages() {
        Path[] backups = getBackupFolders(slackFolder);
        for (Path backup : backups) {
            pachkaData.set(backup);
            pachkaData.syncChannels();
            pachkaData.syncMessages();
        }
    }

    @Command(command = "users", description = "(stage 2) import users and update channel member lists")
    private void users() {
        Path[] backups = getBackupFolders(slackFolder);
        for (Path backup : backups) {
            pachkaData.set(backup);
            pachkaData.syncUsers();
            pachkaData.syncChannelMembers();
        }
    }

    @Value("${slack.data.alternate.root.folder}")
    private String altRootFolder;

    @Value("${slack.data.alternate.channels}")
    private Set<String> toImport;

    @Command(command = "alt", description = "import from alternate dump")
    private void alternate() {
        List<AlternateChannel> altChannels = setPackaData(altRootFolder, toImport, pachkaData);
        pachkaData.syncChannels();
        pachkaData.syncMessages(altChannels);
    }

    static List<AlternateChannel> setPackaData(String altRootFolder, Set<String> toImport, PachkaData pachkaData) {
        Set<Path> jsonSet;
        try (Stream<Path> files = Files.walk(Path.of(altRootFolder), 1)) {
            jsonSet = files
                    .filter(Files::isRegularFile)
                    .filter(f -> f.getFileName().toString().endsWith(".json"))
                    .filter(f -> {
                        String name = f.toFile().getName();
                        String[] split = name.split("\\.");
                        return toImport.contains(split[0]);
                    })
                    .collect(Collectors.toSet());
        } catch (IOException e) {
            throw new ImportRuntimeException(e);
        }

        List<AlternateChannel> altChannels = toAlternateChannels(jsonSet);
        SlackChannel[] channels = toChannels(altChannels);
        Path[] users = toUsers();
        Path[] files = toFiles();

        pachkaData.set(files, users, channels);

        return altChannels;
    }
}
