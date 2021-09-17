package ru.hujna.feature.xo;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.hujna.bot.BotConfig;
import ru.hujna.feature.xo.handler.XOCallbackHandler;
import ru.hujna.feature.xo.handler.XOJoinHandler;
import ru.hujna.feature.xo.handler.XOStartHandler;
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
    public Matcher xoMsgMatcher(@Qualifier("xoMsgRegex") String regex) {
        return new MessageRegexMatcher(regex);
    }

    @Bean
    String xoMsgRegex(BotConfig botConfig) {
        return "^\\/xo(" + botConfig.getBotUsername() + ")?$";
    }

    @Bean
    public Handler xoMsgHandler(XOSessionCash sessionCash) {
        return new XOStartHandler(sessionCash);
    }

    @Bean
    public Processor xoCallbackProcessor(@Qualifier("xoCallbackMatcher") Matcher matcher,
                                         @Qualifier("xoCallbackHandler") Handler handler) {
        return new Processor(matcher, handler);
    }

    @Bean
    public Matcher xoCallbackMatcher(@Qualifier("xoCallbackRegex") String regex) {
        return new CallbackRegexMatcher(regex);
    }

    @Bean
    String xoCallbackRegex() {
        return "^xoMove:\\d+:[0-2]:[0-2]:[EOX]$";
    }

    @Bean
    public Handler xoCallbackHandler(XOSessionCash sessionCash) {
        return new XOCallbackHandler(sessionCash);
    }

    @Bean
    public Processor xoJoinProcessor(@Qualifier("xoJoinMatcher") Matcher matcher,
                                     @Qualifier("xoJoinHandler") Handler handler) {
        return new Processor(matcher, handler);
    }

    @Bean
    public Matcher xoJoinMatcher(@Qualifier("xoJoinRegex") String regex) {
        return new CallbackRegexMatcher(regex);
    }

    @Bean
    String xoJoinRegex() {
        return "^xoJoin:\\d+$";
    }

    @Bean
    public Handler xoJoinHandler(XOSessionCash sessionCash) {
        return new XOJoinHandler(sessionCash);
    }


}
