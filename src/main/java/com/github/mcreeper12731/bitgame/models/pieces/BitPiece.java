package com.github.mcreeper12731.bitgame.models.pieces;

import com.github.mcreeper12731.bitgame.BitGame;
import com.github.mcreeper12731.bitgame.movegeneration.movesets.BitBrawnMoveSet;
import com.github.mcreeper12731.bitgame.movegeneration.movesets.BitPawnMoveSet;
import com.github.mcreeper12731.bitgame.movegeneration.movesets.BitSingleStepMoveSet;
import com.github.mcreeper12731.bitgame.movegeneration.movesets.BitSlidingMoveSet;
import com.github.mcreeper12731.game.models.Color;
import com.github.mcreeper12731.game.models.Move;
import com.github.mcreeper12731.game.models.Point4D;
import com.github.mcreeper12731.game.movegeneration.movesets.MoveDirections;
import com.github.mcreeper12731.game.models.pieces.PieceType;

import java.util.Collections;
import java.util.Iterator;

public class BitPiece {

    public static byte encode(int color, int type, boolean moved) {
        return (byte) (((type + 1) << 2) |
                ((color) << 1) |
                (moved ? 1 : 0));
    }

    public static byte encode(int color, int type) {
        return encode(color, type, false);
    }

    public static byte encode(Color color, PieceType type, boolean moved) {
        return encode(color.ordinal(), type.ordinal(), moved);
    }

    public static byte encode(Color color, PieceType type) {
        return encode(color.ordinal(), type.ordinal(), false);
    }

    public static int colorOrdinal(byte bitPiece) {
        return ((bitPiece >> 1) & 1);
    }

    public static int typeOrdinal(byte bitPiece) {
        return ((bitPiece >> 2) & 0x1F) - 1;
    }

    public static boolean hasMoved(byte bitPiece) {
        return (bitPiece & 1) == 1;
    }

    public static Iterator<Move> getMoveIterator(BitGame game, Point4D location) {
        byte piece = game.getMultiverse().getLocationContents(location);
        if (piece == 0) return Collections.emptyIterator();
        int typeOrdinal = typeOrdinal(piece);
        return switch (PieceType.of(typeOrdinal)) {
            case KING -> new BitSingleStepMoveSet(MoveDirections.DIRECTIONS_1234_DIM).iterator(game, location); // King
            case QUEEN -> new BitSlidingMoveSet(MoveDirections.DIRECTIONS_1234_DIM).iterator(game, location);   // Queen
            case ROOK -> new BitSlidingMoveSet(MoveDirections.DIRECTIONS_1_DIM).iterator(game, location);       // Rook
            case BISHOP -> new BitSlidingMoveSet(MoveDirections.DIRECTIONS_2_DIM).iterator(game, location);     // Bishop
            case KNIGHT -> new BitSingleStepMoveSet(MoveDirections.DIRECTIONS_KNIGHT).iterator(game, location); // Knight
            case PAWN -> new BitPawnMoveSet().iterator(game, location);                                         // Pawn

            case UNICORN -> new BitSlidingMoveSet(MoveDirections.DIRECTIONS_3_DIM).iterator(game, location);    // Unicorn
            case DRAGON -> new BitSlidingMoveSet(MoveDirections.DIRECTIONS_4_DIM).iterator(game, location);     // Dragon
            case PRINCESS -> new BitSlidingMoveSet(MoveDirections.DIRECTIONS_12_DIM).iterator(game, location);  // Princess
            case BRAWN -> new BitBrawnMoveSet().iterator(game, location);                                       // Brawn
            default -> Collections.emptyIterator();
        };
    }
}
