package ru.assist.migrate.slack2pachka.slack.message;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

/**

 *
  *                         |image_height : [NUMBER] : [32]
 *                         |alt_text : [STRING] : [giphy logo, shortcuts icon]
 *                         |action_id : [STRING] : [app_id=296639820346703025&channel=&message_ts=&step_id=001f0673-e1c8-4c76-a44f-e6127ac0ed24&workflow_id=296639822074753349&workflow_instance_id=297973423781525575, PollMessage(jkBTFQgaQr5QD8LNX).ViewAllResponsesButton, PollMessage(jkBTFQgaQr5QD8LNX).VotingButton(2), PollMessage(jkBTFQgaQr5QD8LNX).AddCommentButton, open_unified_modal, PollMessage(jkBTFQgaQr5QD8LNX).VotingButton(1), PollMessage(yZegAjeDBc7PtLB7K).VotingButton(1), PollMessage(jkBTFQgaQr5QD8LNX).VotingButton(0), PollMessage(yZegAjeDBc7PtLB7K).VotingButton(0), app_id=296639820346703025&channel=&message_ts=&step_id=001f0673-e1c8-4c76-a44f-e6127ac0ed24&workflow_id=296639822074753349&workflow_instance_id=300002795476498476]
 *                         |style : [STRING] : [ordered, bullet, danger, primary]
 *                         |text : [STRING] : [Posted using /giphy | GIF by <https://giphy.com/paddockcafe/|paddockcafe>, Posted using /giphy | GIF by <https://giphy.com/hulu/|HULU>, Posted using /giphy | GIF by <https://giphy.com/freeform/|Freeform>, Posted using /giphy | GIF by <https://giphy.com/disney/|Disney>, Posted using /giphy | GIF by <https://giphy.com/cravecanada/|Crave>, Posted using /giphy | GIF by <https://giphy.com/altpress/|Alternative Press>, Posted using /giphy | , Shared with the *Giphy* shortcut, Posted using /giphy | GIF by <https://giphy.com/tipsyelves/|TipsyElves.com>, Posted using /giphy]
 *                         |fallback : [STRING] : [32x32px image]
 *                         |value : [STRING] : [nine, six, four, one, seven, two, three, five, bSdmBXgiY3N6dMYtL, eight]
 */
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Element {
    Element[] elements;
    long skin_tone;
    String usergroup_id;
    String range;
    String type;
    boolean unsafe;
    String url;
    String user_id;
    String name;
    String unicode;
    Style style;
    Text text;
    String alt_text;
    String channel_id;
    String value;

    long border;
    long offset;
    long indent;
    String image_url;
    long image_width;
    long image_height;
    boolean verbatim;
    long image_bytes;

    String action_id;
    String fallback;
    Confirm confirm;
}
