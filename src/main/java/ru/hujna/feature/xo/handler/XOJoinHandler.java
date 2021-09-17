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
import ru.hujna.lock.TryLock;
import ru.hujna.feature.xo.model.Join;
import ru.hujna.feature.xo.model.XO;
import ru.hujna.feature.xo.model.Game;
import ru.hujna.feature.xo.model.State;
import ru.hujna.feature.xo.parse.Parser;
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

    @Override
    public List<? extends PartialBotApiMethod<? extends Serializable>> handle(Update update) {

        var callback = update.getCallbackQuery();
        var msg = callback.getMessage();
        var chatId = msg.getChatId();
        var opponent = callback.getFrom();
        var opponentId = opponent.getId();

        var join = parser.parse(callback.getData());
        var initialMessageId = join.messageId();
        var callbackMessageId = msg.getMessageId();

        return gameCache.get(chatId, initialMessageId).map(game -> {
            try (var lock = TryLock.of(game.getLock())) {
                List<BotApiMethod<? extends Serializable>> result = Collections.emptyList();
                if (lock.isLockAcquired()) {
                    if (validate(game, update)) {
                        var gameNext = XOUtil.join(game, opponentId);
                        gameCache.put(gameNext);

                        result = new ArrayList<>();
                        var starter = gameNext.getPlayers().starter();

                        var editMessage = EditMessageText.builder()
                                .messageId(callbackMessageId)
                                .chatId(chatId.toString())
                                .text(String.format("%s (%s)%n%s joined (%s)%n%s moves first",
                                        callback.getMessage().getText(),
                                        starter.getXo().getCell(),
                                        XOUtil.name(opponent),
                                        starter.getXo().reverse().getCell(),
                                        XO.X.getCell()))
                                .build();
                        result.add(editMessage);

                        var editKeyboard = EditMessageReplyMarkup
                                .builder()
                                .messageId(callbackMessageId)
                                .chatId(chatId.toString())
                                .replyMarkup(XOUtil.markup(gameNext))
                                .build();
                        result.add(editKeyboard);
                    }
                }
                return result;
            }
        }).orElse(Collections.emptyList());
    }

    private boolean validate(Game game, Update update) {
        boolean appropriateState = game.getState() == State.NEW;
        var callback = update.getCallbackQuery();
        var chat = callback.getMessage().getChat();
        boolean isPersonalChat= chat.isUserChat();
        boolean starterNotOpponent = game.getPlayers().starter().getUserId() != callback.getFrom().getId();
        return appropriateState && (isPersonalChat || starterNotOpponent);
    }
}
