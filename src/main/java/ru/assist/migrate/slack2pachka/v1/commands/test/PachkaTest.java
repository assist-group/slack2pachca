package ru.assist.migrate.slack2pachka.v1.commands.test;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.command.CommandRegistration;
import org.springframework.shell.command.annotation.Command;
import org.springframework.shell.command.annotation.Option;
import ru.assist.migrate.slack2pachka.v1.model.channels.Channel;

@Slf4j
@Command(command = "test", description = "Pachka API tests", group = "test")
@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PachkaTest {
    final UserTest userTest;
    final ChannelTest channelTest;
    final MessageTest messageTest;

    @Autowired
    public PachkaTest(ChannelTest channelTest, MessageTest messageTest, UserTest userTest) {
        this.channelTest = channelTest;
        this.messageTest = messageTest;
        this.userTest = userTest;
    }

    private Channel channel;

    @Command(command = "user", description = "user API tests")
    public void user(
            @Option(required = true,
                    arity = CommandRegistration.OptionArity.EXACTLY_ONE,
                    longNames = "mode",
                    shortNames = 'm') String mode
    ) {
        switch (mode) {
            case "all" -> userTest();
            case "crud" -> {
                userTest.crud();
                log.info("user crud test completed");
            }
            case "list" -> {
                userTest.listSimpleTest();
                log.info("user list test completed");
            }
            case "print" -> {
                userTest.print();
                log.info("user print users  test completed");
            }
            default -> log.warn("requested mode={}", mode);
        }
    }

    @Command(command = "channel", description = "channel API tests")
    public void channel(
            @Option(required = true,
                    arity = CommandRegistration.OptionArity.EXACTLY_ONE,
                    longNames = "mode",
                    shortNames = 'm') String mode
    ) {
        switch (mode) {
            case "all" -> channelTest();
            case "crud" -> {
                channelTest.crud();
                log.info("channel crud test completed");
            }
            case "list" -> {
                channelTest.listSimpleTest();
                log.info("channel list test completed");
            }
            default -> log.warn("requested mode={}", mode);
        }
    }

    @Command(command = "message", description = "message API tests")
    public void message(
            @Option(required = true,
                    arity = CommandRegistration.OptionArity.EXACTLY_ONE,
                    longNames = "mode",
                    shortNames = 'm') String mode
    ) {
        channel = messageTest.createTestChannel();
        switch (mode) {
            case "all" -> messageTest(channel.getId());
            case "crud" -> {
                messageTest.crud(channel.getId());
                log.info("message crud test completed");
            }
            case "list" -> {
                messageTest.listSimpleTest(channel.getId());
                log.info("message list test completed");
            }
            default -> log.warn("requested mode={}", mode);
        }
    }

    @Command(command = "all", description = "all API complete tests")
    public void all(
    ) {
        test();
    }


    private void test() {
        userTest();
        channelTest();
        messageTest.createTestChannel();
        messageTest(channel.getId());
        log.info("all Pachka API tests completed");
    }

    private void userTest() {
        userTest.crud();
        userTest.listSimpleTest();
        log.info("user API test completed");
    }

    private void channelTest() {
        channelTest.crud();
        channelTest.listSimpleTest();
        log.info("channel API test completed");
    }

    private void messageTest(long channelId) {
        messageTest.crud(channelId);
        messageTest.listSimpleTest(channelId);
        log.info("message API test completed");
    }

}
