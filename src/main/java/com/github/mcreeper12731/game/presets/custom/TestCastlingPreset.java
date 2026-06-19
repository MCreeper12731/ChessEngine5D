package com.github.mcreeper12731.game.presets.custom;

import com.github.mcreeper12731.game.logic.Game;
import com.github.mcreeper12731.game.models.Board;
import com.github.mcreeper12731.game.models.Color;
import com.github.mcreeper12731.game.models.Multiverse;
import com.github.mcreeper12731.game.models.Timeline;
import com.github.mcreeper12731.game.pieces.PieceType;
import com.github.mcreeper12731.game.presets.GamePreset;

public class TestCastlingPreset implements GamePreset {

    @Override
    public Game createGame() {

        return new Game(new Multiverse.Builder(8)
                .withTimeline(
                        new Timeline.Builder(0)
                                .withBoard(
                                        new Board.Builder(8, 0, 0)
                                                .withWhitePiece(PieceType.KING, 4, 0)
                                                .withWhitePiece(PieceType.ROOK, 0, 0)
                                                .withWhitePiece(PieceType.ROOK, 7, 0)
                                                .withBlackPiece(PieceType.KING, 3, 7)
                                                .build()
                                )
                                .build()
                )
                .build());
    }
}
