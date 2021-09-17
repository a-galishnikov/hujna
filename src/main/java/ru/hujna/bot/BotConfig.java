package ru.hujna.bot;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Getter
public class BotConfig {
    @NonNull
    @Value("${bot.name}")
    private final String botUsername;

    @NonNull
    @Value("${bot.token}")
    private final String botToken;

}
