package ru.hujna.bot;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Optional;

@Component
public class HujnaHandler implements UpdateHandler {

    static final String REGEX = "^([AaАа])+[^A-Za-zА-Яа-я0-9]{0,5}$";
    static final String HUJNA = "Huj na!";

    @Override
    public Optional<BotApiMethod<Message>> handle(Update update) {
        return Optional.ofNullable(update)
                .filter(x -> x.hasMessage() && x.getMessage().hasText())
                .map(x -> x.getMessage().getText())
                .filter(x -> x.matches(REGEX))
                .map(x -> SendMessage
                        .builder()
                        .chatId(update.getMessage().getChatId().toString())
                        .replyToMessageId(update.getMessage().getMessageId())
                        .text(HUJNA)
                        .build());
    }
}
