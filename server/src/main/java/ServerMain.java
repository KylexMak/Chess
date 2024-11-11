import chess.*;
import dataaccess.DataAccessException;
import exception.ResponseException;
import server.Server;

public class ServerMain {
    public static void main(String[] args) throws ResponseException, DataAccessException {
        var piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        System.out.println("♕ 240 Chess Server: " + piece);
        Server server = new Server();
        server.run(226);
    }
}