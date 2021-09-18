package ru.hujna.feature.xo;

import org.telegram.telegrambots.meta.api.objects.User;
import ru.hujna.feature.xo.model.Move;
import ru.hujna.feature.xo.model.State;
import ru.hujna.feature.xo.model.XO;

import java.util.Arrays;

public class XOUtil {

    private static final int DIM = 3;
    private static final XO[][] EMPTY_FIELD = emptyField(DIM);

    public static XO[][] emptyField() {
        return EMPTY_FIELD;
    }

    private static XO[][] emptyField(int size) {
        XO[][] field = new XO[size][size];
        Arrays.stream(field).forEach(row -> Arrays.fill(row, XO.E));
        return field;
    }

    // TODO: naive implementation, can be optimized
    public static State calcState(XO[][] field) {
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

    public static String name(User user) {
        return user.getUserName() == null || user.getUserName().isBlank()
                ? String.format("%s %s", user.getFirstName(), user.getLastName())
                : user.getUserName();
    }
}
