package com.github.mcreeper12731.bitgame.models;

import com.github.mcreeper12731.bitgame.BitGame;
import com.github.mcreeper12731.bitgame.models.pieces.BitPiece;
import com.github.mcreeper12731.game.models.Color;
import com.github.mcreeper12731.game.models.Move;
import com.github.mcreeper12731.game.models.pieces.PieceType;

public class BitBoard {

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
        for (int type = 0; type < BitGame.NUMBER_OF_TYPES; type++) {
            white |= pieceBitBoards[BitGame.WHITE][type];
            black |= pieceBitBoards[BitGame.BLACK][type];
        }
        this.colorOccupancy = new long[]{white, black};
        this.occupancy = white | black;
    }

    public BitBoard applyMove(int newL, int newT, Move move) {

        // TODO: recheck bitboards
//        long[][] pieceBitBoardsNew = new long[2][BitGame.NUMBER_OF_TYPES];
//        System.arraycopy(this.pieceBitBoards[BitGame.WHITE], 0, pieceBitBoardsNew[BitGame.WHITE], 0, BitGame.NUMBER_OF_TYPES);
//        System.arraycopy(this.pieceBitBoards[BitGame.BLACK], 0, pieceBitBoardsNew[BitGame.BLACK], 0, BitGame.NUMBER_OF_TYPES);
        byte[] mailboxNew = new byte[this.size * this.size];
        System.arraycopy(this.mailbox, 0, mailboxNew, 0, this.size * this.size);


        // Remove the piece from the original square
        if (!move.noop() && move.from().l() == newL && this.l == move.from().l() && this.t == move.from().t()) {
            int fromIndex = squareIndex(move.from().x(), move.from().y());
//            pieceBitBoardsNew[move.color().ordinal()][move.fromType().ordinal()] &= ~(1L << fromIndex);
            mailboxNew[fromIndex] = 0;
        }

        // Place the moved piece
        if (!move.noop() && move.to().l() == newL && this.t == move.to().t() || this.l != newL) {
            int toIndex = squareIndex(move.to().x(), move.to().y());
            // Check if there was a piece captured
            byte capturedPiece = this.mailbox[toIndex];
            if (capturedPiece != 0) {
                int capturedColor = BitPiece.colorOrdinal(capturedPiece);
                int capturedType = BitPiece.typeOrdinal(capturedPiece);
//                pieceBitBoardsNew[capturedColor][capturedType] &= ~(1L << toIndex);
            }

            // Place the piece
            int resultType = move.toType().ordinal();
//            pieceBitBoardsNew[move.color().ordinal()][resultType] |= 1L << toIndex;
            mailboxNew[toIndex] = BitPiece.encode(move.color().ordinal(), resultType, true);

        }

        return new BitBoard(this.size, newL, newT, this.pieceBitBoards, mailboxNew);
    }

    public boolean isEmpty(int x, int y) {
        return (this.occupancy & (1L << (squareIndex(x, y)))) == 0;
    }

    public boolean hasPiece(int color, int type, int x, int y) {
        return (pieceBitBoards[color][type] & (1L << (squareIndex(x, y)))) != 0;
    }

    /**
     *
     * @param x - x coordinate of the location
     * @param y - y coordinate of the location
     * @return 0 if the piece is empty, otherwise the encoded bit piece. Throws ArrayIndexOutOfBoundsException if x or y are out of bounds.
     */
    public byte getLocationContents(int x, int y) {
        return this.mailbox[x + y * this.size];
    }

    public byte getLocationContentsFromIndex(int i) {
        return this.mailbox[i];
    }

    private int squareIndex(int x, int y) {
        return y * this.size + x;
    }

    public Color getPlayerTurn() {
        return this.t % 2 == 0 ? Color.WHITE : Color.BLACK;
    }

    public int size() {
        return this.size;
    }

    public int l() {
        return this.l;
    }

    public int t() {
        return this.t;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (int y = this.size - 1; y >= 0; y--) {
            for (int x = 0; x < this.size; x++) {
                byte piece = this.mailbox[this.squareIndex(x, y)];
                int type = BitPiece.typeOrdinal(piece);
                String representation;
                if (piece == 0) representation = "..";
                else {
                    Color color = Color.of(BitPiece.colorOrdinal(piece));
                    representation = PieceType.of(type).name + color.name().charAt(0);
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
            this.pieceBitBoards = new long[2][BitGame.NUMBER_OF_TYPES];
            this.mailbox = new byte[size * size];
        }

        public Builder withWhitePiece(PieceType type, int x, int y) {
            return this.withPiece(BitGame.WHITE, type.ordinal(), x, y);
        }

        public Builder withBlackPiece(PieceType type, int x, int y) {
            return this.withPiece(BitGame.BLACK, type.ordinal(), x, y);
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
