package ru.hujna.feature.xkcd;

public record XkcdComic(
        Integer num,
        String year,
        String month,
        String day,
        String link,
        String news,
        String title,
        String safe_title,
        String alt,
        String transcript,
        String img
) {}
