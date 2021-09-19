package ru.hujna.feature.xo.model;

import java.util.Arrays;

public record Field(XO[][] field) {

    private static final int DIM = 3;
    private static final Field EMPTY_FIELD = new Field(empty(DIM));

    public static Field empty() {
        return EMPTY_FIELD;
    }

    private static XO[][] empty(int size) {
        XO[][] field = new XO[size][size];
        Arrays.stream(field).forEach(row -> Arrays.fill(row, XO.E));
        return field;
    }

    public Field move(Move move) {
        XO[][] newField = copy();
        newField[move.x()][move.y()] = move.xo();
        return new Field(newField);
    }

    private XO[][] copy() {
        XO[][] copy = new XO[DIM][DIM];
        for (int i = 0; i < DIM; i++) {
            System.arraycopy(field[i], 0, copy[i], 0, DIM);
        }
        return copy;
    }

    public XO cell(int x, int y) {
        return field[x][y];
    }

    public int dim() {
        return field.length;
    }

    public State state() {
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

}
