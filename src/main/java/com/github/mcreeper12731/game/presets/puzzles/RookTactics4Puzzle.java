package com.github.mcreeper12731.game.presets.puzzles;

import com.github.mcreeper12731.game.logic.Game;
import com.github.mcreeper12731.game.models.*;
import com.github.mcreeper12731.game.models.Color;
import com.github.mcreeper12731.game.pieces.PieceType;

import java.util.List;

public class RookTactics4Puzzle implements Puzzle {

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
                                                .withPiece(Color.WHITE, PieceType.PAWN, 0, 0)
                                                .withPiece(Color.WHITE, PieceType.KING, 3, 0)
                                                .withPiece(Color.BLACK, PieceType.ROOK, 0, 3)
                                                .withPiece(Color.BLACK, PieceType.KING, 3, 3)
                                                .build()
                                )
                                .build()
                ).build()
        );

        game.applyMovesAndFinalizeTurn(List.of(
                new Move.Builder(game)
                        .withFrom(new Point4D(0, 0, 0, 0))
                        .withTo(new Point4D(0, 0, 0, 1))
                        .build()
                )
        );
        game.applyMovesAndFinalizeTurn(List.of(
                new Move.Builder(game)
                        .withFrom(new Point4D(0, 1, 0, 3))
                        .withTo(new Point4D(0, 1, 2, 3))
                        .build()
                )
        );
        game.applyMovesAndFinalizeTurn(List.of(
                new Move.Builder(game)
                        .withFrom(new Point4D(0, 2, 3, 0))
                        .withTo(new Point4D(0, 0, 2, 1))
                        .build()
                )
        );
        game.applyMovesAndFinalizeTurn(List.of(
                new Move.Builder(game)
                        .withFrom(new Point4D(1, 1, 0, 3))
                        .withTo(new Point4D(1, 1, 0, 1))
                        .build()
                )
        );
        game.applyMovesAndFinalizeTurn(List.of(
                new Move.Builder(game)
                        .withFrom(new Point4D(1, 2, 2, 1))
                        .withTo(new Point4D(1, 2, 1, 2))
                        .build()
                )
        );

        game.clearTurnHistory();

        return game;
    }
}
