package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;
import java.util.List;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {

    ChessGame.TeamColor pieceColor;
    ChessPiece.PieceType type;
    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.pieceColor = pieceColor;
        this.type = type;
    }


    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        return pieceColor;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return type;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        ChessPiece piece = board.getPiece(myPosition);
        List<ChessMove> moves = new ArrayList<>();
        MoveCalculator calculateMoves = new MoveCalculator(board, myPosition);
        switch(piece.getPieceType()){
            case BISHOP ->
                moves.addAll(calculateMoves.bishopMoves(myPosition, piece.getTeamColor()));
            case KING ->
                moves.addAll(calculateMoves.kingMoves(myPosition,piece.getTeamColor()));
            case KNIGHT ->
                moves.addAll(calculateMoves.knightMoves(myPosition, piece.getTeamColor()));
            case PAWN ->
                moves.addAll(calculateMoves.pawnMove(myPosition, piece.getTeamColor()));
            case ROOK ->
                moves.addAll(calculateMoves.rookMoves(myPosition, piece.getTeamColor()));
            case QUEEN ->
                moves.addAll(calculateMoves.queenMoves(myPosition, piece.getTeamColor()));
        }return moves;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o){
            return true;
        }
        if (o == null || getClass() != o.getClass()){
            return false;
        }
        ChessPiece that = (ChessPiece) o;
        return pieceColor == that.pieceColor && type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(pieceColor, type);
    }
}
