package ru.assist.migrate.slack2pachka.slack.user;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

/*
        {
        "title":"",
        "phone":"",
        "skype":"",
        "real_name":"extreg",
        "real_name_normalized":"extreg",
        "display_name":"",
        "display_name_normalized":"",
        "fields":{},
        "status_text":"",
        "status_emoji":"",
        "status_emoji_display_info":[],
        "status_expiration":0,
        "avatar_hash":"gff2b81e4873",
        "email":"extreg@assist.kz",
        "image_24":"https:\/\/secure.gravatar.com\/avatar\/32dc1f1adb8b14dd82fa288b0ab49b2e.jpg?s=24&d=https%3A%2F%2Fa.slack-edge.com%2Fdf10d%2Fimg%2Favatars%2Fava_0010-24.png",
        "image_32":"https:\/\/secure.gravatar.com\/avatar\/32dc1f1adb8b14dd82fa288b0ab49b2e.jpg?s=32&d=https%3A%2F%2Fa.slack-edge.com%2Fdf10d%2Fimg%2Favatars%2Fava_0010-32.png",
        "image_48":"https:\/\/secure.gravatar.com\/avatar\/32dc1f1adb8b14dd82fa288b0ab49b2e.jpg?s=48&d=https%3A%2F%2Fa.slack-edge.com%2Fdf10d%2Fimg%2Favatars%2Fava_0010-48.png",
        "image_72":"https:\/\/secure.gravatar.com\/avatar\/32dc1f1adb8b14dd82fa288b0ab49b2e.jpg?s=72&d=https%3A%2F%2Fa.slack-edge.com%2Fdf10d%2Fimg%2Favatars%2Fava_0010-72.png",
        "image_192":"https:\/\/secure.gravatar.com\/avatar\/32dc1f1adb8b14dd82fa288b0ab49b2e.jpg?s=192&d=https%3A%2F%2Fa.slack-edge.com%2Fdf10d%2Fimg%2Favatars%2Fava_0010-192.png",
        "image_512":"https:\/\/secure.gravatar.com\/avatar\/32dc1f1adb8b14dd82fa288b0ab49b2e.jpg?s=512&d=https%3A%2F%2Fa.slack-edge.com%2Fdf10d%2Fimg%2Favatars%2Fava_0010-512.png",
        "status_text_canonical":"",
        "team":"T0J6CGETT"
        },
 */
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Profile {
    String title;
    String phone;
    String skype;
    String real_name;
    String real_name_normalized;
    String display_name;
    String display_name_normalized;
    Fields fields;
    String status_text;
    String status_emoji;
    Object[] status_emoji_display_info;
    Long status_expiration;
    String avatar_hash;
    String email;
    String image_24;
    String image_32;
    String image_48;
    String image_72;
    String image_192;
    String image_512;
    String status_text_canonical;
    String team;
}
