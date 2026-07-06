package com.github.mcreeper12731.game.models.bit;

import com.github.mcreeper12731.game.models.Color;
import com.github.mcreeper12731.game.models.pieces.PieceType;

public class BitPiece {
    private final static int NUMBER_OF_TYPES = PieceType.values().length;

    public static byte encode(int color, int type) {
        return (byte) (color * NUMBER_OF_TYPES + type + 1);
    }

    public static byte encode(Color color, PieceType type) {
        return (byte) (color.ordinal() * NUMBER_OF_TYPES + type.ordinal() + 1);
    }

    public static boolean isEmpty(byte bitPiece) {
        return bitPiece == 0;
    }

    public static int colorOrdinal(byte bitPiece) {
        return (bitPiece - 1) / NUMBER_OF_TYPES;
    }

    public static int typeOrdinal(byte bitPiece) {
        return (bitPiece - 1) % NUMBER_OF_TYPES;
    }

    public static PieceType type(byte bitPiece) {
        int typeOrdinal = typeOrdinal(bitPiece);
        if (typeOrdinal < 0 || typeOrdinal >= NUMBER_OF_TYPES) return null;
        return PieceType.values()[typeOrdinal];
    }

    public static Color color(byte bitPiece) {
        int colorOrdinal = colorOrdinal(bitPiece);
        if (colorOrdinal < 0 || colorOrdinal >= NUMBER_OF_TYPES) return null;
        return Color.values()[colorOrdinal];
    }
}
