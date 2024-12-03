package ru.assist.migrate.slack2pachka;

public class ImportRuntimeException extends RuntimeException {
    public ImportRuntimeException(String message) {
        super(message);
    }

    public ImportRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public ImportRuntimeException(Throwable cause) {
        super(cause);
    }
}
