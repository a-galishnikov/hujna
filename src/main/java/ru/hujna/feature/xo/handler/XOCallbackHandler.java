package ru.hujna.feature.xo.handler;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.hujna.feature.xo.model.XOMove;
import ru.hujna.feature.xo.model.XOSession;
import ru.hujna.feature.xo.XOSessionCash;
import ru.hujna.feature.xo.XOUtil;
import ru.hujna.processor.handler.Handler;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
@Slf4j
public class XOCallbackHandler implements Handler {

    @NonNull
    private final XOSessionCash sessionCash;

    @Override
    public List<BotApiMethod<? extends Serializable>> handle(Update update) {
        var callback = update.getCallbackQuery();
        var chatId = callback.getMessage().getChatId();
        var chatIdStr = chatId.toString();

        var move = XOUtil.parseMove(callback.getData());
        var initialMessageId = move.messageId();
        var callbackMessageId = callback.getMessage().getMessageId();
        var userId = callback.getFrom().getId();

        return sessionCash.get(chatId, initialMessageId).map(session -> {
            var lockAcquired = false;
            try {
                lockAcquired = session.getLock().tryLock();
                List<BotApiMethod<? extends Serializable>> result = Collections.emptyList();
                if (lockAcquired) {
                    switch (session.getState()) {
                        case STARTED:
                        case PLAYING:
                            if (XOUtil.validate(session, move, userId)) {
                                var newSession = XOUtil.move(session, move);
                                sessionCash.put(newSession);

                                result = new ArrayList<>();

                                var editField = EditMessageReplyMarkup
                                        .builder()
                                        .messageId(callbackMessageId)
                                        .chatId(chatIdStr)
                                        .replyMarkup(XOUtil.markup(newSession))
                                        .build();

                                result.add(editField);

                                switch (newSession.getState()) {
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
            } finally {
                if (lockAcquired) {
                    session.getLock().unlock();
                }
            }
        }).orElse(Collections.emptyList());
    }
}
