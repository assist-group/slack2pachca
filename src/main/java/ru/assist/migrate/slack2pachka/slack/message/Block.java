package ru.assist.migrate.slack2pachka.slack.message;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

/**
 |blocks : OBJECT
 |blocks : ARRAY
 |image_url : [STRING] : [https://media0.giphy.com/media/iI3E2tikmuPpS/giphy.gif?cid=6104955ea9b1a3e7bcc28b43c56843d8d7f38a663a632f53&rid=giphy.gif, https://media0.giphy.com/media/9vk7uNCSJaOqI/giphy.gif?cid=6104955e264b807a56173e04bcfc0ae6680e984aed7e9ea2&rid=giphy.gif, https://media1.giphy.com/media/r7VpvFikTrNFS/giphy.gif?cid=6104955e76d3b5fd092c12d52871336ea5664cd4c869d46d&rid=giphy.gif, https://media1.giphy.com/media/3ornjIhZGFWpbcGMAU/giphy.gif?cid=6104955e6c0c9d0a16ef517332fdaa3ec5201c73da23c234&rid=giphy.gif, https://media2.giphy.com/media/135E47VKw6TM6A/giphy.gif?cid=6104955ec46b446483b953d7a01435e8acfed6a5c4c8e085&rid=giphy.gif, https://media2.giphy.com/media/l0MYSDPzRo6fRZVBu/giphy.gif?cid=6104955ec2703a814ca1a7671c80790e3aeb82197680fe32&rid=giphy.gif, https://media4.giphy.com/media/fKk2I5iiWGN0I/giphy.gif?cid=6104955ec4af68aeb49df214db84b880e1f31cd3f02047fb&rid=giphy.gif, https://media0.giphy.com/media/l0MYJnJQ4EiYLxvQ4/giphy-downsized.gif?cid=6104955e132a86abc4bf53f5b1750c161eb0d0fe3772948c&rid=giphy-downsized.gif, https://media1.giphy.com/media/yM4RN5fNEwrcc/giphy.gif?cid=6104955e69d99cd91ef4055d22b65b53123b82b7b169eb65&rid=giphy.gif, https://media1.giphy.com/media/A2RiDrLMc5Qys/giphy.gif?cid=6104955ekhplzffz3tbc3dskwabsvjw9rpuz1dp8gsz2pq9n&ep=v1_gifs_translate&rid=giphy.gif&ct=g]
 |rotation : [NUMBER] : [0]
 |image_width : [NUMBER] : [320, 200, 300, 400, 334, 500, 339, 480, 250, 295]
 |type : [STRING] : [call, image, divider, rich_text, context, header, section, actions]
 |title : OBJECT
 |emoji : [BOOLEAN] : [true]
 |text : [STRING] : [шутники блин, а я думал что-то серьезное сломалось, kumamon, agent, oxxxymiron, applauses, italiano, dead agent, я не знаю, Максим, что скажешь?, ninja smoke, flash]
 |type : [STRING] : [plain_text]
 |block_id : [STRING] : [aXKQ, eA7lz, SLQZ, Mvs+, 4f+E, DHnc2, MoEja, 3EL, qEcm, wag]
 |image_bytes : [NUMBER] : [1360313, 1757779, 1557308, 702726, 428392, 1505586, 1094831, 967213, 488758, 862862]
 |call_id : [STRING] : [R0140G24YFP, R013ZTXH1A6, R0116LFGPJ6, R01252HKAJK, R014T0W1BU1, R0116FVA43C, R015FRN9VAL, R014VC7JMAR, R01584J4WJX, R034NFKPDQS]
 |call : OBJECT
 |media_backend_type : [STRING] : [platform_call]
 |v1 : OBJECT
 |app_icon_urls : OBJECT
 |image_32 : [STRING] : [https://slack-files2.s3-us-west-2.amazonaws.com/avatars/2017-09-21/245295805989_06e77af1bfb8e3c81d4d_32.png]
 |image_64 : [STRING] : [https://slack-files2.s3-us-west-2.amazonaws.com/avatars/2017-09-21/245295805989_06e77af1bfb8e3c81d4d_64.png]
 |image_96 : [STRING] : [https://slack-files2.s3-us-west-2.amazonaws.com/avatars/2017-09-21/245295805989_06e77af1bfb8e3c81d4d_96.png]
 |image_36 : [STRING] : [https://slack-files2.s3-us-west-2.amazonaws.com/avatars/2017-09-21/245295805989_06e77af1bfb8e3c81d4d_36.png]
 |image_1024 : [STRING] : [https://slack-files2.s3-us-west-2.amazonaws.com/avatars/2017-09-21/245295805989_06e77af1bfb8e3c81d4d_1024.png]
 |image_192 : [STRING] : [https://slack-files2.s3-us-west-2.amazonaws.com/avatars/2017-09-21/245295805989_06e77af1bfb8e3c81d4d_192.png]
 |image_48 : [STRING] : [https://slack-files2.s3-us-west-2.amazonaws.com/avatars/2017-09-21/245295805989_06e77af1bfb8e3c81d4d_48.png]
 |image_72 : [STRING] : [https://slack-files2.s3-us-west-2.amazonaws.com/avatars/2017-09-21/245295805989_06e77af1bfb8e3c81d4d_72.png]
 |image_512 : [STRING] : [https://slack-files2.s3-us-west-2.amazonaws.com/avatars/2017-09-21/245295805989_06e77af1bfb8e3c81d4d_512.png]
 |image_128 : [STRING] : [https://slack-files2.s3-us-west-2.amazonaws.com/avatars/2017-09-21/245295805989_06e77af1bfb8e3c81d4d_128.png]
 |join_url : [STRING] : [https://applications.zoom.us/event/callback/slack/76406927936?startUrl=N9U1pgb7fmOQGAor04MgwftbMWH6apyirX8MNstOC-Dt3mnUMbnxaeXSEPH1YaO70p9pBZl4Cu9nuEkS02Hjk9iBtSinnYFT-wKXpWimCppLQFyeTf5dmDId&state=JIM1sxSsGCjHURhLlKx0rMZxClj2M83-qzFYa6oNWKuWjXLdJdO0J8XaYpj0bOP7gJNSD7l3IcRkkz5wuBP-jpmOpHOi0scF6Vg6TCq1rrYRk4ioh4CVreQvGfTyxqMLm7M93PUlYmvx_9XsHqNCd7rXypf0DMM3bqdsaGHx5mQN6GGNc_YnQmZCfLGiio9G8c2Sn36f8QrmHQncS_cG0q7UMEU-z1J52fhGgH9A2EVZM6redXVXH5sE904BheTp3Out, https://applications.zoom.us/event/callback/slack/72795387294?startUrl=N9U1pgb7fmOQGAor04MgwftbMWH6apyirX8MMshHCOrn3mLeM7nxaeXSPof9RZa88KloBI1cCeBsxUse1BPgnsSOggaFk7R9-wIOrJqpIWDuKAk_SNdUaNjy&state=JIM1sxSsGCjHURhLlKx0rMZxClj2M83-qzFYa6oNWKuWjXLdJdO2KLTfGfHiZuP7gJNSD7l3IcRkkz5wuBOBnPzmqnOk1McF6Vg6TCq1rrYRk4ioh_agvelMDOKLxpkfoNhb_9UwAmfA88XsHqNCd7vRwJr2A8gzb6RoaGHx5mQN6GGNc_YnQmZCfLGiio9G8c2Sn36f8QrmHQncS_cG0q7UMEU-z1J92vFFinVA009bM6qbQgCofVVC9QFw0Rhd3omq, https://applications.zoom.us/event/callback/slack/76836599339?startUrl=N9U1pgb7fmOQGAor04MgwftbMWH6apyirX8MNsdNC-zm0GPUPrnxaeXSDK7DdoyT7LVuO4F4C_1rrngk12T65eKMtRWVqLR9-wJ0xl1PPMCYen7yZXLOMybT&state=JIM1sxSsGCjHURhLlKx0rMZxClj2M83-qzFYa6oNWKuWjXLdJdO0J8XaYpj0bOP7gJNSD7l3IcRkkz5wuBP-jpmOpHOi0scF6Vg6TCq1rrYRk4ioh_mjhsdMMfyoxvoDwZ0c8vRkSWPd9vPsHqNCd7rXyJv1DMI1aqRvaGHx5mQN6GGNc_YnQmZCfLGiio9G8c2Sn36f8QrmHQncS_cG0q7UMEU-z1J51ftGjHRO0kVWM6ou7Bljj9ThbXEtoLIZvM3u, https://applications.zoom.us/event/callback/slack/367973271?startUrl=N9U1pgb7fmOQGAor04MgwftbMWH6apyirX8INshHCurt3mE-YlqROSyf58MRtV4S3Mmc&state=JIM1sxSsGCjHURhLlKx0rMZxClj2M83-qzFYa6oNWKuWjXLdJdO0J8XaYpj0bOP7gJNSD7l3IcRkkz5wuBPy4-2Hsw-woscF6Vg6TCq1rrYRk4ioh_iC0-4RUoqdweMbgp9dzNA7fwGV6cXsHqNCd7vQyJX_DMk1ba5vaGHx5mQN6GGNc_YnQmZCfLGiio9G8c2Sn36f8QrmHQncS_cG0q7UMEU-z1Z52vFHin9A0FQSYL3UWoARVHtk2khIlqSiIg, https://applications.zoom.us/event/callback/slack/75666509425?startUrl=N9U1pgb7fmOQGAor04MgwftbMWH6apyirX8MNclIC-zv0GTVMrnxaeXSDYTbAaS65KZgKLtoJsN3pFEw_hvTgsCArHCOkoFT-wJfQcTZQlIsSA6_AGWkDPq4&state=JIM1sxSsGCjHURhLlKx0rMZxClj2M83-qzFYa6oNWKuWjXLdJdO2KLTfGfHiZuP7gJNSD7l3IcRkkz5wuBOG55uF0A7WstNj6Rd5QyeQprQQoc_rh4vl3p0qDcHv4uc8vqgD_vZlGAjlsurpYsFOHbLTwJL2As4xaqNocnnxqzYc1HS3c-xgFGYNLaGjsJF8682I2D6fsE3uDAXGRdcU8bncIAJ2z19t2v1Gj3tC0U9bI-KKP3DMum9noU00LM6Ko05ZHJE, https://applications.zoom.us/event/callback/slack/71235650125?startUrl=VkVSU046MDAwQ5czsTA7T0eRWTGR4lSU6pKwn91s_5X5A_GG0vJYoXltzz5y5fcpQUs5ctRzww7H_F8KNIpWpM1QU2uzaILnJwDONRTtxR0hL3c1-9GBxsEhgRmwCRPrDFbm_mpwfaE5Db52r_WK&state=VkVSU046MDAwaOVkKgssQiKyYh83WjiHq49fDPOTsCouHtCn6OEj3vO9eHgNJK07-S453VLSWPrGwC70MueaZ-1Hz12F9YAg2a4vra1TfDiqVbttD3_QsevvYODyJwzQ1Sn37I7NtFhxi3S985aA62mK5HysEVDgn-6zFAIC6uBiV4G6THvziK8AXzfUj-LcPfO6kU6pppBO3BjKXU-blSNgl-TEfovAKP5LcbM5JKpQ5S-dKd59U7lpOKLzUDyWd7H6qkAQVAKFOyhVzhC5PEcag-jhiMOmQro, https://applications.zoom.us/event/callback/slack/273812395?startUrl=N9U1pgb7fmOQGAor04MgwftbMWH6apyirX8JN8xGDOvs0GVOfnfkW5xAjRhBt0j2k7wq&state=JIM1sxSsGCjHURhLlKx0rMZxClj2M83-qzFYa6oNWKuWjXLdJdO0J8XaYpj0bOP7gJNSD7l3IcRkkz5wuBPy4-2Hsw-woscF6Vg6TCq1rrYRk4ioh-j_zM4iKuCOwKFcursB8eUmYif_38XsHqNCd7vQyJr0B8o0bKNhaGHx5mQN6GGNc_YnQmZCfLGiio9G8c2Sn36f8QrmHQncS_cG0q7UMEU-z1d43vBBi35O1FQSzwjj5VDLrfvW7f4oCBpnmw, https://applications.zoom.us/event/callback/slack/75549574705?startUrl=N9U1pgb7fmOQGAor04MgwftbMWH6apyirX8MNcpKBOzo3WfXMrnxaeXSCpjlcJST64J3OaBvEf5Cj1J502Pi5J21ggWRtbB9-wJBFiuqA9HZEnOa1BGSsL7l&state=JIM1sxSsGCjHURhLlKx0rMZxClj2M83-qzFYa6oNWKuWjXLdJdO2KLTfGfHiZuP7gJNSD7l3IcRkkz5wuBOG55uF0A7WstNj6Rd5QyeQprQQoc_rh4vlheEFM4a267A8gJoDysUfRyue6tCmRMFOHbLTwJLwAsI4aK9ufXXxqzYc1HS3c-xgFGYNLaGjsJF8682I2D6fsE3uDAXGRdcU8bncIAJ2z19t2v1FjXRC1kJYIeKKP3uHOMJJKOPwDyTmdBxnj_c, https://applications.zoom.us/event/callback/slack/74303912158?startUrl=N9U1pgb7fmOQGAor04MgwftbMWH6apyirX8MNMxODuDu22HSP7nxaeXSDoT2Tpuf4IVpBaMoHcVBtVMcyAnTuOKnrjik0LR9-wLpikQRNZh317QmO0pNzOgA&state=JIM1sxSsGCjHURhLlKx0rMZxClj2M83-qzFYa6oNWKuWjXLdJdO2KLTfGfHiZuP7gJNSD7l3IcRkkz5wuBPy7-qFpRuitccF6Vg6TCq1rrYRk4ioh_aQ1M8aUvyPxp4Dx54cjuBkexfi0tXsHqNCd7vfwJr_DMs5aKZuaGHx5mQN6GGNc_YnQmZCfLGiio9G8c2Sn36f8QrmHQncS_cG0q7UMEU-z1J73vhDgHxF0ENXM6p4sZ7oes4yYNhL16X-RRn9, https://us04web.zoom.us/j/79029034017?pwd=4n4rL5AbbDSkk1yyl-NlSmhA47GesF.1]
 |desktop_app_join_url : [STRING] : [zoommtg://us04web.zoom.us/join?confid=dXRpZD1VVElEXzdhODFkNjVkZGFlYjQ5OGNhM2YwNzAyZTAyNDQ4ZWUwJnVzcz1xdlBLN013eThNSXZQbmxDZFZIYk1IZS1BeTRqTmVPSXIxem9mSFNrcGR4QVRtR3V3S2RhV0p3aE4wSXBfeng2ZExxdkRBb1hNQkU0YnVQdWhiUmlkbldKWGFvWTNjbG5lLVZvLkpzRXJWdU5BQUoyYmR0cjc%3D&action=join&confno=79029034017&pwd=4n4rL5AbbDSkk1yyl-NlSmhA47GesF.1]
 |display_id : [STRING] : [764-0692-7936, 756-6650-9425, 727-9538-7294, 790-2903-4017, 273-812-395, 768-3659-9339, 712-3565-0125, 755-4957-4705, 743-0391-2158, 367-973-271]
 |date_end : [NUMBER] : [1590064235, 1590668919, 1661933027, 1587980783, 1589989879, 1591867697, 1645898248, 1718421725, 1586179895, 1586182364]
 |created_by : [STRING] : [U010131V5ND]
 |was_rejected : [BOOLEAN] : [false]
 |was_accepted : [BOOLEAN] : [false]
 |active_participants : OBJECT
 |date_start : [NUMBER] : [1591193885, 1586179835, 1591865897, 1645783258, 1591352897, 1587980723, 1586182304, 1589989819, 1590064175, 1590668859]
 |channels : OBJECT
 |channels : [STRING] : [CD4CJM53M, C7JT2J1E0, C011YCCU28N, C014L5A7NN5, CD9BQEYCQ, CD5ESSMQF, C01D46MPA02, G9V05BULT, C013Q0L7V6J, CHX1ZD1C6]
 |was_missed : [BOOLEAN] : [false]
 |all_participants : OBJECT
 |all_participants : ARRAY
 |avatar_url : [STRING] : []
 |slack_id : [STRING] : [U7650B1QR, U59D59XGX, U6FBW6RE2, U0J6EU23Z, U1P7QDB6V]
 |external_id : [STRING] : [16786432, 16783360, 16784384, 16787456, 16780288, 16781312, 16779264, 16782336, 16788480, 16785408]
 |display_name : [STRING] : [Serg, Михаил Каменцев, Тема Чесноков, vanisimov, tbelousov, Evgeny Ladvez, vandreev, KIRILL IVANOV, Таня Яковенко, Vitalii]
 |name : [STRING] : [Zoom meeting started by opinaeva, Zoom meeting started by achesnokov, stendup started by opinaeva, Zoom meeting started by sergeyk]
 |is_dm_call : [BOOLEAN] : [false]
 |id : [STRING] : [R0140G24YFP, R013ZTXH1A6, R0116LFGPJ6, R01252HKAJK, R014T0W1BU1, R0116FVA43C, R015FRN9VAL, R014VC7JMAR, R01584J4WJX, R034NFKPDQS]
 |app_id : [STRING] : [A5GE9BMQC]
 |has_ended : [BOOLEAN] : [true]
 |is_animated : [BOOLEAN] : [true, false]
 |image_height : [NUMBER] : [275, 257, 214, 335, 270, 282, 150, 261, 371, 240]
 |api_decoration_available : [BOOLEAN] : [false]
 |alt_text : [STRING] : [шутники блин, а я думал что-то серьезное сломалось, kumamon, agent, oxxxymiron, applauses, italiano, dead agent, я не знаю, Максим, что скажешь?, ninja smoke, flash]
 |elements : OBJECT
 |elements : ARRAY
 |text : OBJECT
 |emoji : [BOOLEAN] : [true]
 |text : [STRING] : [<@U7E5Q1BT9> clicked *Сделано*, Meeting password: K1FUblpoSkNaNGJOUXI1UEpUUjFydz09, *выбираем*, :doge: Добрый вечер <!subteam^S7L8E8ZLN|@tp1>!
 |type : [STRING] : [plain_text, mrkdwn]
 |verbatim : [BOOLEAN] : [false, true]
 |fields : OBJECT
 |fields : ARRAY
 |text : [STRING] : [Понедельник

 |type : [STRING] : [mrkdwn]
 |verbatim : [BOOLEAN] : [true, false]
 |fallback : [STRING] : [200x150px image, 500x371px image, 320x240px image, 295x257px image, 480x282px image, 480x270px image, 500x214px image, 334x335px image, 400x275px image, 339x261px image]
 |accessory : OBJECT
 |action_id : [STRING] : [vote-1, vote-2, PollMessage(yZegAjeDBc7PtLB7K).PollMessageOverflowMenu(), title-and-menu, vote-52095539, vote-44867120, vote-44867121, vote-52095540, vote-3, PollMessage(Bp6HToiCf9uvuou8N).PollMessageOverflowMenu()]
 |options : OBJECT
 |options : ARRAY
 |text : OBJECT
 |emoji : [BOOLEAN] : [true]
 |text : [STRING] : [:information_source: View info, :gear: Go to Settings, :x: Delete Poll, :bar_chart: Create new Poll, :house: Go to App Home, Settings, :unlock: Reopen Poll, :recycle: Recreate this Poll, :pushpin: Capture Decision from Poll, :information_source: Edit Poll]
 |type : [STRING] : [plain_text]
 |value : [STRING] : [view_info, settings, delete_poll, create_new_poll, go_to_app_home, go_to_settings_from_poll, capture_decision_from_poll, reopen_poll, create_poll_from_recent_poll, close_poll]
 |url : [STRING] : [slack://app?team=T0J6CGETT&id=A0HFW7MR6&tab=home, https://simplepoll.rocks/dashboard/assist-ru/settings/]
 |text : OBJECT
 |emoji : [BOOLEAN] : [true]
 |text : [STRING] : [:four:, :five:, :one:, :three:, :two:]
 |type : [STRING] : [plain_text]
 |type : [STRING] : [button, overflow]
 */
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Block {
    String type;
    String block_id;
    Element[] elements;
}
