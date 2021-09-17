package ru.hujna.feature.xo.model;

import lombok.Getter;

import java.util.concurrent.ThreadLocalRandom;

@Getter
public enum XO {
    E("⬜"),

    O("⭕"),

    X("❌") {
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

    public static XO random() {
        return ThreadLocalRandom.current().nextBoolean() ? X : O;
    }
}
