package ru.assist.migrate.slack2pachka.slack.message;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Text {
    String type;
    String text;
    boolean verbatim;
    boolean emoji;

    public Text() {
    }

    public Text(String text) {
        this.text = text;
    }
}
