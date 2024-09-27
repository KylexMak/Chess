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

    private ChessMove upLeft(ChessMove move){
        ChessPosition position = move.end;
        if(position.getRow() < 7 && position.getRow() >= 0 && position.getColumn() <= 7 && position.getColumn() > 0){
            ChessPosition end = new ChessPosition(move.getEndPosition().row + 1, move.getEndPosition().col - 1);
            return new ChessMove(move.start, end, null);
        }
        else{
            return null;
        }
    }
    private ChessMove upRight(ChessMove move){
        ChessPosition position = move.end;
        if(position.getRow() < 7 && position.getRow() >= 0 && position.getColumn() < 7 && position.getColumn() >= 0){
            ChessPosition end = new ChessPosition(move.getEndPosition().row + 1, move.getEndPosition().col + 1);
            return new ChessMove(move.start, end, null);
        }
        else{
            return null;
        }
    }
    private ChessMove downLeft(ChessMove move){
        ChessPosition position = move.end;
        if(position.getRow() <= 7 && position.getRow() > 0 && position.getColumn() <= 7 && position.getColumn() > 0){
            ChessPosition end = new ChessPosition(move.getEndPosition().row - 1, move.getEndPosition().col - 1);
            return new ChessMove(move.start, end, null);
        }
        else{
            return null;
        }
    }
    private ChessMove downRight(ChessMove move){
        ChessPosition position = move.end;
        if(position.getRow() <= 7 && position.getRow() > 0 && position.getColumn() < 7 && position.getColumn() >= 0){
            ChessPosition end = new ChessPosition(move.getEndPosition().row - 1, move.getEndPosition().col + 1);
            return new ChessMove(move.start, end, null);
        }
        else{
            return null;
        }
    }
    private ChessMove up(ChessMove move){
        ChessPosition position = move.end;
        if(position.getRow() < 7 && position.getRow() >= 0 && position.getColumn() <= 7 && position.getColumn() >= 0){
            ChessPosition end = new ChessPosition(move.getEndPosition().row + 1, move.getEndPosition().col);
            return new ChessMove(move.start, end, null);
        }
        else{
            return null;
        }
    }
    private ChessMove down(ChessMove move){
        ChessPosition position = move.end;
        if(position.getRow() <= 7 && position.getRow() > 0 && position.getColumn() <= 7 && position.getColumn() >= 0){
            ChessPosition end = new ChessPosition(move.getEndPosition().row - 1, move.getEndPosition().col);
            return new ChessMove(move.start, end, null);
        }
        else{
            return null;
        }
    }
    private ChessMove right(ChessMove move){
        ChessPosition position = move.end;
        if(position.getRow() <= 7 && position.getRow() >= 0 && position.getColumn() < 7 && position.getColumn() >= 0){
            ChessPosition end = new ChessPosition(move.getEndPosition().row , move.getEndPosition().col + 1);
            return new ChessMove(move.start, end, null);
        }
        else{
            return null;
        }
    }
    private ChessMove left(ChessMove move){
        ChessPosition position = move.end;
        if(position.getRow() <= 7 && position.getRow() >= 0 && position.getColumn() <= 7 && position.getColumn() > 0){
            ChessPosition end = new ChessPosition(move.getEndPosition().row, move.getEndPosition().col - 1);
            return new ChessMove(move.start, end, null);
        }
        else{
            return null;
        }
    }
    private List<ChessMove> validateMoves(List<ChessMove> unvalidatedMoves, ChessGame.TeamColor color){
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
    public Collection<ChessMove> bishopMoves(ChessPosition position, ChessGame.TeamColor color) {
        ChessMove moves = new ChessMove(position, position, null);
        List<ChessMove> unvalidatedMoves = new ArrayList<>();
        List<ChessMove> validMoves = new ArrayList<>();
        while (upLeft(moves) != null) {
            unvalidatedMoves.add(upLeft(moves));
            moves = upLeft(moves);
        }
        validMoves.addAll(validateMoves(unvalidatedMoves, color));
        unvalidatedMoves.clear();
        moves.end = position;
        while (upRight(moves) != null) {
            unvalidatedMoves.add(upRight(moves));
            moves = upRight(moves);
        }
        validMoves.addAll(validateMoves(unvalidatedMoves, color));
        unvalidatedMoves.clear();
        moves.end = position;
        while(downLeft(moves) != null){
            unvalidatedMoves.add(downLeft(moves));
            moves = downLeft(moves);
        }
        validMoves.addAll(validateMoves(unvalidatedMoves, color));
        unvalidatedMoves.clear();
        moves.end = position;
        while(downRight(moves) != null){
            unvalidatedMoves.add(downRight(moves));
            moves = downRight(moves);
        }
        validMoves.addAll(validateMoves(unvalidatedMoves, color));
        unvalidatedMoves.clear();
        validMoves.removeIf(Objects::isNull);
        return validMoves;
    }
    public Collection<ChessMove> kingMoves(ChessPosition position, ChessGame.TeamColor color){
        ChessMove moves = new ChessMove(position, position, null);
        List<ChessMove> unvalidatedMoves = new ArrayList<>();
        List<ChessMove> validMoves = new ArrayList<>();
        if(upLeft(moves) != null){
            unvalidatedMoves.add(upLeft(moves));
        }
        validMoves.addAll(validateMoves(unvalidatedMoves, color));
        unvalidatedMoves.clear();
        if(up(moves) != null){
            unvalidatedMoves.add(up(moves));
        }
        validMoves.addAll(validateMoves(unvalidatedMoves, color));
        unvalidatedMoves.clear();
        if(upRight(moves) != null){
            unvalidatedMoves.add(upRight(moves));
        }
        validMoves.addAll(validateMoves(unvalidatedMoves, color));
        unvalidatedMoves.clear();
        if(left(moves) != null){
            unvalidatedMoves.add(left(moves));
        }
        validMoves.addAll(validateMoves(unvalidatedMoves, color));
        unvalidatedMoves.clear();
        if(right(moves) != null){
            unvalidatedMoves.add(right(moves));
        }
        validMoves.addAll(validateMoves(unvalidatedMoves, color));
        unvalidatedMoves.clear();
        if(downLeft(moves) != null){
            unvalidatedMoves.add(downLeft(moves));
        }
        validMoves.addAll(validateMoves(unvalidatedMoves, color));
        unvalidatedMoves.clear();
        if(down(moves) != null){
            unvalidatedMoves.add(down(moves));
        }
        validMoves.addAll(validateMoves(unvalidatedMoves, color));
        unvalidatedMoves.clear();
        if(downRight(moves) != null){
            unvalidatedMoves.add(downRight(moves));
        }
        validMoves.addAll(validateMoves(unvalidatedMoves, color));
        unvalidatedMoves.clear();
        validMoves.removeIf(Objects::isNull);
        return validMoves;
    }

    public Collection<ChessMove> knightMoves(ChessPosition position, ChessGame.TeamColor color){
        ChessMove moves = new ChessMove(position, position, null);
        ChessMove reset = new ChessMove(position, position, null);
        List<ChessMove> unvalidatedMoves = new ArrayList<>();
        List<ChessMove> validMoves = new ArrayList<>();
        if(up(moves) != null){
            moves = up(moves);
            if(upLeft(moves) != null){
                moves.end = upLeft(moves).getEndPosition();
                moves.start = position;
                unvalidatedMoves.add(moves);
            }
            moves = up(reset);
            if(upRight(moves) != null){
                moves.end = upRight(moves).getEndPosition();
                moves.start = position;
                unvalidatedMoves.add(moves);
            }
            moves = reset;
        }
        validMoves.addAll(validateMoves(unvalidatedMoves, color));
        unvalidatedMoves.clear();
        if(right(moves) != null){
            moves = right(moves);
            if(upRight(moves) != null){
                moves.end = upRight(moves).getEndPosition();
                moves.start = position;
                unvalidatedMoves.add(moves);
            }
            moves = right(reset);
            if(downRight(moves) != null){
                moves.end = downRight(moves).getEndPosition();
                moves.start = position;
                unvalidatedMoves.add(moves);
            }
            moves = reset;
        }
        validMoves.addAll(validateMoves(unvalidatedMoves, color));
        unvalidatedMoves.clear();
        if(down(moves) != null){
            moves = down(moves);
            if(downRight(moves) != null){
                moves.end = downRight(moves).getEndPosition();
                moves.start = position;
                unvalidatedMoves.add(moves);
            }
            moves = down(reset);
            if(downLeft(moves) != null){
                moves.end = downLeft(moves).getEndPosition();
                moves.start = position;
                unvalidatedMoves.add(moves);
            }
            moves = reset;
        }
        validMoves.addAll(validateMoves(unvalidatedMoves, color));
        unvalidatedMoves.clear();
        if(left(moves) != null){
            moves = left(moves);
            if(downLeft(moves) != null){
                moves.end = downLeft(moves).getEndPosition();
                moves.start = position;
                unvalidatedMoves.add(moves);
            }
            moves = left(reset);
            if(upLeft(moves) != null){
                moves.end = upLeft(moves).getEndPosition();
                moves.start = position;
                unvalidatedMoves.add(moves);
            }
        }
        validMoves.addAll(validateMoves(unvalidatedMoves, color));
        unvalidatedMoves.clear();
        validMoves.removeIf(Objects::isNull);
        return validMoves;
    }
    public Collection<ChessMove> pawnMove(ChessPosition position, ChessGame.TeamColor color){
        ChessMove moves = new ChessMove(position, position, null);
        List<ChessMove> validMoves = pawnMovement(moves, color);
        return validMoves;
    }
    private List<ChessMove> pawnMovement(ChessMove move, ChessGame.TeamColor color){
        ChessMove reset = move;
        ChessMove temp;
        List<ChessMove> unvalidatedMoves = new ArrayList<>();
        List<ChessMove> validMoves = new ArrayList<>();
        List<ChessMove> filteredMoves = new ArrayList<>();
        List<ChessMove> promotions = new ArrayList<>();
        if(color == ChessGame.TeamColor.BLACK){
            if(position.row == 7){
                if(down(move) != null){
                    unvalidatedMoves.add(down(move));
                    move = down(move);
                    if(down(move) != null){
                        move.end = down(move).getEndPosition();
                        move.start = reset.getStartPosition();
                        unvalidatedMoves.add(move);
                    }
                }
            }
            else {
                temp = downLeft(move);
                if(temp != null && board.getPiece(temp.end) != null && board.getPiece(temp.end).getTeamColor() != color){
                    unvalidatedMoves.add(temp);
                }
                temp = downRight(move);
                if(temp != null && board.getPiece(temp.end) != null && board.getPiece(temp.end).getTeamColor() != color){
                    unvalidatedMoves.add(temp);
                }
                temp = down(move);
                if(temp != null){
                    unvalidatedMoves.add(temp);
                }
            }
        }
        else{
            if(position.row == 2){
                if(up(move) != null){
                    unvalidatedMoves.add(up(move));
                    move = up(move);
                    if(up(move) != null){
                        move.end = up(move).getEndPosition();
                        move.start = reset.getStartPosition();
                        unvalidatedMoves.add(move);
                    }
                }
                move = reset;
            }
            else {
                temp = upLeft(move);
                if(temp != null && board.getPiece(temp.end) != null && board.getPiece(temp.end).getTeamColor() != color){
                    unvalidatedMoves.add(temp);
                }
                temp = upRight(move);
                if(temp != null && board.getPiece(temp.end) != null && board.getPiece(temp.end).getTeamColor() != color){
                    unvalidatedMoves.add(temp);
                }
                temp = up(move);
                if(temp != null){
                    unvalidatedMoves.add(temp);
                }
            }
        }
        validMoves.addAll(validateMoves(unvalidatedMoves, color));
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

    public Collection<ChessMove> rookMoves(ChessPosition position, ChessGame.TeamColor color) {
        ChessMove moves = new ChessMove(position, position, null);
        List<ChessMove> unvalidatedMoves = new ArrayList<>();
        List<ChessMove> validMoves = new ArrayList<>();
        while (up(moves) != null) {
            unvalidatedMoves.add(up(moves));
            moves = up(moves);
        }
        validMoves.addAll(validateMoves(unvalidatedMoves, color));
        unvalidatedMoves.clear();
        moves.end = position;
        while (down(moves) != null) {
            unvalidatedMoves.add(down(moves));
            moves = down(moves);
        }
        validMoves.addAll(validateMoves(unvalidatedMoves, color));
        unvalidatedMoves.clear();
        moves.end = position;
        while(left(moves) != null){
            unvalidatedMoves.add(left(moves));
            moves = left(moves);
        }
        validMoves.addAll(validateMoves(unvalidatedMoves, color));
        unvalidatedMoves.clear();
        moves.end = position;
        while(right(moves) != null){
            unvalidatedMoves.add(right(moves));
            moves = right(moves);
        }
        validMoves.addAll(validateMoves(unvalidatedMoves, color));
        unvalidatedMoves.clear();
        validMoves.removeIf(Objects::isNull);
        return validMoves;
    }

    public Collection<ChessMove> queenMoves(ChessPosition position, ChessGame.TeamColor color){
        List<ChessMove> validMoves = new ArrayList<>();
        validMoves.addAll(bishopMoves(position, color));
        validMoves.addAll(rookMoves(position, color));
        return validMoves;
    }
}
