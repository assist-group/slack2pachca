package ru.assist.migrate.slack2pachka.slack.channel;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

/**
 * "value": "нотификации от заббикса",
 * "creator": "U0J67FZC4",
 * "last_set": 1452548278
 */
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Purpose {
    String value;
    String creator;
    long last_set;
}
