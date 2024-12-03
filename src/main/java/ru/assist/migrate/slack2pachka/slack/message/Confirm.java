package ru.assist.migrate.slack2pachka.slack.message;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

/**
 * *                         |confirm : OBJECT
 *  *                             |confirm : OBJECT
 *  *                                 |emoji : [BOOLEAN] : [true]
 *  *                                 |text : [STRING] : [Okay]
 *  *                                 |type : [STRING] : [plain_text]
 *  *                             |deny : OBJECT
 *  *                                 |emoji : [BOOLEAN] : [true]
 *  *                                 |text : [STRING] : [Cancel]
 *  *                                 |type : [STRING] : [plain_text]
 *  *                             |text : OBJECT
 *  *                                 |text : [STRING] : [Deleted polls are gone for good. You're parting with it forever and will never see it again! :skull:]
 *  *                                 |type : [STRING] : [mrkdwn]
 *  *                                 |verbatim : [BOOLEAN] : [true]
 *  *                             |title : OBJECT
 *  *                                 |emoji : [BOOLEAN] : [true]
 *  *                                 |text : [STRING] : [Are you sure?]
 *  *                                 |type : [STRING] : [plain_text]
 */
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Confirm {
    boolean emoji;
    Text text;
    String type;
    boolean verbatim;
}
