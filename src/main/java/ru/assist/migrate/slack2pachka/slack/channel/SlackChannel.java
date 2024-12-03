package ru.assist.migrate.slack2pachka.slack.channel;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.FieldDefaults;

/**
 * "id": String,
 * "name": String,
 * "created": long,
 * "creator": String,
 * "is_archived": boolean,
 * "is_general": boolean,
 * "members": String[]
 * "pins": Pin[]
 * "topic": Topic
 * "purpose": Purpose
 */
@Builder
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
public class SlackChannel {
    String id;
    String name;
    long created;
    String creator;
    @JsonProperty("is_archived")
    boolean is_archived;
    @JsonProperty("is_general")
    boolean is_general;
    String[] members;
    Pin[] pins;
    Topic topic;
    Purpose purpose;
}
