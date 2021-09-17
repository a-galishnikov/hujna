package ru.hujna.feature.xo.parse;

import org.springframework.stereotype.Component;
import ru.hujna.feature.xo.model.Join;

@Component
public class JoinParser implements Parser<Join> {
    @Override
    public Join parse(String data) {
        String[] parts = data.split(":");
        return new Join(Integer.parseInt(parts[1]));
    }
}
