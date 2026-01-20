package com.github.mcreeper12731.game.presets.puzzles;

import com.github.mcreeper12731.game.models.*;
import com.github.mcreeper12731.game.pieces.PieceType;

public class RookTactics5Puzzle implements Puzzle {

    @Override
    public int moveLimit() {
        return 2;
    }

    @Override
    public Multiverse createMultiverse() {

        return new Multiverse.Builder(5)
                .withTimeline(
                        new Timeline.Builder(0)
                                .withBoard(
                                        new Board.Builder(5, 0, 0, Color.WHITE)
                                                .withPiece(Color.WHITE, PieceType.KING, 3, 0)
                                                .withPiece(Color.BLACK, PieceType.ROOK, 2, 1)
                                                .withPiece(Color.BLACK, PieceType.KING, 3, 2)
                                                .build()
                                )
                                .build()
                )
                .withTurn(
                        new Point4D(0, 0, 3, 0),
                        new Point4D(0, 0, 4, 0)
                )
                .withStartingPlayer(Color.BLACK)
                .build();
    }
}
