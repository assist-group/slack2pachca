package ru.assist.migrate.slack2pachka.v3.commands;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.command.annotation.Command;
import ru.assist.migrate.slack2pachka.v3.commands.handlers.ChatDropGroupsHandlerImpl;
import ru.assist.migrate.slack2pachka.v3.service.chat.ChatService;

@Slf4j
@Command(command = "dropv3", description = "Pachka drop utilities", group = "drop v3")
@FieldDefaults(level = AccessLevel.PRIVATE)
@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
public class DropV3 {
    final ChatService chatService;
    final ChatDropGroupsHandlerImpl chatDropGroupsHandler;

    @Autowired
    public DropV3(ChatService chatService, ChatDropGroupsHandlerImpl chatDropGroupsHandler) {
        this.chatDropGroupsHandler = chatDropGroupsHandler;
        this.chatService = chatService;
    }

    @Command(command = "tags", description = "drop chat tags")
    private void tags() {
        chatService.list(chatDropGroupsHandler);
    }
}
