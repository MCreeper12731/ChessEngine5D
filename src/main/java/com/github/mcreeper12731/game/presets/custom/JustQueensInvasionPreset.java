package com.github.mcreeper12731.game.presets.custom;

import com.github.mcreeper12731.game.Game;
import com.github.mcreeper12731.game.models.Board;
import com.github.mcreeper12731.game.models.Color;
import com.github.mcreeper12731.game.models.Multiverse;
import com.github.mcreeper12731.game.models.Timeline;
import com.github.mcreeper12731.game.pieces.PieceType;
import com.github.mcreeper12731.game.presets.GamePreset;

public class JustQueensInvasionPreset implements GamePreset {

    @Override
    public Game createGame() {

        return new Game(new Multiverse.Builder(6)
                .withTimeline(
                        new Timeline.Builder(-1)
                                .withBoard(
                                        new Board.Builder(6, -1, 0)
                                                .withPiece(Color.BLACK, PieceType.QUEEN, 1, 5)
                                                .withPiece(Color.BLACK, PieceType.KING, 3, 5)
                                                .build()
                                )
                                .build()
                )
                .withTimeline(
                        new Timeline.Builder(0)
                                .withBoard(
                                        new Board.Builder(6, 0, 0)
                                                .withPiece(Color.WHITE, PieceType.KING, 2, 0)
                                                .withPiece(Color.WHITE, PieceType.QUEEN, 4, 0)
                                                .build()
                                )
                                .build()
                )
                .even()
                .build());

    }
}
