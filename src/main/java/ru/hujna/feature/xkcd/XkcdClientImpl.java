package ru.hujna.feature.xkcd;

import lombok.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Optional;

@Component
public class XkcdClientImpl implements XkcdClient {
    // see https://xkcd.com/json.html

    @NonNull
    private final WebClient cxkcd = WebClient.create("https://c.xkcd.com");

    @NonNull
    private final WebClient xkcd = WebClient.create("https://xkcd.com");

    @Override
    public XkcdComic random() {
        // https://c.xkcd.com/random/comic/ redirects to e.g. https://xkcd.com/1784/
        return cxkcd.get()
                .uri("/random/comic/")
                .exchangeToMono(response -> Mono.just(
                        Optional.of(response.headers().header("location"))
                                .filter(x -> !x.isEmpty())
                                .map(x -> x.get(0))))
                .block()
                .map(loc -> loc.split("/")[3])
                .map(Integer::parseInt)
                .map(this::get)
                .orElse(null);
    }

    @Override
    public XkcdComic latest() {
        return xkcd.get()
                .uri("/info.0.json")
                .retrieve()
                .bodyToMono(XkcdComic.class)
                .block();
    }

    @Override
    public XkcdComic get(Integer id) {
        return xkcd.get()
                .uri(String.format("/%d/info.0.json", id))
                .retrieve()
                .bodyToMono(XkcdComic.class)
                .block();
    }
}