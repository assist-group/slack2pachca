package ru.assist.migrate.slack2pachka.v1.service.user;

import ru.assist.migrate.slack2pachka.v1.model.users.User;

public interface UserService {

    User create(User.Request request);

    User read(long id);

    User update(User.Request request, long id);

    void delete(long id);

    User[] list();
}
