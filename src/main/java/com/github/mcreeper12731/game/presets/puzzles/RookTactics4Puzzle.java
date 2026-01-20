package com.github.mcreeper12731.game.presets.puzzles;

import com.github.mcreeper12731.game.models.*;
import com.github.mcreeper12731.game.models.Color;
import com.github.mcreeper12731.game.pieces.PieceType;

public class RookTactics4Puzzle implements Puzzle {

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
                                                .withPiece(Color.WHITE, PieceType.PAWN, 0, 0)
                                                .withPiece(Color.WHITE, PieceType.KING, 3, 0)
                                                .withPiece(Color.BLACK, PieceType.ROOK, 0, 3)
                                                .withPiece(Color.BLACK, PieceType.KING, 3, 3)
                                                .build()
                                )
                                .build()
                )
                .withTurn(
                        new Point4D(0, 0, 0, 0),
                        new Point4D(0, 0, 0, 1)
                )
                .withTurn(
                        new Point4D(0, 1, 0, 3),
                        new Point4D(0, 1, 2, 3)
                )
                .withTurn(
                        new Point4D(0, 2, 3, 0),
                        new Point4D(0, 0, 2, 1)
                )
                .withTurn(
                        new Point4D(1, 1, 0, 3),
                        new Point4D(1, 1, 0, 1)
                )
                .withTurn(
                        new Point4D(1, 2, 2, 1),
                        new Point4D(1, 2, 1, 2)
                )
                .build();
    }
}
