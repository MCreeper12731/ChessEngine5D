package com.github.mcreeper12731.game.presets.puzzles;

import com.github.mcreeper12731.game.Game;
import com.github.mcreeper12731.game.models.*;
import com.github.mcreeper12731.game.pieces.PieceType;

public class QueenTactics4Puzzle implements Puzzle {

    @Override
    public int moveLimit() {
        return 2;
    }

    @Override
    public Game createGame() {

        return new Game(new Multiverse.Builder(5)
                .withTimeline(
                        new Timeline.Builder(0)
                                .withBoard(
                                        new Board.Builder(5, 0, 0)
                                                .withWhitePiece(PieceType.KING, 0, 0)
                                                .withWhitePiece(PieceType.KNIGHT, 0, 2)
                                                .withBlackPiece(PieceType.PAWN, 4, 2)
                                                .withBlackPiece(PieceType.KING, 4, 4)
                                                .build()
                                )
                                .build()
                )
                .withTurn(
                        new Point4D(0, 0, 0, 2),
                        new Point4D(0, 0, 1, 4)
                )
                .withTurn(
                        new Point4D(0, 1, 4, 2),
                        new Point4D(0, 1, 4, 0)
                )
                .withTurn(
                        new Point4D(0, 2, 0, 0),
                        new Point4D(0, 2, 1, 1)
                )
                .build());
    }
}
