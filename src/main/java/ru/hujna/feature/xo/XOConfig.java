package ru.hujna.feature.xo;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.hujna.bot.BotConfig;
import ru.hujna.processor.Processor;
import ru.hujna.processor.handler.Handler;
import ru.hujna.processor.matcher.CallbackRegexMatcher;
import ru.hujna.processor.matcher.Matcher;
import ru.hujna.processor.matcher.MessageRegexMatcher;


@Configuration
public class XOConfig {

    @Bean
    public Processor xoMsgProcessor(@Qualifier("xoMsgMatcher") Matcher matcher,
                                    @Qualifier("xoMsgHandler") Handler handler) {
        return new Processor(matcher, handler);
    }

    @Bean
    @Qualifier("xoMsgMatcher")
    public Matcher xoMsgMatcher(@Qualifier("xoMsgRegex") String regex) {
        return new MessageRegexMatcher(regex);
    }

    @Bean
    @Qualifier("xoMsgRegex")
    String xoMsgRegex(BotConfig botConfig) {
        return "^\\/xo(" + botConfig.getBotUsername() + ")?$";
    }

    @Bean
    @Qualifier("xoMsgHandler")
    public Handler xoMsgHandler(XOSessionCash sessionCash) {
        return new XOStartHandler(sessionCash);
    }

    @Bean
    public Processor xoCallbackProcessor(@Qualifier("xoCallbackMatcher") Matcher matcher,
                                         @Qualifier("xoCallbackHandler") Handler handler) {
        return new Processor(matcher, handler);
    }

    @Bean
    @Qualifier("xoCallbackMatcher")
    public Matcher xoCallbackMatcher(@Qualifier("xoCallbackRegex") String regex) {
        return new CallbackRegexMatcher(regex);
    }

    @Bean
    @Qualifier("xoCallbackRegex")
    String xoCallbackRegex() {
        return "^xo:\\d+:[0-2]:[0-2]:[EOX]$";
    }

    @Bean
    @Qualifier("xoCallbackHandler")
    public Handler xoCallbackHandler(XOSessionCash sessionCash) {
        return new XOCallbackHandler(sessionCash);
    }
}
