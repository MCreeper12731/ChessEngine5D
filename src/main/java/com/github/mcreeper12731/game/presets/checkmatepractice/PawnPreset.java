package com.github.mcreeper12731.game.presets.checkmatepractice;

import com.github.mcreeper12731.game.Game;
import com.github.mcreeper12731.game.models.Board;
import com.github.mcreeper12731.game.models.Multiverse;
import com.github.mcreeper12731.game.models.Timeline;
import com.github.mcreeper12731.game.models.pieces.PieceType;
import com.github.mcreeper12731.game.presets.GamePreset;

public class PawnPreset implements GamePreset {

    @Override
    public Game createGame() {

        return new Game(
                new Multiverse.Builder(6)
                        .withTimeline(
                                new Timeline.Builder(0)
                                        .withBoard(
                                                new Board.Builder(6, 0, 0)
                                                        .withWhitePiece(PieceType.KING, 3, 0)
                                                        .withBlackPiece(PieceType.PAWN, 2, 5)
                                                        .withBlackPiece(PieceType.PAWN, 3, 5)
                                                        .withBlackPiece(PieceType.PAWN, 4, 5)
                                                        .build()
                                        ).build()
                        ).build()
        );

    }

}
