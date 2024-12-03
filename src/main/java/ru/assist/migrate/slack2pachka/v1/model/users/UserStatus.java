package ru.assist.migrate.slack2pachka.v1.model.users;

import lombok.Data;

/**
 * user_status	object или null	Статус. Возвращается как null, если статус не установлен.
 *
 * emoji	string	Emoji символ статуса
 * title	string	Текст статуса
 * expires_at	string или null	Срок жизни статуса (ISO-8601, UTC+0) в формате YYYY-MM-DDThh:mm:ss.sssZ. Возвращается как null, если срок не установлен.
 */
@Data
public class UserStatus {
    private String emoji;
    private String title;
    private String expires_at;
}
