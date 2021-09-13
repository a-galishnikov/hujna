package ru.hujna.feature.xkcd;

public interface XkcdClient {
    XkcdComic random();
    XkcdComic latest();
    XkcdComic get(Integer id);
}
