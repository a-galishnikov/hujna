package ru.hujna.bot;

import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Optional;

public interface UpdateHandler {
    Optional<BotApiMethod<Message>> handle(Update update);
}
