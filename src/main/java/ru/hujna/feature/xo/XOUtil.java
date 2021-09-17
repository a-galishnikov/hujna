package ru.hujna.feature.xo;

import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import ru.hujna.feature.xo.model.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class XOUtil {

    private static final int DIM = 3;
    private static final XO[][] EMPTY_FIELD = emptyField(DIM);

    public static Game initGame(Long chatId, Integer messageId, Long starterId) {
        return Game.builder()
                .chatId(chatId)
                .messageId(messageId)
                .players(TwoPlayers.of(starterId, XO.random()))
                .state(State.NEW)
                .build();
    }

    public static XO[][] emptyField() {
        return EMPTY_FIELD;
    }

    private static XO[][] emptyField(int size) {
        XO[][] field = new XO[size][size];
        Arrays.stream(field).forEach(row -> Arrays.fill(row, XO.E));
        return field;
    }

    public static Game move(Game game, Move move) {
        XO[][] field = move(game.getField(), move);
        State state = calcState(field);

        return Game.builder()
                .chatId(game.getChatId())
                .messageId(game.getMessageId())
                .players(game.getPlayers())
                .lastMove(move.xo())
                .field(field)
                .state(state)
                .build();
    }

    public static Game join(Game game, long opponentId) {
        return Game.builder()
                .chatId(game.getChatId())
                .messageId(game.getMessageId())
                .players(TwoPlayers.of(game.getPlayers(), opponentId))
                .lastMove(game.getLastMove())
                .field(game.getField())
                .state(State.STARTED)
                .build();
    }

    // TODO: naive implementation, can be optimized
    static State calcState(XO[][] field) {
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
                    return State.FINISHED_WIN;
                }
            }
        }

        return hasMoreMoves ? State.PLAYING : State.FINISHED_TIE;
    }

    public static XO[][] move(XO[][] field, Move move) {
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

    public static InlineKeyboardMarkup markup(Game game) {
        return InlineKeyboardMarkup.builder()
                .keyboard(XOUtil.keyboard(game))
                .build();
    }

    private static List<List<InlineKeyboardButton>> keyboard(Game game) {
        XO[][] field = game.getField();
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>(field.length);
        for (int i = 0; i < field.length; i++) {
            List<InlineKeyboardButton> row = new ArrayList<>(field[i].length);
            for (int j = 0; j < field[i].length; j++) {
                row.add(button(game.getMessageId(), i, j, field[i][j], game.getLastMove().reverse()));
            }
            keyboard.add(row);
        }
        return keyboard;
    }

    private static InlineKeyboardButton button(int messageId, int i, int j, XO current, XO callback) {
        String nextXo = current == XO.E ? callback.name() : "NOP";
        return InlineKeyboardButton.builder()
                .callbackData(String.format("xoMove:%d:%d:%d:%s", messageId, i, j, nextXo))
                .text(current.getCell())
                .build();
    }

    public static boolean validate(Game game, Move move, long userId) {
        boolean cellIsEmpty = game.getField()[move.x()][move.y()] == XO.E;
        boolean moveIsNotDuplicated = game.getLastMove() != move.xo();
        var expectedNextUser = game.getPlayers().get(move.xo());
        boolean userIsAuthorized = expectedNextUser.getUserId() == userId;
        return cellIsEmpty && moveIsNotDuplicated && userIsAuthorized;
    }

    public static String name(User user) {
        return user.getUserName() == null || user.getUserName().isBlank()
                ? String.format("%s %s", user.getFirstName(), user.getLastName())
                : user.getUserName();
    }
}
