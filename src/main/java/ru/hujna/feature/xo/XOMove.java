package ru.hujna.feature.xo;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class XOMove {
    private final int messageId;
    private final int x;
    private final int y;
    private final XO xo;
}
