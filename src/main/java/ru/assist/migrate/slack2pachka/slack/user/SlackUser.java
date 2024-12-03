package ru.assist.migrate.slack2pachka.slack.user;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

/*       {
        "id":"U0J67FZC4",
        "team_id":"T0J6CGETT",
        "name":"extreg",
        "deleted":false,
        "color":"9f69e7",
        "real_name":"extreg",
        "tz":"Asia\/Istanbul",
        "tz_label":"Turkey Time",
        "tz_offset":10800,
        "profile": Profile
        "is_admin":true,
        "is_owner":true,
        "is_primary_owner":true,
        "is_restricted":false,
        "is_ultra_restricted":false,
        "is_bot":false,
        "is_app_user":false,
        "updated":1676541368,
        "is_email_confirmed":true,
        "who_can_share_contact_card":"EVERYONE"
        },

         */
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SlackUser {
    String id;
    String team_id;
    String name;
    boolean deleted;
    String color;
    String real_name;
    String tz;
    String tz_label;
    Long tz_offset;
    Profile profile;
    boolean is_admin;
    boolean is_owner;
    boolean is_primary_owner;
    boolean is_restricted;
    boolean is_ultra_restricted;
    boolean is_bot;
    boolean is_app_user;
    Long updated;
    boolean is_email_confirmed;
    String who_can_share_contact_card;
}
