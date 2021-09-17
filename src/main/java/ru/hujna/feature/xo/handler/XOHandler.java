package ru.hujna.feature.xo.handler;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import ru.hujna.feature.xo.GameCache;
import ru.hujna.feature.xo.XOUtil;
import ru.hujna.feature.xo.model.Game;
import ru.hujna.processor.handler.Handler;

import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
@Slf4j
public class XOHandler implements Handler {

    @NonNull
    private final GameCache gameCache;

    @Override
    public List<SendMessage> handle(Update update) {

        var msg = update.getMessage();
        var chatId = msg.getChatId();
        var messageId = msg.getMessageId();
        var starter = msg.getFrom();
        var starterId = starter.getId();
        var game = XOUtil.initGame(chatId, messageId, starterId);
        gameCache.put(game);

        return Collections.singletonList(
                SendMessage.builder()
                        .chatId(chatId.toString())
                        .text(XOUtil.name(starter) + " wants to play tic tac toe")
                        .replyMarkup(markup(game))
                        .build()
        );
    }

    private ReplyKeyboard markup(Game game) {
        var button = InlineKeyboardButton
                .builder()
                .callbackData(String.format("xoJoin:%d", game.getMessageId()))
                .text("Join")
                .build();
        List<List<InlineKeyboardButton>> keyboard = List.of(List.of(button));
        return InlineKeyboardMarkup.builder().keyboard(keyboard).build();
    }

}
