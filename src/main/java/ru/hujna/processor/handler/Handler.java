package ru.hujna.processor.handler;

import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.io.Serializable;
import java.util.Optional;

public interface Handler {

    Optional<BotApiMethod<? extends Serializable>> handle(Update update);

}
