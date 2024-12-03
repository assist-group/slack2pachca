package ru.assist.migrate.slack2pachka.slack.message;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

/**
 *                        |message_blocks : ARRAY
 *  *                         |channel : [STRING] : [C73PVDUE6, CAZDH6CBC, C7GV3HJ3E, CHAB0AJBE, C6ZE2V5JL, C7G53JJQ0, CD5ESSMQF, C7BV69B2M, C022N4DLY2D, CQQR599DY]
 *  *                         |team : [STRING] : [T04FNDMK1, T0J6CGETT]
 *  *                         |message : OBJECT
 *  *                             |blocks : OBJECT
 *  *                                 |blocks : ARRAY
 *  *                                     |elements : OBJECT
 *  *                                         |elements : ARRAY
 *  *                                             |border : [NUMBER] : [0, 1]
 *  *                                             |offset : [NUMBER] : [0, 1, 2, 3, 4, 5, 6, 7, 10]
 *  *                                             |indent : [NUMBER] : [0, 1, 2]
 *  *                                             |elements : OBJECT
 *  *                                                 |elements : ARRAY
 *  *                                                     |skin_tone : [NUMBER] : [2, 3]
 *  *                                                     |usergroup_id : [STRING] : [S01URGTMGKF, S6Q3Y7Z0U, S75D0U4N7, S74RK0BL0, S7L8E8ZLN, SEMAPP3NC, S6K7SBNMB, S6H21EVPC, S05ADTBNHA8, S6J0KB406]
 *  *                                                     |range : [STRING] : [channel]
 *  *                                                     |type : [STRING] : [broadcast, rich_text_section, emoji, usergroup, link, channel, text, user]
 *  *                                                     |unsafe : [BOOLEAN] : [true]
 *  *                                                     |url : [STRING] : [mailto:Support@assist.ru, https://jira.ps/browse/ASIX-10835, http://dc6io00.ps, https://dc2mpi00.ps:18443, https://jira.ps/browse/SUPPORT-10492, https://jira.ps/browse/SUPPORT-8782, https://assist-ru.slack.com/archives/C7BV69B2M/p1702644616190659?thread_ts=1702455747.475349&cid=C7BV69B2M, https://dc1mpi01.ps:18443, https://jira.ps/browse/SUPPORT-10344, https://assist-ru.slack.com/archives/C7BV69B2M/p1652811193414479]
 *  *                                                     |user_id : [STRING] : [U7650B1QR, U02Q1M72C3H, UDSHTE493, U5V3XLVRQ, U7CFASP4Y, U6SKJ7EGG, U787XN7CJ, UK8FFL74Y, U014NQ1EX7U, UAXK67GR2]
 *  *                                                     |elements : OBJECT
 *  *                                                         |elements : ARRAY
 *  *                                                             |user_id : [STRING] : [UDSHTE493, U5E772ESH, U59D59XGX, U5V3XLVRQ, U787XN7CJ, U6SKJ7EGG, UHL854VUN, U0JAUDWAU, U014NQ1EX7U, UAXK67GR2]
 *  *                                                             |name : [STRING] : [thonking-in-pragriss, dancerbanana, heavy_minus_sign, melting_face, tada, white_check_mark]
 *  *                                                             |unicode : [STRING] : [1fae0, 2796, 2705, 1f389]
 *  *                                                             |style : OBJECT
 *  *                                                                 |code : [BOOLEAN] : [true]
 *  *                                                                 |unlink : [BOOLEAN] : [true]
 *  *                                                                 |strike : [BOOLEAN] : [true, false]
 *  *                                                                 |bold : [BOOLEAN] : [true, false]
 *  *                                                                 |italic : [BOOLEAN] : [true, false]
 *  *                                                             |text : [STRING] : [Договорится с Озоном по включению для теста на боевой, согласовать время,  (если уже готово), Провести тест на ТИ, проверить что дисп не сломается, Надежда разрешила заливаться после 13:00 на ти  - по идее можем залить наши ,   и , Включить ПС UPI на боевой, Добавить с правильной настройкой для ПЦ Халык банк, Марина к сожалению заболела и сегодня не сможет проверить их, надеемся на завтра, Халык банк по вопросам по UPI нас ожидаемо послал: "его хата с краю ничего не знает, пробуйте на боевой что то провести мы посмотрим"(Переврал их слова но суть та же), поэтому, надо будет обсудить план действий как будем это пытаться проверить. Мои заметки и идеи:, Узнать у Халыка на какой терминал нам надо настроить UPI]
 *  *                                                             |type : [STRING] : [emoji, link, channel, text, user]
 *  *                                                             |unsafe : [BOOLEAN] : [true]
 *  *                                                             |channel_id : [STRING] : [G7411DR8E, C7GV3HJ3E, C01QB5K7RCK, C7BV69B2M, C01Q07BKP8B, CHX1ZD1C6, C0J6WK7S7]
 *  *                                                             |url : [STRING] : [https://jira.ps/browse/ASIX-11184, https://assistuxui.invisionapp.com/console/share/K7R8TAESX2N/874867424, https://jira.ps/browse/ASIX-10559, https://jira.ps/browse/ASIX-10561, https://assist-ru.slack.com/archives/CP2GSGL86/p1666090112166999, https://jira.ps/browse/ASIX-10410, https://jira.ps/browse/ASIX-10543, https://jira.ps/browse/ASIX-8548, https://jira.ps/browse/ASIX-8944, https://jira.ps/browse/ASIX-8569]
 *  *                                                     |name : [STRING] : [disappointed, slightly_smiling_face, +1, hankey, wb, wide_eye_pepe, the_horns, white_check_mark, bangbang, smiley]
 *  *                                                     |unicode : [STRING] : [1f61e, 1f615, 1f603, 1f914, 203c-fe0f, 2705, 1f642, 1f44d, 1f389, 1f31a]
 *  *                                                     |style : OBJECT
 *  *                                                         |code : [BOOLEAN] : [true]
 *  *                                                         |unlink : [BOOLEAN] : [true]
 *  *                                                         |strike : [BOOLEAN] : [true, false]
 *  *                                                         |bold : [BOOLEAN] : [true]
 *  *                                                         |italic : [BOOLEAN] : [true, false]
 *  *                                                     |text : [STRING] : [должны были изначально так сделать,  ,   ,
 *  *                                                     |channel_id : [STRING] : [C0J6DM7JL, C7GV3HJ3E, C039ETCCK0D, C7G53JJQ0, C039W6QUA3Z, C02NV4V0G2E, C05QE6UFFHD, C7BV69B2M, C02MRCF4C1X, C0J6WK7S7]
 *  *                                             |style : [STRING] : [ordered, bullet]
 *  *                                             |type : [STRING] : [rich_text_section, rich_text_preformatted, rich_text_quote, rich_text_list]
 *  *                                     |type : [STRING] : [rich_text]
 *  *                                     |block_id : [STRING] : [3V7be, yu3Sj, 5oEKx, b2f, 3sPB5, wmkc7, Z/cd6, EvOA+, sxNS, e6fh]
 *  *                         |ts : [STRING] : [1681374569.993399, 1711524763.572869, 1711545198.595399, 1632494844.192500, 1711533846.467539, 1711548131.638899, 1662134077.985509, 1719389657.903459, 1654519676.690529, 1718752343.873669]
 */
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MessageBlock {
    Message message;
}
