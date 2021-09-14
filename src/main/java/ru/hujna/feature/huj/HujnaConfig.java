package ru.hujna.feature.huj;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.hujna.processor.Processor;
import ru.hujna.processor.handler.Handler;
import ru.hujna.processor.matcher.Matcher;
import ru.hujna.processor.matcher.MessageRegexMatcher;

@Configuration
public class HujnaConfig {
    @Bean
    public Processor hujnaProcessor(@Qualifier("hujnaMatcher") Matcher matcher,
                                    @Qualifier("hujnaHandler") Handler handler) {
        return new Processor(matcher, handler);
    }

    @Bean
    @Qualifier("hujnaMatcher")
    public Matcher hujnaMatcher() {
        return new MessageRegexMatcher("^([AaАа])+[^A-Za-zА-Яа-я0-9]{0,5}$");
    }

    @Bean
    @Qualifier("hujnaHandler")
    public Handler hujnaHandler() {
        return new HujnaHandler();
    }
}
