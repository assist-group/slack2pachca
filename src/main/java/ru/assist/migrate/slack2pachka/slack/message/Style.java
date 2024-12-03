package ru.assist.migrate.slack2pachka.slack.message;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

/**
 * *                                         |style : OBJECT
 * *                                             |code : [BOOLEAN] : [true]
 * *                                             |unlink : [BOOLEAN] : [true]
 * *                                             |strike : [BOOLEAN] : [true, false]
 * *                                             |bold : [BOOLEAN] : [true, false]
 * *                                             |italic : [BOOLEAN] : [true, false]
 */

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Style {
    String style;

    public Style(String style) {
        this.style = style;
    }

    public Style() {
    }

    boolean code;
    boolean unlink;
    boolean strike;
    boolean bold;
    boolean italic;
}
