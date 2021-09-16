package ru.hujna.bot;

import lombok.NonNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public record BotConfig(@NonNull @Value("${bot.name}") String botUsername,
                        @NonNull @Value("${bot.token}") String botToken) {

}
