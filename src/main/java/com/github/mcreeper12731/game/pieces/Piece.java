package com.github.mcreeper12731.game.pieces;

import com.github.mcreeper12731.game.models.Color;
import com.github.mcreeper12731.game.models.Multiverse;
import com.github.mcreeper12731.game.models.Point4D;
import com.github.mcreeper12731.game.models.Move;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

public record Piece(
        Color color, PieceType type, Point4D location,
        boolean moved
        ) {

    public Piece(long packedPiece) {
        this(
                Color.fromNumber((int) ((packedPiece & (0b11L << 2)) >> 2)),
                PieceType.fromNumber((int) ((packedPiece & (0b1111L << 4)) >> 4)),
                new Point4D(
                        (int) ((packedPiece & (0b11111111L << 32)) >> 32),
                        (int) ((packedPiece & (0b11111111L << 24)) >> 24) - (((int)(packedPiece) & 0b1) == 1 ? 256 : 0),
                        (int) ((packedPiece & (0b11111111L << 16)) >> 16),
                        (int) ((packedPiece & (0b11111111L << 8)) >> 8)
                ),
                (packedPiece & 0b10) == 0b10
        );
    }

    public Iterator<Move> getMoveIterator(Multiverse multiverse) {
        return this.type.moveIterator(multiverse, this);
    }

    public List<Move> getAvailableMoves(Multiverse multiverse) {
        List<Move> moves = new ArrayList<>();
        this.getMoveIterator(multiverse).forEachRemaining(moves::add);

        return moves;
    }

    public static long getPackedPiece(Color color, PieceType type, Point4D location, boolean moved) {
        long packedPiece = 0L;
        packedPiece += (moved ? 1 : 0) << 1;
        packedPiece += (Color.toNumber(color) & 0b11) << 2;
        packedPiece += (PieceType.toNumber(type) & 0b1111) << 4;

        if (location != null) {
            packedPiece += location.t() < 0 ? 1 : 0;
            packedPiece += (long) (location.y() & 0b11111111) << 8;
            packedPiece += (long) (location.x() & 0b11111111) << 16;
            packedPiece += (long) (location.t() & 0b11111111) << 24;
            packedPiece += (long) (location.l() & 0b11111111) << 32;
        }

        return packedPiece;
    }

    @Override
    public String toString() {
        if (this.type == PieceType.EMPTY) return "..";

        return this.type.name + this.color.name().charAt(0);
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (!(other instanceof Piece(Color other_color, PieceType other_type, Point4D other_location, boolean other_moved))) return false;
        if (this.type == PieceType.EMPTY || other_type == PieceType.EMPTY) return this.type == other_type;
        return this.color == other_color && this.type == other_type && this.location.equals(other_location) && this.moved == other_moved;
    }

    @Override
    public int hashCode() {
        return Objects.hash(color, type, location, moved);
    }

    public String toLongString() {
        return this.type.longName + this.color.name().charAt(0) + "@" + this.location;
    }
}
