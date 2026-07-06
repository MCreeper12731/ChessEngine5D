package com.github.mcreeper12731.game.movegeneration.movesets;

import com.github.mcreeper12731.game.models.Multiverse;
import com.github.mcreeper12731.game.models.Point4D;
import com.github.mcreeper12731.game.models.Move;
import com.github.mcreeper12731.game.models.pieces.Piece;
import com.github.mcreeper12731.game.models.pieces.PieceType;

import java.util.Iterator;
import java.util.List;

public class SingleStepMoveSet implements MoveSet {

    private final List<Point4D> directions;

    public SingleStepMoveSet(List<Point4D> directions) {
        this.directions = directions;
    }

    @Override
    public Iterator<Move> iterator(Multiverse multiverse, Point4D pieceLocation) {
        return new SingleStepMoveIterator(multiverse, pieceLocation, this.directions);
    }

    private static class SingleStepMoveIterator implements Iterator<Move> {

        private final Multiverse multiverse;
        private final Piece piece;
        private final Point4D pieceLocation;
        private final List<Point4D> directions;

        private int directionIndex = 0;
        private Move nextMove;

        SingleStepMoveIterator(Multiverse multiverse, Point4D pieceLocation, List<Point4D> directions) {
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
                Point4D toLocation = this.pieceLocation.add(direction);
                Piece toPiece = this.multiverse.getLocationContents(toLocation);

                if (toPiece == null) {
                    this.directionIndex++;
                    continue;
                }

                if (toPiece.type() != PieceType.EMPTY && this.piece.color() == toPiece.color()) {
                    this.directionIndex++;
                    continue;
                }

                this.nextMove = new Move.Builder()
                        .withPieceMinimal(this.piece)
                        .withFrom(this.pieceLocation)
                        .withTo(toLocation)
                        .build();

                this.directionIndex++;
                return;
            }
        }

        @Override
        public boolean hasNext() {
            return this.nextMove != null;
        }

        @Override
        public Move next() {
            if (this.nextMove == null) throw new IllegalStateException();
            Move result = this.nextMove;
            this.step();
            return result;
        }

    }
}
