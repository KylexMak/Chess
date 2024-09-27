package chess;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard {

    private ChessPiece[][] board;
    public ChessBoard() {
        board = new ChessPiece[8][8];
        //resetBoard();
    }

    /**
     * Adds a chess piece to the chessboard
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
    public void addPiece(ChessPosition position, ChessPiece piece) {
        board[position.getRow()][position.getColumn()] = piece;
    }

    /**
     * Gets a chess piece on the chessboard
     *
     * @param position The position to get the piece from
     * @return Either the piece at the position, or null if no piece is at that
     * position
     */

    public ChessPiece getPiece(ChessPosition position) {
        return board[position.getRow()][position.getColumn()];
    }

    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    public void resetBoard() {
        board = new ChessPiece[8][8];
        GeneratePieces();
    }

    private void GeneratePieces(){
        List<ChessPiece> blackBackRow = new ArrayList<>();
        blackBackRow.add(new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.ROOK));
        blackBackRow.add(new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KNIGHT));
        blackBackRow.add(new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.BISHOP));
        blackBackRow.add(new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.QUEEN));
        blackBackRow.add(new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KING));
        blackBackRow.add(new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.BISHOP));
        blackBackRow.add(new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KNIGHT));
        blackBackRow.add(new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.ROOK));

        List<ChessPiece> blackFrontRow = new ArrayList<>();
        for(int i = 0; i < board[0].length; i++){
            blackFrontRow.add(new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN));
        }
        PopulateRow(blackBackRow, 8);
        PopulateRow(blackFrontRow, 7);

        List<ChessPiece> whiteBackRow = ColorChangeToWhite(blackBackRow);
        List<ChessPiece> whiteFrontRow = ColorChangeToWhite(blackFrontRow);

        PopulateRow(whiteFrontRow, 2);
        PopulateRow(whiteBackRow, 1);
    }

    private void PopulateRow(List<ChessPiece> pieces, int row){
        for(int i = 1; i <= board[row - 1].length; i++){
            addPiece(new ChessPosition(row, i), pieces.get(i - 1));
        }
    }

    private List<ChessPiece> ColorChangeToWhite(List<ChessPiece> row){
        List<ChessPiece> convertedRow = new ArrayList<>();
        for(ChessPiece piece : row){
            convertedRow.add(new ChessPiece(ChessGame.TeamColor.WHITE, piece.getPieceType()));
        }
        return convertedRow;
    }

    public ChessBoard copy(){
        ChessBoard copy = new ChessBoard();
        for(int i = 1; i <= 8; i++){
            for(int j = 1; j <= 8; j++){
                ChessPosition position = new ChessPosition(i , j);
                ChessPiece piece = this.getPiece(position);
                if(piece != null){
                    ChessPiece copyPiece = new ChessPiece(piece.getTeamColor(), piece.getPieceType());
                    copy.board[i - 1][j - 1] = copyPiece;
                }
                else{
                    copy.board[i - 1][j - 1] = null;
                }
            }
        }
        return copy;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessBoard that = (ChessBoard) o;
        return Objects.deepEquals(board, that.board);
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(board);
    }
}
