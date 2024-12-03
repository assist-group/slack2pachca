package ru.assist.migrate.slack2pachka.v3.model.chat;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

/**
 * {
 * "data": [
 * {
 * "id": 448640,
 * "name": "Алексей",
 * "last_name": "Тананаев",
 * "display_name": "Алексей Тананаев",
 * "title": null,
 * "email": "atananaev@assist.ru",
 * "image_url": null,
 * "thumb_image_url": null,
 * "avatar_color": "light-slate-gray",
 * "avatar_letters": "АТ",
 * "nickname": "atananaev",
 * "phone_number": null,
 * "sip": null,
 * "last_seen": "2024-09-11T08:55:00.000Z",
 * "online": true,
 * "company_role": "admin",
 * "company_id": 399198,
 * "company_name": "tryme",
 * "department": null,
 * "suspended": false,
 * "confirmed": true,
 * "service": false,
 * "properties": [
 * {
 * "id": -1,
 * "data_type": "multi_select",
 * "name": "Теги",
 * "field": "group_tags",
 * "value": null,
 * "entity_type": "User"
 * }
 * ],
 * "provider": null,
 * "bot": false,
 * "bot_source": null,
 * "user_status": null,
 * "deactivated": false,
 * "blocked_fields": [],
 * "blocked_fields_reason": null,
 * "created_at": "2024-09-05T08:57:46.000Z",
 * "status": "active",
 * "auth_type": "common",
 * "activated": true,
 * "inviter_id": null,
 * "time_zone": "Europe/Moscow"
 * },
 * {
 * "id": 450154,
 * "name": "Some",
 * "last_name": "One",
 * "display_name": "Some One",
 * "title": "",
 * "email": "tananaev@gmail.com",
 * "image_url": null,
 * "thumb_image_url": null,
 * "avatar_color": "salmon",
 * "avatar_letters": "SO",
 * "nickname": "tananaev",
 * "phone_number": "",
 * "sip": null,
 * "last_seen": "2024-09-06T13:53:01.000Z",
 * "online": false,
 * "company_role": "user",
 * "company_id": 399198,
 * "company_name": "tryme",
 * "department": "",
 * "suspended": false,
 * "confirmed": true,
 * "service": false,
 * "properties": [
 * {
 * "id": -1,
 * "data_type": "multi_select",
 * "name": "Теги",
 * "field": "group_tags",
 * "value": [
 * 21002
 * ],
 * "entity_type": "User"
 * }
 * ],
 * "provider": null,
 * "bot": false,
 * "bot_source": null,
 * "user_status": null,
 * "deactivated": false,
 * "blocked_fields": [],
 * "blocked_fields_reason": null,
 * "created_at": "2024-09-06T10:03:46.000Z",
 * "status": "active",
 * "auth_type": "common",
 * "activated": true,
 * "inviter_id": 448640,
 * "time_zone": "Europe/Moscow"
 * }
 * ],
 * "meta": {
 * "total": 2
 * }
 * }
 */

@Builder
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Member {
    long id;
    String name;
    String last_name;
    String display_name;
    String title;
    String email;
    String image_url;
    String thumb_image_url;
    String avatar_color;
    String avatar_letters;
    String nickname;
    String phone_number;
    String sip;
    String last_seen;
    boolean online;
    String company_role;
    long company_id;
    String company_name;
    String department;
    boolean suspended;
    boolean confirmed;
    boolean service;
    Properties[] properties;
    String provider;
    boolean bot;
    String bot_source;
    String user_status;
    boolean deactivated;
    String[] blocked_fields;
    String blocked_fields_reason;
    String created_at;
    String status;
    String auth_type;
    boolean activated;
    long inviter_id;
    String time_zone;


    /**
     * *                 {
     * *                     "id": -1,
     * *                     "data_type": "multi_select",
     * *                     "name": "Теги",
     * *                     "field": "group_tags",
     * *                     "value": [
     * *                         21002
     * *                     ],
     * *                     "entity_type": "User"
     * *                 }
     */
    public static class Properties {
        long id;
        String data_type;
        String name;
        String field;
        Object value;
        String entity_type;
    }

    public static class Meta {
        long total;
    }

    @Data
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class Response {
        Member[] data;
        Meta meta;
    }

    @Builder
    @Data
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class Role {
        String role;
    }
}
