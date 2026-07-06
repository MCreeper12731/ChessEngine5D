package com.github.mcreeper12731.game.models;

import com.github.mcreeper12731.game.models.pieces.Piece;
import com.github.mcreeper12731.game.models.pieces.PieceRegistry;
import com.github.mcreeper12731.game.models.pieces.PieceType;

import java.util.*;

/**
 * Essentially a record, except the constructor is not exposed
 */
public class Board {

    private final static Piece EMPTY_PIECE = new Piece(null, PieceType.EMPTY, true);

    private final int size;
    private final int l;
    private final int t;
    /**
     * Full board contents, null if a piece does not exist
     */
    private final Piece[] contents;

    private Board(int size, int l, int t, Piece[] contents) {
        this.size = size;
        this.l = l;
        this.t = t;

        this.contents = contents;
    }

    public Board applyMove(int newL, int newT, Move move) {

        Piece[] contentsNew = new Piece[this.contents.length];

        System.arraycopy(this.contents, 0, contentsNew, 0, this.contents.length);

        if (!move.noop() && move.from().l() == newL && this.l == move.from().l() && this.t == move.from().t()) {
            contentsNew[move.from().x() + move.from().y() * this.size] = null;
        }

        if (!move.noop() && move.to().l() == newL && this.t == move.to().t() || this.l != newL) {

            Piece piece = PieceRegistry.getPiece(move.color(), move.toType(), true);

            contentsNew[move.to().x() + move.to().y() * this.size] = piece;
        }

        return new Board(this.size, newL, newT, contentsNew);
    }

    /**
     * Indexing of board contents
     * @param x x coordinate of location
     * @param y y coordinate of location
     * @return piece at location (x, y) or null if the location is out of bounds. Return a piece with type EMPTY if the
     * location is valid but does not contain a piece
     */
    public Piece getLocationContents(int x, int y) {
        if (x < 0 || x >= this.size || y < 0 || y >= this.size) return null;
        Piece piece = this.contents[y * size + x];

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

    public List<Piece> getPieces() {
        List<Piece> pieces = new ArrayList<>();
        for (Piece piece : this.contents) {
            if (piece == null) continue;
            pieces.add(piece);
        }
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
        private final Piece[] contents;

        public Builder(int size, int l, int t) {
            this.size = size;
            this.l = l;
            this.t = t;
            this.contents = new Piece[size*size];
        }

        @Deprecated
        public Builder(Board boardToCopy, int newL, int newT, Move move) {
            this(boardToCopy.size, newL, newT);

            System.arraycopy(boardToCopy.contents, 0, this.contents, 0, this.size * this.size);

            if (!move.noop() && move.from().l() == newL && boardToCopy.l == move.from().l() && boardToCopy.t == move.from().t()) {
                this.contents[move.from().x() + move.from().y() * this.size] = null;
            }

            if (!move.noop() && move.to().l() == newL && boardToCopy.t == move.to().t() || boardToCopy.l != newL) {
                this.withPiece(move.color(), move.toType(), move.to().x(), move.to().y(), true);
            }
        }

        public Builder withWhitePiece(PieceType pieceType, int x, int y) {
            return this.withPiece(Color.WHITE, pieceType, x, y);
        }

        public Builder withBlackPiece(PieceType pieceType, int x, int y) {
            return this.withPiece(Color.BLACK, pieceType, x, y);
        }

        public Builder withPiece(Color pieceColor, PieceType pieceType, int x, int y) {
            return this.withPiece(pieceColor, pieceType, x, y, false);
        }

        public Builder withPiece(Color pieceColor, PieceType pieceType, int x, int y, boolean moved) {
            Piece piece = PieceRegistry.getPiece(pieceColor, pieceType, moved);

            this.contents[y * this.size + x] = piece;
            return this;
        }

        public Board build() {
            return new Board(this.size, this.l, this.t, this.contents);
        }
    }
}
