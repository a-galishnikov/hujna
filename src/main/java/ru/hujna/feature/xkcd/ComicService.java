package ru.hujna.feature.xkcd;

public interface ComicService {
    Comic random();
    Comic latest();
    Comic get(Integer id);
}
