package ru.hujna.processor.matcher;

import lombok.NonNull;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Optional;

public class MessageRegexMatcher extends RegexMatcher {

    public MessageRegexMatcher(@NonNull String regex) {
        super(regex);
    }

    protected Optional<String> extractText(Update update) {
        return Optional.of(update)
                .filter(x -> x.hasMessage() && x.getMessage().hasText())
                .map(x -> x.getMessage().getText().trim());
    }
}
