package ru.assist.migrate.slack2pachka.v1.model.channels;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

/**
 * data	object	Созданная беседа или канал
 * <p>
 * id	integer	Идентификатор созданной беседы или канала
 * name	string	Название
 * owner_id	integer	Идентификатор пользователя, создавшего беседу или канал
 * created_at	string	Дата и время создания беседы или канала (ISO-8601, UTC+0) в формате YYYY-MM-DDThh:mm:ss.sssZ
 * member_ids	array of integers	Массив идентификаторов пользователей, участников
 * group_tag_ids	array of integers	Массив идентификаторов тегов, участников
 * channel	boolean	Тип: беседа (false) или канал (true)
 * public	boolean	Доступ: закрытый (false) или открытый (true)
 * last_message_at	string	Дата и время создания последнего сообщения в беседе/калане (ISO-8601, UTC+0) в формате YYYY-MM-DDThh:mm:ss.sssZ
 */
@Builder
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Channel {
    long id;
    String name;
    long owner_id;
    String created_at;
    long[] member_ids;
    long[] group_tag_ids;
    boolean channel;

    @JsonProperty("public")
    boolean isPublic;

    @JsonSetter("public")
    public void setPublic(boolean aPublic) {
        isPublic = aPublic;
    }

    @JsonGetter("public")
    public boolean isPublic() {
        return isPublic;
    }

    String last_message_at;

    @Builder
    @Data
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class Request {
        Channel chat;
    }

    @Data
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class Response {
        Channel data;
    }

    @Data
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class ListResponse {
        Channel[] data;
    }

    @Data
    @Builder
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class Members {
        long[] member_ids;
        boolean silent;
    }
}
