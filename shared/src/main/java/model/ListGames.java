package model;

import java.util.ArrayList;
import java.util.List;

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
            gamesAsString.add(game.blackUsername() + " | ");
        }
        return gamesAsString;
    }

    @Override
    public String toString() {
        List<String> gamesList = convert(games);
        String flattened = "";
        for(String gameData : gamesList){
            flattened += gameData;
        }
        return flattened;
    }
}
