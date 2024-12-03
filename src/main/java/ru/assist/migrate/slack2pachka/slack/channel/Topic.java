package ru.assist.migrate.slack2pachka.slack.channel;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

/**
 * "value": "Сообщения от Zabbix!",
 * "creator": "U0JAUDWAU",
 * "last_set": 1507029841
 */
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Topic {
    String value;
    String creator;
    long last_set;
}
