package ru.hujna.bot;

import lombok.SneakyThrows;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.Serializable;

public interface BotExecuteDecorator {

    @SneakyThrows
    default <T extends Serializable> Serializable execute(PartialBotApiMethod<T> method) {
        if (method instanceof BotApiMethod<T> botMethod) {
            return execute(botMethod);
        }
        if (method instanceof SendPhoto sendPhoto) {
            return execute(sendPhoto);
        }
        throw new UnsupportedOperationException("Method type not supported: " + method.getClass());
    }

    <T extends Serializable, Method extends BotApiMethod<T>> T execute(Method method) throws TelegramApiException;

    Message execute(SendPhoto sendPhoto) throws TelegramApiException;

}
