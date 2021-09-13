package ru.hujna.feature.xo;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import ru.hujna.processor.handler.Handler;

import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
@Slf4j
public class XOStartHandler implements Handler {

    @NonNull
    private final XOSessionCash sessionCash;

    @Override
    public List<SendMessage> handle(Update update) {

        var chatId = update.getMessage().getChatId();
        var messageId = update.getMessage().getMessageId();

        XOSession session = XOUtil.initPvPSession(chatId, messageId);
        sessionCash.put(session);
        User sender = update.getMessage().getFrom();

        if (session.getType() == XOType.PVE) {
            return Collections.emptyList();
        } else {
            return Collections.singletonList(
                    SendMessage.builder()
                            .chatId(chatId.toString())
                            .text(sender.getUserName() + " wants to play tic-tac-toe")
                            .replyMarkup(XOUtil.markup(session))
                            .build()
            );
        }
    }
}
