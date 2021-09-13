package ru.hujna.feature.xo;

import lombok.Getter;

@Getter
public enum XO {
    E("⬜"),

    O("⭕"),

    X("❌"){
        @Override
        XO reverse() {
            return O;
        }
    };

    private final String cell;

    XO reverse() {
        return X;
    }

    XO(String cell) {
        this.cell = cell;
    }
}
