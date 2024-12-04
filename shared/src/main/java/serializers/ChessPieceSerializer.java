package serializers;

import chess.ChessGame;
import chess.ChessPiece;
import com.google.gson.*;

import java.lang.reflect.Type;

public class ChessPieceSerializer implements JsonSerializer<ChessPiece>, JsonDeserializer<ChessPiece> {
    @Override
    public ChessPiece deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject object = json.getAsJsonObject();
        String pieceColor = object.get("pieceColor").getAsString();
        String type = object.get("type").getAsString();
        ChessGame.TeamColor pieceColour = ChessGame.TeamColor.valueOf(pieceColor);
        ChessPiece.PieceType pieceType = ChessPiece.PieceType.valueOf(type);
        return new ChessPiece(pieceColour, pieceType);
    }

    @Override
    public JsonElement serialize(ChessPiece src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject result = new JsonObject();
        result.addProperty("pieceColor", src.getTeamColor().toString());
        result.addProperty("type", src.getPieceType().toString());
        return result;
    }
}
