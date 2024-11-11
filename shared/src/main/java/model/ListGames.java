package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public record ListGames(List<GameData> games) {
    private List<String> convert(List<GameData> games){
        List<String> gamesAsString = new ArrayList<>();
        for(int i = 1; i <= games.size(); i++){
            gamesAsString.add("Game ID: ");
            gamesAsString.add(i + " | ");
            gamesAsString.add("Game Name: ");
            gamesAsString.add(games.get(i - 1).gameName() + " | ");
            gamesAsString.add("White Username: ");
            gamesAsString.add(games.get(i - 1).whiteUsername() + " | ");
            gamesAsString.add("Black Username: ");
            gamesAsString.add(games.get(i - 1).blackUsername() + "\n");
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

}
