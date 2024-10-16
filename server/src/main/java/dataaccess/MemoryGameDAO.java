package dataaccess;

import model.GameData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MemoryGameDAO implements GameDAO{
    public static HashMap<Integer, GameData> allGames = new HashMap<>();
    @Override
    public void createGame(GameData game) throws DataAccessException {
        int gameID = game.gameID();
        allGames.put(gameID, game);
    }

    @Override
    public GameData getGame(int gameID) throws DataAccessException {
        if(allGames.containsKey(gameID)){
            return allGames.get(gameID);
        }
        else{
            throw new DataAccessException("Error: Game does not exist");
        }
    }

    @Override
    public List<Integer> getAllGameIds(){
        List<Integer> gameIds = new ArrayList<>();
        for(Map.Entry<Integer, GameData> entry: allGames.entrySet()){
            gameIds.add(entry.getKey());
        }
        return gameIds;
    }

    @Override
    public List<GameData> listGames() throws DataAccessException {
        List<GameData> games = new ArrayList<>();
        for(Map.Entry<Integer, GameData> entry: allGames.entrySet()){
            games.add(entry.getValue());
        }
        return games;
    }

    @Override
    public void updateGame(int gameID, GameData game) throws DataAccessException {
        if(allGames.containsKey(gameID)){
            allGames.put(gameID, game);
        }
        else{
            throw new DataAccessException("Error: Game does not exist");
        }
    }

    @Override
    public void deleteGame(int gameID) throws DataAccessException {
        if(allGames.containsKey(gameID)){
            allGames.remove(gameID);
        }
        else{
            throw new DataAccessException("Game does not exist");
        }
    }

    @Override
    public void clearGames() throws DataAccessException {
        allGames.clear();
    }
}
