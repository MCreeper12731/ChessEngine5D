package com.github.mcreeper12731.game.pieces;

import com.github.mcreeper12731.game.models.Multiverse;
import com.github.mcreeper12731.game.models.Move;
import com.github.mcreeper12731.game.pieces.movesets.*;

import java.util.Collections;
import java.util.Iterator;

public enum PieceType {
    EMPTY("", (e, p) -> Collections.emptyIterator()),

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
    BRAWN("W", new BrawnMoveSet())
    // Missing common king and royal queen but who cares
    ;

    public final String name;
    public final String longName;
    private final MoveSet moveSet;

    PieceType(String name, MoveSet moveSet) {
        this.name = name;
        this.longName = this.name().charAt(0) + this.name().toLowerCase().substring(1);
        this.moveSet = moveSet;
    }

    public Iterator<Move> moveIterator(Multiverse multiverse, Piece pieceInstance) {
        return this.moveSet.iterator(multiverse, pieceInstance);
    }

    public static long toNumber(PieceType type) {
        return switch (type) {
            case EMPTY -> 0;
            case KING -> 1;
            case QUEEN -> 2;
            case ROOK -> 3;
            case BISHOP -> 4;
            case KNIGHT -> 5;
            case PAWN -> 6;
            case UNICORN -> 7;
            case DRAGON -> 8;
            case PRINCESS -> 9;
            case BRAWN -> 10;
            case null -> 15;
        };
    }

    public static PieceType fromNumber(int number) {
        return switch (number) {
            case 0 -> PieceType.EMPTY;
            case 1 -> PieceType.KING;
            case 2 -> PieceType.QUEEN;
            case 3 -> PieceType.ROOK;
            case 4 -> PieceType.BISHOP;
            case 5 -> PieceType.KNIGHT;
            case 6 -> PieceType.PAWN;
            case 7 -> PieceType.UNICORN;
            case 8 -> PieceType.DRAGON;
            case 9 -> PieceType.PRINCESS;
            case 10 -> PieceType.BRAWN;
            case 15 -> null;
            default -> throw new IllegalArgumentException("Invalid piece number: " + number);
        };
    }
}
