package ru.assist.migrate.slack2pachka.v1.model.users;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

/**
 * user	object*	Собранный объект параметров создаваемого сотрудника
 * <p>
 * id	integer	Идентификатор созданного пользователя
 * first_name	string	Имя
 * last_name	string	Фамилия
 * nickname	string	Имя пользователя
 * email	string	Электронная почта
 * phone_number	string	Телефон
 * department	string	Подразделение
 * role	string	Уровень доступа: admin (администратор), user (сотрудник), multi_guest (мульти-гость)
 * suspended	boolean	Приостановка доступа
 * invite_status	string	Статус пришлашения: confirmed (принято), sent (отправлено)
 * list_tags	array of strings	Массив тегов, привязанных к сотруднику
 * custom_properties UserCustomProperties
 * user_status UserStatus
 * bot	boolean	Тип: пользователь (false) или бот (true)
 * created_at	string	Дата создания (ISO-8601, UTC+0) в формате YYYY-MM-DDThh:mm:ss.sssZ
 */
@Builder
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User {
    long id;
    String first_name;
    String last_name;
    String nickname;
    String email;
    String phone_number;
    String department;
    String role;
    boolean suspended;
    String invite_status;
    String[] list_tags;
    UserCustomProperties[] custom_properties;
    UserStatus user_status;
    boolean bot;
    String created_at;

    @Builder
    @Data
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class Request {
        User user;
        boolean skip_email_notify;
    }

    @Data
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class ListResponse {
        User[] data;
    }

    /**
     * per	integer	Количество возвращаемых сущностей за один запрос (по умолчанию 50, максимум 50)
     * page	integer	Страница выборки (по умолчанию 1)
     * query	string	Поисковая фраза для фильтрации результатов (поиск идет по полям first_name (имя), last_name (фамилия), email (электронная почта), phone_number (телефон) и nickname (никнейм))
     *
     */
    @Builder
    @Data
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class ListRequest {
        int per;
        int page;
        String query;
    }

    @Data
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class Response {
        User data;
    }
}