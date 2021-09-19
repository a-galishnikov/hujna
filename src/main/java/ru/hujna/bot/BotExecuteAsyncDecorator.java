package ru.hujna.bot;

import lombok.SneakyThrows;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.Serializable;
import java.util.concurrent.CompletableFuture;

public interface BotExecuteAsyncDecorator {

    @SneakyThrows
    default <T extends Serializable> CompletableFuture<? extends Serializable> executeAsync(PartialBotApiMethod<T> method) {
        if (method instanceof BotApiMethod<T> botMethod) {
            return executeAsync(botMethod);
        }
        if (method instanceof SendPhoto sendPhoto) {
            return executeAsync(sendPhoto);
        }
        throw new UnsupportedOperationException("Method type not supported: " + method.getClass());
    }

    <T extends Serializable, Method extends BotApiMethod<T>> CompletableFuture<T> executeAsync(Method method) throws TelegramApiException;

    CompletableFuture<Message> executeAsync(SendPhoto sendPhoto);

}
