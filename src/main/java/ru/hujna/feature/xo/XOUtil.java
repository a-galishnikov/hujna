package ru.hujna.feature.xo;

import org.telegram.telegrambots.meta.api.objects.User;
import ru.hujna.feature.xo.model.Move;
import ru.hujna.feature.xo.model.State;
import ru.hujna.feature.xo.model.XO;

import java.util.Arrays;

public class XOUtil {

    public static String name(User user) {
        return user.getUserName() == null || user.getUserName().isBlank()
                ? String.format("%s %s", user.getFirstName(), user.getLastName())
                : user.getUserName();
    }
}
