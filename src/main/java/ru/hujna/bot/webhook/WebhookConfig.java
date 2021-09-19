package ru.hujna.bot.webhook;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.updates.SetWebhook;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import org.telegram.telegrambots.updatesreceivers.DefaultWebhook;

@Profile("webhook")
@Configuration
public class WebhookConfig {

    @Bean
    public TelegramBotsApi telegramBotsApi(@Value("${PORT:8181}") String port) throws TelegramApiException {
        var webhook = new DefaultWebhook();
        webhook.setInternalUrl(String.format("http://localhost:%s", port));
        return new TelegramBotsApi(DefaultBotSession.class, webhook);
    }

    @Bean
    public DefaultBotOptions options() {
        return new DefaultBotOptions();
    }

    @Bean
    public SetWebhook setWebhook(@Value("${bot.url}") String url) {
        return SetWebhook.builder()
                .url(url)
                .build();
    }
}