package com.github.mcreeper12731.game.presets.puzzles;

import com.github.mcreeper12731.game.Game;
import com.github.mcreeper12731.game.models.Board;
import com.github.mcreeper12731.game.models.Multiverse;
import com.github.mcreeper12731.game.models.Timeline;
import com.github.mcreeper12731.game.pieces.PieceType;

public class QueenTactics3Puzzle implements Puzzle {

    @Override
    public int moveLimit() {
        return 3;
    }

    @Override
    public Game createGame() {

        return new Game(new Multiverse.Builder(4)
                .withTimeline(
                        new Timeline.Builder(0)
                                .withBoard(
                                        new Board.Builder(4, 0, 0)
                                                .withWhitePiece(PieceType.KING, 0, 0)
                                                .withWhitePiece(PieceType.PAWN, 1, 0)
                                                .withWhitePiece(PieceType.PAWN, 2, 0)
                                                .withWhitePiece(PieceType.PAWN, 3, 0)
                                                .withBlackPiece(PieceType.KING, 0, 3)
                                                .build()
                                )
                                .build()
                )
                .build());
    }
}
