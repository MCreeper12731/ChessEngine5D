package com.github.mcreeper12731.game.presets.puzzles;

import com.github.mcreeper12731.game.Game;
import com.github.mcreeper12731.game.models.*;
import com.github.mcreeper12731.game.models.Color;
import com.github.mcreeper12731.game.models.pieces.PieceType;

import java.util.List;

public class RookTactics2Puzzle implements Puzzle {

    @Override
    public int moveLimit() {
        return 2;
    }

    @Override
    public Game createGame() {

        Game game = new Game(new Multiverse.Builder(6)
                .withTimeline(
                        new Timeline.Builder(0)
                                .withBoard(
                                        new Board.Builder(6, 0, 0)
                                                .withPiece(Color.WHITE, PieceType.KING, 0, 0)
                                                .withPiece(Color.WHITE, PieceType.ROOK, 1, 0)
                                                .withPiece(Color.WHITE, PieceType.ROOK, 1, 1)
                                                .withPiece(Color.BLACK, PieceType.KING, 5, 4)
                                                .build()
                                )
                                .build()
                ).build()
        );
        game.applyMovesAndFinalizeTurn(List.of(
                new Move.Builder(game)
                        .withFrom(new Point4D(0, 0, 1, 1))
                        .withTo(new Point4D(0, 0, 4, 1))
                        .build()
        ));
        game.applyMovesAndFinalizeTurn(List.of(
                new Move.Builder(game)
                        .withFrom(new Point4D(0, 1, 5, 4))
                        .withTo(new Point4D(0, 1, 5, 3))
                        .build()
        ));
        game.clearTurnHistory();
        return game;
    }
}
