package ru.hujna.feature.config;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.hujna.bot.BotConfig;
import ru.hujna.feature.xo.XOSessionCash;
import ru.hujna.processor.Processor;
import ru.hujna.processor.handler.Handler;
import ru.hujna.feature.huj.HujnaHandler;
import ru.hujna.feature.xo.XOCallbackHandler;
import ru.hujna.feature.xo.XOStartHandler;
import ru.hujna.processor.matcher.CallbackRegexMatcher;
import ru.hujna.processor.matcher.MessageRegexMatcher;


@Configuration
@RequiredArgsConstructor
public class ProcessorConfig {

    @NonNull
    private final BotConfig botConfig;

    @Bean
    public Processor hujnaProcessor() {
        return msgProcessor("^([AaАа])+[^A-Za-zА-Яа-я0-9]{0,5}$", new HujnaHandler());
    }

    @Bean
    public Processor xoStartProcessor(XOSessionCash sessionCash) {
        return msgProcessor("^\\/xo(" + botConfig.getBotUsername() + ")?$", new XOStartHandler(sessionCash));
    }

    @Bean
    public Processor xoCallbackProcessor(XOSessionCash sessionCash) {
        return callbackProcessor("^xo:\\d+:[0-2]:[0-2]:[EOX]$", new XOCallbackHandler(sessionCash));
    }

    private Processor msgProcessor(String regex, Handler handler) {
        return new Processor(new MessageRegexMatcher(regex), handler);
    }

    private Processor callbackProcessor(String regex, Handler handler) {
        return new Processor(new CallbackRegexMatcher(regex), handler);
    }
}
