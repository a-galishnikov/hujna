package ru.hujna.feature.xo.parse;

public interface Parser<T> {
    T parse(String data);
}
