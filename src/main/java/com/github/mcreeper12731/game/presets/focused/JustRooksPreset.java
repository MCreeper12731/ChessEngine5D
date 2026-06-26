package com.github.mcreeper12731.game.presets.focused;

import com.github.mcreeper12731.game.Game;
import com.github.mcreeper12731.game.models.Board;
import com.github.mcreeper12731.game.models.Color;
import com.github.mcreeper12731.game.models.Multiverse;
import com.github.mcreeper12731.game.models.Timeline;
import com.github.mcreeper12731.game.pieces.PieceType;
import com.github.mcreeper12731.game.presets.GamePreset;

public class JustRooksPreset implements GamePreset {

    @Override
    public Game createGame() {

        return new Game(new Multiverse.Builder(5)
                .withTimeline(
                        new Timeline.Builder(0)
                                .withBoard(
                                        new Board.Builder(5, 0, 0)
                                                .withPiece(Color.WHITE, PieceType.ROOK, 0, 0)
                                                .withPiece(Color.WHITE, PieceType.KING, 2, 0)
                                                .withPiece(Color.WHITE, PieceType.ROOK, 3, 0)
                                                .withPiece(Color.BLACK, PieceType.ROOK, 1, 4)
                                                .withPiece(Color.BLACK, PieceType.KING, 2, 4)
                                                .withPiece(Color.BLACK, PieceType.ROOK, 4, 4)
                                                .build()
                                )
                                .build()
                )
                .build());
    }
}
