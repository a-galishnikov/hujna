package ru.hujna.bot;

import lombok.SneakyThrows;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;

import java.io.Serializable;

abstract class BotExecuteDecorator extends TelegramLongPollingBot {

    @SneakyThrows
    public <T extends Serializable> Serializable execute(PartialBotApiMethod<T> method) {
        if (method instanceof BotApiMethod<T> botMethod) {
            return execute(botMethod);
        }
        if (method instanceof SendPhoto sendPhoto) {
            return execute(sendPhoto);
        }
        throw new UnsupportedOperationException("Method type not supported: " + method.getClass());
    }
}
