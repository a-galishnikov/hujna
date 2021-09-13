package ru.hujna.processor.handler;

import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.io.Serializable;
import java.util.List;

public interface Handler {

    List<? extends PartialBotApiMethod<? extends Serializable>> handle(Update update);

}
