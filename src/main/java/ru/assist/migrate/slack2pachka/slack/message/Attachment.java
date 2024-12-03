package ru.assist.migrate.slack2pachka.slack.message;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

/**
 *             |attachments : ARRAY
 *
 */
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Attachment {
    String fallback;
    MessageBlock[] message_blocks;
}
