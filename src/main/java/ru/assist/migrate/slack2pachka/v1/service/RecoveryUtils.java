package ru.assist.migrate.slack2pachka.v1.service;

import lombok.extern.slf4j.Slf4j;
import ru.assist.migrate.slack2pachka.ImportRuntimeException;
import ru.assist.migrate.slack2pachka.exceptions.ServiceRuntimeException;

import java.io.IOException;

@Slf4j
public class RecoveryUtils {
    public static void waitIfError(long minutes){
        try {
            Thread.sleep(1000 * 60 * minutes);
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
            throw new ImportRuntimeException(ex);
        }
    }

    public static void printResponseCode(ServiceRuntimeException e){
        try {
            log.info(e.getResponse().getStatusCode().toString());
        } catch (IOException ex) {
            throw new ImportRuntimeException(ex);
        }
    }
}
