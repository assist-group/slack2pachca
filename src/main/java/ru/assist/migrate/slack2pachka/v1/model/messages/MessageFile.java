package ru.assist.migrate.slack2pachka.v1.model.messages;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

/**
 * id	integer	Идентификатор файла
 * * key	string	Путь к файлу
 * * name	string	Название файла
 * * file_type	string	Тип файла: файл (file), изображение (image)
 * * url	string	Прямая временная ссылка на скачивание файла
 * <p>
 * key	string*	Путь к файлу, полученный в результате загрузки файла (каждый файл в каждом сообщении должен иметь свой уникальный key, не допускается использование одного и того же key в разных сообщениях)
 * name	string*	Название файла, которое вы хотите отображать пользователю (рекомендуется писать вместе с расширением)
 * file_type	string*	Тип файла: файл (file), изображение (image)
 * size	integer*	Размер файла в байтах, отображаемый пользователю
 */
@Builder
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonIgnoreProperties(value = {"id", "url"}, allowSetters = true)
public class MessageFile {
    long id;
    String key;
    String name;
    String file_type;
    String url;
    long size;
}
