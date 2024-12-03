package ru.assist.migrate.slack2pachka.v3.commands.handlers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.assist.migrate.slack2pachka.v3.model.chat.Group;
import ru.assist.migrate.slack2pachka.v3.service.chat.ChatService;
import ru.assist.migrate.slack2pachka.v3.service.chat.handlers.GroupListHandler;

@Component
@Slf4j
public class DropChatGroupsHandlerImpl implements GroupListHandler {
    final ChatService chatService;

    @Autowired
    public DropChatGroupsHandlerImpl(ChatService chatService) {
        this.chatService = chatService;
    }

    @Override
    public void handle(long chatId, Group[] groups) {
        log.info("Drop chat groups for {}", chatId);
        for (Group group : groups) {
            log.info("    ->"+group.getName());
            chatService.remove(chatId, group.getId());
        }
    }
}
