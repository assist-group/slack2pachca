package ru.assist.migrate.slack2pachka.v1.commands.test;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.assist.migrate.slack2pachka.v1.commands.test.utils.Utils;
import ru.assist.migrate.slack2pachka.v1.model.channels.Channel;
import ru.assist.migrate.slack2pachka.exceptions.TooManyRequestsRuntimeException;
import ru.assist.migrate.slack2pachka.v1.service.channel.ChannelService;

@Component
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ChannelTest {
    final ChannelService channelService;

    @Autowired
    public ChannelTest(ChannelService channelService) {
        this.channelService = channelService;
    }

    @Value("${client.user.id}")
    long ownerId;

    public void crud() {

        Channel channel = createArtificalChannel("ArtificalChannel " + Utils.generateRandomString(20));
        Channel.Request request = Channel.Request.builder()
                .chat(channel)
                .build();
        // create
        channel = channelService.create(request);
        // read
        channel = channelService.read(channel.getId());
        //update
        channel.setPublic(false);
        request = Channel.Request.builder()
                .chat(channel)
                .build();
        channel = channelService.update(request, channel.getId());
        // ensure updated
        channel = channelService.read(channel.getId());
        // delete
//        channelService.delete(channel.getId());
    }

    public void listSimpleTest() {
        //create list

        for (int i = 0; i < 10; i++) {
            String name = "ArtificalChannel" + i + " " + Utils.generateRandomString(20);
            Channel channel = createArtificalChannel(name);
        }

        // list
        Channel[] channels = channelService.list();
        //drop all
        int i = 0;
        for (Channel channel : channels) {
            if (channel.getOwner_id() != ownerId) continue;
            if (channel.getName().isEmpty()) continue;
            channel.setName("dropped_" + i++);
            channel.setChannel(false);
            channel.setPublic(false);
            Channel.Request request = Channel.Request.builder()
                    .chat(channel)
                    .build();

            try {
                channelService.update(request, channel.getId());
            } catch (TooManyRequestsRuntimeException e) {
                log.info(e.getMessage());
            }
        }
    }

    private Channel createArtificalChannel(String fullName) {
        return Channel.builder()
                .name(fullName)
                .isPublic(true)
                .channel(false)
                .build();
    }

}
