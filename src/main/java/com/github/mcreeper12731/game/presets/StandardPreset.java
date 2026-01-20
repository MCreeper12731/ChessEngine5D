package com.github.mcreeper12731.game.presets;

import com.github.mcreeper12731.game.models.Board;
import com.github.mcreeper12731.game.models.Color;
import com.github.mcreeper12731.game.models.Multiverse;
import com.github.mcreeper12731.game.models.Timeline;
import com.github.mcreeper12731.game.pieces.PieceType;

public class StandardPreset implements GamePreset {

    @Override
    public Multiverse createMultiverse() {

        return new Multiverse.Builder(8)
                .withTimeline(
                        new Timeline.Builder(0)
                                .withBoard(
                                        new Board.Builder(8, 0, 0, Color.WHITE)
                                                .withWhitePiece(PieceType.ROOK, 0, 0)
                                                .withWhitePiece(PieceType.KNIGHT, 1, 0)
                                                .withWhitePiece(PieceType.BISHOP, 2, 0)
                                                .withWhitePiece(PieceType.QUEEN, 3, 0)
                                                .withWhitePiece(PieceType.KING, 4, 0)
                                                .withWhitePiece(PieceType.BISHOP, 5, 0)
                                                .withWhitePiece(PieceType.KNIGHT, 6, 0)
                                                .withWhitePiece(PieceType.ROOK, 7, 0)
                                                .withWhitePiece(PieceType.PAWN, 0, 1)
                                                .withWhitePiece(PieceType.PAWN, 1, 1)
                                                .withWhitePiece(PieceType.PAWN, 2, 1)
                                                .withWhitePiece(PieceType.PAWN, 3, 1)
                                                .withWhitePiece(PieceType.PAWN, 4, 1)
                                                .withWhitePiece(PieceType.PAWN, 5, 1)
                                                .withWhitePiece(PieceType.PAWN, 6, 1)
                                                .withWhitePiece(PieceType.PAWN, 7, 1)
                                                .withBlackPiece(PieceType.ROOK, 0, 7)
                                                .withBlackPiece(PieceType.KNIGHT, 1, 7)
                                                .withBlackPiece(PieceType.BISHOP, 2, 7)
                                                .withBlackPiece(PieceType.QUEEN, 3, 7)
                                                .withBlackPiece(PieceType.KING, 4, 7)
                                                .withBlackPiece(PieceType.BISHOP, 5, 7)
                                                .withBlackPiece(PieceType.KNIGHT, 6, 7)
                                                .withBlackPiece(PieceType.ROOK, 7, 7)
                                                .withBlackPiece(PieceType.PAWN, 0, 6)
                                                .withBlackPiece(PieceType.PAWN, 1, 6)
                                                .withBlackPiece(PieceType.PAWN, 2, 6)
                                                .withBlackPiece(PieceType.PAWN, 3, 6)
                                                .withBlackPiece(PieceType.PAWN, 4, 6)
                                                .withBlackPiece(PieceType.PAWN, 5, 6)
                                                .withBlackPiece(PieceType.PAWN, 6, 6)
                                                .withBlackPiece(PieceType.PAWN, 7, 6)
                                                .build()
                                )
                                .build()
                )
                .build();
    }
}
