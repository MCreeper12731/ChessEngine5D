package com.github.mcreeper12731.game.presets.custom;

import com.github.mcreeper12731.bitgame.BitGame;
import com.github.mcreeper12731.bitgame.models.BitBoard;
import com.github.mcreeper12731.bitgame.models.BitMultiverse;
import com.github.mcreeper12731.bitgame.models.BitTimeline;
import com.github.mcreeper12731.game.Game;
import com.github.mcreeper12731.game.models.Board;
import com.github.mcreeper12731.game.models.Multiverse;
import com.github.mcreeper12731.game.models.Timeline;
import com.github.mcreeper12731.game.models.pieces.PieceType;
import com.github.mcreeper12731.game.presets.GamePreset;

public class SimpleMultiBoardPreset implements GamePreset {

    @Override
    public Game createGame() {
        return new Game(
                new Multiverse.Builder(4)
                        .withTimeline(
                                new Timeline.Builder(-1)
                                        .withBoard(new Board.Builder(4, -1, 0)
                                                .withWhitePiece(PieceType.PAWN, 1, 0)
                                                .build()
                                        ).build()
                        )
                        .withTimeline(
                                new Timeline.Builder(0)
                                        .withBoard(new Board.Builder(4, 0, 0)
                                                .withWhitePiece(PieceType.PAWN, 1, 0)
                                                .build()
                                        ).build()
                        )
                        .withTimeline(
                                new Timeline.Builder(1)
                                        .withBoard(new Board.Builder(4, 1, 0)
                                                .withWhitePiece(PieceType.PAWN, 1, 0)
                                                .build()
                                        ).build()
                        )
                        .build()
        );
    }
}
