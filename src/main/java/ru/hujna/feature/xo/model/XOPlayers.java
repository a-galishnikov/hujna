package ru.hujna.feature.xo.model;

import lombok.ToString;

import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@ToString
public class XOPlayers {
    private final Map<XO, XOPlayer> players;

    public static XOPlayers of(XOPlayers current, long opponentId) {
        var starter = current.single();
        var xo = starter.getXo().reverse();
        return XOPlayers.of(starter, new XOPlayer(opponentId, xo, false));
    }

    public static XOPlayers of(long starterId, XO xo) {
        return XOPlayers.of(new XOPlayer(starterId, xo, true));
    }

    public static XOPlayers of(XOPlayer... players) {
        return new XOPlayers(players);
    }

    private XOPlayers(XOPlayer... players) {
        validate(players);
        this.players = Arrays.stream(players).collect(Collectors.toUnmodifiableMap(XOPlayer::getXo, p -> p));
    }

    private void validate(XOPlayer[] players) {
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

    public XOPlayer get(XO xo) {
        return players.get(xo);
    }

    public XOPlayer getX() {
        return get(XO.X);
    }

    public XOPlayer getO() {
        return get(XO.O);
    }

    public XOPlayer single() {
        if (players.size() != 1) {
            throw new IllegalStateException("Expected one player, found: " + players.size());
        }
        var xPlayer = getX();
        return xPlayer == null ? getO() : xPlayer;
    }

    public XOPlayer starter() {
        return getX() != null && getX().isGameStarter() ? getX() : getO();
    }
}
