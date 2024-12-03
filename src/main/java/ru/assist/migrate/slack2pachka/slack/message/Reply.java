package ru.assist.migrate.slack2pachka.slack.message;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
/**
**             {
 *  *                 "user": "U07C4DAHC3D",
 *  *                 "ts": "1721376112.201409"
 *  *             }
 */
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Reply {
    String user;
    String ts;
}
