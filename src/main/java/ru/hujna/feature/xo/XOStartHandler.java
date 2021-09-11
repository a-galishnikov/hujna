package ru.hujna.feature.xo;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.hujna.processor.handler.Handler;

import java.io.Serializable;
import java.util.Optional;

@RequiredArgsConstructor
public class XOStartHandler implements Handler {

    @NonNull
    private final XOSessionCash sessionCash;

    @Override
    public Optional<BotApiMethod<? extends Serializable>> handle(Update update) {

        var chatId = update.getMessage().getChatId();
        XOSession session = sessionCash.get(chatId);

        return Optional.ofNullable(update)
                .map(x -> SendMessage
                        .builder()
                        .chatId(chatId.toString())
                        .text("Let's play")
                        .replyMarkup(XOUtil.markup(session))
                        .build());
    }
}
