package ru.hujna.feature.xo.model;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;
import ru.hujna.feature.xo.XOUtil;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Builder
@Getter
@ToString
public class Game {

    @NonNull
    private final Long chatId;

    @NonNull
    private final Integer messageId;

    @NonNull
    private final TwoPlayers players;

    @NonNull
    private final State state;

    @Builder.Default
    @NonNull
    private final XO lastMove = XO.E;

    @Builder.Default
    @NonNull
    private final XO[][] field = XOUtil.emptyField();

    @NonNull
    private final Lock lock = new ReentrantLock();

}
