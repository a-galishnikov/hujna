package ru.hujna.feature.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.hujna.feature.xo.XOSessionCash;
import ru.hujna.processor.Processor;
import ru.hujna.processor.handler.Handler;
import ru.hujna.feature.huj.HujnaHandler;
import ru.hujna.feature.xo.XOCallbackHandler;
import ru.hujna.feature.xo.XOStartHandler;
import ru.hujna.processor.matcher.CallbackRegexMatcher;
import ru.hujna.processor.matcher.MessageRegexMatcher;


@Configuration
public class ProcessorConfig {

    @Bean
    public Processor hujnaProcessor() {
        return msgProcessor("^([AaАа])+[^A-Za-zА-Яа-я0-9]{0,5}$", new HujnaHandler());
    }

    @Bean
    public Processor xoStartProcessor(XOSessionCash sessionCash) {
        return msgProcessor("^\\/xo$", new XOStartHandler(sessionCash));
    }

    @Bean
    public Processor xoCallbackProcessor(XOSessionCash sessionCash) {
        return callbackProcessor("^xo:[0-2]:[0-2]:[EOX]$", new XOCallbackHandler(sessionCash));
    }

    private Processor msgProcessor(String regex, Handler handler) {
        return new Processor(new MessageRegexMatcher(regex), handler);
    }

    private Processor callbackProcessor(String regex, Handler handler) {
        return new Processor(new CallbackRegexMatcher(regex), handler);
    }
}
