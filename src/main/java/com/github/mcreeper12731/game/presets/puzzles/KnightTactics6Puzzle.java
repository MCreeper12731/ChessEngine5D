package com.github.mcreeper12731.game.presets.puzzles;

import com.github.mcreeper12731.game.Game;
import com.github.mcreeper12731.game.models.Board;
import com.github.mcreeper12731.game.models.Multiverse;
import com.github.mcreeper12731.game.models.Timeline;
import com.github.mcreeper12731.game.models.pieces.PieceType;

public class KnightTactics6Puzzle implements Puzzle {

    @Override
    public int moveLimit() {
        return 3;
    }

    @Override
    public Game createGame() {

        return new Game(new Multiverse.Builder(5)
                .withTimeline(
                        new Timeline.Builder(0)
                                .withBoard(
                                        new Board.Builder(5, 0, 0)
                                                .withWhitePiece(PieceType.KING, 0, 0)
                                                .withWhitePiece(PieceType.KNIGHT, 2, 0)
                                                .withBlackPiece(PieceType.KING, 4, 4)
                                                .build()
                                )
                                .build()
                )
                .build());
    }
}
