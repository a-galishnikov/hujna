package ru.hujna.feature.xo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.hujna.feature.xo.model.Game;
import ru.hujna.feature.xo.model.Key;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Slf4j
public class GameCache {
    private final Map<Key, Game> cash = new ConcurrentHashMap<>();

    public Optional<Game> get(Long chatId, Integer messageId) {
        var res = Optional.ofNullable(cash.get(Key.of(chatId, messageId)));
        log.info("get {}:{} returning {}", chatId, messageId, res);
        return res;
    }

    public void put(Game game) {
        var prev = cash.put(Key.of(game), game);
        log.info("put\nprev:{}\nnew:{}", prev, game);
    }

}
