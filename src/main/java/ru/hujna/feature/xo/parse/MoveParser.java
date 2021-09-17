package ru.hujna.feature.xo.parse;

import org.springframework.stereotype.Component;
import ru.hujna.feature.xo.model.Move;
import ru.hujna.feature.xo.model.XO;

@Component
public class MoveParser implements Parser<Move> {
    @Override
    public Move parse(String data) {
        String[] parts = data.split(":");
        return new Move(Integer.parseInt(parts[1]), Integer.parseInt(parts[2]), Integer.parseInt(parts[3]), XO.valueOf(parts[4]));
    }
}
