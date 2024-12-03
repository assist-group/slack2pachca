package ru.assist.migrate.slack2pachka.v3.model.chat;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

/**
 * {
 * "data": [
 * {
 * "id": 21002,
 * "name": "super",
 * "color_id": 17,
 * "color": "#333333",
 * "created_at": "2024-09-11T06:07:46.856Z",
 * "updated_at": "2024-09-11T06:07:46.856Z",
 * "relations_count": 1
 * }
 * ],
 * "meta": {
 * "total": 1
 * }
 * }
 */

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Group {
    long id;
    String name;
    String created_at;
    String updated_at;
    long relations_count;

    public static class Meta {
        long total;
    }

    @Data
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class Response {
        Group[] data;
        Meta meta;
    }

    @Builder
    @Data
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class Tags {
        long[] ids;
    }
}
