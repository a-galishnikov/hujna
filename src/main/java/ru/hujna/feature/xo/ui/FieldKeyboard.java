package ru.hujna.feature.xo.ui;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import ru.hujna.feature.xo.model.Game;
import ru.hujna.feature.xo.model.XO;

import java.util.ArrayList;
import java.util.List;

public class FieldKeyboard implements Keyboard {
    @Override
    public InlineKeyboardMarkup markup(Game game) {
        return InlineKeyboardMarkup.builder()
                .keyboard(keyboard(game))
                .build();
    }

    private List<List<InlineKeyboardButton>> keyboard(Game game) {
        var field = game.getField();
        var dim = field.dim();
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>(dim);
        for (int i = 0; i < dim; i++) {
            List<InlineKeyboardButton> row = new ArrayList<>(dim);
            for (int j = 0; j < dim; j++) {
                row.add(button(game.getMessageId(), i, j, field.cell(i, j), game.getLastMove().reverse()));
            }
            keyboard.add(row);
        }
        return keyboard;
    }

    private InlineKeyboardButton button(int messageId, int i, int j, XO current, XO callback) {
        String nextXo = current == XO.E ? callback.name() : "NOP";
        return InlineKeyboardButton.builder()
                .callbackData(String.format("xoMove:%d:%d:%d:%s", messageId, i, j, nextXo))
                .text(current.getCell())
                .build();
    }
}
