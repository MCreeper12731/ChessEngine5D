package com.github.mcreeper12731.game.presets.puzzles;

import com.github.mcreeper12731.game.models.*;
import com.github.mcreeper12731.game.pieces.PieceType;

public class BackrankBasics4Puzzle implements Puzzle {

    @Override
    public int moveLimit() {
        return 3;
    }

    @Override
    public Multiverse createMultiverse() {
        return new Multiverse.Builder(5)
                .withTimeline(
                        new Timeline.Builder(0)
                                .withBoard(
                                        new Board.Builder(5, 0, 0, Color.WHITE)
                                                .withWhitePiece(PieceType.KING, 0, 0)
                                                .withWhitePiece(PieceType.ROOK, 3, 0)
                                                .withBlackPiece(PieceType.PAWN, 2, 3)
                                                .withBlackPiece(PieceType.PAWN, 3, 3)
                                                .withBlackPiece(PieceType.PAWN, 4, 3)
                                                .withBlackPiece(PieceType.KING, 4, 4)
                                                .build()
                                )
                                .build()
                )
                .withTurn(
                        new Point4D(0, 0, 3, 0),
                        new Point4D(0, 0, 1, 0)
                )
                .withTurn(
                        new Point4D(0, 1, 3, 3),
                        new Point4D(0, 1, 3, 2)
                )
                .build();
    }
}
