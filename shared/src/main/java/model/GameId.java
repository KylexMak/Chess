package model;

import java.util.Objects;

public record GameId(Integer gameID) {
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GameId gameId1 = (GameId) o;
        return Objects.equals(gameID, gameId1.gameID);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(gameID);
    }
}
