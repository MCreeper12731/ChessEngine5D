package com.github.mcreeper12731.game.presets.puzzles;

import com.github.mcreeper12731.game.logic.Game;
import com.github.mcreeper12731.game.models.*;
import com.github.mcreeper12731.game.moves.Move;
import com.github.mcreeper12731.game.pieces.PieceType;

import java.util.List;

public class BackrankBasics4Puzzle implements Puzzle {

    @Override
    public int moveLimit() {
        return 3;
    }

    @Override
    public Game createGame() {
        Multiverse multiverse = new Multiverse.Builder(5)
                .withTimeline(
                        new Timeline.Builder(0)
                                .withBoard(
                                        new Board.Builder(5, 0, 0)
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
                .build();

        Game game = new Game(multiverse);

        game.applyMovesAndFinalizeTurn(List.of(new Move.Builder(multiverse)
                .withFrom(new Point4D(0, 0, 3, 0))
                .withTo(new Point4D(0, 0, 1, 0))
                .build()
        ));

        game.applyMovesAndFinalizeTurn(List.of(new Move.Builder(multiverse)
                .withFrom(new Point4D(0, 1, 3, 3))
                .withTo(new Point4D(0, 1, 3, 2))
                .build()
        ));

        game.clearTurnHistory();

        return game;
    }
}
