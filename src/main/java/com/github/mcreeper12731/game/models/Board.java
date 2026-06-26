package com.github.mcreeper12731.game.models;

import com.github.mcreeper12731.game.pieces.Piece;
import com.github.mcreeper12731.game.pieces.PieceType;

import java.util.*;

/**
 * Essentially a record, except the constructor is not exposed
 */
public class Board {

    private final static Piece EMPTY_PIECE = new Piece(null, PieceType.EMPTY, null, true);

    private final int size;
    private final int l;
    private final int t;
    /**
     * Full board contents, null if piece does not exist
     */
    private final Piece[][] contents;
    /**
     * Non-empty pieces of board contents
     */
    private final List<Piece> pieces;

    private Board(Builder builder) {
        this.size = builder.size;
        this.l = builder.l;
        this.t = builder.t;

        this.contents = builder.contents;
        this.pieces = Collections.unmodifiableList(builder.pieces);
    }

    /**
     * Indexing of board contents
     * @param x x coordinate of location
     * @param y y coordinate of location
     * @return piece at location (x, y) or null if the location is out of bounds. Return piece with type EMPTY if the
     * location is valid but does not contain a piece
     */
    public Piece getLocationContents(int x, int y) {
        if (x < 0 || x >= this.size || y < 0 || y >= this.size) return null;
        Piece piece = this.contents[y][x];

        if (piece == null) return EMPTY_PIECE;
        return piece;
    }

    public Color getPlayerTurn() {
        return this.t % 2 == 0 ? Color.WHITE : Color.BLACK;
    }

    public int size() {
        return size;
    }

    public int l() {
        return l;
    }

    public int t() {
        return t;
    }

    public List<Piece> pieces() {
        return pieces;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (int y = this.size - 1; y >= 0; y--) {
            for (int x = 0; x < this.size; x++) {
                Piece piece = this.getLocationContents(x, y);
                builder.append(piece.toString()).append(" ");
            }
            builder.append("\n");
        }
        return builder.toString();
    }

    public static class Builder {

        private final int size;
        private final int l;
        private final int t;
        private final Piece[][] contents;
        private final List<Piece> pieces;

        public Builder(int size, int l, int t) {
            this.size = size;
            this.l = l;
            this.t = t;
            this.contents = new Piece[size][size];

            this.pieces = new ArrayList<>();
        }

        public Builder(Board boardToCopy, int l, int t, Move move) {
            this(boardToCopy.size, l, t);

            if (
                    !move.noop() && move.to().l() == this.l && boardToCopy.t == move.to().t()
                            || boardToCopy.l != l
            ) {
                this.withPiece(move.color(), move.toType(), move.to().x(), move.to().y(), true);
            }

            for (Piece piece : boardToCopy.pieces) {
                if (piece.type() == PieceType.EMPTY) continue;
                if (move.from().l() == this.l && piece.location().equals(move.from())) continue;
                if (this.contents[piece.location().y()][piece.location().x()] != null) continue;

                this.withPiece(piece, piece.moved());
            }
        }

        public Builder withWhitePiece(PieceType pieceType, int x, int y) {
            return this.withPiece(Color.WHITE, pieceType, x, y);
        }

        public Builder withBlackPiece(PieceType pieceType, int x, int y) {
            return this.withPiece(Color.BLACK, pieceType, x, y);
        }

        public Builder withPiece(Piece piece, boolean moved) {
            return this.withPiece(piece.color(), piece.type(), piece.location().x(), piece.location().y(), moved);
        }

        public Builder withPiece(Color pieceColor, PieceType pieceType, int x, int y) {
            return this.withPiece(pieceColor, pieceType, x, y, false);
        }

        public Builder withPiece(Color pieceColor, PieceType pieceType, int x, int y, boolean moved) {
            Piece piece = new Piece(
                    pieceColor,
                    pieceType,
                    new Point4D(this.l, this.t, x, y),
                    moved
            );

            this.contents[y][x] = piece;
            this.pieces.add(piece);
            return this;
        }

        public Board build() {
            return new Board(this);
        }
    }
}
