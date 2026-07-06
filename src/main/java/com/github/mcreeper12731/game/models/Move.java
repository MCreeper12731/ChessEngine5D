package com.github.mcreeper12731.game.models;

import com.github.mcreeper12731.game.Game;
import com.github.mcreeper12731.game.bitmodels.BitPiece;
import com.github.mcreeper12731.game.models.pieces.Piece;
import com.github.mcreeper12731.game.models.pieces.PieceType;

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

        /**
         * Converts a move from the following representation:
         * (fromL,fromT;fromX,fromY)->(toL,toT;toX,toY)
         * @param stringMove string representing the move
         * @return move
         */
        public Move fromStringAndBuild(String stringMove) {
            stringMove = stringMove.replace(";", ",");

            String[] fromToSplit = stringMove.split("->");
            if (fromToSplit.length != 2) throw new IllegalArgumentException("Invalid move string");

            // Remove parentheses
            fromToSplit[0] = fromToSplit[0].substring(1, fromToSplit[0].length() - 1);
            fromToSplit[1] = fromToSplit[1].substring(1, fromToSplit[1].length() - 1);

            String[] fromPoint4DString = fromToSplit[0].split(",");
            if (fromPoint4DString.length != 4) throw new IllegalArgumentException("Invalid move string");
            int[] fromPoint4D = new int[4];
            for (int i = 0; i < fromPoint4DString.length; i++) {
                fromPoint4D[i] = Integer.parseInt(fromPoint4DString[i]);
            }
            this.withFrom(fromPoint4D[0], fromPoint4D[1], fromPoint4D[2], fromPoint4D[3]);

            String[] toPoint4DString = fromToSplit[1].split(",");
            if (toPoint4DString.length != 4) throw new IllegalArgumentException("Invalid move string");
            int[] toPoint4D = new int[4];
            for (int i = 0; i < toPoint4DString.length; i++) {
                toPoint4D[i] = Integer.parseInt(toPoint4DString[i]);
            }
            this.withTo(toPoint4D[0], toPoint4D[1], toPoint4D[2], toPoint4D[3]);

            return this.build();
        }

        public Builder withPieceMinimal(Piece piece) {
            this.fromType = piece.type();
            this.toType = piece.type();
            this.color = piece.color();
            return this;
        }

        public Builder withBitPiece(byte piece) {
            this.fromType = BitPiece.type(piece);
            this.toType = BitPiece.type(piece);
            this.color = BitPiece.color(piece);
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
                throw new IllegalStateException("Provide more move information or declare it noop");

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
