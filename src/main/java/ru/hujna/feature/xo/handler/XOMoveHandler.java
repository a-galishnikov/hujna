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
                if (lock.isLockAcquired()) {
                    switch (game.getState()) {
                        case STARTED:
                        case PLAYING:
                            if (XOUtil.validate(game, move, userId)) {
                                var gameNext = XOUtil.move(game, move);
                                cache.put(gameNext);

                                result = new ArrayList<>();

                                var editField = EditMessageReplyMarkup
                                        .builder()
                                        .messageId(callbackMessageId)
                                        .chatId(chatIdStr)
                                        .replyMarkup(XOUtil.markup(gameNext))
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
}
