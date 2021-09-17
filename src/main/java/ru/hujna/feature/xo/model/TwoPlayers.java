package ru.hujna.feature.xo.model;

import lombok.ToString;

import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@ToString
public class TwoPlayers {
    private final Map<XO, Player> players;

    public static TwoPlayers of(TwoPlayers current, long opponentId) {
        var starter = current.single();
        var xo = starter.getXo().reverse();
        return TwoPlayers.of(starter, new Player(opponentId, xo, false));
    }

    public static TwoPlayers of(long starterId, XO xo) {
        return TwoPlayers.of(new Player(starterId, xo, true));
    }

    public static TwoPlayers of(Player... players) {
        return new TwoPlayers(players);
    }

    private TwoPlayers(Player... players) {
        validate(players);
        this.players = Arrays.stream(players).collect(Collectors.toUnmodifiableMap(Player::getXo, p -> p));
    }

    private void validate(Player[] players) {
        Objects.requireNonNull(players);
        if (players.length == 0 || players.length > 2) {
            throw new IllegalArgumentException("Expecting one or two players, found: " + players.length);
        }
        if (players.length == 2 && players[0].getXo() == players[1].getXo()) {
            throw new IllegalArgumentException("Players should have different xo assigned");
        }
    }

    public int size() {
        return players.size();
    }

    public Player get(XO xo) {
        return players.get(xo);
    }

    public Player getX() {
        return get(XO.X);
    }

    public Player getO() {
        return get(XO.O);
    }

    public Player single() {
        if (players.size() != 1) {
            throw new IllegalStateException("Expected one player, found: " + players.size());
        }
        var xPlayer = getX();
        return xPlayer == null ? getO() : xPlayer;
    }

    public Player starter() {
        return getX() != null && getX().isGameStarter() ? getX() : getO();
    }
}
