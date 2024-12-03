package ru.assist.migrate.slack2pachka.v1.model.messages;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

/**
 *  *id	integer	Идентификатор треда (используется для отправки новых комментариев)
 *  * chat_id	integer	Идентификатор чата треда (используется для отправки
 *  новых комментариев в тред и получения списка комментариев)
 */
@Builder
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MessageThread {
    long id;
    long chat_id;
}
