package ru.assist.migrate.slack2pachka.slack.message;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

/**
 * "user_profile": {
 * "avatar_hash": "g62ab26cc685",
 * "image_72": "https:\/\/secure.gravatar.com\/avatar\/62ab26cc68508e374b41d6236a3bda26.jpg?s=72&d=https%3A%2F%2Fa.slack-edge.com%2Fdf10d%2Fimg%2Favatars%2Fava_0003-72.png",
 * "first_name": "Алексей",
 * "real_name": "Алексей Тананаев",
 * "display_name": "atananaev",
 * "team": "T07BPR62AF9",
 * "name": "atananaev",
 * "is_restricted": false,
 * "is_ultra_restricted": false
 * },
 */
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserProfile {
    String avatar_hash;
    String image_72;
    String first_name;
    String real_name;
    String display_name;
    String team;
    String name;
    boolean is_restricted;
    boolean is_ultra_restricted;
}
