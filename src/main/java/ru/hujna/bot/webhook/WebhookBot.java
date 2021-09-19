package ru.hujna.bot.webhook;


import lombok.NonNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.updates.SetWebhook;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.starter.SpringWebhookBot;
import ru.hujna.bot.BotConfig;
import ru.hujna.bot.BotExecuteAsyncDecorator;
import ru.hujna.processor.handler.Handler;

@Component
@Profile("webhook")
public class WebhookBot extends SpringWebhookBot implements BotExecuteAsyncDecorator {

    private final BotConfig config;
    private final Handler dispatcher;
    private final String path;

    public WebhookBot(@NonNull BotConfig config,
                      @NonNull Handler dispatcher,
                      @NonNull @Value("${bot.path}") String path,
                      @NonNull DefaultBotOptions options,
                      @NonNull SetWebhook setWebhook) {
        super(options, setWebhook);
        this.config = config;
        this.dispatcher = dispatcher;
        this.path = path;
    }

    @Override
    public String getBotUsername() {
        return config.getBotUsername();
    }

    @Override
    public String getBotToken() {
        return config.getBotToken();
    }

    @Override
    public String getBotPath() {
        return path;
    }

    @Override
    public BotApiMethod<?> onWebhookUpdateReceived(Update update) {
        dispatcher.handle(update).forEach(this::executeAsync);
        return null;
    }


}
