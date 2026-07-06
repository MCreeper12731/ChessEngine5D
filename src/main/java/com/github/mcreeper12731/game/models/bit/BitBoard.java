package com.github.mcreeper12731.game.models.bit;

import com.github.mcreeper12731.game.models.Color;
import com.github.mcreeper12731.game.models.Move;
import com.github.mcreeper12731.game.models.pieces.PieceType;

public class BitBoard {

    private final static int NUMBER_OF_TYPES = PieceType.values().length;
    private final static int WHITE = Color.WHITE.ordinal();
    private final static int BLACK = Color.BLACK.ordinal();

    private final int size;
    private final int l;
    private final int t;

    private final long[][] pieceBitBoards;
    private final long[] colorOccupancy;
    private final long occupancy;
    private final byte[] mailbox;

    private BitBoard(int size, int l, int t, long[][] pieceBitBoards, byte[] mailbox) {
        this.size = size;
        this.l = l;
        this.t = t;
        this.pieceBitBoards = pieceBitBoards;
        this.mailbox = mailbox;

        long white = 0;
        long black = 0;
        for (int type = 0; type < NUMBER_OF_TYPES; type++) {
            white |= pieceBitBoards[WHITE][type];
            black |= pieceBitBoards[BLACK][type];
        }
        this.colorOccupancy = new long[]{white, black};
        this.occupancy = white | black;
    }

    public BitBoard applyMove(int newL, int newT, Move move) {

        long[][] pieceBitBoardsNew = new long[2][NUMBER_OF_TYPES];
        System.arraycopy(this.pieceBitBoards[WHITE], 0, pieceBitBoardsNew[WHITE], 0, NUMBER_OF_TYPES);
        System.arraycopy(this.pieceBitBoards[BLACK], 0, pieceBitBoardsNew[BLACK], 0, NUMBER_OF_TYPES);
        byte[] mailboxNew = new byte[this.size * this.size];
        System.arraycopy(this.mailbox, 0, mailboxNew, 0, this.size * this.size);

        int fromIndex = squareIndex(move.from().x(), move.from().y());
        int toIndex = squareIndex(move.to().x(), move.to().y());
        byte movedPiece = this.mailbox[fromIndex];
        int movingColor = BitPiece.colorOrdinal(movedPiece);
        int movingType = BitPiece.typeOrdinal(movedPiece);

        // Remove the piece from the original square
        if (!move.noop() && move.from().l() == newL && this.l == move.from().l() && this.t == move.from().t()) {
            pieceBitBoardsNew[movingColor][movingType] &= ~(1L << fromIndex);
            mailboxNew[fromIndex] = 0;
        }

        // Place the moved piece
        if (!move.noop() && move.to().l() == newL && this.t == move.to().t() || this.l != newL) {
            // Check if there was a piece captured
            byte capturedPiece = this.mailbox[toIndex];
            if (!BitPiece.isEmpty(capturedPiece)) {
                int capturedColor = BitPiece.colorOrdinal(capturedPiece);
                int capturedType = BitPiece.typeOrdinal(capturedPiece);
                pieceBitBoardsNew[capturedColor][capturedType] &= ~(1L << toIndex);
            }

            // Place the piece
            int resultType = move.toType().ordinal();
            pieceBitBoardsNew[movingColor][resultType] |= 1L << toIndex;
            mailboxNew[toIndex] = BitPiece.encode(movingColor, resultType);
        }

        return new BitBoard(this.size, newL, newT, pieceBitBoardsNew, mailboxNew);
    }

    public boolean isEmpty(int x, int y) {
        return (this.occupancy & (1L << (squareIndex(x, y)))) == 0;
    }

    public boolean hasPiece(int color, int type, int x, int y) {
        return (pieceBitBoards[color][type] & (1L << (squareIndex(x, y)))) != 0;
    }

    public byte getLocationContents(int x, int y) {
        if (x < 0 || y < 0 || x >= size || y >= size) return -1;
        return mailbox[squareIndex(x, y)];
    }

    private int squareIndex(int x, int y) {
        return y * this.size + x;
    }

    public Color getPlayerTurn() {
        return this.t % 2 == 0 ? Color.WHITE : Color.BLACK;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (int y = this.size - 1; y >= 0; y--) {
            for (int x = 0; x < this.size; x++) {
                byte piece = this.mailbox[this.squareIndex(x, y)];
                int colorInt = BitPiece.colorOrdinal(piece);
                int typeInt = BitPiece.typeOrdinal(piece);
                String representation;
                if (colorInt == -1 || typeInt == -1) {
                    representation = "..";
                }
                else {
                    Color color = Color.values()[colorInt];
                    PieceType type = PieceType.values()[typeInt];
                    representation = type.name + color.name().charAt(0);
                }
                builder.append(representation).append(" ");
            }
            builder.append("\n");
        }
        return builder.toString();
    }

    public static class Builder {

        private final int size;
        private final int l;
        private final int t;
        private final long[][] pieceBitBoards;
        private final byte[] mailbox;

        public Builder(int size, int l, int t) {
            this.size = size;
            this.l = l;
            this.t = t;
            this.pieceBitBoards = new long[2][NUMBER_OF_TYPES];
            this.mailbox = new byte[size * size];
        }

        public Builder withWhitePiece(PieceType type, int x, int y) {
            return this.withPiece(Color.WHITE.ordinal(), type.ordinal(), x, y);
        }

        public Builder withBlackPiece(PieceType type, int x, int y) {
            return this.withPiece(Color.BLACK.ordinal(), type.ordinal(), x, y);
        }

        public Builder withPiece(Color color, PieceType type, int x, int y) {
            return this.withPiece(color.ordinal(), type.ordinal(), x, y);
        }

        public Builder withPiece(int color, int type, int x, int y) {
            int index = y * this.size + x;
            this.pieceBitBoards[color][type] |= (1L << index);
            this.mailbox[index] = BitPiece.encode(color, type);
            return this;
        }

        public BitBoard build() {
            return new BitBoard(
                    this.size,
                    this.l,
                    this.t,
                    this.pieceBitBoards,
                    this.mailbox
            );
        }
    }
}
