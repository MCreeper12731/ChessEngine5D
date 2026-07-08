package com.github.mcreeper12731.game.models.pieces;

import com.github.mcreeper12731.game.models.Color;

public class PieceRegistry {

    private final static Piece[][][] pieces = new Piece[Color.values().length][PieceType.values().length][2];
    private static boolean generated = false;

    private static void generate() {
        for (Color color : Color.values()) {
            for (PieceType pieceType : PieceType.values()) {
                pieces[color.ordinal()][pieceType.ordinal()][0] = new Piece(color, pieceType, false);
                pieces[color.ordinal()][pieceType.ordinal()][1] = new Piece(color, pieceType, true);
            }
        }
    }

    public static Piece getPiece(Color color, PieceType type, boolean moved) {
        if (!generated) {
            generate();
            generated = true;
        }

        return pieces[color.ordinal()][type.ordinal()][!moved ? 0 : 1];
    }

}
