package serializers;

import chess.ChessBoard;
import chess.ChessGame;
import com.google.gson.*;

import java.lang.reflect.Type;

public class ChessGameSerializer implements JsonSerializer<ChessGame>, JsonDeserializer<ChessGame> {
    @Override
    public ChessGame deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject object = json.getAsJsonObject();
        String teamTurnString = object.get("currentTurn").getAsString();
        JsonElement boardJson = object.get("board");

        ChessGame.TeamColor teamColor = ChessGame.TeamColor.valueOf(teamTurnString);
        ChessBoard board = context.deserialize(boardJson, ChessBoard.class);

        ChessGame game = new ChessGame();
        game.setBoard(board);
        game.setTeamTurn(teamColor);

        return game;
    }

    @Override
    public JsonElement serialize(ChessGame src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject result = new JsonObject();
        result.addProperty("currentTurn", src.getTeamTurn().toString());
        result.add("board", context.serialize(src.getBoard(), ChessBoard.class));
        return result;
    }
}
