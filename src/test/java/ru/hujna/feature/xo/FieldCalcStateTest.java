package ru.hujna.feature.xo;

import org.junit.jupiter.api.Test;
import ru.hujna.feature.xo.model.Field;
import ru.hujna.feature.xo.model.XO;
import ru.hujna.feature.xo.model.State;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.hujna.feature.xo.model.XO.*;

public class FieldCalcStateTest {

    @Test
    void tie() {
        var field = new Field(new XO[][]{
                {X, O, X},
                {O, O, X},
                {X, X, O}
        });
        var state = field.state();
        assertEquals(State.FINISHED_TIE, state);
    }

    @Test
    void horizontalWinX() {
        var field = new Field(new XO[][]{
                {X, X, X},
                {O, O, X},
                {X, O, O}
        });
        var state = field.state();
        assertEquals(State.FINISHED_WIN, state);
    }

    @Test
    void horizontalWinO() {
        var field = new Field(new XO[][]{
                {X, O, X},
                {O, O, O},
                {X, X, O}
        });
        var state = field.state();
        assertEquals(State.FINISHED_WIN, state);
    }

    @Test
    void verticalWinX() {
        var field = new Field(new XO[][]{
                {X, O, X},
                {O, O, X},
                {O, X, X}
        });
        var state = field.state();
        assertEquals(State.FINISHED_WIN, state);
    }

    @Test
    void verticalWinO() {
        var field = new Field(new XO[][]{
                {X, O, X},
                {O, O, X},
                {X, O, O}
        });
        var state = field.state();
        assertEquals(State.FINISHED_WIN, state);
    }

    @Test
    void diagonal1WinX() {
        var field = new Field(new XO[][]{
                {X, O, X},
                {O, X, O},
                {O, X, X}
        });
        var state = field.state();
        assertEquals(State.FINISHED_WIN, state);
    }

    @Test
    void diagonal1WinO() {
        var field = new Field(new XO[][]{
                {O, X, X},
                {X, O, O},
                {O, X, O}
        });
        var state = field.state();
        assertEquals(State.FINISHED_WIN, state);
    }

    @Test
    void diagonal2WinX() {
        var field = new Field(new XO[][]{
                {X, O, X},
                {O, X, O},
                {X, X, O}
        });
        var state = field.state();
        assertEquals(State.FINISHED_WIN, state);
    }

    @Test
    void diagonal2WinO() {
        var field = new Field(new XO[][]{
                {X, O, O},
                {O, O, X},
                {O, X, O}
        });
        var state = field.state();
        assertEquals(State.FINISHED_WIN, state);
    }

    @Test
    void stillPlaying() {
        var field = new Field(new XO[][]{
                {X, O, O},
                {O, E, X},
                {O, X, O}
        });
        var state = field.state();
        assertEquals(State.PLAYING, state);
    }
}
