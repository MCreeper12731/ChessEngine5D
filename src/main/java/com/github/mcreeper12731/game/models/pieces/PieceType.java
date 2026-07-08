package com.github.mcreeper12731.game.models.pieces;

import com.github.mcreeper12731.game.models.Multiverse;
import com.github.mcreeper12731.game.models.Move;
import com.github.mcreeper12731.game.models.Point4D;
import com.github.mcreeper12731.game.movegeneration.movesets.*;

import java.util.Collections;
import java.util.Iterator;

public enum PieceType {

    // Normal pieces
    KING("K", new SingleStepMoveSet(MoveDirections.DIRECTIONS_1234_DIM)),
    QUEEN("Q", new SlidingMoveSet(MoveDirections.DIRECTIONS_1234_DIM)),
    ROOK("R", new SlidingMoveSet(MoveDirections.DIRECTIONS_1_DIM)),
    BISHOP("B", new SlidingMoveSet(MoveDirections.DIRECTIONS_2_DIM)),
    KNIGHT("N", new SingleStepMoveSet(MoveDirections.DIRECTIONS_KNIGHT)),
    PAWN("P", new PawnMoveSet()),

    // Fairy pieces
    UNICORN("U", new SlidingMoveSet(MoveDirections.DIRECTIONS_3_DIM)),
    DRAGON("D", new SlidingMoveSet(MoveDirections.DIRECTIONS_4_DIM)),
    PRINCESS("S", new SlidingMoveSet(MoveDirections.DIRECTIONS_12_DIM)),
    BRAWN("W", new BrawnMoveSet()),
    // Missing common king
    // and royal queen but current implementation does not check for checkmates
    EMPTY("", (e, pieceLocation) -> Collections.emptyIterator()),
    ;

    public final String name;
    public final String longName;
    private final MoveSet moveSet;

    PieceType(String name, MoveSet moveSet) {
        this.name = name;
        this.longName = this.name().charAt(0) + this.name().toLowerCase().substring(1);
        this.moveSet = moveSet;
    }

    public Iterator<Move> moveIterator(Multiverse multiverse, Point4D pieceLocation) {
        return this.moveSet.iterator(multiverse, pieceLocation);
    }

    public static PieceType of(int ordinal) {
        return switch (ordinal) {
            case 0 -> KING;
            case 1 -> QUEEN;
            case 2 -> ROOK;
            case 3 -> BISHOP;
            case 4 -> KNIGHT;
            case 5 -> PAWN;

            case 6 -> UNICORN;
            case 7 -> DRAGON;
            case 8 -> PRINCESS;
            case 9 -> BRAWN;
            case 10 -> EMPTY;

            default -> throw new IllegalArgumentException("Invalid ordinal!");
        };
    }
}
