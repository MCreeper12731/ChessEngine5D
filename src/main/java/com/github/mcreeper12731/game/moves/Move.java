package com.github.mcreeper12731.game.moves;

import com.github.mcreeper12731.game.logic.Game;
import com.github.mcreeper12731.game.models.Color;
import com.github.mcreeper12731.game.models.Multiverse;
import com.github.mcreeper12731.game.models.Point4D;
import com.github.mcreeper12731.game.pieces.Piece;
import com.github.mcreeper12731.game.pieces.PieceType;

import java.util.Objects;

public record Move(
        Point4D from, Point4D to,
        PieceType fromType, PieceType toType,
        Color color,
        Point4D castledRook,
        Point4D enPassant,
        boolean noop
) implements Comparable<Move> {

    @Override
    public int compareTo(Move other) {
        return this.hashCode() - other.hashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (!(other instanceof Move move)) return false;
        return this.hashCode() == move.hashCode();
    }

    @Override
    public int hashCode() {
        return Objects.hash(from, to, fromType, toType, castledRook, enPassant, noop);
    }

    @Override
    public String toString() {
        if (noop) return "MoveNew{noop}";

        return
                this.fromType + "_" + this.color + ":" + from + "->" + to +
                (fromType != toType ? ", toType=" + toType : "") +
                (castledRook != null ? ", castledRook=" + castledRook : "") +
                (enPassant != null ? ", enPassant=" + enPassant : "");
    }

    public static class Builder {

        private final Multiverse multiverse;

        private Point4D from;
        private Point4D to;
        private PieceType fromType;
        private PieceType toType;
        private Color color;
        private Point4D castledRook;
        private Point4D enPassant;
        private boolean noop = false;

        public Builder() {
            this.multiverse = null;
        }

        public Builder(Game game) {
            this.multiverse = game.getMultiverse();
        }

        public Builder(Multiverse multiverse) {
            this.multiverse = multiverse;
        }

        public Builder withPiece(Piece piece) {
            this.from = piece.location();
            this.fromType = piece.type();
            this.toType = piece.type();
            this.color = piece.color();
            return this;
        }

        public Builder withFrom(Point4D from) {
            this.from = from;
            if (this.multiverse != null) {
                Piece piece = this.multiverse.getLocationContents(this.from);

                this.fromType = piece.type();
                this.toType = piece.type();
                this.color = piece.color();
            }
            return this;
        }

        public Builder withFrom(int l, int t, int x, int y) {
            this.from = new Point4D(l, t, x, y);
            if (this.multiverse != null) {
                Piece piece = this.multiverse.getLocationContents(this.from);

                this.fromType = piece.type();
                this.toType = piece.type();
                this.color = piece.color();
            }
            return this;
        }

        public Builder withTo(Point4D to) {
            this.to = to;
            return this;
        }

        public Builder withTo(int l, int t, int x, int y) {
            this.to = new Point4D(l, t, x, y);
            return this;
        }

        public Builder withNoop(int l, int t) {
            this.from = new Point4D(l, t, -1, -1);
            this.to = new Point4D(l, t, -1, -1);
            this.noop = true;
            return this;
        }

        public Builder withColor(Color color) {
            this.color = color;
            return this;
        }

        public Builder withPromotion(PieceType toType) {
            this.toType = toType;
            return this;
        }

        public Builder withCastledRook(Point4D castledRook) {
            this.castledRook = castledRook;
            return this;
        }

        public Builder withEnPassant(Point4D enPassant) {
            this.enPassant = enPassant;
            return this;
        }

        public Builder withNoop() {
            this.noop = true;
            return this;
        }

        public Move build() {
            if ((this.from == null || this.to == null || this.fromType == null || this.toType == null || this.color == null) && !this.noop)
                throw new IllegalStateException();

            return new Move(
                    this.from, this.to,
                    this.fromType, this.toType,
                    this.color,
                    this.castledRook,
                    this.enPassant,
                    this.noop
            );
        }

    }
}
