package ru.assist.migrate.slack2pachka.slack.message;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

/**
 *                  "id": "F07D5LRPEBV",
 *  *                 "created": 1721376056,
 *  *                 "timestamp": 1721376056,
 *  *                 "name": "test.xlsx",
 *  *                 "title": "test.xlsx",
 *  *                 "mimetype": "application\/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
 *  *                 "filetype": "xlsx",
 *  *                 "pretty_type": "Excel Spreadsheet",
 *  *                 "user": "U07C4DAHC3D",
 *  *                 "user_team": "T07BPR62AF9",
 *  *                 "editable": false,
 *  *                 "size": 3374,
 *  *                 "mode": "hosted",
 *  *                 "is_external": false,
 *  *                 "external_type": "",
 *  *                 "is_public": true,
 *  *                 "public_url_shared": false,
 *  *                 "display_as_bot": false,
 *  *                 "username": "",
 *  *                 "url_private": "https:\/\/files.slack.com\/files-pri\/T07BPR62AF9-F07D5LRPEBV\/test.xlsx?t=xoxe-7397856078519-7445916930293-7445917002181-26c400031b71f9ec72547ea96f91f625",
 *  *                 "url_private_download": "https:\/\/files.slack.com\/files-pri\/T07BPR62AF9-F07D5LRPEBV\/download\/test.xlsx?t=xoxe-7397856078519-7445916930293-7445917002181-26c400031b71f9ec72547ea96f91f625",
 *  *                 "media_display_type": "unknown",
 *  *                 "converted_pdf": "https:\/\/files.slack.com\/files-tmb\/T07BPR62AF9-F07D5LRPEBV-f9896f85e4\/test_converted.pdf?t=xoxe-7397856078519-7445916930293-7445917002181-26c400031b71f9ec72547ea96f91f625",
 *  *                 "thumb_pdf": "https:\/\/files.slack.com\/files-tmb\/T07BPR62AF9-F07D5LRPEBV-f9896f85e4\/test_thumb_pdf.png?t=xoxe-7397856078519-7445916930293-7445917002181-26c400031b71f9ec72547ea96f91f625",
 *  *                 "thumb_pdf_w": 909,
 *  *                 "thumb_pdf_h": 1286,
 *  *                 "permalink": "https:\/\/workspace-rnq6323.slack.com\/files\/U07C4DAHC3D\/F07D5LRPEBV\/test.xlsx",
 *  *                 "permalink_public": "https:\/\/slack-files.com\/T07BPR62AF9-F07D5LRPEBV-0b8e04b436",
 *  *                 "is_starred": false,
 *  *                 "has_rich_preview": false,
 *  *                 "file_access": "visible"
 *  *             }
 */
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class File {
    String id;
    long created;
    long timestamp;
    String name;
    String title;
    String mimetype;
    String filetype;
    String pretty_type;
    String user;
    String user_team;
    boolean editable;
    long size;
    String mode;
    boolean is_external;
    String external_type;
    boolean is_public;
    boolean public_url_shared;
    boolean display_as_bot;
    String username;
    String url_private;
    String url_private_download;
    String media_display_type;
    String converted_pdf;
    String thumb_pdf;
    int thumb_pdf_w;
    int thumb_pdf_h;
    String permalink;
    String permalink_public;
    boolean is_starred;
    boolean has_rich_preview;
    String file_access;
}
