package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public record ListGames(List<GameData> games) {
    private List<String> convert(List<GameData> games){
        List<String> gamesAsString = new ArrayList<>();
        for(GameData game : games){
            gamesAsString.add("Game ID: ");
            gamesAsString.add(game.gameID() + " | ");
            gamesAsString.add("Game Name: ");
            gamesAsString.add(game.gameName() + " | ");
            gamesAsString.add("White Username: ");
            gamesAsString.add(game.whiteUsername() + " | ");
            gamesAsString.add("Black Username: ");
            gamesAsString.add(game.blackUsername() + "\n");
        }
        return gamesAsString;
    }

    @Override
    public String toString() {
        List<String> gamesList = convert(games);
        String flattened = "";
        for(String gameData : gamesList){
            flattened = flattened + gameData;
        }
        return flattened;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ListGames listGames = (ListGames) o;
        return Objects.equals(games, listGames.games);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(games);
    }
}
