package ru.assist.migrate.slack2pachka.exceptions;

import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpResponse;


public class TooManyRequestsRuntimeException extends RuntimeException {
    private final ClientHttpResponse response;
    private final HttpRequest request;

    public TooManyRequestsRuntimeException(HttpRequest request, ClientHttpResponse response) {
        this.request = request;
        this.response = response;
    }

}
