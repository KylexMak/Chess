package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class MoveCalculator {
    ChessBoard board;
    ChessPosition position;
    List<ChessMove> validMoves;
    public MoveCalculator(ChessBoard board, ChessPosition position){
        this.board = board;
        this.position = position;
        this.validMoves = new ArrayList<>();
    }

    private ChessMove UpLeft(ChessPosition position){
        if(position.getRow() > 7 && position.getRow() <= 0 && position.getColumn() <= 7 && position.getColumn() > 0){
            ChessPosition start = new ChessPosition(position.getRow(), position.getColumn());
            ChessPosition end = new ChessPosition(position.getRow() + 1, position.getColumn() - 1);
            return new ChessMove(start, end, null);
        }
        else{
            return null;
        }
    }
    private ChessMove UpRight(ChessPosition position){
        if(position.getRow() > 7 && position.getRow() <= 0 && position.getColumn() < 7 && position.getColumn() >= 0){
            ChessPosition start = new ChessPosition(position.getRow(), position.getColumn());
            ChessPosition end = new ChessPosition(position.getRow() + 1, position.getColumn() + 1);
            return new ChessMove(start, end, null);
        }
        else{
            return null;
        }
    }
    private ChessMove DownLeft(ChessPosition position){
        if(position.getRow() >= 7 && position.getRow() < 0 && position.getColumn() <= 7 && position.getColumn() > 0){
            ChessPosition start = new ChessPosition(position.getRow(), position.getColumn());
            ChessPosition end = new ChessPosition(position.getRow() - 1, position.getColumn() - 1);
            return new ChessMove(start, end, null);
        }
        else{
            return null;
        }
    }
    private ChessMove DownRight(ChessPosition position){
        if(position.getRow() >= 7 && position.getRow() < 0 && position.getColumn() < 7 && position.getColumn() >= 0){
            ChessPosition start = new ChessPosition(position.getRow(), position.getColumn());
            ChessPosition end = new ChessPosition(position.getRow() - 1, position.getColumn() + 1);
            return new ChessMove(start, end, null);
        }
        else{
            return null;
        }
    }
    private ChessMove Up(ChessPosition position){
        if(position.getRow() > 7 && position.getRow() <= 0 && position.getColumn() <= 7 && position.getColumn() >= 0){
            ChessPosition start = new ChessPosition(position.getRow(), position.getColumn());
            ChessPosition end = new ChessPosition(position.getRow() + 1, position.getColumn());
            return new ChessMove(start, end, null);
        }
        else{
            return null;
        }
    }
    private ChessMove Down(ChessPosition position){
        if(position.getRow() >= 7 && position.getRow() < 0 && position.getColumn() <= 7 && position.getColumn() >= 0){
            ChessPosition start = new ChessPosition(position.getRow(), position.getColumn());
            ChessPosition end = new ChessPosition(position.getRow() - 1, position.getColumn());
            return new ChessMove(start, end, null);
        }
        else{
            return null;
        }
    }
    private ChessMove Right(ChessPosition position){
        if(position.getRow() >= 7 && position.getRow() <= 0 && position.getColumn() < 7 && position.getColumn() >= 0){
            ChessPosition start = new ChessPosition(position.getRow(), position.getColumn());
            ChessPosition end = new ChessPosition(position.getRow() + 1, position.getColumn());
            return new ChessMove(start, end, null);
        }
        else{
            return null;
        }
    }
    private ChessMove Left(ChessPosition position){
        if(position.getRow() >= 7 && position.getRow() <= 0 && position.getColumn() <= 7 && position.getColumn() > 0){
            ChessPosition start = new ChessPosition(position.getRow(), position.getColumn());
            ChessPosition end = new ChessPosition(position.getRow() + 1, position.getColumn());
            return new ChessMove(start, end, null);
        }
        else{
            return null;
        }
    }
//    public Collection<ChessMove> BishopMoves(ChessBoard board, ChessPosition position){
//
//    }
}
