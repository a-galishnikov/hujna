package ru.hujna.feature.xkcd;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import ru.hujna.bot.BotConfig;
import ru.hujna.processor.Processor;
import ru.hujna.processor.handler.Handler;
import ru.hujna.processor.matcher.Matcher;
import ru.hujna.processor.matcher.MessageRegexMatcher;

@Configuration
public class XkcdConfig {

    @Bean
    public Processor xkcdProcessor(@Qualifier("xkcdMatcher") Matcher matcher,
                                   @Qualifier("xkcdHandler") Handler handler) {
        return new Processor(matcher, handler);
    }

    @Bean
    public Matcher xkcdMatcher(@Qualifier("xkcdRegex") String regex) {
        return new MessageRegexMatcher(regex);
    }

    @Bean
    String xkcdRegex(BotConfig botConfig) {
        return "^\\/xkcd(" + botConfig.getBotUsername() + ")?$";
    }

    @Bean
    public Handler xkcdHandler(XkcdClient xkcdClient) {
        return new XkcdHandler(xkcdClient);
    }

    @Bean
    XkcdClient xkcdClient(@Qualifier("xkcdWebClient") WebClient xkcdClient,
                          @Qualifier("cxkcdWebClient") WebClient cxkcdClient) {
        return new XkcdClientImpl(xkcdClient, cxkcdClient);
    }

    @Bean
    WebClient cxkcdWebClient(@Value("https://c.xkcd.com") String cxkcdUrl) {
        return WebClient.create(cxkcdUrl);
    }

    @Bean
    WebClient xkcdWebClient(@Value("https://xkcd.com") String xkcdUrl) {
        return WebClient.create(xkcdUrl);
    }

}
