package ru.hujna.processor;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.hujna.processor.handler.Handler;
import ru.hujna.processor.matcher.Matcher;

import java.io.Serializable;
import java.util.List;

@RequiredArgsConstructor
public class Processor implements Handler, Matcher {

    @NonNull
    private final Matcher matcher;

    @NonNull
    private final Handler handler;

    @Override
    public boolean match(Update update) {
        return matcher.match(update);
    }

    @Override
    public List<? extends PartialBotApiMethod<? extends Serializable>> handle(Update update) {
        return handler.handle(update);
    }

}
