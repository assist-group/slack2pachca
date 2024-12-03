package ru.assist.migrate.slack2pachka.slack.message;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

/**
 *         |bot_profile : OBJECT
 *             |deleted : [BOOLEAN] : [false, true]
 *             |name : [STRING] : [Simple Poll, testbot, Ace, giphy, marbot, Jenkins CI, PaperBot, OOO, Scorebot, GitLab (gitlab.assist.zone)]
 *             |id : [STRING] : [B014A2BGR35, B05SHLZ10AX, B5RMWMUKZ, B05NKLL6Z1A, B8HGWATCN, B63CK9W1G, B363RHZAR, B5ZSKN97T, B6MECD3Q8, B014T91FQ3H]
 *             |team_id : [STRING] : [T0J6CGETT]
 *             |app_id : [STRING] : [A0GRU84TF, A05SEQVK2GM, A0F827J2C, A0NE9L9RR, A0HFW7MR6, A05NH4KTH51, A60EKU70C, A19U7N0HH, A012NANP7U3, A24D29E1M]
 *             |updated : [NUMBER] : [1496944890, 1692381085, 1558031915, 1586274694, 1590510389, 1479853239, 1502698133, 1694774224, 1498592726, 1597227477]
 */

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BotProfile {
    boolean deleted;
    String name;
    String id;
    String team_id;
    String app_id;
    long updated;
}
