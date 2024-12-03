package ru.assist.migrate.slack2pachka.v3.service.chat.handlers;

import ru.assist.migrate.slack2pachka.v3.model.chat.Group;

public interface GroupListHandler {
    void handle(long chatId, Group[] groups);
}
