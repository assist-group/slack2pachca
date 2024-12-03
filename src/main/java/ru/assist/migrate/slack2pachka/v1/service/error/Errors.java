package ru.assist.migrate.slack2pachka.v1.service.error;

import lombok.Data;

@Data
public class Errors {
    private Error[] errors;
}
