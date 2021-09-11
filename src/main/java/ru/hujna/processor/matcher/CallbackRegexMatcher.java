package ru.hujna.processor.matcher;

import lombok.NonNull;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Optional;

public class CallbackRegexMatcher extends RegexMatcher {

    public CallbackRegexMatcher(@NonNull String regex) {
        super(regex);
    }

    protected Optional<String> extractText(Update update) {
        return Optional.of(update)
                .filter(Update::hasCallbackQuery)
                .map(x -> x.getCallbackQuery().getData());
    }
}
