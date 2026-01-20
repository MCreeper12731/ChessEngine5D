package com.github.mcreeper12731.game.presets.puzzles;

import com.github.mcreeper12731.game.models.*;
import com.github.mcreeper12731.game.moves.Move;
import com.github.mcreeper12731.game.moves.MoveFactory;
import com.github.mcreeper12731.game.pieces.PieceType;

public class QueenTactics1Puzzle implements Puzzle {

    @Override
    public int moveLimit() {
        return 1;
    }

    @Override
    public Multiverse createMultiverse() {

        return new Multiverse.Builder(4)
                .withTimeline(
                        new Timeline.Builder(0)
                                .withBoard(
                                        new Board.Builder(4, 0, 0, Color.WHITE)
                                                .withPiece(Color.WHITE, PieceType.KING, 0, 0)
                                                .withPiece(Color.WHITE, PieceType.QUEEN, 2, 0)
                                                .withPiece(Color.BLACK, PieceType.KING, 3, 3)
                                                .build()
                                )
                                .build()
                )
                .withTurn(
                        new Point4D(0, 0, 0, 0),
                        new Point4D(0, 0, 1, 0)
                )
                .withTurn(
                        new Point4D(0, 1, 3, 3),
                        new Point4D(0, 1, 3, 2)
                )
                .build();
    }
}
