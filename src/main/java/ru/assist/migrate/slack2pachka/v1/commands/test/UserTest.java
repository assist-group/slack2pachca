package ru.assist.migrate.slack2pachka.v1.commands.test;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.assist.migrate.slack2pachka.v1.commands.test.utils.Utils;
import ru.assist.migrate.slack2pachka.v1.model.users.User;
import ru.assist.migrate.slack2pachka.v1.service.user.UserService;

@Component
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserTest {
    final UserService userService;

    @Autowired
    public UserTest(UserService userService) {
        this.userService = userService;
    }

    public void crud() {

        User user = createArtificalUser("Artifical " + Utils.generateRandomString(10));
        User.Request request = User.Request.builder()
                .user(user)
                .skip_email_notify(true)
                .build();
        // create
        user = userService.create(request);
        // read
        user = userService.read(user.getId());
        //update
        user.setNickname("pupkin");
        user = userService.update(request, user.getId());
        // ensure updated
        user = userService.read(user.getId());
        // delete
        userService.delete(user.getId());
    }

    public void listSimpleTest() {
        //create list

        for (int i = 0; i < 10; i++) {
            String name = "Artifical" + i + " " + Utils.generateRandomString(10);
            User user = createArtificalUser(name);
        }

        // list
        User[] users = userService.list();
        //drop all
        for (User user : users) {
            if (user.getRole().equals("admin")) continue;
            if (user.getInvite_status().equals("confirmed")) continue;

            userService.delete(user.getId());
        }
    }

    public void print() {
        // list
        User[] users = userService.list();
        //drop all
        for (User user : users) {
            log.info(user.toString());
        }
    }


    private User createArtificalUser(String fullName) {
        String[] name = fullName.split(" ");
        return User.builder()
                .bot(false)
                .email(name[1].toLowerCase() + "@somewhere.net")
                .first_name(name[0])
                .last_name(name[1])
                .role("user")
                .department(name[0] + name[1] + "&Sons")
                .phone_number("+1 555 555 55 55")
                .nickname(name[1].toLowerCase())
                .build();
    }
}
