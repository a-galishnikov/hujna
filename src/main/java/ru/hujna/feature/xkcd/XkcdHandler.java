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
                                .caption(buildCaption(comic))
                                .parseMode("HTML")
                                .photo(new InputFile(comic.img()))
                                .build())).orElse(Collections.emptyList());
    }

    private String buildCaption(XkcdComic comic) {
        var caption = new StringBuilder();

        if (comic.link() == null || comic.link().isBlank()) {
            caption.append(String.format("<b>%s</b>", comic.title()));
        } else {
            caption.append(String.format("<a href='%s'><b>%s</b></a>", comic.link(), comic.title()));
        }
        if (comic.alt() != null && !comic.alt().isBlank()) {
            caption.append(String.format("%n%s", comic.alt()));
        }

        return caption.toString();
    }


}
