package com.github.mcreeper12731.game.pieces.movesets;

import com.github.mcreeper12731.game.models.Multiverse;
import com.github.mcreeper12731.game.models.Point4D;
import com.github.mcreeper12731.game.models.Move;
import com.github.mcreeper12731.game.pieces.Piece;
import com.github.mcreeper12731.game.pieces.PieceType;

import java.util.Iterator;
import java.util.List;

public class SlidingMoveSet implements MoveSet {

    private final List<Point4D> directions;

    public SlidingMoveSet(List<Point4D> directions) {
        this.directions = directions;
    }

    @Override
    public Iterator<Move> iterator(Multiverse multiverse, Piece piece) {
        return new SlidingMoveIterator(multiverse, piece, this.directions);
    }

    private static class SlidingMoveIterator implements Iterator<Move> {

        private final Multiverse multiverse;
        private final Piece piece;
        private final List<Point4D> directions;

        private int directionIndex = 0;
        private int length = 1;
        private Move nextMove;

        SlidingMoveIterator(Multiverse multiverse, Piece piece, List<Point4D> directions) {
            this.multiverse = multiverse;
            this.piece = piece;
            this.directions = directions;
            this.step();
        }

        private void step() {

            this.nextMove = null;
            while (this.directionIndex < this.directions.size()) {
                Point4D direction = this.directions.get(this.directionIndex);
                Point4D toLocation = this.piece.location().add(direction.multiply(this.length));
                Piece toPiece = this.multiverse.getLocationContents(toLocation);

                if (toPiece == null) {
                    this.nextDirection();
                    continue;
                }

                if (toPiece.type() != PieceType.EMPTY && this.piece.color() == toPiece.color()) {
                    this.nextDirection();
                    continue;
                }

                this.nextMove = new Move.Builder()
                        .withPiece(this.piece)
                        .withTo(toLocation)
                        .build();
                if (toPiece.type() != PieceType.EMPTY) {
                    this.nextDirection();
                } else {
                    this.length++;
                }
                return;
            }
        }

        private void nextDirection() {
            this.directionIndex++;
            this.length = 1;
        }

        @Override
        public boolean hasNext() {
            return nextMove != null;
        }

        @Override
        public Move next() {
            if (this.nextMove == null) throw new IllegalStateException();
            Move result = this.nextMove;
            step();
            return result;
        }
    }
}
