package ru.hujna.processor.matcher;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Optional;

@RequiredArgsConstructor
public class RegexMatcher implements Matcher {

    @NonNull
    private final String regex;

    @Override
    public boolean match(Update update) {
        return Optional.ofNullable(update)
                .filter(x -> x.hasMessage() && x.getMessage().hasText())
                .map(x -> x.getMessage().getText().trim())
                .map(x -> x.matches(regex))
                .orElse(false);
    }
}
