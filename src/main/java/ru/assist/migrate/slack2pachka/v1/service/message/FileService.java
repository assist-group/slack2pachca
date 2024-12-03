package ru.assist.migrate.slack2pachka.v1.service.message;

import ru.assist.migrate.slack2pachka.v1.model.messages.FileMeta;

import java.io.File;

public interface FileService {
    FileMeta upload(File file);
}
