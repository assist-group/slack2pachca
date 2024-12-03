package ru.assist.migrate.slack2pachka.v1.service.channel;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import ru.assist.migrate.slack2pachka.exceptions.ServiceRuntimeException;
import ru.assist.migrate.slack2pachka.exceptions.TooManyRequestsRuntimeException;
import ru.assist.migrate.slack2pachka.v1.model.channels.Channel;
import ru.assist.migrate.slack2pachka.v1.service.RecoveryUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static org.springframework.http.MediaType.APPLICATION_JSON;

@Service
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ChannelServiceImpl implements ChannelService {
    final RestClient restClient;
    final RetryTemplate retryTemplate;

    @Autowired
    public ChannelServiceImpl(RestClient restClient, RetryTemplate retryTemplate) {
        this.restClient = restClient;
        this.retryTemplate = retryTemplate;
    }

    @Override
    public Channel create(Channel.Request request) {
        return retryTemplate.execute(context -> {
            ResponseEntity<Channel.Response> responseEntity = restClient.post()
                    .uri("/chats")
                    .accept(APPLICATION_JSON)
                    .body(request)
                    .retrieve()
                    .toEntity(Channel.Response.class);

            return Objects.requireNonNull(responseEntity.getBody()).getData();
        }, context -> {
            RecoveryUtils.waitIfError(5L);
            return create(request);
        });
    }

    @Override
    public Channel update(Channel.Request request, long id) {
        return retryTemplate.execute(context -> {
            ResponseEntity<Channel.Response> responseEntity = restClient.put()
                    .uri("/chats/{id}", id)
                    .contentType(APPLICATION_JSON)
                    .accept(APPLICATION_JSON)
                    .body(request)
                    .retrieve()
                    .toEntity(Channel.Response.class);
            return Objects.requireNonNull(responseEntity.getBody()).getData();
        }, context -> {
            RecoveryUtils.waitIfError(5L);
            return update(request, id);
        });
    }


    @Override
    public Channel read(long id) {
        return retryTemplate.execute(context -> {
            Channel.Response response = restClient.get()
                    .uri("/chats/{id}", id)
                    .accept(APPLICATION_JSON)
                    .retrieve()
                    .body(Channel.Response.class);

            return Objects.requireNonNull(response).getData();
        }, context -> {
            RecoveryUtils.waitIfError(5L);
            return read(id);
        });
    }

    @Override
    public Channel[] list() {
        List<Channel> result = new ArrayList<>();
        int i = 1;
        while (true) {
            Channel[] channelsPart = getChannels(100, i++);
            if (channelsPart.length == 0) break;
            Collections.addAll(result, channelsPart);
        }
        return result.toArray(new Channel[0]);
    }

    public Channel[] getChannels(int per, int page) {
        return retryTemplate.execute(context -> {
            ResponseEntity<Channel.ListResponse> responseEntity = restClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/chats")
                            .queryParam("per", String.valueOf(per))
                            .queryParam("page", String.valueOf(page))
                            .build())
                    .accept(APPLICATION_JSON)
                    .retrieve()
                    .toEntity(Channel.ListResponse.class);
            return Objects.requireNonNull(responseEntity.getBody()).getData();
        }, context -> {
            RecoveryUtils.waitIfError(5L);
            return getChannels(per, page);
        });
    }

    @Override
    public void delete(long id) {
        throw new UnsupportedOperationException("Not supported yet.");
    }


    @Override
    public void deleteMember(long channelId, long memberId) {
        retryTemplate.execute(context -> {
            return restClient.delete()
                    .uri(uriBuilder -> uriBuilder
                            .pathSegment("chats", "{channelId}", "members", "{memberId}")
                            .build(channelId, memberId))
                    .retrieve()
                    .toBodilessEntity();
        }, context -> {
            RecoveryUtils.waitIfError(5);
            deleteMember(channelId, memberId);
            return null;
        });
    }

    @Override
    public void addMembers(long channelId, Channel.Members members) {
        retryTemplate.execute(context -> {
            return restClient.post()
                    .uri(uriBuilder -> uriBuilder
                            .pathSegment("chats", "{channelId}", "members")
                            .build(channelId))
                    .body(members)
                    .retrieve()
                    .toBodilessEntity();
        }, context -> {
            RecoveryUtils.waitIfError(5);
            addMembers(channelId, members);
            return null;
        });
    }

}
