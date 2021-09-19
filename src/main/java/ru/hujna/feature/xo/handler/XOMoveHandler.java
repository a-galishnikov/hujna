package ru.hujna.feature.xo.handler;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.hujna.feature.xo.GameCache;
import ru.hujna.feature.xo.XOUtil;
import ru.hujna.feature.xo.model.Game;
import ru.hujna.feature.xo.model.XO;
import ru.hujna.feature.xo.ui.Keyboard;
import ru.hujna.lock.TryLock;
import ru.hujna.feature.xo.model.Move;
import ru.hujna.feature.xo.parse.Parser;
import ru.hujna.processor.handler.Handler;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
@Slf4j
public class XOMoveHandler implements Handler {

    @NonNull
    private final GameCache cache;

    @NonNull
    private final Parser<Move> parser;

    @NonNull
    private final Keyboard keyboard;

    @Override
    public List<BotApiMethod<? extends Serializable>> handle(Update update) {
        var callback = update.getCallbackQuery();
        var chatId = callback.getMessage().getChatId();
        var chatIdStr = chatId.toString();

        var move = parser.parse(callback.getData());
        var initialMessageId = move.messageId();
        var callbackMessageId = callback.getMessage().getMessageId();
        var userId = callback.getFrom().getId();

        return cache.get(chatId, initialMessageId).map(game -> {
            try(var lock = TryLock.of(game.getLock())) {
                List<BotApiMethod<? extends Serializable>> result = Collections.emptyList();
                if (lock.isAcquired()) {
                    switch (game.getState()) {
                        case STARTED:
                        case PLAYING:
                            if (validate(game, move, userId)) {
                                var gameNext = game.move(move);
                                cache.put(gameNext);

                                result = new ArrayList<>();

                                var editField = EditMessageReplyMarkup
                                        .builder()
                                        .messageId(callbackMessageId)
                                        .chatId(chatIdStr)
                                        .replyMarkup(keyboard.markup(gameNext))
                                        .build();

                                result.add(editField);

                                switch (gameNext.getState()) {
                                    case FINISHED_WIN:
                                        var wonMsg = SendMessage.builder()
                                                .chatId(chatIdStr)
                                                .replyToMessageId(callbackMessageId)
                                                .text(XOUtil.name(callback.getFrom()) + " won")
                                                .build();
                                        result.add(wonMsg);
                                        break;
                                    case FINISHED_TIE:
                                        var tieMsg = SendMessage.builder()
                                                .chatId(chatIdStr)
                                                .replyToMessageId(callbackMessageId)
                                                .text("Tie")
                                                .build();
                                        result.add(tieMsg);
                                    default:
                                        break;
                                }
                            }
                            break;
                        case NEW:
                        case CANCELED:
                        case FINISHED_WIN:
                        case FINISHED_TIE:
                        default:
                            break;
                    }
                }
                return result;
            }
        }).orElse(Collections.emptyList());
    }

    private boolean validate(Game game, Move move, long userId) {
        boolean cellIsEmpty = game.getField().cell(move.x(), move.y()) == XO.E;
        boolean moveIsNotDuplicated = game.getLastMove() != move.xo();
        var expectedNextUser = game.getPlayers().get(move.xo());
        boolean userIsAuthorized = expectedNextUser.getUserId() == userId;
        return cellIsEmpty && moveIsNotDuplicated && userIsAuthorized;
    }
}
