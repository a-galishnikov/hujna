package ru.hujna.processor.handler;

import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Optional;

public class XOHandler implements Handler {

    @Override
    public Optional<BotApiMethod<Message>> handle(Update update) {
        return Optional.ofNullable(update)
                .map(x -> SendMessage
                        .builder()
                        .chatId(update.getMessage().getChatId().toString())
                        .text("Coming soon...")
                        .build());
    }
}
