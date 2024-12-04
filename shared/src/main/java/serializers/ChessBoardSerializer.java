package serializers;

import chess.ChessBoard;
import chess.ChessPiece;
import chess.ChessPosition;
import com.google.gson.*;

import java.lang.reflect.Type;

public class ChessBoardSerializer implements JsonSerializer<ChessBoard>, JsonDeserializer<ChessBoard> {
    @Override
    public ChessBoard deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject object = json.getAsJsonObject();

        ChessBoard board = new ChessBoard();
        JsonArray boardJson = object.getAsJsonArray("board");
        for (int i = 0; i < 8; i++){
            JsonArray rowArray = boardJson.get(i).getAsJsonArray();
            for(int j = 0; j < 8; j++){
                JsonElement pieceJson = rowArray.get(j);
                ChessPiece piece = context.deserialize(pieceJson, ChessPiece.class);
                board.addPieceByIndex(i , j, piece);
            }
        }
        return board;
    }

    @Override
    public JsonElement serialize(ChessBoard src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject result = new JsonObject();
        JsonArray board = new JsonArray();
        for(ChessPiece[] row : src.getBoard()){
            JsonArray rowArray = new JsonArray();
            for(ChessPiece piece : row){
                JsonElement pieceJson = context.serialize(piece, ChessPiece.class);
                rowArray.add(pieceJson);
            }
            board.add(rowArray);
        }
        result.add("board", board);

        return result;
    }
}
