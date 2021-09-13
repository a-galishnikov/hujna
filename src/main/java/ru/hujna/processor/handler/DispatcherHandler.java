package ru.hujna.processor.handler;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.hujna.processor.Processor;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

@Primary
@Component
@RequiredArgsConstructor
public class DispatcherHandler implements Handler {

    @NonNull
    private final List<Processor> processors;

    @Override
    public List<? extends PartialBotApiMethod<? extends Serializable>> handle(Update update) {
        return processors.stream()
                .filter(x -> x.match(update))
                .findFirst()
                .map(x -> x.handle(update))
                .orElse(Collections.emptyList());
    }
}
