package ru.assist.migrate.slack2pachka.slack.channel;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

/**
 * "id": "1507804218.000281",
 * "type": "C",
 * "created": 1507804224,
 * "user": "U0JAUDWAU",
 * "owner": "U0JAUDWAU"
 */
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Pin {
    String id;
    String type;
    long created;
    String user;
    String owner;
}
