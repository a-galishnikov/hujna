package ru.hujna.bot.polling;


import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.hujna.bot.BotConfig;
import ru.hujna.bot.BotExecuteDecorator;
import ru.hujna.processor.handler.Handler;

@Component
@Profile("polling")
@RequiredArgsConstructor
public class PollingBot extends TelegramLongPollingBot implements BotExecuteDecorator {

    @NonNull
    private final BotConfig config;

    @NonNull
    private final Handler dispatcher;

    @Override
    public String getBotUsername() {
        return config.getBotUsername();
    }

    @Override
    public String getBotToken() {
        return config.getBotToken();
    }

    @Override
    public void onUpdateReceived(Update update) {
        dispatcher.handle(update).forEach(this::execute);
    }
}
