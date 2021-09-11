package ru.hujna.processor.matcher;

import org.telegram.telegrambots.meta.api.objects.Update;

public interface Matcher {

    boolean match(Update update);

}
