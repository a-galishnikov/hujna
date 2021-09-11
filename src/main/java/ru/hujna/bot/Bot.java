package ru.hujna.bot;


import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.hujna.processor.handler.Handler;

@Component
@RequiredArgsConstructor
public class Bot extends TelegramLongPollingBot {

    @NonNull
    private final BotConfig config;

    @NonNull
    private final Handler handler;

    @Override
    public String getBotUsername() {
        return config.getBotUsername();
    }

    @Override
    public String getBotToken() {
        return config.getBotToken();
    }

    @SneakyThrows
    @Override
    public void onUpdateReceived(Update update) {
        var res = handler.handle(update);
        if (res.isPresent()) {
            execute(res.get());
        }
    }
}
