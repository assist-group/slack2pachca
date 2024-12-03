package ru.assist.migrate.slack2pachka.v1.service.message;

import ru.assist.migrate.slack2pachka.v1.model.messages.Thread;

public interface ThreadService {
    Thread create(long message_id);
}
