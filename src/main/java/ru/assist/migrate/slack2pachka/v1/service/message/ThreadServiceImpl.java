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
import ru.assist.migrate.slack2pachka.v1.model.messages.Thread;
import ru.assist.migrate.slack2pachka.v1.service.RecoveryUtils;
import ru.assist.migrate.slack2pachka.exceptions.ServiceRuntimeException;
import ru.assist.migrate.slack2pachka.exceptions.TooManyRequestsRuntimeException;

import java.util.Objects;

import static org.springframework.http.MediaType.APPLICATION_JSON;

@Service
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ThreadServiceImpl implements ThreadService {
    final RestClient restClient;

    @Autowired
    public ThreadServiceImpl(RestClient restClient) {
        this.restClient = restClient;
    }

    @Override
    @Retryable(retryFor = {TooManyRequestsRuntimeException.class, ServiceRuntimeException.class}, maxAttemptsExpression = "${retry.maxAttempts}",
            backoff = @Backoff(delayExpression = "${retry.delay}", maxDelayExpression = "${retry.maxDelay}", multiplierExpression = "${retry.multiplier}"))
    public Thread create(long message_id) {
        ResponseEntity<Thread.Response> responseEntity = restClient.post()
                .uri("/messages/{id}/thread", message_id)
                .accept(APPLICATION_JSON)
                .retrieve()
                .toEntity(Thread.Response.class);

        return Objects.requireNonNull(responseEntity.getBody()).getData();
    }

    @Recover
    private Thread createRecover(TooManyRequestsRuntimeException e, long message_id) {
        RecoveryUtils.waitIfError(5);
        return create(message_id);
    }

    @Recover
    private Thread createRecover(ServiceRuntimeException e, long message_id) {
        RecoveryUtils.printResponseCode(e);
        RecoveryUtils.waitIfError(10);
        return create(message_id);
    }

}
