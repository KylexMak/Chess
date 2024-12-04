package serializers;

import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;
import com.google.gson.*;

import java.lang.reflect.Type;

public class ChessMoveSerializer implements JsonSerializer<ChessMove>, JsonDeserializer<ChessMove> {
    @Override
    public ChessMove deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject object = json.getAsJsonObject();
        ChessPosition start = context.deserialize(object.get("start"), ChessPosition.class);
        ChessPosition end = context.deserialize(object.get("end"), ChessPosition.class);

        JsonElement promotionPieceElement = object.get("promotionPiece");
        ChessPiece.PieceType promoPiece = null;
        if(promotionPieceElement != null && !promotionPieceElement.isJsonNull()){
            promoPiece = ChessPiece.PieceType.valueOf(promotionPieceElement.getAsString());
        }

        return new ChessMove(start, end, promoPiece);
    }

    @Override
    public JsonElement serialize(ChessMove src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject result = new JsonObject();
        result.add("start", context.serialize(src.getStartPosition(), ChessPosition.class));
        result.add("end", context.serialize(src.getEndPosition(), ChessPosition.class));

        ChessPiece.PieceType promoPiece = src.getPromotionPiece();
        if(promoPiece != null){
            result.addProperty("promotionPiece", promoPiece.name());
        }
        else{
            result.addProperty("promotionPiece", (String) null);
        }
        return result;
    }
}
