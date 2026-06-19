package com.github.mcreeper12731.game.presets.puzzles;

import com.github.mcreeper12731.game.logic.Game;
import com.github.mcreeper12731.game.models.*;
import com.github.mcreeper12731.game.models.Move;
import com.github.mcreeper12731.game.pieces.PieceType;

import java.util.List;

public class QueenTactics1Puzzle implements Puzzle {

    @Override
    public int moveLimit() {
        return 1;
    }

    @Override
    public Game createGame() {

        Game game = new Game(new Multiverse.Builder(4)
                .withTimeline(
                        new Timeline.Builder(0)
                                .withBoard(
                                        new Board.Builder(4, 0, 0)
                                                .withPiece(Color.WHITE, PieceType.KING, 0, 0)
                                                .withPiece(Color.WHITE, PieceType.QUEEN, 2, 0)
                                                .withPiece(Color.BLACK, PieceType.KING, 3, 3)
                                                .build()
                                )
                                .build()
                ).build()
        );

        game.applyMovesAndFinalizeTurn(List.of(
                new Move.Builder(game.getMultiverse())
                        .withFrom(0, 0, 0, 0)
                        .withTo(0, 0, 1, 0)
                        .build()
        ));

        game.applyMovesAndFinalizeTurn(List.of(
                new Move.Builder(game.getMultiverse())
                        .withFrom(0, 1, 3, 3)
                        .withTo(0, 1, 3, 2)
                        .build()
        ));

        game.clearTurnHistory();

        return game;
    }
}
