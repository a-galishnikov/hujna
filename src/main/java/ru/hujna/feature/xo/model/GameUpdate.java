package ru.hujna.feature.xo.model;

import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import ru.hujna.feature.xo.parse.Parser;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Objects;

/**
 * Информация необходимая для обновления состояния игры.
 */
@ParametersAreNonnullByDefault
public class GameUpdate {
    private final Long chatId;
    private final User opponent;
    private final long opponentId;
    private final int initialMessageId;
    private final int callbackMessageId;
    private final CallbackQuery callback;

    public Long getChatId() {
        return chatId;
    }

    public User getOpponent() {
        return opponent;
    }

    public long getOpponentId() {
        return opponentId;
    }

    public int getInitialMessageId() {
        return initialMessageId;
    }

    public int getCallbackMessageId() {
        return callbackMessageId;
    }

    public CallbackQuery getCallback() {
        return callback;
    }

    public GameUpdate(Update update, Parser<Join> parser) {
        var callback = update.getCallbackQuery();
        var message = callback.getMessage();
        var join = parser.parse(callback.getData());
        this.callback = update.getCallbackQuery();
        this.chatId = Objects.requireNonNull(message.getChatId(), "chatId");
        this.opponent = Objects.requireNonNull(callback.getFrom(), "opponent");
        this.opponentId = Objects.requireNonNull(opponent.getId(), "opponentId");
        this.callbackMessageId = Objects.requireNonNull(message.getMessageId(), "callbackMessageId");
        this.initialMessageId = join.messageId();
    }
}
