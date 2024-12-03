package ru.assist.migrate.slack2pachka.v1.commands.pachka.common;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import ru.assist.migrate.slack2pachka.ImportRuntimeException;
import ru.assist.migrate.slack2pachka.v1.model.messages.Message;
import ru.assist.migrate.slack2pachka.v1.model.messages.MessageFile;
import ru.assist.migrate.slack2pachka.v1.model.messages.MessageThread;
import ru.assist.migrate.slack2pachka.slack.message.SlackMessage;
import ru.assist.migrate.slack2pachka.v1.service.message.MessageListHandler;
import ru.assist.migrate.slack2pachka.v1.service.message.MessageService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;

import static ru.assist.migrate.slack2pachka.v1.commands.pachka.common.PachkaData.getCreatedAsLocalDateTime;

@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class Handler implements MessageListHandler {
    final boolean content;
    final boolean attachments;
    final boolean thread;

    @Setter
    MessageService messageService;
    @Setter
    PachkaData pachkaData;

    Map<String, SlackMessage> ts2SlackMessage;
    Map<String, SlackMessage> time2SlackMessage;

    public Handler() {
        this.content = true;
        this.attachments = true;
        this.thread = true;
    }

    public void setTs2SlackMessage(Map<String, SlackMessage> ts2SlackMessage) {
        if (ts2SlackMessage.isEmpty()) {
            throw new ImportRuntimeException("no slack messages?");
        }
        time2SlackMessage = new HashMap<>();
        for (Map.Entry<String, SlackMessage> entry : ts2SlackMessage.entrySet()) {
            SlackMessage slackMessage = entry.getValue();
            String created = getCreatedAsLocalDateTime(slackMessage).format(DateTimeFormatter.ISO_DATE_TIME);
            LocalDateTime ldt = LocalDateTime.parse(created, DateTimeFormatter.ISO_DATE_TIME);
            ldt = ldt.truncatedTo(ChronoUnit.SECONDS);
            created = ldt.format(DateTimeFormatter.ISO_DATE_TIME);
            if (time2SlackMessage.containsKey(created)) {
                log.info(slackMessage.getText());
            }
            time2SlackMessage.put(created, slackMessage);
        }
        this.ts2SlackMessage = ts2SlackMessage;
    }

    @Override
    public void handle(Message[] messages) {
        for (Message message : messages) {
            String created_at = message.getCreated_at();

            LocalDateTime ldt = LocalDateTime.parse(created_at, DateTimeFormatter.ISO_DATE_TIME);
            ldt = ldt.truncatedTo(ChronoUnit.SECONDS);
            created_at = ldt.format(DateTimeFormatter.ISO_DATE_TIME);

            SlackMessage slackMessage = time2SlackMessage.get(created_at);
            if (slackMessage == null) {
                continue;
            }

            Message.MessageBuilder messageBuilder = Message.builder();

            if (attachments) {
                if (message.getFiles().length == 0) continue;
                if (slackMessage.getFiles() == null || slackMessage.getFiles().length == 0) continue;
                MessageFile[] files = pachkaData.toMessageFiles(slackMessage);
                messageBuilder.files(files);
            }
            if (content) {
                String content = pachkaData.toMessageContent(slackMessage);
                messageBuilder.content(content);
            }
            if (content || attachments) {
                MessageThread thread = message.getThread();
                if (thread != null) {
                    messageService.list(thread.getChat_id(), ts2SlackMessage.size(), this);
                }
                Message.Request request = Message.Request.builder()
                        .message(messageBuilder.build())
                        .build();
                messageService.update(request, message.getId());
            }

//            if (thread) {
//                pachkaData.toMessageThread(message.getId(), slackMessage, ts2SlackMessage);
//            }
        }
    }


}
