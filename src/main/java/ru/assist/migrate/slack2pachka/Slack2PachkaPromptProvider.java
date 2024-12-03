package ru.assist.migrate.slack2pachka;

import org.jline.utils.AttributedString;
import org.jline.utils.AttributedStyle;
import org.springframework.shell.jline.PromptProvider;
import org.springframework.stereotype.Component;


@Component
public class Slack2PachkaPromptProvider implements PromptProvider {
    @Override
    public AttributedString getPrompt() {
        return new AttributedString("slack2pachka:> ", AttributedStyle.DEFAULT.foreground(AttributedStyle.BLUE));
    }

}


