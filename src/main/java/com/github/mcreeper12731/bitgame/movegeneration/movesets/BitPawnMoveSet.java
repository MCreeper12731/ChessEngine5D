package com.github.mcreeper12731.bitgame.movegeneration.movesets;

import com.github.mcreeper12731.bitgame.BitGame;
import com.github.mcreeper12731.bitgame.models.BitMultiverse;
import com.github.mcreeper12731.bitgame.models.pieces.BitPiece;
import com.github.mcreeper12731.game.models.Move;
import com.github.mcreeper12731.game.models.Point4D;
import com.github.mcreeper12731.game.models.pieces.PieceType;
import com.github.mcreeper12731.game.movegeneration.movesets.MoveDirections;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class BitPawnMoveSet implements BitMoveSet {

    private final List<Point4D> whiteMoves;
    private final List<Point4D> whiteCaptures;

    private final List<Point4D> blackMoves;
    private final List<Point4D> blackCaptures;

    public BitPawnMoveSet() {
        this.whiteMoves = MoveDirections.DIRECTIONS_WHITE_PAWN_MOVES;
        this.whiteCaptures = MoveDirections.DIRECTIONS_WHITE_PAWN_CAPTURES;

        this.blackMoves = MoveDirections.DIRECTIONS_BLACK_PAWN_MOVES;
        this.blackCaptures = MoveDirections.DIRECTIONS_BLACK_PAWN_CAPTURES;
    }

    public BitPawnMoveSet(List<Point4D> whiteCaptures, List<Point4D> blackCaptures) {
        this.whiteMoves = MoveDirections.DIRECTIONS_WHITE_PAWN_MOVES;
        this.whiteCaptures = whiteCaptures;

        this.blackMoves = MoveDirections.DIRECTIONS_BLACK_PAWN_MOVES;
        this.blackCaptures = blackCaptures;
    }

    @Override
    public Iterator<Move> iterator(BitGame game, Point4D pieceLocation) {
        BitMultiverse multiverse = game.getMultiverse();
        byte piece = multiverse.getLocationContents(pieceLocation);
        if (BitPiece.colorOrdinal(piece) == BitGame.WHITE)
            return new PawnMoveIterator(multiverse, piece, pieceLocation, this.whiteMoves, this.whiteCaptures);
        return new PawnMoveIterator(multiverse, piece, pieceLocation, this.blackMoves, this.blackCaptures);
    }

    private static class PawnMoveIterator implements Iterator<Move> {

        private final BitMultiverse multiverse;
        private final byte piece;
        private final Point4D pieceLocation;
        private final List<Point4D> moveDirections;
        private final List<Point4D> captureDirections;
        private final List<Move> validMoves;

        private int moveIndex = 0;

        public PawnMoveIterator(BitMultiverse multiverse, byte piece, Point4D pieceLocation, List<Point4D> moveDirections, List<Point4D> captureDirections) {
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
                byte toPiece = this.multiverse.getLocationContents(toLocation);

                if (toPiece == -1) continue;
                if (toPiece != 0) continue;

                this.validMoves.add(Move.of(this.piece, this.pieceLocation, toLocation));

                // If the pawn hasn't moved, check 2 spaces ahead
                if (BitPiece.hasMoved(this.piece)) continue;
                toLocation = toLocation.add(moveDirection);
                toPiece = this.multiverse.getLocationContents(toLocation);

                if (toPiece != 0) continue;

                this.validMoves.add(Move.of(this.piece, this.pieceLocation, toLocation));
            }

            for (Point4D captureDirection : this.captureDirections) {
                Point4D toLocation = this.pieceLocation.add(captureDirection);
                byte toPiece = this.multiverse.getLocationContents(toLocation);

                if (toPiece <= 0) continue;

                if (1 - BitPiece.colorOrdinal(this.piece) == BitPiece.colorOrdinal(toPiece))
                    this.validMoves.add(Move.of(this.piece, this.pieceLocation, toLocation));

                // Pesky en-passant
                if (captureDirection.l() != 0 || captureDirection.t() != 0) continue;
                Point4D potentialPawnLocation = toLocation.add(0, 0, 0, -captureDirection.y());
                byte potentialPawn = this.multiverse.getLocationContents(potentialPawnLocation);

                if (potentialPawn == -1 || BitPiece.typeOrdinal(potentialPawn) != PieceType.PAWN.ordinal()) continue;
                Point4D potentialPastPawnLocation = potentialPawnLocation.add(0, -1, 0, captureDirection.y() * 2);
                potentialPawn = this.multiverse.getLocationContents(potentialPastPawnLocation);

                if (potentialPawn == -1 || BitPiece.typeOrdinal(potentialPawn) != PieceType.PAWN.ordinal()) continue;

                this.validMoves.add(Move.of(this.piece, this.pieceLocation, toLocation, potentialPawnLocation));
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
