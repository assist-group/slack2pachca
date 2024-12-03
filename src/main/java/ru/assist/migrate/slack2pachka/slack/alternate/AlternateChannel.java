package ru.assist.migrate.slack2pachka.slack.alternate;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import ru.assist.migrate.slack2pachka.slack.message.SlackMessage;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AlternateChannel {
    SlackMessage[] messages;
    String name;
    String channel_id;
}
