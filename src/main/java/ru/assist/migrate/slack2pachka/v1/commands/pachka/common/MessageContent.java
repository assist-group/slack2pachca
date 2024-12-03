package ru.assist.migrate.slack2pachka.v1.commands.pachka.common;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import ru.assist.migrate.slack2pachka.ImportRuntimeException;
import ru.assist.migrate.slack2pachka.SpringRestclientAppApplication;
import ru.assist.migrate.slack2pachka.slack.channel.SlackChannel;
import ru.assist.migrate.slack2pachka.slack.message.*;
import ru.assist.migrate.slack2pachka.slack.user.SlackUser;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MessageContent {
    private final SlackData slackData;

    public MessageContent(SlackData slackData) {
        this.slackData = slackData;
    }

    private static final ObjectMapper MAPPER = getObjectMapper();

    private static ObjectMapper getObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.configure(DeserializationFeature.USE_JAVA_ARRAY_FOR_JSON_ARRAY, true);
        return objectMapper;
    }

    private static final Map<String, String> emojiMap = loadSmiles();

    @SuppressWarnings("unchecked")
    public static Map<String, String> loadSmiles() {
        try {
            InputStream inputStream = SpringRestclientAppApplication.class.getClassLoader().getResourceAsStream("emoji.json");
            return MAPPER.readValue(inputStream, Map.class);
        } catch (IOException e) {
            throw new ImportRuntimeException(e);
        }
    }

    public String getContentExtended(SlackMessage slackMessage) {
        StringBuilder sb = new StringBuilder();
        String subtype = slackMessage.getSubtype();
//
// |subtype : [STRING] : [channel_leave, channel_join, tombstone, huddle_thread,
// thread_broadcast, sh_room_created, pinned_item,
// file_comment, slackbot_response, bot_message]

        if(subtype == null) {
            sb.append(processNormalMessage(slackMessage));
            return sb.toString();
        }
        switch (subtype) {
            case "channel_leave" -> sb.append(toUserTag(slackMessage.getUser())).append(" _has left the channel_");
            case "channel_join" -> sb.append(toUserTag(slackMessage.getUser())).append(" _has joined the channel_");
            case "tombstone", "slackbot_response" -> sb.append("SlackBot: ").append(slackMessage.getText());
            case "sh_room_created" -> sb.append("**call started**");
            case "pinned_item" ->
                    sb.append(toUserTag(slackMessage.getUser())).append(" _pinned a message to this channel._");
            case "file_comment" -> sb.append(getNormalizedMessage(slackMessage.getText()));
            case "bot_add", "bot_enable", "bot_disable", "channel_topic", "canvas_sharing_message", "sh_room_shared",
                 "document_mention", "channel_purpose", "channel_name", "channel_convert_to_public" ->
                    sb.append(toUserTag(slackMessage.getUser())).append(" _").append(getNormalizedMessage(slackMessage.getText())).append("_");
            case "document_comment_root" -> sb.append(processNormalMessage(slackMessage));
            case "group_join" -> sb.append(toUserTag(slackMessage.getUser())).append(" _has joined the group_");
            case "bot_remove", "group_purpose", "group_topic", "group_name", "channel_archive", "channel_unarchive",
                 "app_conversation_join" ->
                    sb.append(toUserTag(slackMessage.getUser())).append(" _").append(slackMessage.getText()).append("_");
            case "group_leave" -> sb.append(toUserTag(slackMessage.getUser())).append(" _has left the group_");
            case "reminder_add" -> sb.append(slackMessage.getText());

            //            case "channel_canvas_updated"-> sb.append(processNormalMessage(slackMessage));
            //            case "huddle_thread" -> sb.append(processNormalMessage(slackMessage));
            //            case "thread_broadcast" -> sb.append(processNormalMessage(slackMessage));
            //            case "bot_message" -> sb.append(processNormalMessage(slackMessage));
            //            case "document_unfurl" -> sb.append(processNormalMessage(slackMessage));
            //            case "reply_broadcast" -> sb.append(processNormalMessage(slackMessage));

            default -> sb.append(processNormalMessage(slackMessage));
        }
        return sb.toString();
    }

    private String getNormalizedMessage(String msgText) {
        if (msgText == null) return "";
        String result = msgText;

        Pattern pattern = Pattern.compile("(<[^><]+>)");
        Matcher matcher = pattern.matcher(result);

        while (matcher.find()) {
            result = result.replace(matcher.group(1), getLinkNormalized(matcher.group(1)));
        }

        pattern = Pattern.compile(":([a-z\\d_+]+):");
        matcher = pattern.matcher(result);

        while (matcher.find()) {
            result = result.replace(matcher.group(1), getEmojiNormalized(matcher.group(1)));
        }

        return result;
    }


    private String getEmojiNormalized(String emojiText) {
        String emoji = emojiMap.get(emojiText);
        return emoji == null ? (":" + emojiText + ":") : emoji;
    }

    private String getLinkNormalized(String link) {
        String trimmed = trimLink(link);
        if (link.startsWith("<http")) {
            return normalizeHTTPLink(trimmed);
        } else if (link.startsWith("<@")) {
            return normalizeUserLink(trimmed);
        } else if (link.startsWith("<!")) {
            return normalizeSubteamLink(trimmed);
        } else if (link.startsWith("<#")) {
            return normalizeChannelLink(trimmed);
        }
        return trimmed;
    }

    private String normalizeChannelLink(String trimmed) {
        String[] splitted = trimmed.replace("#", "").split("\\|");
        if (splitted.length >= 2) return String.format("[%s](%s)", splitted[1], "");
        if (splitted.length == 1) return String.format("%s", splitted[0]);
        return "";
    }

    private String normalizeHTTPLink(String trimmed) {
        String[] split = trimmed.split("\\|");
        if (split.length == 1) return split[0];
        if (split.length == 2) return String.format("[%s](%s)", split[1], split[0]);
        if (split.length > 2) {
            return String.format("[%s](%s)", split[1], split[0]);
        }

        return null;
    }

    private String normalizeSubteamLink(String trimmed) {
        String[] split = trimmed.split("\\|");

        String team = "@unknown-team";
        if (split.length == 1) team = split[0];
        if (split.length == 2) team = split[1];
        team = team.replace("!", "@");
        return String.format("[%s](%s)", team, "");
    }

    private String normalizeUserLink(String trimmed) {
        String slackUserId = trimmed.replace("@", "");
        SlackUser slackUser = slackData.getUsers().get(slackUserId);
        if (slackUser == null) return "@unknown";

        String nick = slackUser.getName();
        return "@" + (nick == null ? "unknown" : nick);
    }


    private String trimLink(String link) {
        return link.replaceAll("^<", "").replaceAll(">$", "");
    }

    private String toUserTag(String slackUserId) {
        SlackUser slackUser = slackData.getUsers().get(slackUserId);
        if (slackUser == null) return "@unknown";
        String nickname = slackUser.getName();
        return "@" + nickname;
    }

    private String processNormalMessage(SlackMessage slackMessage) {
        StringBuilder sb = new StringBuilder();
        sb
                .append("**")
                .append(getUserNamePrefix(slackMessage))
                .append("** ");

        Block[] blocks = slackMessage.getBlocks();
        Attachment[] attachments = slackMessage.getAttachments();
        if (blocks == null && attachments == null) {
            sb.append(getNormalizedMessage(slackMessage.getText()));
            return sb.toString();
        }

        if (blocks != null) {
            for (Block block : blocks) {
                Element[] elements = block.getElements();
                if (elements == null) continue;
                sb.append(processElements(elements));
            }
        }

        if (attachments != null) {
            for (Attachment attachment : attachments) {
                String fallback = attachment.getFallback();
                if (fallback == null) continue;
                sb
                        .append(getNormalizedMessage(fallback))
                        .append("  ");
            }
        }

        final Map<String, String> usergroupMap = new HashMap<>();
        Pattern pattern = Pattern.compile("<!subteam\\^(S[A-Z\\d]+)\\|(@[^@>]+)>");
        String text = slackMessage.getText();
        if(text != null) {
            Matcher matcher = pattern.matcher(text);
            while (matcher.find()) {
                usergroupMap.put(matcher.group(1), matcher.group(2).replace(" ", "_"));
            }
        }

        String result = sb.toString();
        for (Map.Entry<String, String> entry : usergroupMap.entrySet()) {
            result = result.replace(entry.getKey(), entry.getValue());
        }
        usergroupMap.clear();
        return result;
    }

    private String getUserNamePrefix(SlackMessage slackMessage) {
        UserProfile userProfile = slackMessage.getUser_profile();
        BotProfile botProfile = slackMessage.getBot_profile();

        if (userProfile == null && botProfile == null) {
            String userId = slackMessage.getUser();
            SlackUser slackUser = slackData.getUsers().get(userId);
            if (slackUser == null) return userId + " : ";
            String realName = slackUser.getReal_name();
            if (realName == null) return userId + " : ";

            return realName + " : ";
        }
        if (userProfile != null) {
            return slackMessage.getUser_profile().getReal_name() + " : ";
        }
        return slackMessage.getBot_profile().getName() + " : ";
    }

    private String processElements(Element[] elements) {
        if (elements == null) return "";
        StringBuilder sb = new StringBuilder();
        for (Element element : elements) {
            sb.append(processSingleElement(element));
        }
        return sb.toString();
    }

    final static String CITE = "\n```\n";

    private String processSingleElement(Element element) {
        StringBuilder sb = new StringBuilder();
        String type = element.getType();
        switch (type) {
            case "rich_text", "rich_text_section" -> sb.append(processElements(element.getElements()));
            case "rich_text_quote" -> sb.append(processQuote(element.getElements()));
            case "rich_text_list" -> sb.append(processList(element.getElements()));
            case "rich_text_preformatted" ->
                    sb.append(CITE).append(processElements(element.getElements())).append(CITE);
            case "text" -> sb.append(element.getText().getText());
            case "broadcast" -> sb.append("@channel ");
            case "emoji" -> sb.append(getEmojiString(element));
            case "color" -> {
                return sb.toString();
            }
            case "link" -> sb.append(getLinkString(element));
            case "channel" -> sb.append(getChannelName(element));
            case "usergroup" -> sb.append(element.getUsergroup_id());
            case "user" -> sb.append(toUserTag(element.getUser_id()));
            case "image" -> sb.append("[").append(element.getAlt_text()).append("]");
            case "button" -> sb.append("[").append(element.getText().getText()).append("]");
            case "mrkdwn" -> sb.append("[").append(getNormalizedMessage(element.getText().getText())).append("]");
            default -> throw new ImportRuntimeException(String.format("unknown type: %s", type));
        }
        return sb.toString();
    }

    private String processQuote(Element[] elements) {
        StringBuilder sb = new StringBuilder();
        sb.append(CITE);
        for (Element element : elements) {
            sb.append(processSingleElement(element));
        }
        sb.append(CITE);
        return sb.toString();
    }

    private String processList(Element[] elements) {
        StringBuilder sb = new StringBuilder();
        sb.append(CITE);
        int count = 1;
        for (Element element : elements) {
            sb.append(count).append(". ")
                    .append(processElements(element.getElements()))
                    .append("\n");
            count++;
        }
        sb.append(CITE);
        return sb.toString();
    }

    private String getChannelName(Element element) {
        Map<String, SlackChannel> slackChannels = slackData.getChannels();
        String slackChannelId = element.getChannel_id();
        SlackChannel slackChannel = slackChannels.get(slackChannelId);
        if (slackChannel == null) return "**_Unknown channel_**";
        return "**_" + slackChannel.getName() + "_**";
    }

    private String getEmojiString(Element element) {
        String emojiName = element.getName();
        String emoji = emojiMap.get(emojiName);
        return emoji == null ? (":" + emojiName + ":") : emoji;
    }

    private String getLinkString(Element element) {
        String url = element.getUrl();
        Text text = element.getText();
        String linkName = text == null ? url : text.getText();
        return String.format("[%s](%s) ", linkName, url);
    }


}
