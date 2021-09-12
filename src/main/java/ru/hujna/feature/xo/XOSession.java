package ru.hujna.feature.xo;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;

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
    private final XOState state;

    @NonNull
    private final XOType type;

    @Builder.Default
    @NonNull
    private final XO lastXo = XO.E;

    @Builder.Default
    @NonNull
    private final XO[][] field = XOUtil.emptyField();

    @NonNull
    private final Lock lock = new ReentrantLock();

}
