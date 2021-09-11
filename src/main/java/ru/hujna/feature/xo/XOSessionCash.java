package ru.hujna.feature.xo;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class XOSessionCash {
    private final Map<Long, XOSession> cash = new ConcurrentHashMap<>();

    public XOSession get(Long chatId) {
        return cash.computeIfAbsent(chatId, x -> XOUtil.initSession(chatId));
    }

    public void put(XOSession session) {
        cash.put(session.getChatId(), session);
    }

}
