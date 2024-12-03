package ru.assist.migrate.slack2pachka.v3.commands.handlers;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.assist.migrate.slack2pachka.v3.model.chat.Chat;
import ru.assist.migrate.slack2pachka.v3.service.chat.handlers.ChatListHandler;
import ru.assist.migrate.slack2pachka.v3.service.chat.ChatService;

@Component
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ChatDropGroupsHandlerImpl implements ChatListHandler {
    final ChatService chatService;
    final DropChatGroupsHandlerImpl dropChatGroupsHandler;

    @Autowired
    public ChatDropGroupsHandlerImpl(ChatService chatService, DropChatGroupsHandlerImpl dropChatGroupsHandler) {
        this.chatService = chatService;
        this.dropChatGroupsHandler = dropChatGroupsHandler;
    }

    @Override
    public void handle(Chat[] chats) {
        for (Chat chat : chats) {
            log.info(chat.getName());
            chatService.list(chat.getId(), dropChatGroupsHandler);
        }
    }
}
