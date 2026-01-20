package com.github.mcreeper12731.game.pieces.movesets;

import com.github.mcreeper12731.game.models.Color;
import com.github.mcreeper12731.game.models.Multiverse;
import com.github.mcreeper12731.game.models.Point4D;
import com.github.mcreeper12731.game.moves.Move;
import com.github.mcreeper12731.game.moves.MoveValidator;
import com.github.mcreeper12731.game.pieces.Piece;
import com.github.mcreeper12731.game.pieces.PieceType;

import java.util.*;
import java.util.List;

public class PawnMoveSet implements MoveSet {

    // TODO: this entire file is so ugly, refactor at some point

    private MoveValidator moveValidator = null;

    private Optional<Move> simpleMove(Multiverse multiverse, Piece piece, Point4D moveVector) {

        int lastRank = piece.color() == Color.WHITE ? multiverse.getBoardSize() - 1 : 0;

        Point4D toLocation = piece.location().add(moveVector);

        if (!moveValidator.isToCorrect(piece.color(), toLocation))
            return Optional.empty();

        if (moveVector.x() == 0 && moveVector.time() == 0) {
            // non-capturing, forward move

            for (int i = 1; i <= Math.abs(moveVector.timeline()); i++) {
                Optional<Piece> pieceAtLocation = multiverse.getPiece(
                        piece.location().timeline() + i * Math.signum(moveVector.timeline()),
                        toLocation.time(),
                        toLocation.x(),
                        toLocation.y()
                );

                if (pieceAtLocation.isPresent()) return Optional.empty();
            }

            for (int i = 1; i <= Math.abs(moveVector.y()); i++) {
                Optional<Piece> pieceAtLocation = multiverse.getPiece(
                        toLocation.timeline(),
                        toLocation.time(),
                        toLocation.x(),
                        piece.location().y() + i * Integer.signum(moveVector.y())
                );
                if (pieceAtLocation.isPresent()) return Optional.empty();
            }
        } else {
            // capturing, sideways move

            Optional<Piece> pieceAtLocation = multiverse.getPiece(toLocation);

            if (pieceAtLocation.isEmpty()) return Optional.empty();
            if (pieceAtLocation.get().color() == piece.color()) return Optional.empty();
        }


        return Optional.of(new Move(
                piece,
                toLocation,
                toLocation.y() == lastRank ? PieceType.QUEEN : null
        ));
    }

    @Override
    public List<Move> generateMoves(Multiverse multiverse, Piece piece) {

        if (moveValidator == null)
            moveValidator = new MoveValidator(multiverse);

        List<Move> moves = new ArrayList<>();

        int direction = piece.color() == Color.WHITE ? 1 : -1;

        // non-capturing, forward moves
        simpleMove(
                multiverse,
                piece,
                new Point4D(0, 0, 0, direction)
        ).ifPresent(moves::add);

        if (!piece.moved()) {
            simpleMove(
                    multiverse,
                    piece,
                    new Point4D(0, 0, 0, direction * 2)
            ).ifPresent(moves::add);
        }

        simpleMove(
                multiverse,
                piece,
                new Point4D(0, 0, 1, direction)
        ).ifPresent(moves::add);

        simpleMove(
                multiverse,
                piece,
                new Point4D(0, 0, -1, direction)
        ).ifPresent(moves::add);

        simpleMove(
                multiverse,
                piece,
                new Point4D(direction, 1, 0, 0)
        ).ifPresent(moves::add);

        simpleMove(
                multiverse,
                piece,
                new Point4D(direction, -1, 0, 0)
        ).ifPresent(moves::add);

        return moves;
    }
}
