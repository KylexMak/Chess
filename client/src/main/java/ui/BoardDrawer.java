package ui;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;

import java.util.Objects;

import static ui.EscapeSequences.*;

public class BoardDrawer {
    public BoardDrawer(){}
    private static final String[] COLUMN_LABELS = {"a", "b", "c", "d", "e", "f", "g", "h"};
    private static final String[] ROW_LABELS = {"1", "2", "3", "4", "5", "6", "7", "8"};

    public static String printBoard(ChessBoard board, String color){
        boolean isWhite = color == null || color.equalsIgnoreCase("white");
        StringBuilder boardString = new StringBuilder(ERASE_SCREEN);
        printColLabels(boardString, isWhite);
        boardString.append("\n");

        for(int row = 0; row < ROW_LABELS.length; row ++){
            int rIndex = isWhite ? 7 - row : row;
            printRowLabels(boardString, ROW_LABELS[rIndex]);
            for(int col = 0; col < COLUMN_LABELS.length; col++){
                int colIndex = isWhite ? col : (7 - col);
                String squareColor = getSquareColor(row, colIndex, isWhite);
                String piece = getPiece(board.getPiece(new ChessPosition(rIndex + 1, colIndex + 1)), squareColor);
                boardString.append(squareColor).append(piece)
                        .append(RESET_BG_COLOR);
            }
            printRowLabels(boardString,ROW_LABELS[rIndex]);
            boardString.append("\n");
        }

        printColLabels(boardString, isWhite);
        boardString.append("\n");

        return boardString.toString();
    }

    private static void printColLabels(StringBuilder boardString, boolean isWhite){
        boardString.append(ROW_LABEL_PADDING).append(ROW_LABEL_SPACING).append(" ");
        for(int i = 0; i < COLUMN_LABELS.length; i++){
            int orientationColIndex = isWhite ? i : COLUMN_LABELS.length - i - 1;
            boardString.append(SET_TEXT_COLOR_WHITE)
                    .append(COLUMN_LABELS[orientationColIndex])
                    .append(ROW_LABEL_SPACING)
                    .append(RESET_TEXT_COLOR);
        }
        boardString.append(ROW_LABEL_SPACING);
    }

    private static void printRowLabels(StringBuilder board, String rowLabel){
        board.append(SET_TEXT_COLOR_WHITE)
                .append(" ")
                .append(rowLabel)
                .append(" ");
    }

    private static String getSquareColor(int row, int col, boolean isWhite){
        boolean isLightSquare = (row + col) % 2 == 0;
        if(!isWhite){
            isLightSquare = !isLightSquare;
        }
        return isLightSquare ? SET_BG_COLOR_LIGHT_BROWN : SET_BG_COLOR_DARK_BROWN;
    }

    private static String getPiece(ChessPiece piece, String color){
        if(piece == null){
            return EMPTY_PIECE;
        }
        String pieceChar = "";
        switch(piece.getPieceType()){
            case ROOK -> pieceChar = (piece.getTeamColor() == ChessGame.TeamColor.WHITE) ?
                    SET_TEXT_COLOR_WHITE + BLACK_ROOK : SET_TEXT_COLOR_BLACK + BLACK_ROOK;
            case KNIGHT -> pieceChar = (piece.getTeamColor() == ChessGame.TeamColor.WHITE) ?
                    SET_TEXT_COLOR_WHITE + BLACK_KNIGHT : SET_TEXT_COLOR_BLACK + BLACK_KNIGHT;
            case BISHOP -> pieceChar = (piece.getTeamColor() == ChessGame.TeamColor.WHITE) ?
                    SET_TEXT_COLOR_WHITE + BLACK_BISHOP : SET_TEXT_COLOR_BLACK + BLACK_BISHOP;
            case KING -> pieceChar = (piece.getTeamColor() == ChessGame.TeamColor.WHITE) ?
                    SET_TEXT_COLOR_WHITE + BLACK_KING : SET_TEXT_COLOR_BLACK + BLACK_KING;
            case QUEEN -> pieceChar = (piece.getTeamColor() == ChessGame.TeamColor.WHITE) ?
                    SET_TEXT_COLOR_WHITE + BLACK_QUEEN : SET_TEXT_COLOR_BLACK + BLACK_QUEEN;
            case PAWN -> pieceChar = (piece.getTeamColor() == ChessGame.TeamColor.WHITE) ?
                    SET_TEXT_COLOR_WHITE + BLACK_PAWN : SET_TEXT_COLOR_BLACK + BLACK_PAWN;
        }
        return pieceChar;
    }
}
