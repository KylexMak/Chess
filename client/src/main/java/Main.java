import chess.*;
import ui.Prelogin;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
//        var piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
//        System.out.println("♕ 240 Chess Client: " + piece);
        Prelogin.preLoginCommands(8080);
    }
}