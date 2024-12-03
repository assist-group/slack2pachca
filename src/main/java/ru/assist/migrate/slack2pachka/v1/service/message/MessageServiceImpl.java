package ru.assist.migrate.slack2pachka.v1.service.message;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import ru.assist.migrate.slack2pachka.v1.model.messages.Message;
import ru.assist.migrate.slack2pachka.v1.service.RecoveryUtils;
import ru.assist.migrate.slack2pachka.exceptions.ServiceRuntimeException;
import ru.assist.migrate.slack2pachka.exceptions.TooManyRequestsRuntimeException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static org.springframework.http.MediaType.APPLICATION_JSON;

@Service
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MessageServiceImpl implements MessageService {
    final RestClient restClient;
    static final int perMax = 50;

    @Autowired
    public MessageServiceImpl(RestClient restClient) {
        this.restClient = restClient;
    }

    @Override
    @Retryable(retryFor = {TooManyRequestsRuntimeException.class, ServiceRuntimeException.class}, maxAttemptsExpression = "${retry.maxAttempts}",
            backoff = @Backoff(delayExpression = "${retry.delay}", maxDelayExpression = "${retry.maxDelay}", multiplierExpression = "${retry.multiplier}"))
    public Message create(Message.Request request) {
        ResponseEntity<Message.Response> responseEntity = restClient.post()
                .uri("/messages")
                .accept(APPLICATION_JSON)
                .body(request)
                .retrieve()
                .toEntity(Message.Response.class);

        return Objects.requireNonNull(responseEntity.getBody()).getData();
    }

    @Recover
    private Message createRecover(TooManyRequestsRuntimeException e, Message.Request request) {
        RecoveryUtils.waitIfError(5);
        return create(request);
    }

    @Recover
    private Message createRecover(ServiceRuntimeException e, Message.Request request) {
        RecoveryUtils.printResponseCode(e);
        RecoveryUtils.waitIfError(10);
        return create(request);
    }

    @Override
    @Retryable(retryFor = {TooManyRequestsRuntimeException.class, ServiceRuntimeException.class}, maxAttemptsExpression = "${retry.maxAttempts}",
            backoff = @Backoff(delayExpression = "${retry.delay}", maxDelayExpression = "${retry.maxDelay}", multiplierExpression = "${retry.multiplier}"))
    public Message update(Message.Request request, long id) {
        ResponseEntity<Message.Response> responseEntity = restClient.put()
                .uri("/messages/{id}", id)
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .body(request)
                .retrieve()
                .toEntity(Message.Response.class);

        return Objects.requireNonNull(responseEntity.getBody()).getData();
    }

    @Recover
    private Message updateRecover(TooManyRequestsRuntimeException e, Message.Request request, long id) {
        RecoveryUtils.waitIfError(5);
        return update(request, id);
    }

    @Recover
    private Message updateRecover(ServiceRuntimeException e, Message.Request request, long id) {
        RecoveryUtils.printResponseCode(e);
        RecoveryUtils.waitIfError(10);
        return update(request, id);
    }

    @Override
    @Retryable(retryFor = {TooManyRequestsRuntimeException.class, ServiceRuntimeException.class}, maxAttemptsExpression = "${retry.maxAttempts}",
            backoff = @Backoff(delayExpression = "${retry.delay}", maxDelayExpression = "${retry.maxDelay}", multiplierExpression = "${retry.multiplier}"))
    public Message read(long id) {
        Message.Response response = restClient.get()
                .uri("/messages/{id}", id)
                .accept(APPLICATION_JSON)
                .retrieve()
                .body(Message.Response.class);

        return Objects.requireNonNull(response).getData();
    }

    @Recover
    private Message readRecover(TooManyRequestsRuntimeException e, long id) {
        RecoveryUtils.waitIfError(5);
        return read(id);
    }

    @Recover
    private Message readRecover(ServiceRuntimeException e, long id) {
        RecoveryUtils.printResponseCode(e);
        RecoveryUtils.waitIfError(10);
        return read(id);
    }


    @Override
    public Message[] list(long chat_id) {
        List<Message> result = new ArrayList<>();
        int i = 1;
        while (true) {
            Message[] channelsPart = getMessages(chat_id, perMax, i++);
            if (channelsPart.length == 0) break;
            Collections.addAll(result, channelsPart);
        }
        return result.toArray(new Message[0]);
    }


    @Override
    public void list(long chat_id, MessageListHandler handler) {
        int i = 1;
        while (true) {
            Message[] channelsPart = getMessages(chat_id, perMax, i++);
            if (channelsPart.length == 0) break;
            handler.handle(channelsPart);
        }
    }

    @Override
    public void list(long chat_id, int limit, MessageListHandler handler) {
        int i = 1;
        int perLimit = Math.min(limit, perMax);
        while (true) {
            Message[] channelsPart = getMessages(chat_id, perLimit, i++);
            if (channelsPart.length == 0) break;
            handler.handle(channelsPart);
            limit -= channelsPart.length;
            if (limit <= 0) break;
            perLimit = Math.min(limit, perMax);
        }
    }

    @Override
    public boolean isEmpty(long chat_id) {
        Message[] messages = getMessages(chat_id, 2, 1);
        // at least one message exists in new channel
        return messages.length < 2;
    }

    @Retryable(retryFor = {TooManyRequestsRuntimeException.class, ServiceRuntimeException.class}, maxAttemptsExpression = "${retry.maxAttempts}",
            backoff = @Backoff(delayExpression = "${retry.delay}", maxDelayExpression = "${retry.maxDelay}", multiplierExpression = "${retry.multiplier}"))
    private Message[] getMessages(long chat_id, int per, int page) {
        ResponseEntity<Message.ListResponse> responseEntity = restClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/messages")
                        .queryParam("chat_id", chat_id)
                        .queryParam("per", per)
                        .queryParam("page", page)
                        .build())
                .accept(APPLICATION_JSON)
                .retrieve()
                .toEntity(Message.ListResponse.class);

        return Objects.requireNonNull(responseEntity.getBody()).getData();
    }

    @Recover
    private Message[] getMessagesRecover(TooManyRequestsRuntimeException e, long chat_id, int per, int page) {
        RecoveryUtils.waitIfError(5);
        return getMessages(chat_id, per, page);
    }

    @Recover
    private Message[] getMessagesRecover(ServiceRuntimeException e, long chat_id, int per, int page) {
        RecoveryUtils.printResponseCode(e);
        RecoveryUtils.waitIfError(10);
        return getMessages(chat_id, per, page);
    }

    @Override
    public void delete(long id) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
