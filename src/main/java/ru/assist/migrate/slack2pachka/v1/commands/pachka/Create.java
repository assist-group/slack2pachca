package ru.assist.migrate.slack2pachka.v1.commands.pachka;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.command.CommandRegistration;
import org.springframework.shell.command.annotation.Command;
import org.springframework.shell.command.annotation.Option;
import ru.assist.migrate.slack2pachka.v1.model.users.User;
import ru.assist.migrate.slack2pachka.v1.service.user.UserService;

@Slf4j
@Command(command = "create", description = "Pachka create utilities", group = "create")
@FieldDefaults(level = AccessLevel.PRIVATE)
@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
public class Create {
    final UserService userService;

    @Autowired
    public Create(UserService userService) {
        this.userService = userService;
    }

    @Command(command = "user", description = "create user")
    public void user(
            @Option(required = true,
                    arity = CommandRegistration.OptionArity.EXACTLY_ONE,
                    longNames = "email",
                    shortNames = 'e',
                    description = "email"
            ) String email,
            @Option(
                    arity = CommandRegistration.OptionArity.EXACTLY_ONE,
                    longNames = "role",
                    shortNames = 'r',
                    description = "role",
                    defaultValue = "user"
            ) String role,
            @Option(
                    arity = CommandRegistration.OptionArity.EXACTLY_ONE,
                    longNames = "phone_number",
                    shortNames = 'p',
                    description = "phone_number"
            ) String phone_number,
            @Option(required = true,
                    arity = CommandRegistration.OptionArity.EXACTLY_ONE,
                    longNames = "first_name",
                    shortNames = 'f',
                    description = "first_name"
            ) String first_name,
            @Option(required = true,
                    arity = CommandRegistration.OptionArity.EXACTLY_ONE,
                    longNames = "last_name",
                    shortNames = 'l',
                    description = "last_name"
            ) String last_name,
            @Option(required = true,
                    arity = CommandRegistration.OptionArity.EXACTLY_ONE,
                    longNames = "nickname",
                    shortNames = 'n',
                    description = "nickname"
            ) String nickname,
            @Option(
                    arity = CommandRegistration.OptionArity.EXACTLY_ONE,
                    longNames = "suspended",
                    shortNames = 's',
                    description = "suspended",
                    defaultValue = "false"
            ) boolean suspended
    ) {
        User.UserBuilder userBuilder = getUserBuilder(
                email, role, phone_number, first_name, last_name, nickname, suspended);

        User.Request request = User.Request.builder()
                .user(userBuilder.build())
                .skip_email_notify(false)
                .build();
        userService.create(request);
    }

    static User.UserBuilder getUserBuilder(@Option(required = true, arity = CommandRegistration.OptionArity.EXACTLY_ONE, longNames = "email", shortNames = 'e', description = "email") String email, @Option(arity = CommandRegistration.OptionArity.EXACTLY_ONE, longNames = "role", shortNames = 'r', description = "role", defaultValue = "user") String role, @Option(arity = CommandRegistration.OptionArity.EXACTLY_ONE, longNames = "phone_number", shortNames = 'p', description = "phone_number") String phone_number, @Option(required = true, arity = CommandRegistration.OptionArity.EXACTLY_ONE, longNames = "first_name", shortNames = 'f', description = "first_name") String first_name, @Option(required = true, arity = CommandRegistration.OptionArity.EXACTLY_ONE, longNames = "last_name", shortNames = 'l', description = "last_name") String last_name, @Option(required = true, arity = CommandRegistration.OptionArity.EXACTLY_ONE, longNames = "nickname", shortNames = 'n', description = "nickname") String nickname, @Option(arity = CommandRegistration.OptionArity.EXACTLY_ONE, longNames = "suspended", shortNames = 's', description = "suspended", defaultValue = "false") boolean suspended) {
        User.UserBuilder userBuilder = User.builder();

        userBuilder.bot(false);
        if (email != null) userBuilder.email(email);
        if (role != null) userBuilder.role(role);
        if (phone_number != null) userBuilder.phone_number(phone_number);
        if (first_name != null) userBuilder.first_name(first_name);
        if (last_name != null) userBuilder.last_name(last_name);
        if (nickname != null) userBuilder.nickname(nickname);
        userBuilder.suspended(suspended);

        return userBuilder;
    }
}

