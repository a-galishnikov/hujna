package ru.hujna.feature.xkcd;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class ComicServiceImpl implements ComicService {
    // see https://xkcd.com/json.html

    @NonNull
    private final WebClient xkcd;

    @NonNull
    private final WebClient cxkcd;

    @Override
    public Comic random() {
        // https://c.xkcd.com/random/comic/ redirects to e.g. https://xkcd.com/1784/
        return cxkcd.get()
                .uri("/random/comic/")
                .exchangeToMono(response -> Mono.just(response.headers()))
                .blockOptional()
                .map(x -> x.header("location"))
                .filter(x -> !x.isEmpty())
                .map(x -> x.get(0))
                .map(loc -> loc.split("/")[3])
                .map(Integer::parseInt)
                .map(this::get)
                .orElseThrow(() -> new AccessException("Unable to retrieve random comic from xkcd.com"));
    }

    @Override
    public Comic latest() {
        return xkcd.get()
                .uri("/info.0.json")
                .retrieve()
                .bodyToMono(Comic.class)
                .block();
    }

    @Override
    public Comic get(Integer id) {
        return xkcd.get()
                .uri(String.format("/%d/info.0.json", id))
                .retrieve()
                .bodyToMono(Comic.class)
                .block();
    }
}
