package ru.hujna.feature.xo.model;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class XOPlayer {

    private final long userId;
    private final XO xo;
    private final boolean gameStarter;

    XOPlayer(long userId, XO xo, boolean gameStarter) {
        if (xo == null || xo == XO.E) {
            throw new IllegalArgumentException("XOPlayer can only be assigned to X or O");
        }

        this.userId = userId;
        this.xo = xo;
        this.gameStarter = gameStarter;

    }
}
