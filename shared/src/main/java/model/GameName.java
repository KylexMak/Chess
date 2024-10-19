package model;

import java.util.Objects;

public record GameName(String gameName) {
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GameName gameName1 = (GameName) o;
        return Objects.equals(gameName, gameName1.gameName);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(gameName);
    }
}
