package ru.assist.migrate.slack2pachka.v1.model.messages;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

/**
 *id	integer	Идентификатор созданного сообщения
 * entity_type	string	Тип сущности, к которой относится сообщение: беседа/канал (discussion), тред (thread) или пользователь (user)
 * entity_id	integer	Идентификатор сущности, к которой относится сообщение (беседы/канала, треда или пользователя)
 * chat_id	integer	Идентификатор чата, в котором находится сообщение
 * content	string	Текст сообщения
 * user_id	integer	Идентификатор пользователя, создавшего сообщение
 * created_at	string	Дата и время создания сообщения (ISO-8601, UTC+0) в формате YYYY-MM-DDThh:mm:ss.sssZ
 * files	MessageFile[]
 * thread	MessageThread
 * parent_message_id	integer или null	Идентификатор сообщения, к которому написан ответ. Возвращается как null, если сообщение не является ответом.
 */
@Builder
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Message {
    long id;
    String entity_type;
    long entity_id;
    long chat_id;
    String content;
    long user_id;
    String created_at;
    MessageFile[] files;
    MessageThread thread;
    long parent_message_id;

    @Builder
    @Data
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class Request {
        Message message;
    }

    @Data
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class ListResponse {
        Message[] data;
    }

    @Data
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class Response {
        Message data;
    }
}
