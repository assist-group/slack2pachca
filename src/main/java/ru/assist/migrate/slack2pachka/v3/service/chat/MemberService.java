package ru.assist.migrate.slack2pachka.v3.service.chat;

import ru.assist.migrate.slack2pachka.v3.model.chat.Member;
import ru.assist.migrate.slack2pachka.v3.service.chat.handlers.MemberListHandler;

public interface MemberService {
    Member[] list(long chatId);

    void list(long chatId, MemberListHandler handler);

    void role(long chatId, long memberId, String role);
}
