package ru.hujna.feature.xo;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class XOUtil {

    public static XOSession initSession(Long chatId) {
        return XOSession.builder()
                .chatId(chatId)
                .lastXo(XO.E)
                .field(emptyField())
                .build();
    }

    private static XO[][] emptyField() {
        XO[][] field = new XO[3][3];
        Arrays.stream(field).forEach(row -> Arrays.fill(row, XO.E));
        return field;
    }

    public static XOSession move(XOSession session, XOMove move) {
        return XOSession.builder()
                .chatId(session.getChatId())
                .lastXo(session.getLastXo().reverse())
                .field(move(session.getField(), move))
                .build();
    }

    public static XO[][] move(XO[][] field, XOMove move) {
        XO[][] newField = copy(field);
        newField[move.getX()][move.getY()] = move.getXo();
        return newField;
    }

    private static XO[][] copy(XO[][] arr) {
        XO[][] copy = new XO[3][3];
        for (int i = 0; i < 3; i++) {
            System.arraycopy(arr[i], 0, copy[i], 0, 3);
        }
        return copy;
    }

    public static InlineKeyboardMarkup markup(XOSession session) {
        return InlineKeyboardMarkup.builder()
                .keyboard(XOUtil.keyboard(session))
                .build();
    }

    private static List<List<InlineKeyboardButton>> keyboard(XOSession session) {
        XO[][] field = session.getField();
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>(field.length);
        for (int i = 0; i < field.length; i++) {
            List<InlineKeyboardButton> row = new ArrayList<>(field[i].length);
            for (int j = 0; j < field[i].length; j++) {
                row.add(button(i, j, field[i][j], session.getLastXo().reverse()));
            }
            keyboard.add(row);
        }
        return keyboard;
    }

    private static InlineKeyboardButton button(int i, int j, XO current, XO callback) {
        String nextXo = current == XO.E ? callback.name() : "NOP";
        return InlineKeyboardButton.builder()
                .callbackData(String.format("xo:%d:%d:%s", i, j, nextXo))
                .text(current.getCell())
                .build();
    }

    public static XOMove parseMove(String data) {
        String[] parts = data.split(":");
        return new XOMove(Integer.parseInt(parts[1]), Integer.parseInt(parts[2]), XO.valueOf(parts[3]));
    }
}
