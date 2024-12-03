package ru.assist.migrate.slack2pachka.v1.service.message;

import ru.assist.migrate.slack2pachka.v1.model.messages.Message;

public interface MessageListHandler {
    void handle(Message[] messages);
}
