package ru.hujna.feature.xo;

import org.junit.jupiter.api.Test;
import ru.hujna.feature.xo.model.XO;
import ru.hujna.feature.xo.model.XOState;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class XOUtilCalcStateTest {

    @Test
    void tie() {
        var field = new XO[][]{
                {XO.X, XO.O, XO.X},
                {XO.O, XO.O, XO.X},
                {XO.X, XO.X, XO.O}
        };
        var state = XOUtil.calcState(field);
        assertEquals(XOState.FINISHED_TIE, state);
    }

    @Test
    void horizontalWinX() {
        var field = new XO[][]{
                {XO.X, XO.X, XO.X},
                {XO.O, XO.O, XO.X},
                {XO.X, XO.O, XO.O}
        };
        var state = XOUtil.calcState(field);
        assertEquals(XOState.FINISHED_WIN, state);
    }

    @Test
    void horizontalWinO() {
        var field = new XO[][]{
                {XO.X, XO.O, XO.X},
                {XO.O, XO.O, XO.O},
                {XO.X, XO.X, XO.O}
        };
        var state = XOUtil.calcState(field);
        assertEquals(XOState.FINISHED_WIN, state);
    }

    @Test
    void verticalWinX() {
        var field = new XO[][]{
                {XO.X, XO.O, XO.X},
                {XO.O, XO.O, XO.X},
                {XO.O, XO.X, XO.X}
        };
        var state = XOUtil.calcState(field);
        assertEquals(XOState.FINISHED_WIN, state);
    }

    @Test
    void verticalWinO() {
        var field = new XO[][]{
                {XO.X, XO.O, XO.X},
                {XO.O, XO.O, XO.X},
                {XO.X, XO.O, XO.O}
        };
        var state = XOUtil.calcState(field);
        assertEquals(XOState.FINISHED_WIN, state);
    }

    @Test
    void diagonal1WinX() {
        var field = new XO[][]{
                {XO.X, XO.O, XO.X},
                {XO.O, XO.X, XO.O},
                {XO.O, XO.X, XO.X}
        };
        var state = XOUtil.calcState(field);
        assertEquals(XOState.FINISHED_WIN, state);
    }

    @Test
    void diagonal1WinO() {
        var field = new XO[][]{
                {XO.O, XO.X, XO.X},
                {XO.X, XO.O, XO.O},
                {XO.O, XO.X, XO.O}
        };
        var state = XOUtil.calcState(field);
        assertEquals(XOState.FINISHED_WIN, state);
    }

    @Test
    void diagonal2WinX() {
        var field = new XO[][]{
                {XO.X, XO.O, XO.X},
                {XO.O, XO.X, XO.O},
                {XO.X, XO.X, XO.O}
        };
        var state = XOUtil.calcState(field);
        assertEquals(XOState.FINISHED_WIN, state);
    }

    @Test
    void diagonal2WinO() {
        var field = new XO[][]{
                {XO.X, XO.O, XO.O},
                {XO.O, XO.O, XO.X},
                {XO.O, XO.X, XO.O}
        };
        var state = XOUtil.calcState(field);
        assertEquals(XOState.FINISHED_WIN, state);
    }

    @Test
    void stillPlaying() {
        var field = new XO[][]{
                {XO.X, XO.O, XO.O},
                {XO.O, XO.E, XO.X},
                {XO.O, XO.X, XO.O}
        };
        var state = XOUtil.calcState(field);
        assertEquals(XOState.PLAYING, state);
    }
}
