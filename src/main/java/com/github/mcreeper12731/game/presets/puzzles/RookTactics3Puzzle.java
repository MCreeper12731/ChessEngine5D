package com.github.mcreeper12731.game.presets.puzzles;

import com.github.mcreeper12731.game.Game;
import com.github.mcreeper12731.game.models.*;
import com.github.mcreeper12731.game.models.Move;
import com.github.mcreeper12731.game.models.pieces.PieceType;

import java.util.List;

public class RookTactics3Puzzle implements Puzzle {

    @Override
    public int moveLimit() {
        return 1;
    }

    @Override
    public Game createGame() {

        Game game = new Game(new Multiverse.Builder(5)
                .withTimeline(
                        new Timeline.Builder(0)
                                .withBoard(
                                        new Board.Builder(5, 0, 0)
                                                .withPiece(Color.WHITE, PieceType.KING, 0, 0)
                                                .withPiece(Color.WHITE, PieceType.ROOK, 1, 0)
                                                .withPiece(Color.BLACK, PieceType.PAWN, 2, 4)
                                                .withPiece(Color.BLACK, PieceType.KING, 4, 4)
                                                .build()
                                )
                                .build()
                )
                .build());

        game.applyMovesAndFinalizeTurn(List.of(new Move.Builder(game.getMultiverse())
                .withFrom(0, 0, 1, 0)
                .withTo(0, 0, 3, 0)
                .build()
        ));

        game.applyMovesAndFinalizeTurn(List.of(new Move.Builder(game.getMultiverse())
                .withFrom(0, 1, 4, 4)
                .withTo(0, 1, 4, 3)
                .build()
        ));

        game.applyMovesAndFinalizeTurn(List.of(new Move.Builder(game.getMultiverse())
                .withFrom(0, 2, 3, 0)
                .withTo(0, 2, 4, 0)
                .build()
        ));

        game.applyMovesAndFinalizeTurn(List.of(new Move.Builder(game.getMultiverse())
                .withFrom(0, 3, 4, 3)
                .withTo(0, 3, 3, 4)
                .build()
        ));

        game.applyMovesAndFinalizeTurn(List.of(new Move.Builder(game.getMultiverse())
                .withFrom(0, 4, 4, 0)
                .withTo(0, 2, 4, 0)
                .build()
        ));

        game.applyMovesAndFinalizeTurn(List.of(new Move.Builder(game.getMultiverse())
                .withFrom(1, 3, 4, 3)
                .withTo(0, 5, 4, 3)
                .build()
        ));

        return game;

    }
}
