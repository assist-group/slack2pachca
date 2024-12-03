package ru.assist.migrate.slack2pachka.v1.service.error;

import lombok.Data;

/**
 * key	string	Ключ параметра, в котором произошла ошибка
 * value	string	Значение ключа, которое вызвало ошибку
 * message	string	Ошибка текстом, который вы можете вывести пользователю
 * code	string	Внутренний код ошибки (коды ошибок представлены в описании каждого метода)
 * payload	object	Объект, который предоставляет любую дополнительную информацию (возможные дополнения представлены в описании каждого метода)
 */
@Data
public class Error {
    private String key;
    private String value;
    private String message;
    private String code;
    private Object payload;
}