package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

public class MoveCalculator {
    ChessBoard board;
    ChessPosition position;
    public MoveCalculator(ChessBoard board, ChessPosition position){
        this.board = board;
        this.position = position;
    }

    private ChessMove UpLeft(ChessMove move){
        ChessPosition position = move.end;
        if(position.getRow() < 7 && position.getRow() >= 0 && position.getColumn() <= 7 && position.getColumn() > 0){
            ChessPosition end = new ChessPosition(move.getEndPosition().row + 1, move.getEndPosition().col - 1);
            return new ChessMove(move.start, end, null);
        }
        else{
            return null;
        }
    }
    private ChessMove UpRight(ChessMove move){
        ChessPosition position = move.end;
        if(position.getRow() < 7 && position.getRow() >= 0 && position.getColumn() < 7 && position.getColumn() >= 0){
            ChessPosition end = new ChessPosition(move.getEndPosition().row + 1, move.getEndPosition().col + 1);
            return new ChessMove(move.start, end, null);
        }
        else{
            return null;
        }
    }
    private ChessMove DownLeft(ChessMove move){
        ChessPosition position = move.end;
        if(position.getRow() <= 7 && position.getRow() > 0 && position.getColumn() <= 7 && position.getColumn() > 0){
            ChessPosition end = new ChessPosition(move.getEndPosition().row - 1, move.getEndPosition().col - 1);
            return new ChessMove(move.start, end, null);
        }
        else{
            return null;
        }
    }
    private ChessMove DownRight(ChessMove move){
        ChessPosition position = move.end;
        if(position.getRow() <= 7 && position.getRow() > 0 && position.getColumn() < 7 && position.getColumn() >= 0){
            ChessPosition end = new ChessPosition(move.getEndPosition().row - 1, move.getEndPosition().col + 1);
            return new ChessMove(move.start, end, null);
        }
        else{
            return null;
        }
    }
    private ChessMove Up(ChessMove move){
        ChessPosition position = move.end;
        if(position.getRow() < 7 && position.getRow() >= 0 && position.getColumn() <= 7 && position.getColumn() >= 0){
            ChessPosition end = new ChessPosition(move.getEndPosition().row + 1, move.getEndPosition().col);
            return new ChessMove(move.start, end, null);
        }
        else{
            return null;
        }
    }
    private ChessMove Down(ChessMove move){
        ChessPosition position = move.end;
        if(position.getRow() <= 7 && position.getRow() > 0 && position.getColumn() <= 7 && position.getColumn() >= 0){
            ChessPosition end = new ChessPosition(move.getEndPosition().row - 1, move.getEndPosition().col);
            return new ChessMove(move.start, end, null);
        }
        else{
            return null;
        }
    }
    private ChessMove Right(ChessMove move){
        ChessPosition position = move.end;
        if(position.getRow() <= 7 && position.getRow() >= 0 && position.getColumn() < 7 && position.getColumn() >= 0){
            ChessPosition end = new ChessPosition(move.getEndPosition().row , move.getEndPosition().col + 1);
            return new ChessMove(move.start, end, null);
        }
        else{
            return null;
        }
    }
    private ChessMove Left(ChessMove move){
        ChessPosition position = move.end;
        if(position.getRow() <= 7 && position.getRow() >= 0 && position.getColumn() <= 7 && position.getColumn() > 0){
            ChessPosition end = new ChessPosition(move.getEndPosition().row, move.getEndPosition().col - 1);
            return new ChessMove(move.start, end, null);
        }
        else{
            return null;
        }
    }
    private List<ChessMove> ValidateMove(List<ChessMove> unvalidatedMoves, ChessGame.TeamColor color){
        List<ChessMove> validMove = new ArrayList<>();
        for(ChessMove move: unvalidatedMoves){
            ChessPiece blocked = board.getPiece(move.end);
            if(blocked == null){
                validMove.add(move);
            }
            else if (blocked.getTeamColor() != color) {
                validMove.add(move);
                break;
            }
            else{
                break;
            }
        }

        return validMove;
    }
    public Collection<ChessMove> BishopMoves(ChessPosition position, ChessGame.TeamColor color) {
        ChessMove moves = new ChessMove(position, position, null);
        List<ChessMove> unvalidatedMoves = new ArrayList<>();
        List<ChessMove> validMoves = new ArrayList<>();
        while (UpLeft(moves) != null) {
            unvalidatedMoves.add(UpLeft(moves));
            moves = UpLeft(moves);
        }
        validMoves.addAll(ValidateMove(unvalidatedMoves, color));
        unvalidatedMoves.clear();
        moves.end = position;
        while (UpRight(moves) != null) {
            unvalidatedMoves.add(UpRight(moves));
            moves = UpRight(moves);
        }
        validMoves.addAll(ValidateMove(unvalidatedMoves, color));
        unvalidatedMoves.clear();
        moves.end = position;
        while(DownLeft(moves) != null){
            unvalidatedMoves.add(DownLeft(moves));
            moves = DownLeft(moves);
        }
        validMoves.addAll(ValidateMove(unvalidatedMoves, color));
        unvalidatedMoves.clear();
        moves.end = position;
        while(DownRight(moves) != null){
            unvalidatedMoves.add(DownRight(moves));
            moves = DownRight(moves);
        }
        validMoves.addAll(ValidateMove(unvalidatedMoves, color));
        unvalidatedMoves.clear();
        validMoves.removeIf(Objects::isNull);
        return validMoves;
    }
    public Collection<ChessMove> KingMoves(ChessPosition position, ChessGame.TeamColor color){
        ChessMove moves = new ChessMove(position, position, null);
        List<ChessMove> unvalidatedMoves = new ArrayList<>();
        List<ChessMove> validMoves = new ArrayList<>();
        if(UpLeft(moves) != null){
            unvalidatedMoves.add(UpLeft(moves));
        }
        validMoves.addAll(ValidateMove(unvalidatedMoves, color));
        unvalidatedMoves.clear();
        if(Up(moves) != null){
            unvalidatedMoves.add(Up(moves));
        }
        validMoves.addAll(ValidateMove(unvalidatedMoves, color));
        unvalidatedMoves.clear();
        if(UpRight(moves) != null){
            unvalidatedMoves.add(UpRight(moves));
        }
        validMoves.addAll(ValidateMove(unvalidatedMoves, color));
        unvalidatedMoves.clear();
        if(Left(moves) != null){
            unvalidatedMoves.add(Left(moves));
        }
        validMoves.addAll(ValidateMove(unvalidatedMoves, color));
        unvalidatedMoves.clear();
        if(Right(moves) != null){
            unvalidatedMoves.add(Right(moves));
        }
        validMoves.addAll(ValidateMove(unvalidatedMoves, color));
        unvalidatedMoves.clear();
        if(DownLeft(moves) != null){
            unvalidatedMoves.add(DownLeft(moves));
        }
        validMoves.addAll(ValidateMove(unvalidatedMoves, color));
        unvalidatedMoves.clear();
        if(Down(moves) != null){
            unvalidatedMoves.add(Down(moves));
        }
        validMoves.addAll(ValidateMove(unvalidatedMoves, color));
        unvalidatedMoves.clear();
        if(DownRight(moves) != null){
            unvalidatedMoves.add(DownRight(moves));
        }
        validMoves.addAll(ValidateMove(unvalidatedMoves, color));
        unvalidatedMoves.clear();
        validMoves.removeIf(Objects::isNull);
        return validMoves;
    }
}
