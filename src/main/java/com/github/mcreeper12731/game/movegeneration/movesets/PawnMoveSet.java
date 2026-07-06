package com.github.mcreeper12731.game.movegeneration.movesets;

import com.github.mcreeper12731.game.models.Color;
import com.github.mcreeper12731.game.models.Multiverse;
import com.github.mcreeper12731.game.models.Point4D;
import com.github.mcreeper12731.game.models.Move;
import com.github.mcreeper12731.game.models.pieces.MoveDirections;
import com.github.mcreeper12731.game.models.pieces.Piece;
import com.github.mcreeper12731.game.models.pieces.PieceType;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class PawnMoveSet implements MoveSet {

    private final List<Point4D> whiteMoves;
    private final List<Point4D> whiteCaptures;

    private final List<Point4D> blackMoves;
    private final List<Point4D> blackCaptures;

    public PawnMoveSet() {
        this.whiteMoves = MoveDirections.DIRECTIONS_WHITE_PAWN_MOVES;
        this.whiteCaptures = MoveDirections.DIRECTIONS_WHITE_PAWN_CAPTURES;

        this.blackMoves = MoveDirections.DIRECTIONS_BLACK_PAWN_MOVES;
        this.blackCaptures = MoveDirections.DIRECTIONS_BLACK_PAWN_CAPTURES;
    }

    public PawnMoveSet(List<Point4D> whiteCaptures, List<Point4D> blackCaptures) {
        this.whiteMoves = MoveDirections.DIRECTIONS_WHITE_PAWN_MOVES;
        this.whiteCaptures = whiteCaptures;

        this.blackMoves = MoveDirections.DIRECTIONS_BLACK_PAWN_MOVES;
        this.blackCaptures = blackCaptures;
    }

    @Override
    public Iterator<Move> iterator(Multiverse multiverse, Point4D pieceLocation) {
        Piece piece = multiverse.getLocationContents(pieceLocation);
        if (piece.color() == Color.WHITE)
            return new PawnMoveIterator(multiverse, piece, pieceLocation, this.whiteMoves, this.whiteCaptures);
        return new PawnMoveIterator(multiverse, piece, pieceLocation, this.blackMoves, this.blackCaptures);
    }

    private static class PawnMoveIterator implements Iterator<Move> {

        private final Multiverse multiverse;
        private final Piece piece;
        private final Point4D pieceLocation;
        private final List<Point4D> moveDirections;
        private final List<Point4D> captureDirections;
        private final List<Move> validMoves;

        private int moveIndex = 0;

        public PawnMoveIterator(Multiverse multiverse, Piece piece, Point4D pieceLocation, List<Point4D> moveDirections, List<Point4D> captureDirections) {
            this.multiverse = multiverse;
            this.piece = piece;
            this.pieceLocation = pieceLocation;
            this.moveDirections = moveDirections;
            this.captureDirections = captureDirections;
            this.validMoves = new ArrayList<>();
            this.constructValidMoves();
        }

        private void constructValidMoves() {

            for (Point4D moveDirection : this.moveDirections) {
                Point4D toLocation = this.pieceLocation.add(moveDirection);
                Piece toPiece = this.multiverse.getLocationContents(toLocation);

                if (toPiece == null) continue;
                if (toPiece.type() != PieceType.EMPTY) continue;

                this.validMoves.add(
                        new Move.Builder()
                                .withPieceMinimal(this.piece)
                                .withFrom(this.pieceLocation)
                                .withTo(toLocation)
                                .build()
                );

                // If the pawn hasn't moved, check 2 spaces ahead
                if (piece.moved()) continue;
                toLocation = toLocation.add(moveDirection);
                toPiece = this.multiverse.getLocationContents(toLocation);

                if (toPiece == null) continue;
                if (toPiece.type() != PieceType.EMPTY) continue;

                this.validMoves.add(
                        new Move.Builder()
                                .withPieceMinimal(this.piece)
                                .withFrom(this.pieceLocation)
                                .withTo(toLocation)
                                .build()
                );
            }

            for (Point4D captureDirection : this.captureDirections) {
                Point4D toLocation = this.pieceLocation.add(captureDirection);
                Piece toPiece = this.multiverse.getLocationContents(toLocation);

                if (toPiece == null) continue;
                
                if (this.piece.color().other() == toPiece.color())
                    this.validMoves.add(
                            new Move.Builder()
                                    .withPieceMinimal(this.piece)
                                    .withFrom(this.pieceLocation)
                                    .withTo(toLocation)
                                    .build()
                    );

                // Pesky en-passant
                if (captureDirection.l() != 0 || captureDirection.t() != 0) continue;
                Point4D potentialPawnLocation = toLocation.add(0, 0, 0, -captureDirection.y());
                Piece potentialPawn = this.multiverse.getLocationContents(potentialPawnLocation);

                if (potentialPawn == null || potentialPawn.type() != PieceType.PAWN) continue;
                Point4D potentialPastPawnLocation = potentialPawnLocation.add(0, -1, 0, captureDirection.y() * 2);
                potentialPawn = this.multiverse.getLocationContents(potentialPastPawnLocation);

                if (potentialPawn == null || potentialPawn.type() != PieceType.PAWN) continue;

                this.validMoves.add(
                        new Move.Builder()
                                .withPieceMinimal(this.piece)
                                .withFrom(this.pieceLocation)
                                .withTo(toLocation)
                                .withEnPassant(potentialPawnLocation)
                                .build()
                );
            }

        }

        @Override
        public boolean hasNext() {
            return this.moveIndex < this.validMoves.size();
        }

        @Override
        public Move next() {
            if (this.moveIndex >= this.validMoves.size()) throw new IllegalStateException();
            Move result = this.validMoves.get(this.moveIndex);
            this.moveIndex++;
            return result;
        }
    }
}
