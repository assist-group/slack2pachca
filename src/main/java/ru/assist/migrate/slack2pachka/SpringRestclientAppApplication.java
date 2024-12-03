package ru.assist.migrate.slack2pachka;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.shell.command.annotation.EnableCommand;
import ru.assist.migrate.slack2pachka.v1.commands.pachka.Create;
import ru.assist.migrate.slack2pachka.v1.commands.pachka.Fix;
import ru.assist.migrate.slack2pachka.v1.commands.pachka.Import;
import ru.assist.migrate.slack2pachka.v1.commands.slack.Analyze;
import ru.assist.migrate.slack2pachka.v1.commands.test.PachkaTest;
import ru.assist.migrate.slack2pachka.v3.commands.DropV3;
import ru.assist.migrate.slack2pachka.v3.commands.Set;

@SpringBootApplication
@EnableCommand({PachkaTest.class,
        Import.class,
        Create.class,
        ru.assist.migrate.slack2pachka.v1.commands.pachka.Drop.class,
        DropV3.class,
        Set.class,
        Fix.class,
        Analyze.class
})
@Slf4j
public class SpringRestclientAppApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(SpringRestclientAppApplication.class, args);
    }

    @Override
    public void run(String... args) {
    }
}

