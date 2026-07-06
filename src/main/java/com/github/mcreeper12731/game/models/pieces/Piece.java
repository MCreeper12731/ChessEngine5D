package com.github.mcreeper12731.game.models.pieces;

import com.github.mcreeper12731.game.models.Color;
import com.github.mcreeper12731.game.models.Multiverse;
import com.github.mcreeper12731.game.models.Point4D;
import com.github.mcreeper12731.game.models.Move;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

public record Piece(Color color, PieceType type, boolean moved) {

    public Iterator<Move> getMoveIterator(Multiverse multiverse, Point4D pieceLocation) {
        return this.type.moveIterator(multiverse, pieceLocation);
    }

    public List<Move> getAvailableMoves(Multiverse multiverse, Point4D pieceLocation) {
        List<Move> moves = new ArrayList<>();
        this.getMoveIterator(multiverse, pieceLocation).forEachRemaining(moves::add);

        return moves;
    }

    @Override
    public String toString() {
        if (this.type == PieceType.EMPTY) return "..";

        return this.type.name + this.color.name().charAt(0);
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (!(other instanceof Piece(Color other_color, PieceType other_type, boolean other_moved))) return false;
        if (this.type == PieceType.EMPTY || other_type == PieceType.EMPTY) return this.type == other_type;
        return this.color == other_color && this.type == other_type && this.moved == other_moved;
    }

    @Override
    public int hashCode() {
        return Objects.hash(color, type, moved);
    }

    public String toLongString() {
        return this.type.longName + this.color.name().charAt(0);
    }
}
