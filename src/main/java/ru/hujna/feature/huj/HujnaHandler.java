package ru.hujna.feature.huj;

import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.hujna.processor.handler.Handler;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class HujnaHandler implements Handler {

    static final String HUJNA = "Huj na!";

    @Override
    public List<? extends BotApiMethod<? extends Serializable>> handle(Update update) {
        return Optional.ofNullable(update)
                .map(x -> Collections.singletonList(
                        SendMessage.builder()
                                .chatId(x.getMessage().getChatId().toString())
                                .replyToMessageId(x.getMessage().getMessageId())
                                .text(HUJNA)
                                .build())).orElse(Collections.emptyList());
    }
}
