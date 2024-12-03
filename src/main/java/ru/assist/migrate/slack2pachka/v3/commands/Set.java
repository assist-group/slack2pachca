package ru.assist.migrate.slack2pachka.v3.commands;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.command.annotation.Command;
import ru.assist.migrate.slack2pachka.v3.commands.handlers.SetChatArchivedHandlerImpl;
import ru.assist.migrate.slack2pachka.v3.commands.handlers.SetChatAdminHandlerImpl;
import ru.assist.migrate.slack2pachka.v3.service.chat.ChatService;

@Slf4j
@Command(command = "setv3", description = "Pachka set utilities", group = "set v3")
@FieldDefaults(level = AccessLevel.PRIVATE)
@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
public class Set {
    final ChatService chatService;
    final SetChatArchivedHandlerImpl setChatArchivedHandler;
    final SetChatAdminHandlerImpl setChatAdminHandler;

    @Autowired
    public Set(ChatService chatService, SetChatAdminHandlerImpl setChatAdminHandler, SetChatArchivedHandlerImpl setChatArchivedHandler) {
        this.setChatAdminHandler = setChatAdminHandler;
        this.setChatArchivedHandler = setChatArchivedHandler;
        this.chatService = chatService;
    }

    @Command(command = "archived", description = "set chat archived (slack based)")
    private void tags() {
        chatService.list(setChatArchivedHandler);
    }

    @Command(command = "admin", description = "set chat admin (slack based)")
    private void admin() {
        chatService.list(setChatAdminHandler);
    }
}
