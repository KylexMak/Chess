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
    private List<ChessMove> ValidateMoves(List<ChessMove> unvalidatedMoves, ChessGame.TeamColor color){
        List<ChessMove> validMove = new ArrayList<>();
        for(ChessMove move: unvalidatedMoves){
            ChessPiece movingPiece = board.getPiece(move.start);
            ChessPiece.PieceType type = movingPiece.getPieceType();
            ChessPiece blocked = board.getPiece(move.end);
            boolean isPawnForwardBlock = type == ChessPiece.PieceType.PAWN && move.start.col == move.end.col && blocked != null;
            if(blocked == null){
                validMove.add(move);
            }
            else if (blocked.getTeamColor() != color){
                if(!isPawnForwardBlock) {
                    validMove.add(move);
                    if (type != ChessPiece.PieceType.KNIGHT && type != ChessPiece.PieceType.PAWN) {
                        break;
                    }
                }
            }
            else if(type != ChessPiece.PieceType.KNIGHT && (!isPawnForwardBlock)){
                break;
            }
            else{
                if(type != ChessPiece.PieceType.KNIGHT){
                    break;
                }
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
        validMoves.addAll(ValidateMoves(unvalidatedMoves, color));
        unvalidatedMoves.clear();
        moves.end = position;
        while (UpRight(moves) != null) {
            unvalidatedMoves.add(UpRight(moves));
            moves = UpRight(moves);
        }
        validMoves.addAll(ValidateMoves(unvalidatedMoves, color));
        unvalidatedMoves.clear();
        moves.end = position;
        while(DownLeft(moves) != null){
            unvalidatedMoves.add(DownLeft(moves));
            moves = DownLeft(moves);
        }
        validMoves.addAll(ValidateMoves(unvalidatedMoves, color));
        unvalidatedMoves.clear();
        moves.end = position;
        while(DownRight(moves) != null){
            unvalidatedMoves.add(DownRight(moves));
            moves = DownRight(moves);
        }
        validMoves.addAll(ValidateMoves(unvalidatedMoves, color));
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
        validMoves.addAll(ValidateMoves(unvalidatedMoves, color));
        unvalidatedMoves.clear();
        if(Up(moves) != null){
            unvalidatedMoves.add(Up(moves));
        }
        validMoves.addAll(ValidateMoves(unvalidatedMoves, color));
        unvalidatedMoves.clear();
        if(UpRight(moves) != null){
            unvalidatedMoves.add(UpRight(moves));
        }
        validMoves.addAll(ValidateMoves(unvalidatedMoves, color));
        unvalidatedMoves.clear();
        if(Left(moves) != null){
            unvalidatedMoves.add(Left(moves));
        }
        validMoves.addAll(ValidateMoves(unvalidatedMoves, color));
        unvalidatedMoves.clear();
        if(Right(moves) != null){
            unvalidatedMoves.add(Right(moves));
        }
        validMoves.addAll(ValidateMoves(unvalidatedMoves, color));
        unvalidatedMoves.clear();
        if(DownLeft(moves) != null){
            unvalidatedMoves.add(DownLeft(moves));
        }
        validMoves.addAll(ValidateMoves(unvalidatedMoves, color));
        unvalidatedMoves.clear();
        if(Down(moves) != null){
            unvalidatedMoves.add(Down(moves));
        }
        validMoves.addAll(ValidateMoves(unvalidatedMoves, color));
        unvalidatedMoves.clear();
        if(DownRight(moves) != null){
            unvalidatedMoves.add(DownRight(moves));
        }
        validMoves.addAll(ValidateMoves(unvalidatedMoves, color));
        unvalidatedMoves.clear();
        validMoves.removeIf(Objects::isNull);
        return validMoves;
    }

    public Collection<ChessMove> KnightMoves(ChessPosition position, ChessGame.TeamColor color){
        ChessMove moves = new ChessMove(position, position, null);
        ChessMove reset = new ChessMove(position, position, null);
        List<ChessMove> unvalidatedMoves = new ArrayList<>();
        List<ChessMove> validMoves = new ArrayList<>();
        if(Up(moves) != null){
            moves = Up(moves);
            if(UpLeft(moves) != null){
                moves.end = UpLeft(moves).getEndPosition();
                moves.start = position;
                unvalidatedMoves.add(moves);
            }
            moves = Up(reset);
            if(UpRight(moves) != null){
                moves.end = UpRight(moves).getEndPosition();
                moves.start = position;
                unvalidatedMoves.add(moves);
            }
            moves = reset;
        }
        validMoves.addAll(ValidateMoves(unvalidatedMoves, color));
        unvalidatedMoves.clear();
        if(Right(moves) != null){
            moves = Right(moves);
            if(UpRight(moves) != null){
                moves.end = UpRight(moves).getEndPosition();
                moves.start = position;
                unvalidatedMoves.add(moves);
            }
            moves = Right(reset);
            if(DownRight(moves) != null){
                moves.end = DownRight(moves).getEndPosition();
                moves.start = position;
                unvalidatedMoves.add(moves);
            }
            moves = reset;
        }
        validMoves.addAll(ValidateMoves(unvalidatedMoves, color));
        unvalidatedMoves.clear();
        if(Down(moves) != null){
            moves = Down(moves);
            if(DownRight(moves) != null){
                moves.end = DownRight(moves).getEndPosition();
                moves.start = position;
                unvalidatedMoves.add(moves);
            }
            moves = Down(reset);
            if(DownLeft(moves) != null){
                moves.end = DownLeft(moves).getEndPosition();
                moves.start = position;
                unvalidatedMoves.add(moves);
            }
            moves = reset;
        }
        validMoves.addAll(ValidateMoves(unvalidatedMoves, color));
        unvalidatedMoves.clear();
        if(Left(moves) != null){
            moves = Left(moves);
            if(DownLeft(moves) != null){
                moves.end = DownLeft(moves).getEndPosition();
                moves.start = position;
                unvalidatedMoves.add(moves);
            }
            moves = Left(reset);
            if(UpLeft(moves) != null){
                moves.end = UpLeft(moves).getEndPosition();
                moves.start = position;
                unvalidatedMoves.add(moves);
            }
        }
        validMoves.addAll(ValidateMoves(unvalidatedMoves, color));
        unvalidatedMoves.clear();
        validMoves.removeIf(Objects::isNull);
        return validMoves;
    }
    public Collection<ChessMove> PawnMove(ChessPosition position, ChessGame.TeamColor color){
        ChessMove moves = new ChessMove(position, position, null);
        List<ChessMove> validMoves = PawnMovement(moves, color);
        return validMoves;
    }
    private List<ChessMove> PawnMovement(ChessMove move, ChessGame.TeamColor color){
        ChessMove reset = move;
        ChessMove temp;
        List<ChessMove> unvalidatedMoves = new ArrayList<>();
        List<ChessMove> validMoves = new ArrayList<>();
        List<ChessMove> filteredMoves = new ArrayList<>();
        List<ChessMove> promotions = new ArrayList<>();
        if(color == ChessGame.TeamColor.BLACK){
            if(position.row == 7){
                if(Down(move) != null){
                    unvalidatedMoves.add(Down(move));
                    move = Down(move);
                    if(Down(move) != null){
                        move.end = Down(move).getEndPosition();
                        move.start = reset.getStartPosition();
                        unvalidatedMoves.add(move);
                    }
                }
            }
            else {
                temp = DownLeft(move);
                if(temp != null && board.getPiece(temp.end) != null && board.getPiece(temp.end).getTeamColor() != color){
                    unvalidatedMoves.add(temp);
                }
                temp = DownRight(move);
                if(temp != null && board.getPiece(temp.end) != null && board.getPiece(temp.end).getTeamColor() != color){
                    unvalidatedMoves.add(temp);
                }
                temp = Down(move);
                if(temp != null){
                    unvalidatedMoves.add(temp);
                }
            }
        }
        else{
            if(position.row == 2){
                if(Up(move) != null){
                    unvalidatedMoves.add(Up(move));
                    move = Up(move);
                    if(Up(move) != null){
                        move.end = Up(move).getEndPosition();
                        move.start = reset.getStartPosition();
                        unvalidatedMoves.add(move);
                    }
                }
                move = reset;
            }
            else {
                temp = UpLeft(move);
                if(temp != null && board.getPiece(temp.end) != null && board.getPiece(temp.end).getTeamColor() != color){
                    unvalidatedMoves.add(temp);
                }
                temp = UpRight(move);
                if(temp != null && board.getPiece(temp.end) != null && board.getPiece(temp.end).getTeamColor() != color){
                    unvalidatedMoves.add(temp);
                }
                temp = Up(move);
                if(temp != null){
                    unvalidatedMoves.add(temp);
                }
            }
        }
        validMoves.addAll(ValidateMoves(unvalidatedMoves, color));
        for(ChessMove promo : validMoves){
            if(promo.getEndPosition().row == 1 || promo.getEndPosition().row == 8){
                ChessMove promoMove = new ChessMove(promo.start, promo.end, ChessPiece.PieceType.QUEEN);
                promotions.add(promoMove);
                promoMove = new ChessMove(promo.start, promo.end, ChessPiece.PieceType.BISHOP);
                promotions.add(promoMove);
                promoMove = new ChessMove(promo.start, promo.end, ChessPiece.PieceType.KNIGHT);
                promotions.add(promoMove);
                promoMove = new ChessMove(promo.start, promo.end, ChessPiece.PieceType.ROOK);
                promotions.add(promoMove);
            }
        }
        filteredMoves.addAll(validMoves);
        for(ChessMove duplicate : filteredMoves){
            if(duplicate.getEndPosition().row == 1 || duplicate.getEndPosition().row == 8){
                if(validMoves.contains(duplicate)){
                    validMoves.remove(duplicate);
                }
            }
        }
        validMoves.addAll(promotions);
        validMoves.removeIf(Objects::isNull);
        return validMoves;
    }

    public Collection<ChessMove> RookMoves(ChessPosition position, ChessGame.TeamColor color) {
        ChessMove moves = new ChessMove(position, position, null);
        List<ChessMove> unvalidatedMoves = new ArrayList<>();
        List<ChessMove> validMoves = new ArrayList<>();
        while (Up(moves) != null) {
            unvalidatedMoves.add(Up(moves));
            moves = Up(moves);
        }
        validMoves.addAll(ValidateMoves(unvalidatedMoves, color));
        unvalidatedMoves.clear();
        moves.end = position;
        while (Down(moves) != null) {
            unvalidatedMoves.add(Down(moves));
            moves = Down(moves);
        }
        validMoves.addAll(ValidateMoves(unvalidatedMoves, color));
        unvalidatedMoves.clear();
        moves.end = position;
        while(Left(moves) != null){
            unvalidatedMoves.add(Left(moves));
            moves = Left(moves);
        }
        validMoves.addAll(ValidateMoves(unvalidatedMoves, color));
        unvalidatedMoves.clear();
        moves.end = position;
        while(Right(moves) != null){
            unvalidatedMoves.add(Right(moves));
            moves = Right(moves);
        }
        validMoves.addAll(ValidateMoves(unvalidatedMoves, color));
        unvalidatedMoves.clear();
        validMoves.removeIf(Objects::isNull);
        return validMoves;
    }

    public Collection<ChessMove> QueenMoves(ChessPosition position, ChessGame.TeamColor color){
        List<ChessMove> validMoves = new ArrayList<>();
        validMoves.addAll(BishopMoves(position, color));
        validMoves.addAll(RookMoves(position, color));
        return validMoves;
    }
}
