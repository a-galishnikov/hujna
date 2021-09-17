package ru.hujna.feature.xo.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class XOPlayerTest {

    @Test
    void constructorShouldNotTakeE() {
        assertThrows(IllegalArgumentException.class, () -> new XOPlayer(1, XO.E, true));
    }

    @Test
    void constructorShouldNotTakeNullXO() {
        assertThrows(IllegalArgumentException.class, () -> new XOPlayer(1, null, true));
    }

    @Test
    void constructorShouldWork() {
        var player = new XOPlayer(1, XO.X, true);
        assertEquals(1, player.getUserId());
        assertEquals(XO.X, player.getXo());
        assertEquals(true, player.isGameStarter());
    }
}
