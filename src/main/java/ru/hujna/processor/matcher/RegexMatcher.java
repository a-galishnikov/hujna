package ru.hujna.processor.matcher;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Optional;

@RequiredArgsConstructor
public abstract class RegexMatcher implements Matcher {

    @NonNull
    private String regex;

    protected abstract Optional<String> extractText(Update update);

    @Override
    public boolean match(Update update) {
        return Optional.ofNullable(update)
                .flatMap(this::extractText)
                .map(x -> x.matches(regex))
                .orElse(false);
    }
}
