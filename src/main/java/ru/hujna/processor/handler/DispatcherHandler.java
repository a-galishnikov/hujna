package ru.hujna.processor.handler;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.hujna.processor.Processor;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

@Primary
@Component
@RequiredArgsConstructor
public class DispatcherHandler implements Handler {

    @NonNull
    private final List<Processor> processors;

    @Override
    public Optional<BotApiMethod<? extends Serializable>> handle(Update update) {
        return processors.stream()
                .filter(x -> x.match(update))
                .findFirst()
                .flatMap(x -> x.handle(update));
    }
}
