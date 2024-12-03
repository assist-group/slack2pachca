package ru.assist.migrate.slack2pachka.v1.commands.pachka;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.shell.command.annotation.Command;
import ru.assist.migrate.slack2pachka.v1.model.channels.Channel;
import ru.assist.migrate.slack2pachka.v1.model.users.User;
import ru.assist.migrate.slack2pachka.v1.service.channel.ChannelService;
import ru.assist.migrate.slack2pachka.v1.service.user.UserService;
import ru.assist.migrate.slack2pachka.v3.service.chat.ChatService;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Command(command = "drop", description = "Pachka drop utilities", group = "drop")
@FieldDefaults(level = AccessLevel.PRIVATE)
@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
public class Drop {
    final UserService userService;
    final ChannelService channelService;
    final ChatService chatService;

    @Value("${client.user.id}")
    long ownerId;

    @Autowired
    public Drop(UserService userService, ChannelService channelService, ChatService chatService) {
        this.userService = userService;
        this.channelService = channelService;
        this.chatService = chatService;
    }

    @Command(command = "members", description = "drop channel members")
    private void members() {
        Channel[] channels = getChannels();
        for (Channel channel : channels) {
            if (shouldSkip(channel)) continue;
            dropMembers(channel);
            log.info("members dropped : {}", channel.getName());
        }
    }

    private Channel[] getChannels() {
        List<Channel> channels = new ArrayList<>();
        log.info("channel list loading... ");
        Channel[] currentChannels = channelService.list();
        for (Channel channel : currentChannels) {
            if (channel.isPublic()) channels.add(channel);
        }
        log.info("channel list loaded");
        return channels.toArray(new Channel[0]);
    }

    private boolean shouldSkip(Channel channel) {
        if (channel.getOwner_id() == ownerId) return true;
        return channel.getName().isEmpty();
    }

    private boolean shouldSkip(User user) {
        if (user.getId() == ownerId) return true;
        String email = user.getEmail();
        return email != null;
    }

    private void dropMembers(Channel channel) {
        long[] members = channel.getMember_ids();
        for (long memberId : members) {
            if (memberId == ownerId) continue;
            channelService.deleteMember(channel.getId(), memberId);
        }
    }

    @Command(command = "channels", description = "drop channels")
    private void channels() {
        // there is no possibility to drop channels
        // so, we drop all members from channels
        // then, we rename it to "dropped"
        // and then make channel private (actually invisible to all users except creator)

        Channel[] channels = getChannels();

        for (Channel channel : channels) {
            if (shouldSkip(channel)) continue;
            dropMembers(channel);

            Channel.Request request = Channel.Request.builder()
                    .chat(Channel.builder()
                            .name("dropped")
                            .isPublic(false)
                            .build())
                    .build();

            channelService.update(request, channel.getId());
            log.info("-{}", channel.getName());
        }
    }

    @Command(command = "users", description = "drop users")
    private void users() {
        User[] users = userService.list();

        for (User user : users) {
            if (shouldSkip(user)) continue;
            userService.delete(user.getId());
            log.info("-{}", user.getEmail());
        }
    }
}
