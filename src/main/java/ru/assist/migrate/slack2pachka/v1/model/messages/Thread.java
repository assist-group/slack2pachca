package ru.assist.migrate.slack2pachka.v1.model.messages;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

/**
 * id	integer	Идентификатор созданного треда (используется для отправки новых комментариев в тред)
 * chat_id	integer	Идентификатор чата треда (используется для отправки новых комментариев в тред и получения списка комментариев)
 * message_id	integer	Идентификатор сообщения, к которому был создан тред
 * message_chat_id	integer	Идентификатор чата сообщения
 * updated_at	string	Дата и время обновления треда (ISO-8601, UTC+0) в формате YYYY-MM-DDThh:mm:ss.sssZ
 */

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Thread {
    long id;
    long chat_id;
    long message_id;
    long message_chat_id;
    String updated_at;

    @Data
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class Response {
        Thread data;
    }
}


