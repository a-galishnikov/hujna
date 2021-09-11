package ru.hujna.feature.xo;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.hujna.processor.handler.Handler;

import java.io.Serializable;
import java.util.Optional;

@RequiredArgsConstructor
public class XOCallbackHandler implements Handler {

    @NonNull
    private final XOSessionCash sessionCash;

    @Override
    public Optional<BotApiMethod<? extends Serializable>> handle(Update update) {

        var chatId = update.getCallbackQuery().getMessage().getChatId();
        XOSession session = sessionCash.get(chatId);
        XOMove move = XOUtil.parseMove(update.getCallbackQuery().getData());
        XOSession newSession = XOUtil.move(session, move);
        sessionCash.put(newSession);

        return Optional.of(update)
                .map(x -> EditMessageReplyMarkup
                        .builder()
                        .messageId(x.getCallbackQuery().getMessage().getMessageId())
                        .chatId(chatId.toString())
                        .replyMarkup(XOUtil.markup(newSession))
                        .build());
    }
}
