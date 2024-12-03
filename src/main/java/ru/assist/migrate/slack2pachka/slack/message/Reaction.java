package ru.assist.migrate.slack2pachka.slack.message;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

/**
 * *             {
 *  *                 "name": "raised_hands",
 *  *                 "users": [
 *  *                     "U07C4DAHC3D"
 *  *                 ],
 *  *                 "count": 1
 *  *             }
 */
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Reaction {
    String name;
    String[] users;
    int count;
}
