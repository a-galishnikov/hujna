package ru.hujna.feature.xo.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PlayerTest {

    @Test
    @DisplayName("Конструктор не должен принимать пустую клетку как ход")
    void constructorShouldNotTakeE() {
        assertThrows(IllegalArgumentException.class, () -> new Player(1, XO.E, true));
    }

    @Test
    @DisplayName("Конструктор падает при пустых параметрах")
    void constructorShouldNotTakeNullXO() {
        assertThrows(IllegalArgumentException.class, () -> new Player(1, null, true));
    }

    @Test
    @DisplayName("Конструктор должен отработать, когда всё есть")
    void constructorShouldWork() {
        var player = new Player(1, XO.X, true);
        assertEquals(1, player.getUserId());
        assertEquals(XO.X, player.getXo());
        assertTrue(player.isGameStarter());
    }
}
