package ru.hujna.feature.xo.handler;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.hujna.feature.xo.XOSessionCash;
import ru.hujna.feature.xo.XOUtil;
import ru.hujna.feature.xo.model.XO;
import ru.hujna.feature.xo.model.XOState;
import ru.hujna.processor.handler.Handler;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
@Slf4j
public class XOJoinHandler implements Handler {

    @NonNull
    private final XOSessionCash sessionCash;


    @Override
    public List<? extends PartialBotApiMethod<? extends Serializable>> handle(Update update) {

        var callback = update.getCallbackQuery();
        var msg = callback.getMessage();
        var chatId = msg.getChatId();
        var opponent = callback.getFrom();
        var opponentId = opponent.getId();

        var join = XOUtil.parseJoin(callback.getData());
        var initialMessageId = join.messageId();
        var callbackMessageId = msg.getMessageId();

        return sessionCash.get(chatId, initialMessageId).map(session -> {
            var lockAcquired = false;
            try {
                lockAcquired = session.getLock().tryLock();
                List<BotApiMethod<? extends Serializable>> result = Collections.emptyList();
                if (lockAcquired) {
                    if (session.getState() == XOState.NEW) {
                        var newSession = XOUtil.join(session, opponentId);
                        sessionCash.put(newSession);

                        result = new ArrayList<>();
                        var starter = newSession.getPlayers().starter();

                        var editMessage = EditMessageText.builder()
                                .messageId(callbackMessageId)
                                .chatId(chatId.toString())
                                .text(String.format("%s (%s)%n%s joined (%s)%n%s moves first",
                                        callback.getMessage().getText(),
                                        starter.getXo().getCell(),
                                        XOUtil.name(opponent),
                                        starter.getXo().reverse().getCell(),
                                        XO.X.getCell()))
                                .build();
                        result.add(editMessage);

                        var editKeyboard = EditMessageReplyMarkup
                                .builder()
                                .messageId(callbackMessageId)
                                .chatId(chatId.toString())
                                .replyMarkup(XOUtil.markup(newSession))
                                .build();
                        result.add(editKeyboard);
                    }
                }
                return result;
            } finally {
                if (lockAcquired) {
                    session.getLock().unlock();
                }
            }
        }).orElse(Collections.emptyList());
    }
}
