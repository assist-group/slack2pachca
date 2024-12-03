package ru.assist.migrate.slack2pachka.v3.service.chat;

import ru.assist.migrate.slack2pachka.v3.service.chat.handlers.GroupListHandler;

public interface GroupService {
    void add(long chatId, long[] groupIds);

    void add(long chatId, long groupId);

    void remove(long chatId, long groupId);

    void list(long chatId, GroupListHandler handler);

}
