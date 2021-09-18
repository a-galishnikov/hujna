package ru.hujna.feature.xo.ui;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import ru.hujna.feature.xo.model.Game;

import java.util.List;

public class JoinKeyboard implements Keyboard {
    @Override
    public InlineKeyboardMarkup markup(Game game) {
        var button = InlineKeyboardButton
                .builder()
                .callbackData(String.format("xoJoin:%d", game.getMessageId()))
                .text("Join")
                .build();
        var keyboard = List.of(List.of(button));
        return InlineKeyboardMarkup.builder().keyboard(keyboard).build();
    }
}
