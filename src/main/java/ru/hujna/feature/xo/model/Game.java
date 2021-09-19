package ru.hujna.feature.xo.model;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;

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
    private final Field field = Field.empty();

    @NonNull
    private final Lock lock = new ReentrantLock();

    public static Game init(Long chatId, Integer messageId, Long starterId) {
        return Game.builder()
                .chatId(chatId)
                .messageId(messageId)
                .players(TwoPlayers.of(starterId, XO.random()))
                .state(State.NEW)
                .build();
    }

    public Game join(long opponentId) {
        return Game.builder()
                .chatId(this.getChatId())
                .messageId(this.getMessageId())
                .players(TwoPlayers.of(this.getPlayers(), opponentId))
                .lastMove(this.getLastMove())
                .field(this.getField())
                .state(State.STARTED)
                .build();
    }

    public Game move(Move move) {
        var field = getField().move(move);
        var state = field.state();

        return Game.builder()
                .chatId(this.getChatId())
                .messageId(this.getMessageId())
                .players(this.getPlayers())
                .lastMove(move.xo())
                .field(field)
                .state(state)
                .build();
    }

}
