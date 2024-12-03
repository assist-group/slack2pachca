package ru.assist.migrate.slack2pachka.v1.model.users;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

/**
 * custom_properties	array of objects	Дополнительные поля сотрудника
 * <p>
 * id	integer	Идентификатор поля
 * name	string	Название поля
 * data_type	string	Тип поля (string, number, date или link)
 * value	string	Значение
 */
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserCustomProperties {
    int id;
    String name;
    String data_type;
    String value;
}
