package ru.hujna.feature.xo;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
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
        XOSession session = XOUtil.initSession(chatId);
        sessionCash.put(session);
        User sender = update.getMessage().getFrom();

        return Optional.of(update)
                .map(x -> SendMessage
                        .builder()
                        .chatId(chatId.toString())
                        .text(sender.getUserName() + " want's to play tic-tac-toe")
                        .replyMarkup(XOUtil.markup(session))
                        .build());
    }
}
