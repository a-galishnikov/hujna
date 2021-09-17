package ru.hujna.feature.xo.model;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;
import ru.hujna.feature.xo.XOUtil;

import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Builder
@Getter
@ToString
public class XOSession {

    @NonNull
    private final Long chatId;

    @NonNull
    private final Integer messageId;

    @NonNull
    private final XOPlayers players;

    @NonNull
    private final XOState state;

    @Builder.Default
    @NonNull
    private final XO lastXo = XO.E;

    @Builder.Default
    @NonNull
    private final XO[][] field = XOUtil.emptyField();

    @NonNull
    private final Lock lock = new ReentrantLock();

}
