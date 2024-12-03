package ru.assist.migrate.slack2pachka.exceptions;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpResponse;
import ru.assist.migrate.slack2pachka.ImportRuntimeException;

import java.io.IOException;

@Slf4j
@Getter
public class ServiceRuntimeException extends RuntimeException {
    private final ClientHttpResponse response;
    private final HttpRequest request;

    public ServiceRuntimeException(HttpRequest request, ClientHttpResponse response) {
        this.request = request;
        this.response = response;

        try {
            log.info(response.getStatusCode().toString());
        } catch (IOException e) {
            throw new ImportRuntimeException(e);
        }
    }
}
