package com.github.mcreeper12731.game.movegeneration.movesets;

import com.github.mcreeper12731.game.models.Multiverse;
import com.github.mcreeper12731.game.models.Point4D;
import com.github.mcreeper12731.game.models.Move;
import com.github.mcreeper12731.game.models.pieces.Piece;
import com.github.mcreeper12731.game.models.pieces.PieceType;

import java.util.Iterator;
import java.util.List;

public class SlidingMoveSet implements MoveSet {

    private final List<Point4D> directions;

    public SlidingMoveSet(List<Point4D> directions) {
        this.directions = directions;
    }

    @Override
    public Iterator<Move> iterator(Multiverse multiverse, Point4D pieceLocation) {
        return new SlidingMoveIterator(multiverse, pieceLocation, this.directions);
    }

    private static class SlidingMoveIterator implements Iterator<Move> {

        private final Multiverse multiverse;
        private final Piece piece;
        private final Point4D pieceLocation;
        private final List<Point4D> directions;

        private int directionIndex = 0;
        private int length = 1;
        private Move nextMove;

        SlidingMoveIterator(Multiverse multiverse, Point4D pieceLocation, List<Point4D> directions) {
            this.multiverse = multiverse;
            this.piece = multiverse.getLocationContents(pieceLocation);
            this.pieceLocation = pieceLocation;
            this.directions = directions;
            this.step();
        }

        private void step() {

            this.nextMove = null;
            while (this.directionIndex < this.directions.size()) {
                Point4D direction = this.directions.get(this.directionIndex);
                Point4D toLocation = this.pieceLocation.add(direction.multiply(this.length));
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
                        .withPieceMinimal(this.piece)
                        .withFrom(this.pieceLocation)
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
