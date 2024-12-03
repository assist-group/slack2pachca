package ru.assist.migrate.slack2pachka.v1.service.message;

import ru.assist.migrate.slack2pachka.v1.model.messages.Message;

public interface MessageService {

    Message create(Message.Request request);

    Message read(long id);

    Message update(Message.Request request, long id);

    void delete(long id);

    Message[] list(long chat_id);

    void list(long chat_id, MessageListHandler handler);

    void list(long chat_id, int limit, MessageListHandler handler);

    boolean isEmpty(long chat_id);

}
