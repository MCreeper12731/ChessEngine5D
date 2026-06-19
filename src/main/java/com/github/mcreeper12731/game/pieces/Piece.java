package com.github.mcreeper12731.game.pieces;

import com.github.mcreeper12731.game.models.Color;
import com.github.mcreeper12731.game.models.Multiverse;
import com.github.mcreeper12731.game.models.Point4D;
import com.github.mcreeper12731.game.models.Move;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public record Piece(
        Color color, PieceType type, Point4D location,
        boolean moved
        ) {

    public Piece(Color color, PieceType type, Point4D position) {
        this(color, type, position, false);
    }

    public Iterator<Move> getMoveIterator(Multiverse multiverse) {
        return this.type.moveIterator(multiverse, this);
    }

    public List<Move> getAvailableMoves(Multiverse multiverse) {
        List<Move> moves = new ArrayList<>();
        this.getMoveIterator(multiverse).forEachRemaining(moves::add);

        return moves;
    }

    @Override
    public String toString() {
        if (this.type == PieceType.EMPTY) return "..";

        return this.type.name + this.color.name().charAt(0);
    }

    public String toLongString() {
        return this.type.longName + this.color.name().charAt(0) + "@" + this.location;
    }
}
