package ru.assist.migrate.slack2pachka.v3.service.chat.handlers;

import ru.assist.migrate.slack2pachka.v3.model.chat.Chat;

public interface ChatListHandler {
    void handle(Chat[] chats);
}
