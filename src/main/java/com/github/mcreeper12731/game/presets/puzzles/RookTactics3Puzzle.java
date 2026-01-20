package com.github.mcreeper12731.game.presets.puzzles;

import com.github.mcreeper12731.game.models.*;
import com.github.mcreeper12731.game.pieces.PieceType;

public class RookTactics3Puzzle implements Puzzle {

    @Override
    public int moveLimit() {
        return 1;
    }

    @Override
    public Multiverse createMultiverse() {

        return new Multiverse.Builder(5)
                .withTimeline(
                        new Timeline.Builder(0)
                                .withBoard(
                                        new Board.Builder(5, 0, 0, Color.WHITE)
                                                .withPiece(Color.WHITE, PieceType.KING, 0, 0)
                                                .withPiece(Color.WHITE, PieceType.ROOK, 1, 0)
                                                .withPiece(Color.BLACK, PieceType.PAWN, 2, 4)
                                                .withPiece(Color.BLACK, PieceType.KING, 4, 4)
                                                .build()
                                )
                                .build()
                )
                .withTurn(
                        new Point4D(0, 0, 1, 0),
                        new Point4D(0, 0, 3, 0)
                )
                .withTurn(
                        new Point4D(0, 1, 4, 4),
                        new Point4D(0, 1, 4, 3)
                )
                .withTurn(
                        new Point4D(0, 2, 3, 0),
                        new Point4D(0, 2, 4, 0)
                )
                .withTurn(
                        new Point4D(0, 3, 4, 3),
                        new Point4D(0, 3, 3, 4)
                )
                .withTurn(
                        new Point4D(0, 4, 4, 0),
                        new Point4D(0, 2, 4, 0)
                )
                .withTurn(
                        new Point4D(1, 3, 4, 3),
                        new Point4D(0, 5, 4, 3)
                )
                .build();

    }
}
