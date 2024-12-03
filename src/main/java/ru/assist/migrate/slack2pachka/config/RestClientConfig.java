package ru.assist.migrate.slack2pachka.config;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.event.Level;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.*;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestClient;
import ru.assist.migrate.slack2pachka.ImportRuntimeException;
import ru.assist.migrate.slack2pachka.exceptions.ServiceRuntimeException;
import ru.assist.migrate.slack2pachka.exceptions.TooManyRequestsRuntimeException;

import java.io.IOException;


/**
 * uthorization: Bearer Pm7gdycNeHV_F1y4_tjWWIutC0Aq0gwl9wRnX-KBuHw
 * Content-Type: application/json; charset=utf-8
 * Host: api.pachca.com
 * Connection: close
 * User-Agent: Paw/3.1.10 (Macintosh; OS X/10.15.3) GCDHTTPRequest
 * Content-Length: 219
 * {"user":
 * {
 * "first_name":"Олег",
 * "last_name":"Петров",
 * "email":"olegp@example.com",
 * "department":"Продукт",
 * "list_tags":["Product","Design"],
 * "custom_properties":[{"id":1678,"value":"Санкт-Петербург"}]
 * },
 * "skip_email_notify":true
 * }
 */

@Configuration
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RestClientConfig {
    final EventStatCounter eventStatCounter;

    @Autowired
    public RestClientConfig(EventStatCounter eventStatCounter) {
        this.eventStatCounter = eventStatCounter;
    }

    @Value("${base.url}")
    String baseUrl;

    @Value("${client.auth.string}")
    String authString;

    @Value("${pachka.api.version}")
    String apiVersion;

    @Value("${client.user.id}")
    String userId;

    @Bean
    RestClient restClient() {
        RestClient.Builder builder = RestClient.builder();
        builder
                .requestFactory(new HttpComponentsClientHttpRequestFactory())
                .baseUrl(baseUrl)
                .defaultStatusHandler(HttpStatusCode::isError, this::catchResponseError)
                .defaultStatusHandler(HttpStatusCode::is2xxSuccessful, this::catchResponseSuccess)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, String.valueOf(MediaType.APPLICATION_JSON));
        if (apiVersion.equals("v3")) {
            builder.defaultHeaders(
                    httpHeaders -> {
                        httpHeaders.add("Cookie", authString);
                        httpHeaders.add("User-Id", userId);
                    });
        } else if (apiVersion.equals("v1")) {
            builder.defaultHeaders(
                    httpHeaders -> {
                        httpHeaders.setBearerAuth(authString);
                    });
        }


        return builder.build();
    }

    private void catchResponseError(HttpRequest request, ClientHttpResponse response) {
        eventStatCounter.addFailure();
        try {
            log.atLevel(Level.DEBUG).log("{} : {}", requestToString(request), String.format("%s", response.getStatusCode()));
            if (response.getStatusCode().isSameCodeAs(HttpStatus.TOO_MANY_REQUESTS))
                throw new TooManyRequestsRuntimeException(request, response);
            else
                throw new ServiceRuntimeException(request, response);
        } catch (IOException e) {
            throw new ImportRuntimeException(e);
        }
    }

    private void catchResponseSuccess(HttpRequest request, ClientHttpResponse response) {
        eventStatCounter.addSuccess();
        log.atLevel(Level.TRACE).log("\n{} \n{}", requestToString(request), responseToString(response));
    }

    private static String requestToString(HttpRequest request) {
        return String.format("%s %s", request.getMethod(), request.getURI());
    }

    private static String responseToString(ClientHttpResponse response) {
        try {
            return String.format("%s %s", response.getStatusCode(), response.getBody());
        } catch (IOException e) {
            throw new ImportRuntimeException(e);
        }
    }

}
