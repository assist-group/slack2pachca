package ru.assist.migrate.slack2pachka.v3.service.chat;

import ru.assist.migrate.slack2pachka.v3.service.chat.handlers.ChatListHandler;

public interface ChatService extends GroupService, MemberService {
    void list(ChatListHandler handler);

    void archive(long chatId);

    void unarchive(long chatId);
}
