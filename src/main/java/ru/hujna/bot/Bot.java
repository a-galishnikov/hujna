package ru.hujna.bot;


import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.hujna.processor.handler.Handler;

@Component
@RequiredArgsConstructor
public class Bot extends BotExecuteDecorator {

    @NonNull
    private final BotConfig config;

    @NonNull
    private final Handler dispatcher;

    @Override
    public String getBotUsername() {
        return config.botUsername();
    }

    @Override
    public String getBotToken() {
        return config.botToken();
    }

    @Override
    public void onUpdateReceived(Update update) {
        dispatcher.handle(update).forEach(this::execute);
    }
}
