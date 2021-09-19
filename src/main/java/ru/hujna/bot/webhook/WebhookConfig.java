package ru.hujna.bot.webhook;

import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class WebhookConfig {

    @Bean
    public TelegramBotsApi telegramBotsApi(@Value("${PORT:8181}") String port) throws TelegramApiException {
        var webhook = new DefaultWebhook();
        webhook.setInternalUrl(String.format("http://0.0.0.0:%s", port));
        return new TelegramBotsApi(DefaultBotSession.class, webhook);
    }

    @Bean
    public DefaultBotOptions options() {
        var options = new DefaultBotOptions();
        int cores = Runtime.getRuntime().availableProcessors();
        int maxThreads = cores > 2 ? cores / 2 : 1; // heroku cores are shared, so will try to use only half
        log.info("Found {} cores, setting {} maxThreads", cores, maxThreads);
        options.setMaxThreads(cores);
        return options;
    }

    @Bean
    public SetWebhook setWebhook(@Value("${bot.url}") String url) {
        return SetWebhook.builder()
                .url(url)
                .build();
    }
}