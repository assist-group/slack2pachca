package ru.assist.migrate.slack2pachka.v1.service.channel;

import ru.assist.migrate.slack2pachka.v1.model.channels.Channel;

public interface ChannelService {

    Channel create(Channel.Request request);

    Channel read(long id);

    Channel update(Channel.Request request, long id);

    void delete(long id);

    void deleteMember(long channelId, long memberId);

    void addMembers(long channelId, Channel.Members members);

    Channel[] list();

}
