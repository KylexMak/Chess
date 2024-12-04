package ui;

import chess.*;
import model.AuthData;
import model.GameData;
import websocket.NotificationHandler;
import websocket.WebsocketClient;

import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.Scanner;
import java.util.stream.Collectors;

import static ui.BoardDrawer.*;
import static ui.EscapeSequences.SET_TEXT_COLOR_BLUE;

public class Gameplay {
    public static WebsocketClient ws;
    public static NotificationHandler handler = new NotificationHandler();
    private static ChessBoard gameBoard;
    private static GameData gameData;

    public static void gameplayCommands(int port, AuthData auth, GameData game) throws Exception {
        gameData = game;
        String url = "http://localhost:" + port;
        ws = new WebsocketClient(url, auth, game.gameID(), handler);
        Scanner input = new Scanner(System.in);
        String command = input.nextLine();
        String[] decodeCommand = new String[0];
        if(command.isEmpty()){
            System.out.println("Please enter a command or press 6 for help");
            gameplayCommands(port, auth, game);
        }
        else{
            decodeCommand = command.split(" ");
        }
        evalCommand(decodeCommand, port, auth, game);
    }

    public static void redraw(ChessBoard gameNew, AuthData auth){
        gameBoard = gameNew;
        String color = Objects.equals(auth.username(), gameData.whiteUsername()) ?
                "WHITE" : "BLACK";
        String board = BoardDrawer.printBoard(gameBoard, color);
        System.out.println(board);
    }

    private static void highlight(GameData game, AuthData auth, String move) throws Exception {
        ChessBoard board = gameBoard;
        String color = Objects.equals(auth.username(), game.whiteUsername()) ?
                "WHITE" : "BLACK";
        if(move.length() != 2){
            throw new Exception("Error: Move syntax incorrect");
        }

        Integer col = INTERPRET_COL.get(move.charAt(0));
        Integer row = INTERPRET_ROW.get(move.charAt(1));

        if(row == null || col == null){
            throw new Exception("Error: The row or column input is not recognized.");
        }

        ChessPosition position = new ChessPosition(row, col);
        ChessPiece piece = board.getPiece(position);

        if(piece.getTeamColor() != game.game().getTeamTurn()){
            throw new Exception("Error: It is not your turn so you cannot highlight moves.");
        }

        Collection<ChessMove> validMoves = game.game().validMoves(position);

        HashSet<ChessPosition> moves = validMoves.stream()
                .map(ChessMove::getEndPosition)
                .collect(Collectors.toCollection(HashSet::new));
        moves.add(position);

        String highlightedBoard = printHighlightedBoard(board, color, moves);
        System.out.println(highlightedBoard);
    }

    private static void makeMove(AuthData auth, GameData game, String start, String end, String promo) throws Exception {
        Integer startCol = INTERPRET_COL.get(start.charAt(0));
        Integer startRow = INTERPRET_ROW.get(start.charAt(1));
        Integer endCol = INTERPRET_COL.get(end.charAt(0));
        Integer endRow = INTERPRET_ROW.get(end.charAt(1));
        ChessPiece.PieceType promotion = null;

        if(promo != null){
            promotion = INTERPRET_PIECE.get(promo.charAt(0));
        }

        if(startCol == null || startRow == null ||
                endCol == null || endRow == null){
            throw new Exception("Error: Syntax is not correct.");
        }

        ChessPosition startPosition = new ChessPosition(startRow, startCol);
        ChessPosition endPosition = new ChessPosition(endRow, endCol);
        ChessBoard board = game.game().getBoard();
        ChessPiece piece = board.getPiece(startPosition);

        ChessMove move = new ChessMove(startPosition, endPosition, promotion);
        ws.makeMove(auth, game.gameID(), move);
    }

    private static void gameHelp(){
        System.out.println(SET_TEXT_COLOR_BLUE + "Options: \n" +
                "\t1.Redraw board: \"1\" \n" +
                "\t2.Move: \"2\", <START POSITION> <END POSITION> <PROMOPIECE(only if applicable)> \n" +
                "\t3.Highlight possible moves: \"3\" <POSITION>\n" +
                "\t4.Resign: \"4\" \n" +
                "\t5.Leave: \"5\" \n" +
                "\t6.Print this message: \"6\" \n");
    }

    private static void evalCommand(String[] decodeCommand, int port, AuthData auth, GameData gameInfo) throws Exception {
        if(decodeCommand.length == 1){
            switch(decodeCommand[0]){
                case "1" -> {
                    redraw(gameBoard, auth);
                    gameplayCommands(port, auth, gameInfo);
                }
                case "4" -> {
                    ws.resignGame(auth, gameInfo.gameID());
                    gameplayCommands(port, auth, gameInfo);
                }
                case "5"-> {
                    ws.leaveGame(auth, gameInfo.gameID());
                    gameplayCommands(port, auth, gameInfo);
                }
                case "6" -> {
                    gameHelp();
                    gameplayCommands(port, auth, gameInfo);
                }
                default -> {
                    System.out.println("Command not recognized. You may have added too many parameters. Please type 6 for help. \n");
                    gameplayCommands(port, auth, gameInfo);
                }
            }
        }
        else if (decodeCommand.length == 2){
            if(Objects.equals(decodeCommand[0], "3")){
                try{
                    highlight(gameInfo, auth, decodeCommand[1]);
                    gameplayCommands(port, auth, gameInfo);
                }
                catch(Exception e){
                    System.out.println(e.getMessage() + " Please type 6 for help. \n");
                    gameplayCommands(port, auth, gameInfo);
                }
            }
        }
        else if (decodeCommand.length == 3){
            try{
                makeMove(auth, gameInfo, decodeCommand[1], decodeCommand[2], null);
                gameplayCommands(port, auth, gameInfo);
            }
            catch (Exception e) {
                System.out.println(e.getMessage() + " Please type 6 for help. \n");
                gameplayCommands(port, auth, gameInfo);
            }
        }
        else if (decodeCommand.length == 4){
            try{
                makeMove(auth, gameInfo, decodeCommand[1], decodeCommand[2], decodeCommand[3]);
                gameplayCommands(port, auth, gameInfo);
            }
            catch (Exception e) {
                System.out.println(e.getMessage() + " Please type 6 for help. \n");
                gameplayCommands(port, auth, gameInfo);
            }
        }
        else{
            System.out.println("Command not recognized. You may have added too many parameters. Please type 6 for help. \n");
            gameplayCommands(port, auth, gameInfo);
        }
    }
}
