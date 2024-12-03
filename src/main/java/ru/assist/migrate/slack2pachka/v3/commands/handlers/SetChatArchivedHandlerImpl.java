package ru.assist.migrate.slack2pachka.v3.commands.handlers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.assist.migrate.slack2pachka.v1.commands.pachka.common.SlackData;
import ru.assist.migrate.slack2pachka.slack.channel.SlackChannel;
import ru.assist.migrate.slack2pachka.v3.model.chat.Chat;
import ru.assist.migrate.slack2pachka.v3.service.chat.ChatService;
import ru.assist.migrate.slack2pachka.v3.service.chat.handlers.ChatListHandler;

import java.nio.file.Path;
import java.util.Map;

import static ru.assist.migrate.slack2pachka.v1.commands.test.utils.SlackUtils.getBackupFolders;

@Component
@Slf4j
public class SetChatArchivedHandlerImpl implements ChatListHandler {
    @Value("${slack.data.root.folder}")
    private String slackFolder;

    final ChatService chatService;
    final SlackData slackData;

    @Autowired
    public SetChatArchivedHandlerImpl(SlackData slackData, ChatService chatService) {
        this.slackData = slackData;
        this.chatService = chatService;
    }

    @Override
    public void handle(Chat[] chats) {
        Path[] backups = getBackupFolders(slackFolder);
        slackData.set(backups[0]);
        Map<String, SlackChannel> name2SlackChannel = slackData.getName2Channel();
        for (Chat chat : chats) {
            archive(name2SlackChannel, chat);
        }
    }

    private void archive(Map<String, SlackChannel> name2SlackChannel, Chat chat) {
        SlackChannel slackChannel = name2SlackChannel.get(chat.getName());
        if (slackChannel == null) return;
        if (!slackChannel.is_archived()) return;

        log.info(chat.getName());
        chatService.archive(chat.getId());
    }
}
