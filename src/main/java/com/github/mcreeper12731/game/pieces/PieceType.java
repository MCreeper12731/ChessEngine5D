package com.github.mcreeper12731.game.pieces;

import com.github.mcreeper12731.game.models.Multiverse;
import com.github.mcreeper12731.game.moves.Move;
import com.github.mcreeper12731.game.pieces.movesets.*;

import java.util.List;

public enum PieceType {
    KING("K", new KingMoveSet()),
    QUEEN("Q", new QueenMoveSet()),
    ROOK("R", new RookMoveSet()),
    BISHOP("B", new BishopMoveSet()),
    KNIGHT("N", new KnightMoveSet()),
    PAWN("P", new PawnMoveSet()),
    ;
    //TODO: implement more pieces

    public final String name;
    public final String longName;
    private final MoveSet moveSet;

    PieceType(String name, MoveSet moveSet) {
        this.name = name;
        this.longName = this.name().charAt(0) + this.name().toLowerCase().substring(1);
        this.moveSet = moveSet;
    }

    public List<Move> getAvailableMoves(Multiverse multiverse, Piece pieceInstance) {
        return moveSet.generateMoves(multiverse, pieceInstance);
    }
}
