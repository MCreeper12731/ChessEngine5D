package com.github.mcreeper12731.game.presets.puzzles;

import com.github.mcreeper12731.game.Game;
import com.github.mcreeper12731.game.models.*;
import com.github.mcreeper12731.game.models.pieces.PieceType;

import java.util.List;

public class RookTactics5Puzzle implements Puzzle {

    @Override
    public int moveLimit() {
        return 2;
    }

    @Override
    public Game createGame() {

        Game game = new Game(new Multiverse.Builder(5)
                .withTimeline(
                        new Timeline.Builder(0)
                                .withBoard(
                                        new Board.Builder(5, 0, 0)
                                                .withPiece(Color.WHITE, PieceType.KING, 3, 0)
                                                .withPiece(Color.BLACK, PieceType.ROOK, 2, 1)
                                                .withPiece(Color.BLACK, PieceType.KING, 3, 2)
                                                .build()
                                )
                                .build()
                ).build()
        );

        game.applyMovesAndFinalizeTurn(List.of(
                new Move.Builder(game)
                        .withFrom(new Point4D(0, 0, 3, 0))
                        .withTo(new Point4D(0, 0, 4, 0))
                        .build()
        ));

        return game;
    }
}
