package com.github.mcreeper12731.game.movegeneration.iterators;

import com.github.mcreeper12731.game.models.Board;
import com.github.mcreeper12731.game.models.Multiverse;
import com.github.mcreeper12731.game.models.Move;
import com.github.mcreeper12731.game.models.Point4D;
import com.github.mcreeper12731.game.models.pieces.Piece;

import java.util.Collections;
import java.util.Iterator;

public class BoardMoveIterator implements Iterator<Move> {

    private final Multiverse multiverse;
    private final Board board;

    private int index;
    private Iterator<Move> currentIterator;
    private boolean suppliedNoop;

    public BoardMoveIterator(Board board, Multiverse multiverse) {
        this.multiverse = multiverse;
        this.board = board;

        this.index = -1;
        this.currentIterator = Collections.emptyIterator();
        this.suppliedNoop = false;
        this.step();
    }

    private void step() {

        if (this.currentIterator.hasNext()) return;

        while (this.index < this.board.size() * this.board.size()) {

            this.index++;

            int x = this.index % this.board.size();
            int y = this.index / this.board.size();

            Piece nextPiece = this.board.getLocationContents(x, y);
            if (nextPiece == null) continue;

            if (nextPiece.color() != board.getPlayerTurn()) continue;

            this.currentIterator = nextPiece.getMoveIterator(this.multiverse, new Point4D(this.board.l(), this.board.t(), x, y));
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
