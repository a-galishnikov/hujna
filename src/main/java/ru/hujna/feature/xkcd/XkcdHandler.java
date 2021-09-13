package ru.hujna.feature.xkcd;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.hujna.processor.handler.Handler;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class XkcdHandler implements Handler {

    @NonNull
    private final XkcdClient client;

    @Override
    public List<? extends PartialBotApiMethod<? extends Serializable>> handle(Update update) {

        XkcdComic comic = client.random();

        return Optional.ofNullable(update)
                .map(x -> Collections.singletonList(
                        SendPhoto.builder()
                                .chatId(x.getMessage().getChatId().toString())
                                .caption(comic.getAlt())
                                .photo(new InputFile(comic.getImg()))
                                .build())).orElse(Collections.emptyList());
    }


}
