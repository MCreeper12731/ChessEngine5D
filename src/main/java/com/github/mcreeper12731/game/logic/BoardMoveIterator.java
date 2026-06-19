package com.github.mcreeper12731.game.logic;

import com.github.mcreeper12731.game.models.Board;
import com.github.mcreeper12731.game.models.Multiverse;
import com.github.mcreeper12731.game.moves.Move;
import com.github.mcreeper12731.game.pieces.Piece;

import java.util.Collections;
import java.util.Iterator;

public class BoardMoveIterator implements Iterator<Move> {

    private final Multiverse multiverse;
    private final Board board;
    private final Iterator<Piece> pieces;

    private Iterator<Move> currentIterator;
    private boolean suppliedNoop;

    public BoardMoveIterator(Board board, Multiverse multiverse) {
        this.multiverse = multiverse;
        this.board = board;

        this.pieces = board.pieces().iterator();
        this.currentIterator = Collections.emptyIterator();
        this.suppliedNoop = false;
        this.step();
    }

    private void step() {

        if (this.currentIterator.hasNext()) return;

        while (this.pieces.hasNext()) {

            Piece nextPiece = pieces.next();

            if (nextPiece.color() != board.getPlayerTurn()) continue;

            this.currentIterator = nextPiece.getMoveIterator(this.multiverse);
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
