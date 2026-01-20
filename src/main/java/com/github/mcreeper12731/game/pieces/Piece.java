package com.github.mcreeper12731.game.pieces;

import com.github.mcreeper12731.game.models.Color;
import com.github.mcreeper12731.game.models.Multiverse;
import com.github.mcreeper12731.game.models.Point4D;
import com.github.mcreeper12731.game.moves.Move;

import java.util.List;

public record Piece(
        Color color, PieceType type, Point4D location,
        boolean moved
        ) {

    public Piece(Color color, PieceType type, Point4D position) {
        this(color, type, position, false);
    }

    public List<Move> getAvailableMoves(Multiverse multiverse) {
        return type.getAvailableMoves(multiverse, this);
    }

    @Override
    public String toString() {
        return type.name + color.name().charAt(0);
    }

    public String toLongString() {
        return type.longName + color.name().charAt(0);
    }
}
