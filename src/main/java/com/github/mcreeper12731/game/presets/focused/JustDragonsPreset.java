package com.github.mcreeper12731.game.presets.focused;

import com.github.mcreeper12731.game.Game;
import com.github.mcreeper12731.game.models.Board;
import com.github.mcreeper12731.game.models.Multiverse;
import com.github.mcreeper12731.game.models.Timeline;
import com.github.mcreeper12731.game.models.pieces.PieceType;
import com.github.mcreeper12731.game.presets.GamePreset;

public class JustDragonsPreset implements GamePreset {

    @Override
    public Game createGame() {

        return new Game(
                new Multiverse.Builder(5)
                        .withTimeline(
                                new Timeline.Builder(0)
                                        .withBoard(
                                                new Board.Builder(5, 0, 0)
                                                        .withWhitePiece(PieceType.KING, 0, 0)
                                                        .withWhitePiece(PieceType.DRAGON, 1, 0)
                                                        .withWhitePiece(PieceType.DRAGON, 2, 0)
                                                        .withBlackPiece(PieceType.DRAGON, 2, 4)
                                                        .withBlackPiece(PieceType.DRAGON, 3, 4)
                                                        .withBlackPiece(PieceType.KING, 4, 4)
                                                        .build()
                                        ).build()
                        ).build()
        );
    }
}
