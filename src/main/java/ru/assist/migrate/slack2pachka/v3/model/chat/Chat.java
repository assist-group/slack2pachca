package ru.assist.migrate.slack2pachka.v3.model.chat;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

/**
 *         {
 *             "id": 12288908,
 *             "avatar_color": "aquamarine",
 *             "avatar_letters": "DB",
 *             "avatar_url": null,
 *             "name": "db_dba",
 *             "channel": false,
 *             "is_member": false,
 *             "members_count": 16
 *         },
 */

@Builder
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Chat {
    long id;
    String name;
    boolean channel;
    int members_count;

    @Data
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class Response{
        Chat[] data;
    }
}
