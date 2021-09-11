package ru.hujna.processor.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.hujna.processor.Processor;
import ru.hujna.processor.handler.Handler;
import ru.hujna.processor.handler.HujnaHandler;
import ru.hujna.processor.handler.XOHandler;
import ru.hujna.processor.matcher.RegexMatcher;


@Configuration
public class ProcessorConfig {

    @Bean
    public Processor hujnaProcessor() {
        return processor("^([AaАа])+[^A-Za-zА-Яа-я0-9]{0,5}$", new HujnaHandler());
    }

    @Bean
    public Processor xoProcessor() {
        return processor("^\\/xo$", new XOHandler());
    }

    private Processor processor(String regex, Handler handler) {
        return new Processor(new RegexMatcher(regex), handler);
    }
}
