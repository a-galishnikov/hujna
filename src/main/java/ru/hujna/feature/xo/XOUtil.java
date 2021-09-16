package ru.hujna.feature.xo;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import ru.hujna.feature.xo.model.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class XOUtil {

    private static final int DIM = 3;
    private static final XO[][] EMPTY_FIELD = emptyField(DIM);

    public static XOSession initPvPSession(Long chatId, Integer messageId) {
        return XOSession.builder()
                .chatId(chatId)
                .messageId(messageId)
                .type(XOType.PVP)
                .state(XOState.NEW)
                .build();
    }

    public static XOSession initPvESession(Long chatId, Integer messageId) {
        return XOSession.builder()
                .chatId(chatId)
                .messageId(messageId)
                .type(XOType.PVE)
                .state(XOState.STARTED)
                .build();
    }

    public static String sessionKey(XOSession session) {
        return sessionKey(session.getChatId(), session.getMessageId());
    }

    public static String sessionKey(Long chatId, Integer messageId) {
        return chatId + ":" + messageId;
    }

    public static XO[][] emptyField() {
        return EMPTY_FIELD;
    }

    private static XO[][] emptyField(int size) {
        XO[][] field = new XO[size][size];
        Arrays.stream(field).forEach(row -> Arrays.fill(row, XO.E));
        return field;
    }

    public static XOSession move(XOSession session, XOMove move) {
        XO[][] field = move(session.getField(), move);
        XOState state = calcState(field);

        return XOSession.builder()
                .chatId(session.getChatId())
                .messageId(session.getMessageId())
                .type(session.getType())
                .lastXo(move.xo())
                .field(field)
                .state(state)
                .build();
    }

    // TODO: naive implementation, can be optimized
    static XOState calcState(XO[][] field) {
        int dim = field.length;
        var hasMoreMoves = false;
        var diag1SumX = 0;
        var diag1SumO = 0;
        var diag2SumX = 0;
        var diag2SumO = 0;
        var colSumX = new int[dim];
        var colSumO = new int[dim];
        for (int i = 0; i < dim; i++) {
            var rowSumX = 0;
            var rowSumO = 0;
            for (int j = 0; j < dim; j++) {
                switch (field[i][j]) {
                    case E -> hasMoreMoves = true;
                    case O -> {
                        rowSumO++;
                        colSumO[j]++;
                        if (i == j) diag1SumO++;
                        if (i == dim - j - 1) diag2SumO++;
                    }
                    case X -> {
                        colSumX[j]++;
                        rowSumX++;
                        if (i == j) diag1SumX++;
                        if (i == dim - j - 1) diag2SumX++;
                    }
                }
                if ((i == dim - 1 || j == dim - 1) &&
                        (rowSumO == dim || rowSumX == dim ||
                        diag1SumO == dim || diag1SumX == dim ||
                        diag2SumO == dim || diag2SumX == dim ||
                        colSumO[j] == dim || colSumX[j] == dim)) {
                    return XOState.FINISHED_WIN;
                }
            }
        }

        return hasMoreMoves ? XOState.PLAYING : XOState.FINISHED_TIE;
    }

    public static XO[][] move(XO[][] field, XOMove move) {
        XO[][] newField = copy(field);
        newField[move.x()][move.y()] = move.xo();
        return newField;
    }

    private static XO[][] copy(XO[][] arr) {
        XO[][] copy = new XO[DIM][DIM];
        for (int i = 0; i < DIM; i++) {
            System.arraycopy(arr[i], 0, copy[i], 0, DIM);
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
                row.add(button(session.getMessageId(), i, j, field[i][j], session.getLastXo().reverse()));
            }
            keyboard.add(row);
        }
        return keyboard;
    }

    private static InlineKeyboardButton button(int messageId, int i, int j, XO current, XO callback) {
        String nextXo = current == XO.E ? callback.name() : "NOP";
        return InlineKeyboardButton.builder()
                .callbackData(String.format("xo:%d:%d:%d:%s", messageId, i, j, nextXo))
                .text(current.getCell())
                .build();
    }

    public static XOMove parseMove(String data) {
        String[] parts = data.split(":");
        return new XOMove(Integer.parseInt(parts[1]), Integer.parseInt(parts[2]), Integer.parseInt(parts[3]), XO.valueOf(parts[4]));
    }

    public static boolean validate(XOSession session, XOMove move) {
        boolean cellIsEmpty = session.getField()[move.x()][move.y()] == XO.E;
        boolean moveIsNotDuplicated = session.getLastXo() != move.xo();
        return cellIsEmpty && moveIsNotDuplicated;
    }
}
