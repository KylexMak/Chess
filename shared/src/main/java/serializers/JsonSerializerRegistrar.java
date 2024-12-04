package serializers;

import chess.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class JsonSerializerRegistrar {
    private static final Gson chessGameGSON = new GsonBuilder()
            .registerTypeAdapter(ChessBoard.class, new ChessBoardSerializer())
            .registerTypeAdapter(ChessGame.class, new ChessGameSerializer())
            .registerTypeAdapter(ChessMove.class, new ChessMoveSerializer())
            .registerTypeAdapter(ChessPiece.class, new ChessPieceSerializer())
            .registerTypeAdapter(ChessPosition.class, new ChessPositionSerializer())
            .create();

    public static Gson getChessGameGSON(){
        return chessGameGSON;
    }
}
