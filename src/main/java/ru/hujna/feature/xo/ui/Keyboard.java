package ru.hujna.feature.xo.ui;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import ru.hujna.feature.xo.model.Game;

public interface Keyboard {
    InlineKeyboardMarkup markup(Game game);
}
