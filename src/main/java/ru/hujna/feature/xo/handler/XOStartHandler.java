package ru.hujna.feature.xo.handler;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import ru.hujna.feature.xo.XOSessionCash;
import ru.hujna.feature.xo.XOUtil;
import ru.hujna.feature.xo.model.XOSession;
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

        var msg = update.getMessage();
        var chatId = msg.getChatId();
        var messageId = msg.getMessageId();
        var starter = msg.getFrom();
        var starterId = starter.getId();
        var session = XOUtil.initPvPSession(chatId, messageId, starterId);
        sessionCash.put(session);

        return Collections.singletonList(
                SendMessage.builder()
                        .chatId(chatId.toString())
                        .text(XOUtil.name(starter) + " wants to play tic tac toe")
                        .replyMarkup(markup(session))
                        .build()
        );
    }

    private ReplyKeyboard markup(XOSession session) {
        var button = InlineKeyboardButton
                .builder()
                .callbackData(String.format("xoJoin:%d", session.getMessageId()))
                .text("Join")
                .build();
        List<List<InlineKeyboardButton>> keyboard = List.of(List.of(button));
        return InlineKeyboardMarkup.builder().keyboard(keyboard).build();
    }

}
