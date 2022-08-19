package ru.hujna.feature.xo.handler;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.hujna.feature.xo.GameCache;
import ru.hujna.feature.xo.XOUtil;
import ru.hujna.feature.xo.model.Game;
import ru.hujna.feature.xo.model.GameUpdate;
import ru.hujna.feature.xo.model.Join;
import ru.hujna.feature.xo.model.State;
import ru.hujna.feature.xo.model.XO;
import ru.hujna.feature.xo.parse.Parser;
import ru.hujna.feature.xo.ui.Keyboard;
import ru.hujna.lock.TryLock;
import ru.hujna.processor.handler.Handler;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
@Slf4j
public class XOJoinHandler implements Handler {

    @NonNull
    private final GameCache gameCache;

    @NonNull
    private final Parser<Join> parser;

    @NonNull
    private final Keyboard keyboard;

    @Override
    public List<? extends PartialBotApiMethod<? extends Serializable>> handle(Update update) {

        var gameUpdate = new GameUpdate(update, parser);

        return gameCache.get(gameUpdate.getChatId(), gameUpdate.getInitialMessageId()).map(game -> {
            try (var lock = TryLock.of(game.getLock())) {
                List<BotApiMethod<? extends Serializable>> result = Collections.emptyList();
                if (lock.isAcquired()) {
                    if (validate(game, gameUpdate)) {
                        result = getGameUpdateResult(gameUpdate, game);
                    }
                }
                return result;
            }
        }).orElse(Collections.emptyList());
    }

    @NonNull
    private List<BotApiMethod<? extends Serializable>> getGameUpdateResult(GameUpdate gameUpdate, Game game) {
        List<BotApiMethod<? extends Serializable>> result;
        var gameNext = game.join(gameUpdate.getOpponentId());
        gameCache.put(gameNext);

        result = new ArrayList<>();
        var starter = gameNext.getPlayers().starter();

        var editMessage = EditMessageText.builder()
                .messageId(gameUpdate.getCallbackMessageId())
                .chatId(gameUpdate.getChatId().toString())
                .text(String.format("%s (%s)%n%s joined (%s)%n%s moves first",
                        gameUpdate.getCallback().getMessage().getText(),
                        starter.getXo().getCell(),
                        XOUtil.name(gameUpdate.getOpponent()),
                        starter.getXo().reverse().getCell(),
                        XO.X.getCell()))
                .build();
        result.add(editMessage);

        var editKeyboard = EditMessageReplyMarkup
                .builder()
                .messageId(gameUpdate.getCallbackMessageId())
                .chatId(gameUpdate.getChatId().toString())
                .replyMarkup(keyboard.markup(gameNext))
                .build();
        result.add(editKeyboard);
        return result;
    }

    private boolean validate(Game game, GameUpdate update) {
        boolean appropriateState = game.getState() == State.NEW;
        var callback = update.getCallback();
        var chat = callback.getMessage().getChat();
        boolean isPersonalChat = chat.isUserChat();
        boolean starterNotOpponent = game.getPlayers().starter().getUserId() != callback.getFrom().getId();
        return appropriateState && (isPersonalChat || starterNotOpponent);
    }
}
