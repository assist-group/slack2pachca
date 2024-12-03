package ru.assist.migrate.slack2pachka.v3.service.chat.handlers;

import ru.assist.migrate.slack2pachka.v3.model.chat.Group;
import ru.assist.migrate.slack2pachka.v3.model.chat.Member;

public interface MemberListHandler {
    void handle(long chatId, Member[] members);
}
