package ru.hujna.feature.xo;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.objects.Update;
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
    public List<? extends BotApiMethod<? extends Serializable>> handle(Update update) {
        var callback = update.getCallbackQuery();
        var chatId = callback.getMessage().getChatId();
        var chatIdStr = chatId.toString();
        XOMove move = XOUtil.parseMove(update.getCallbackQuery().getData());
        var initialMessageId = move.getMessageId();
        var callbackMessageId = callback.getMessage().getMessageId();

        return sessionCash.get(chatId, initialMessageId).map(session -> {
            var lockAcquired = false;
            try {
                List<BotApiMethod<? extends Serializable>> result = Collections.emptyList();
                lockAcquired = session.getLock().tryLock();
                if (lockAcquired) {
                    switch (session.getState()) {
                        case NEW:
                        case STARTED:
                        case PLAYING:

                            if (XOUtil.validate(session, move)) {
                                XOSession newSession = XOUtil.move(session, move);
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
                                                .text(callback.getFrom().getUserName() + " won")
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
