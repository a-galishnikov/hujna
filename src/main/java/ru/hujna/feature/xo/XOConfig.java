package ru.hujna.feature.xo;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.hujna.bot.BotConfig;
import ru.hujna.feature.xo.handler.XOHandler;
import ru.hujna.feature.xo.handler.XOJoinHandler;
import ru.hujna.feature.xo.handler.XOMoveHandler;
import ru.hujna.feature.xo.model.Join;
import ru.hujna.feature.xo.model.Move;
import ru.hujna.feature.xo.parse.Parser;
import ru.hujna.feature.xo.ui.FieldKeyboard;
import ru.hujna.feature.xo.ui.JoinKeyboard;
import ru.hujna.feature.xo.ui.Keyboard;
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
    public Handler xoMsgHandler(GameCache gameCache, @Qualifier("joinKeyboard") Keyboard keyboard) {
        return new XOHandler(gameCache, keyboard);
    }

    public @Bean Keyboard joinKeyboard() {
        return new JoinKeyboard();
    }

    @Bean
    public Processor xoMoveProcessor(@Qualifier("xoMoveMatcher") Matcher matcher,
                                     @Qualifier("xoMoveHandler") Handler handler) {
        return new Processor(matcher, handler);
    }

    @Bean
    public Matcher xoMoveMatcher(@Qualifier("xoMoveRegex") String regex) {
        return new CallbackRegexMatcher(regex);
    }

    @Bean
    String xoMoveRegex() {
        return "^xoMove:\\d+:[0-2]:[0-2]:[EOX]$";
    }

    @Bean
    public Handler xoMoveHandler(GameCache gameCache, Parser<Move> parser, @Qualifier("fieldKeyboard") Keyboard keyboard) {
        return new XOMoveHandler(gameCache, parser, keyboard);
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
    public Handler xoJoinHandler(GameCache gameCache, Parser<Join> parser, @Qualifier("fieldKeyboard") Keyboard keyboard) {
        return new XOJoinHandler(gameCache, parser, keyboard);
    }

    @Bean
    public Keyboard fieldKeyboard() {
        return new FieldKeyboard();
    }

}
