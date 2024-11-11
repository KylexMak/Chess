import ui.Prelogin;

import java.io.IOException;

import static ui.EscapeSequences.BLACK_QUEEN;

public class ClientMain {
    public static void main(String[] args) throws IOException {
//        var piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
//        System.out.println("♕ 240 Chess Client: " + piece);
        System.out.println(BLACK_QUEEN + " Welcome to Chess. Login to start." + BLACK_QUEEN);
        System.out.println("""
                Options:\s
                \t1.Login as existing user: "1", <USERNAME> <PASSWORD>\s
                \t2.Register a new user: "2", <USERNAME> <PASSWORD> <EMAIL>\s
                \t3.Exit the program: "3"\s
                \t4.Print this message: "4"\s
                """);
        Prelogin.preLoginCommands(226);
    }
}