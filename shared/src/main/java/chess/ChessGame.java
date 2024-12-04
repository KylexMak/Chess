package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {
    TeamColor currentTurn;
    ChessBoard board;
    public ChessGame() {
        board = new ChessBoard();
        board.resetBoard();
        currentTurn = TeamColor.WHITE;
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return currentTurn;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        currentTurn = team;
    }


    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        ChessBoard activeGame = getBoard().copy();
        ChessPiece piece = activeGame.getPiece(startPosition);
        Collection<ChessMove> moves;
        List<ChessMove> validMoves = new ArrayList<>();
        if(piece == null){
            return null;
        }
        else {
            moves = piece.pieceMoves(activeGame, startPosition);
        }
        for(ChessMove move : moves){
            activeGame.addPiece(move.getStartPosition(), null);
            activeGame.addPiece(move.getEndPosition(), piece);
            if(!isInCheck(piece.getTeamColor())) {
                validMoves.add(move);
            }
           activeGame = board.copy();
        }
        return validMoves;
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        ChessPosition start = move.getStartPosition();
        ChessPosition end = move.getEndPosition();
        ChessPiece movingPiece = board.getPiece(start);
        if(movingPiece == null){
            throw new InvalidMoveException("There is no piece to be moved");
        }
        if(movingPiece.getTeamColor() != currentTurn){
            throw new InvalidMoveException("This piece cannot move on this turn");
        }
        ChessPiece.PieceType promotion = move.getPromotionPiece();

        if(end.getRow() > 7 || end.getRow() < 0 || end.getColumn() > 7 || end.getColumn() < 0){
            throw new InvalidMoveException("Out of Bounds");
        }
        if(!validMoves(start).contains(move)){
            throw new InvalidMoveException("Not a valid move");
        }
        else{
            board.addPiece(start, null);
            if(move.getPromotionPiece() != null){
                board.addPiece(end, new ChessPiece(movingPiece.getTeamColor(), promotion));
            }
            else{
                board.addPiece(end, movingPiece);
            }
            if(currentTurn == TeamColor.WHITE){
                setTeamTurn(TeamColor.BLACK);
            }
            else{
                setTeamTurn(TeamColor.WHITE);
            }
        }
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        ChessPosition kingPosition = findKing(board, teamColor);
        for(int i = 1; i <= 8; i++){
            for(int j = 1; j <= 8; j++){
                ChessPosition enemyPosition = new ChessPosition(i, j);
                ChessPiece enemyPiece = board.getPiece(enemyPosition);
                if(enemyPiece != null && enemyPiece.getTeamColor() != teamColor){
                    Collection<ChessMove> threateningMoves = enemyPiece.pieceMoves(board, enemyPosition);
                    if (threatensKing(threateningMoves, kingPosition)){
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private static boolean threatensKing(Collection<ChessMove> threateningMoves, ChessPosition kingPosition) {
        for(ChessMove move : threateningMoves){
            if(move.getEndPosition().equals(kingPosition)){
                return true;
            }
        }
        return false;
    }

    private ChessPosition findKing(ChessBoard board, TeamColor color){
        for(int i = 1; i <= 8; i++){
            for(int j = 1; j <= 8; j++){
                ChessPosition currentPosition = new ChessPosition(i, j);
                ChessPiece currentPiece = board.getPiece(currentPosition);
                if(currentPiece != null && currentPiece.getPieceType() == ChessPiece.PieceType.KING && currentPiece.getTeamColor() == color){
                    return currentPosition;
                }
            }
        }
        return null;
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        Collection<ChessMove> validMoves = new ArrayList<>();
        if(!isInCheck(teamColor)){
            return false;
        }
        for(int i = 1; i <= 8; i++){
            for(int j = 1; j <=8; j++){
                ChessPosition parsePosition = new ChessPosition(i, j);
                ChessPiece piece = board.getPiece(parsePosition);
                if(piece != null && piece.getTeamColor() == teamColor){
                    validMoves.addAll(validMoves(parsePosition));
                    if (canResolveCheck(teamColor, validMoves, piece)){
                        return false;
                    }
                }
            }
        }
        return true;
    }

    private boolean canResolveCheck(TeamColor teamColor, Collection<ChessMove> validMoves, ChessPiece piece) {
        for(ChessMove move : validMoves){
            board.addPiece(move.getEndPosition(), piece);
            board.addPiece(move.getStartPosition(), null);
            if(!isInCheck(teamColor)){
                return true;
            }
            board.addPiece(move.getEndPosition(), null);
            board.addPiece(move.getStartPosition(), piece);
        }
        return false;
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        Collection<ChessMove> availableMoves = new ArrayList<>();
        if(isInCheck(teamColor)){
            return false;
        }

        for(int i = 1; i <= 8; i++){
            for(int j = 1; j <= 8; j++){
                ChessPosition parsePosition = new ChessPosition(i, j);
                ChessPiece piece = board.getPiece(parsePosition);
                if(piece != null && piece.getTeamColor() == teamColor){
                    availableMoves.addAll(validMoves(new ChessPosition(i, j)));
                    if(!availableMoves.isEmpty()) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        this.board = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return board;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o){
            return true;
        }
        if (o == null || getClass() != o.getClass()){
            return false;
        }
        ChessGame chessGame = (ChessGame) o;
        return currentTurn == chessGame.currentTurn && Objects.equals(board, chessGame.board);
    }

    @Override
    public int hashCode() {
        return Objects.hash(currentTurn, board);
    }
}
