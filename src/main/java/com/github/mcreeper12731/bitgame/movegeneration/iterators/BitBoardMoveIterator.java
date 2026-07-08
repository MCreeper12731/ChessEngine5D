package com.github.mcreeper12731.bitgame.movegeneration.iterators;

import com.github.mcreeper12731.bitgame.BitGame;
import com.github.mcreeper12731.bitgame.models.BitBoard;
import com.github.mcreeper12731.bitgame.models.pieces.BitPiece;
import com.github.mcreeper12731.game.models.Move;
import com.github.mcreeper12731.game.models.Point4D;

import java.util.Collections;
import java.util.Iterator;

public class BitBoardMoveIterator implements Iterator<Move>  {

    private final BitGame game;
    private final BitBoard board;

    private int index;
    private Iterator<Move> currentIterator;
    private boolean suppliedNoop;

    public BitBoardMoveIterator(BitBoard board, BitGame game) {
        this.game = game;
        this.board = board;

        this.index = -1;
        this.currentIterator = Collections.emptyIterator();
        this.suppliedNoop = false;
        this.step();
    }

    private void step() {

        if (this.currentIterator.hasNext()) return;

        while (this.index + 1 < this.board.size() * this.board.size()) {

            this.index++;

            int x = this.index % this.board.size();
            int y = this.index / this.board.size();

            byte nextPiece = this.board.getLocationContents(x, y);
            if (nextPiece == -1) continue;

            if (BitPiece.colorOrdinal(nextPiece) != board.getPlayerTurn().ordinal()) continue;

            this.currentIterator = BitPiece.getMoveIterator(this.game, new Point4D(this.board.l(), this.board.t(), x, y));
            if (!this.currentIterator.hasNext()) continue;
            return;
        }
    }

    @Override
    public boolean hasNext() {
        return this.currentIterator.hasNext();
    }

    @Override
    public Move next() {
        if (!this.suppliedNoop) {
            this.suppliedNoop = true;
            return new Move.Builder()
                    .withNoop()
                    .build();
        }

        if (!this.currentIterator.hasNext()) throw new IllegalStateException();

        Move move = this.currentIterator.next();
        this.step();
        return move;
    }
}
