package ru.hujna.processor.handler;

import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.io.Serializable;
import java.util.List;

public interface Handler {

    List<? extends BotApiMethod<? extends Serializable>> handle(Update update);

}
