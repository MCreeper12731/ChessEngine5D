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

    private int x;
    private int y;
    private Iterator<Move> currentIterator;
    private boolean suppliedNoop;

    public BitBoardMoveIterator(BitBoard board, BitGame game) {
        this.game = game;
        this.board = board;

        this.x = -1;
        this.y = 0;
        this.currentIterator = Collections.emptyIterator();
        this.suppliedNoop = false;
        this.step();
    }

    private void step() {

        if (this.currentIterator.hasNext()) return;

        while (true) {

            this.x++;
            if (this.x >= this.board.size()) {
                this.x = 0;
                this.y++;
                if (this.y >= this.board.size()) break;
            }

            byte nextPiece = this.board.getLocationContents(this.x, this.y);
            if (nextPiece == -1) continue;

            if (BitPiece.colorOrdinal(nextPiece) != board.getPlayerTurn().ordinal()) continue;

            this.currentIterator = BitPiece.getMoveIterator(this.game, new Point4D(this.board.l(), this.board.t(), this.x, this.y));
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
