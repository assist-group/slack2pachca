package ru.assist.migrate.slack2pachka.v1.commands.test;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.assist.migrate.slack2pachka.ImportRuntimeException;
import ru.assist.migrate.slack2pachka.v1.commands.test.utils.Utils;
import ru.assist.migrate.slack2pachka.v1.model.channels.Channel;
import ru.assist.migrate.slack2pachka.v1.model.messages.FileMeta;
import ru.assist.migrate.slack2pachka.v1.model.messages.Message;
import ru.assist.migrate.slack2pachka.v1.model.messages.MessageFile;
import ru.assist.migrate.slack2pachka.v1.model.messages.MessageThread;
import ru.assist.migrate.slack2pachka.exceptions.TooManyRequestsRuntimeException;
import ru.assist.migrate.slack2pachka.v1.service.channel.ChannelService;
import ru.assist.migrate.slack2pachka.v1.service.message.FileService;
import ru.assist.migrate.slack2pachka.v1.service.message.MessageService;
import ru.assist.migrate.slack2pachka.v1.service.message.ThreadService;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Objects;

@Component
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MessageTest {
    final MessageService messageService;
    final ChannelService channelService;
    final FileService fileService;
    final ThreadService threadService;

    @Autowired
    public MessageTest(MessageService messageService, ChannelService channelService, FileService fileService, ThreadService threadService) {
        this.messageService = messageService;
        this.channelService = channelService;
        this.fileService = fileService;
        this.threadService = threadService;
    }

    public void crud(long chat_id) {

        Message message = createArtificalMessage(chat_id);
        Message.Request request = Message.Request.builder()
                .message(message)
                .build();
        // create
        message = messageService.create(request);
        // read
        message = messageService.read(message.getId());
        //update
        message.setContent("Updated content " + Utils.generateRandomString(20));
        message.setThread(createMessageThread(message.getId()));
        message.setFiles(new MessageFile[]{createMessageFile()});
        request = Message.Request.builder()
                .message(message)
                .build();
        message = messageService.update(request, message.getId());
        // ensure updated
        message = messageService.read(message.getId());
        // delete
//        messageService.delete(message.getId());
    }

    public void listSimpleTest(long chat_id) {
        //create list

        for (int i = 0; i < 10; i++) {
            Message message = createArtificalMessage(chat_id);
            Message.Request request = Message.Request.builder()
                    .message(message)
                    .build();
            messageService.create(request);
        }

        // list
        Message[] messages = messageService.list(chat_id);
        //drop all
        int i = 0;
        for (Message message : messages) {
            message.setContent("ArtificalMessage " + Utils.generateRandomString(12) + i++);
            Message.Request request = Message.Request.builder()
                    .message(message)
                    .build();

            try {
                messageService.update(request, message.getId());
            } catch (TooManyRequestsRuntimeException e) {
                log.info(e.getMessage());
            }
        }
    }

    private Message createArtificalMessage(long chat_id) {

/*

entity_type	string	Тип сущности: беседа или канал (по умолчанию, discussion), пользователь (user), тред (thread). Для создания треда к сообщению воспользуйтесь методом новый тред.
entity_id	integer*	Идентификатор сущности
content	string*	Текст сообщения
files	array of objects	Прикрепляемые файлы
key	string*	Путь к файлу, полученный в результате загрузки файла (каждый файл в каждом сообщении должен иметь свой уникальный key, не допускается использование одного и того же key в разных сообщениях)
name	string*	Название файла, которое вы хотите отображать пользователю (рекомендуется писать вместе с расширением)
file_type	string*	Тип файла: файл (file), изображение (image)
size	integer*	Размер файла в байтах, отображаемый пользователю
parent_message_id	integer	Идентификатор сообщения. Указывается в случае, если вы отправляете ответ на другое сообщение.

*/
        return Message.builder()
                .entity_type("discussion")
                .entity_id(chat_id)
                .content("ArtificalMessage " + Utils.generateRandomString(10))
                .files(new MessageFile[]{})
                .build();
    }

    private MessageThread createMessageThread(long message_id) {
        ru.assist.migrate.slack2pachka.v1.model.messages.Thread thread = threadService.create(message_id);
        return MessageThread.builder()
                .chat_id(thread.getChat_id())
                .id(thread.getId())
                .build();
    }

    private MessageFile createMessageFile() {
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();

        try (InputStream is = classloader.getResourceAsStream("empty.file")) {
            Path tempDirWithPrefix = Files.createTempDirectory("test");
            File file = new File(tempDirWithPrefix.toFile(), "empty.file");
            Files.copy(Objects.requireNonNull(is), file.toPath(), StandardCopyOption.REPLACE_EXISTING);
            FileMeta fileMeta = fileService.upload(file);
            String key = fileMeta.getKey().replaceAll("\\$\\{filename}", file.getName());

            return MessageFile.builder()
                    .file_type("file")
                    .key(key)
                    .name(file.getName())
                    .size(file.length())
                    .build();
        } catch (IOException e) {
            throw new ImportRuntimeException(e);
        }
    }

    public Channel createTestChannel() {

        Channel.Request request = Channel.Request.builder()
                .chat(Channel.builder()
                        .channel(false)
                        .name("MessageTestChannel")
                        .isPublic(true)
                        .build())
                .build();
        return channelService.create(request);
    }
}

