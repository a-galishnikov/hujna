package ru.hujna.feature.xo.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TwoPlayersTest {

    @Test
    void constructorShouldValidateEmptyCase() {
        assertThrows(IllegalArgumentException.class, TwoPlayers::of);
    }

    @Test
    void constructorShouldValidateMoreThanTwo() {
        assertThrows(IllegalArgumentException.class, () -> TwoPlayers.of(
                new Player(1, XO.X, true),
                new Player(2, XO.O, false),
                new Player(3, XO.X, false)
        ));
    }

    @Test
    void constructorShouldValidateSameXO() {
        assertThrows(IllegalArgumentException.class, () -> TwoPlayers.of(
                new Player(1, XO.O, true),
                new Player(2, XO.O, false)
        ));
    }

    @Test
    void simpleConstructorWorksWith1Arg() {
        var player = new Player(1, XO.O, true);
        var players = TwoPlayers.of(player);
        assertNotNull(players);
        assertEquals(player, players.getO());
        assertEquals(1, players.size());
    }

    @Test
    void simpleConstructorWorksWith2Args() {
        var playerO = new Player(1, XO.O, true);
        var playerX = new Player(2, XO.X, false);
        var players = TwoPlayers.of(playerO, playerX);
        assertNotNull(players);
        assertEquals(playerO, players.getO());
        assertEquals(playerX, players.getX());
        assertEquals(2, players.size());
    }

    @Test
    void starterConstructorWorks() {
        var players = TwoPlayers.of(1, XO.X);
        var player = players.getX();
        assertEquals(1, players.size());
        assertEquals(1, player.getUserId());
        assertEquals(XO.X, player.getXo());
        assertEquals(true, player.isGameStarter());
    }

    @Test
    void opponentConstructorWorks() {
        var starterPlayers = TwoPlayers.of(1, XO.X);
        var opponentPlayers = TwoPlayers.of(starterPlayers, 2);
        assertEquals(2, opponentPlayers.size());

        var playerX = opponentPlayers.getX();
        assertEquals(1, playerX.getUserId());
        assertEquals(XO.X, playerX.getXo());
        assertEquals(true, playerX.isGameStarter());

        var playerO = opponentPlayers.getO();
        assertEquals(2, playerO.getUserId());
        assertEquals(XO.O, playerO.getXo());
        assertEquals(false, playerO.isGameStarter());
    }

    @Test
    void singleShouldFailWhenNotSingle() {
        var playerO = new Player(1, XO.O, true);
        var playerX = new Player(2, XO.X, false);
        var players = TwoPlayers.of(playerO, playerX);
        assertThrows(IllegalStateException.class, players::single);
    }

    @Test
    void singleShouldWork() {
        var players = TwoPlayers.of(1, XO.O);
        var single = players.single();
        assertNotNull(single);
        assertEquals(players.getO(), single);
    }
}
