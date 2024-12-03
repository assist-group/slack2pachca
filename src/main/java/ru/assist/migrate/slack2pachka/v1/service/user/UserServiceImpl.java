package ru.assist.migrate.slack2pachka.v1.service.user;

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
import ru.assist.migrate.slack2pachka.v1.model.users.User;
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
public class UserServiceImpl implements UserService {
    final RestClient restClient;

    @Autowired
    public UserServiceImpl(RestClient restClient) {
        this.restClient = restClient;
    }

    @Override
    @Retryable(retryFor = {TooManyRequestsRuntimeException.class, ServiceRuntimeException.class}, maxAttemptsExpression = "${retry.maxAttempts}",
            backoff = @Backoff(delayExpression = "${retry.delay}", maxDelayExpression = "${retry.maxDelay}", multiplierExpression = "${retry.multiplier}"))
    public User create(User.Request request) {
        ResponseEntity<User.Response> responseEntity = restClient.post()
                .uri("/users")
                .accept(APPLICATION_JSON)
                .body(request)
                .retrieve()
                .toEntity(User.Response.class);

        return Objects.requireNonNull(responseEntity.getBody()).getData();
    }

    @Recover
    private void createRecover(TooManyRequestsRuntimeException e, User.Request request) {
        RecoveryUtils.waitIfError(5);
        create(request);
    }

    @Recover
    private void createRecover(ServiceRuntimeException e, User.Request request) {
        RecoveryUtils.printResponseCode(e);
        RecoveryUtils.waitIfError(10);
        create(request);
    }

    @Override
    @Retryable(retryFor = {TooManyRequestsRuntimeException.class, ServiceRuntimeException.class}, maxAttemptsExpression = "${retry.maxAttempts}",
            backoff = @Backoff(delayExpression = "${retry.delay}", maxDelayExpression = "${retry.maxDelay}", multiplierExpression = "${retry.multiplier}"))
    public User update(User.Request request, long id) {
        ResponseEntity<User.Response> responseEntity = restClient.put()
                .uri("/users/{id}", id)
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .body(request)
                .retrieve()
                .toEntity(User.Response.class);

        return Objects.requireNonNull(responseEntity.getBody()).getData();
    }

    @Recover
    private void updateRecover(TooManyRequestsRuntimeException e, User.Request request, long id) {
        RecoveryUtils.waitIfError(5);
        update(request, id);
    }

    @Recover
    private void updateRecover(ServiceRuntimeException e, User.Request request, long id) {
        RecoveryUtils.printResponseCode(e);
        RecoveryUtils.waitIfError(10);
        update(request, id);
    }

    @Override
    @Retryable(retryFor = {TooManyRequestsRuntimeException.class, ServiceRuntimeException.class}, maxAttemptsExpression = "${retry.maxAttempts}",
            backoff = @Backoff(delayExpression = "${retry.delay}", maxDelayExpression = "${retry.maxDelay}", multiplierExpression = "${retry.multiplier}"))
    public User read(long id) {
        User.Response user = restClient.get()
                .uri("/users/{id}", id)
                .accept(APPLICATION_JSON)
                .retrieve()
                .body(User.Response.class);

        return Objects.requireNonNull(user).getData();
    }

    @Recover
    private void readRecover(TooManyRequestsRuntimeException e, long id) {
        RecoveryUtils.waitIfError(5);
        read(id);
    }

    @Recover
    private void readRecover(ServiceRuntimeException e, long id) {
        RecoveryUtils.printResponseCode(e);
        RecoveryUtils.waitIfError(10);
        read(id);
    }

    @Override
    public User[] list() {
        List<User> result = new ArrayList<>();
        int i = 1;
        while (true) {
            User[] usersPart = getUsers(100, i++);
            if (usersPart.length == 0) break;
            Collections.addAll(result, usersPart);
        }
        return result.toArray(new User[0]);
    }

    @Retryable(retryFor = {TooManyRequestsRuntimeException.class, ServiceRuntimeException.class}, maxAttemptsExpression = "${retry.maxAttempts}",
            backoff = @Backoff(delayExpression = "${retry.delay}", maxDelayExpression = "${retry.maxDelay}", multiplierExpression = "${retry.multiplier}"))
    public User[] getUsers(int per, int page) {
        /*
        per	integer	Количество возвращаемых сущностей за один запрос (по умолчанию 50, максимум 50)
        page	integer	Страница выборки (по умолчанию 1)
        query	string	Поисковая фраза для фильтрации результатов
        (поиск идет по полям first_name (имя),
        last_name (фамилия),
        email (электронная почта),
        phone_number (телефон) и
        nickname (никнейм))
         */
        ResponseEntity<User.ListResponse> responseEntity = restClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/users")
                        .queryParam("per", String.valueOf(per))
                        .queryParam("page", String.valueOf(page))
                        .build())
                .accept(APPLICATION_JSON)
                .retrieve()
                .toEntity(User.ListResponse.class);
        return Objects.requireNonNull(responseEntity.getBody()).getData();
    }

    @Recover
    private User[] getUsersRecover(TooManyRequestsRuntimeException e, int per, int page) {
        RecoveryUtils.waitIfError(5);
        return getUsers(per, page);
    }

    @Recover
    private User[] getUsersRecover(ServiceRuntimeException e, int per, int page) {
        RecoveryUtils.printResponseCode(e);
        RecoveryUtils.waitIfError(10);
        return getUsers(per, page);
    }

    @Override
    @Retryable(retryFor = {TooManyRequestsRuntimeException.class, ServiceRuntimeException.class}, maxAttemptsExpression = "${retry.maxAttempts}",
            backoff = @Backoff(delayExpression = "${retry.delay}", maxDelayExpression = "${retry.maxDelay}", multiplierExpression = "${retry.multiplier}"))
    public void delete(long id) {
        restClient.delete()
                .uri("/users/{id}", id)
                .accept(APPLICATION_JSON)
                .retrieve()
                .toBodilessEntity();
    }

    @Recover
    private void deleteRecover(TooManyRequestsRuntimeException e, long id) {
        RecoveryUtils.waitIfError(5);
        delete(id);
    }

    @Recover
    private void deleteRecover(ServiceRuntimeException e, long id) {
        RecoveryUtils.printResponseCode(e);
        RecoveryUtils.waitIfError(10);
        delete(id);
    }

}
