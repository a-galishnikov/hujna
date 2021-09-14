package ru.hujna.feature.xo.model;

import lombok.Getter;

@Getter
public enum XO {
    E("⬜"),

    O("⭕"),

    X("❌"){
        @Override
        public XO reverse() {
            return O;
        }
    };

    private final String cell;

    public XO reverse() {
        return X;
    }

    XO(String cell) {
        this.cell = cell;
    }
}
