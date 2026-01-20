package com.github.mcreeper12731.game.models;

import com.github.mcreeper12731.game.pieces.Piece;
import com.github.mcreeper12731.game.pieces.PieceType;

import java.util.*;

public class Board {

    private final Color playerTurn;
    private final int size;
    private final double timeline;
    private final int time;
    private final Piece[][] pieces;

    private Board(Color playerTurn, int size, double timeline, int time, Set<Piece> pieces) {
        this.playerTurn = playerTurn;
        this.size = size;
        this.timeline = timeline;
        this.time = time;
        this.pieces = new Piece[size][size];

        for (Piece piece : pieces) {
            this.pieces[piece.location().y()][piece.location().x()] = piece;
        }
    }

    private Board(Color playerTurn, int size, double timeline, int time, Piece[][] pieces) {
        this.playerTurn = playerTurn;
        this.size = size;
        this.timeline = timeline;
        this.time = time;
        this.pieces = new Piece[size][size];

        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                Piece originalPiece = pieces[y][x];
                if (originalPiece != null)
                    setPiece(x, y, originalPiece.color(), originalPiece.type(), originalPiece.moved());
            }
        }
    }

    public Board copyWithProgressedTurn(double timeline, int time) {
        return new Board(
                playerTurn.other(),
                size,
                timeline,
                time,
                pieces
        );
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (int y = this.getSize() - 1; y >= 0; y--) {
            for (int x = 0; x < this.getSize(); x++) {
                Piece piece = pieces[y][x];
                builder.append(piece != null ? piece.toString() : "..").append(" ");
            }
            builder.append("\n");
        }
        return builder.toString();
    }

    public Set<Piece> getPieces() {

        Set<Piece> pieces = new HashSet<>();

        for (int x = 0; x < getSize(); x++) {
            for (int y = 0; y < getSize(); y++) {

                Piece piece = getPiece(x, y);
                if (piece != null) pieces.add(piece);
            }
        }
        return pieces;
    }

    public Piece getPiece(int x, int y) {
        return pieces[y][x];
    }

    public void setPiece(int x, int y, Color pieceColor, PieceType pieceType, boolean movedBefore) {
        pieces[y][x] = new Piece(
                pieceColor,
                pieceType,
                new Point4D(
                        timeline,
                        time,
                        x,
                        y
                ),
                movedBefore
        );
    }

    public void setPieceFromMoving(int x, int y, Color pieceColor, PieceType pieceType) {
        setPiece(x, y, pieceColor, pieceType, true);
    }

    public void removePiece(int x, int y) {
        pieces[y][x] = null;
    }

    public Color getPlayerTurn() {
        return playerTurn;
    }

    public int getSize() {
        return size;
    }

    public double getTimeline() {
        return timeline;
    }

    public int getTime() {
        return time;
    }

    public static class Builder {

        private final int size;
        private final double timeline;
        private final int time;
        private final Color playerColor;
        private final Set<Piece> pieces = new HashSet<>();

        public Builder(int size, double timeline, int time, Color playerColor) {
            this.size = size;
            this.timeline = timeline;
            this.time = time;
            this.playerColor = playerColor;
        }

        public Builder withWhitePiece(PieceType pieceType, int x, int y) {
            return withPiece(Color.WHITE, pieceType, x, y);
        }

        public Builder withBlackPiece(PieceType pieceType, int x, int y) {
            return withPiece(Color.BLACK, pieceType, x, y);
        }

        public Builder withPiece(Color pieceColor, PieceType pieceType, int x, int y) {
            pieces.add(new Piece(
                    pieceColor,
                    pieceType,
                    new Point4D(timeline, time, x, y),
                    false
            ));
            return this;
        }

        public Board build() {
            return new Board(
                    playerColor,
                    size,
                    timeline,
                    time,
                    pieces
            );
        }
    }
}
