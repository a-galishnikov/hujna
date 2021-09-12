package ru.hujna.feature.xo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import static ru.hujna.feature.xo.XOUtil.*;

@Component
@Slf4j
public class XOSessionCash {
    private final Map<String, XOSession> cash = new ConcurrentHashMap<>();

    public Optional<XOSession> get(Long chatId, Integer messageId) {
        var res = Optional.ofNullable(cash.get(sessionKey(chatId, messageId)));
        log.info("get {}:{} returning%n{}", chatId, messageId, res);
        return res;
    }

    public void put(XOSession session) {
        var prev = cash.put(sessionKey(session), session);
        log.info("put new {}\nprev {}", session, prev);
    }

    public void remove(XOSession session) {
        cash.remove(sessionKey(session));
        log.info("remove {}", session);
    }

}
