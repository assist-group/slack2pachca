package ru.assist.migrate.slack2pachka.slack.message;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Message {
    Block[] blocks;
}
