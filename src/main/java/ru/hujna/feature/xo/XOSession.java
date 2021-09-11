package ru.hujna.feature.xo;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

@Builder
@Getter
public class XOSession {

    @NonNull
    private final Long chatId;

    @NonNull
    private final XO lastXo;

    @NonNull
    private final XO[][] field;

}
