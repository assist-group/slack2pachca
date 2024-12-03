package ru.assist.migrate.slack2pachka.slack.message;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

/**
 *     {
 *         "text": "сообщение со всем на свете",
 *         "user_profile" : UserProfile
 *         "files": File[],
 *         "upload": false,
 *         "user": "U07C4DAHC3D",
 *         "display_as_bot": false,
 *         "blocks": Block[],
 *         "type": "message",
 *         "ts": "1721376062.219919",
 *         "client_msg_id": "25fe6ef0-1353-43d4-aca1-e94c858bf001",
 *         "thread_ts": "1721376062.219919",
 *         "reply_count": 1,
 *         "reply_users_count": 1,
 *         "latest_reply": "1721376112.201409",
 *         "reply_users": [
 *             "U07C4DAHC3D"
 *         ],
 *         "replies": Reply[],
 *         "is_locked": false,
 *         "subscribed": false,
 *         "reactions": Reaction[]
 *     },
 */
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SlackMessage {
    String text;
    String type;
    String subtype;
    boolean display_as_bot;
    String app_id;
    String bot_id;
    UserProfile user_profile;
    BotProfile bot_profile;
    File[] files;
    boolean upload;
    String user;
    Block[] blocks;
    String ts;
    String client_msg_id;
    String thread_ts;
    int reply_count;
    int reply_users_count;
    String latest_reply;
    String[] reply_users;
    Reply[] replies;
    boolean is_locked;
    boolean subscribed;
    Reaction[] reactions;
    Attachment[] attachments;
    SlackMessage[] slackdump_thread_replies;
}
