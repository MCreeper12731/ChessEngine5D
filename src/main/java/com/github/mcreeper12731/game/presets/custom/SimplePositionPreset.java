package com.github.mcreeper12731.game.presets.custom;

import com.github.mcreeper12731.game.Game;
import com.github.mcreeper12731.game.models.Board;
import com.github.mcreeper12731.game.models.Multiverse;
import com.github.mcreeper12731.game.models.Timeline;
import com.github.mcreeper12731.game.pieces.PieceType;
import com.github.mcreeper12731.game.presets.GamePreset;

public class SimplePositionPreset implements GamePreset {

    @Override
    public Game createGame() {
        return new Game(
                new Multiverse.Builder(4)
                        .withTimeline(
                                new Timeline.Builder(0)
                                        .withBoard(
                                                new Board.Builder(4, 0, 0)
                                                        .withWhitePiece(PieceType.PAWN, 0, 0)
                                                        .withBlackPiece(PieceType.PAWN, 3, 3)
                                                        .build()
                                        ).build()
                        ).build()
        );
    }
}
