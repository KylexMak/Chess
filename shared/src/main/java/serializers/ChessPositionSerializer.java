package serializers;

import chess.ChessPosition;
import com.google.gson.*;

import java.lang.reflect.Type;

public class ChessPositionSerializer implements JsonSerializer<ChessPosition>, JsonDeserializer<ChessPosition> {
    @Override
    public ChessPosition deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject object = json.getAsJsonObject();
        int row = object.get("row").getAsInt();
        int col = object.get("col").getAsInt();
        return new ChessPosition(row, col);
    }

    @Override
    public JsonElement serialize(ChessPosition src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject result = new JsonObject();
        result.addProperty("row", src.getRow());
        result.addProperty("col", src.getColumn());
        return result;
    }
}
