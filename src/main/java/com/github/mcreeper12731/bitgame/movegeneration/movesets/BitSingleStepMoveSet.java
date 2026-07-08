package com.github.mcreeper12731.bitgame.movegeneration.movesets;

import com.github.mcreeper12731.bitgame.BitGame;
import com.github.mcreeper12731.bitgame.models.BitMultiverse;
import com.github.mcreeper12731.bitgame.models.pieces.BitPiece;
import com.github.mcreeper12731.game.models.Move;
import com.github.mcreeper12731.game.models.Point4D;
import com.github.mcreeper12731.game.models.pieces.PieceType;

import java.util.Iterator;
import java.util.List;

public class BitSingleStepMoveSet implements BitMoveSet {

    private final List<Point4D> directions;

    public BitSingleStepMoveSet(List<Point4D> directions) {
        this.directions = directions;
    }

    @Override
    public Iterator<Move> iterator(BitGame game, Point4D pieceLocation) {
        return new SingleStepMoveIterator(game.getMultiverse(), pieceLocation, this.directions);
    }

    private static class SingleStepMoveIterator implements Iterator<Move> {

        private final BitMultiverse multiverse;
        private final byte piece;
        private final Point4D pieceLocation;
        private final List<Point4D> directions;

        private int directionIndex = 0;
        private Move nextMove;

        SingleStepMoveIterator(BitMultiverse multiverse, Point4D pieceLocation, List<Point4D> directions) {
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
                byte toPiece = this.multiverse.getLocationContents(toLocation);

                if (toPiece == -1) {
                    this.directionIndex++;
                    continue;
                }

                if (toPiece != 0 && BitPiece.colorOrdinal(this.piece) == BitPiece.colorOrdinal(toPiece)) {
                    this.directionIndex++;
                    continue;
                }

                this.nextMove = Move.of(this.piece, this.pieceLocation, toLocation);

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
