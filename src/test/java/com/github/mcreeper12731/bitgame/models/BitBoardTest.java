package com.github.mcreeper12731.bitgame.models;

import com.github.mcreeper12731.bitgame.models.pieces.BitPiece;
import com.github.mcreeper12731.game.models.Board;
import com.github.mcreeper12731.game.models.Color;
import com.github.mcreeper12731.game.models.Move;
import com.github.mcreeper12731.game.models.Point4D;
import com.github.mcreeper12731.game.models.pieces.PieceType;
import com.github.mcreeper12731.utility.Log;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BitBoardTest {

    @Test
    public void toStringSameAsNormalBoard() {

        BitBoard bitBoard = new BitBoard.Builder(4, 0, 0)
                .withWhitePiece(PieceType.KING, 0, 0)
                .withBlackPiece(PieceType.KING, 3, 3)
                .build();

        Board board = new Board.Builder(4, 0, 0)
                .withWhitePiece(PieceType.KING, 0, 0)
                .withBlackPiece(PieceType.KING, 3, 3)
                .build();

//        Log.debug("Test", bitBoard);
        assertEquals(bitBoard.toString(), board.toString());
    }

    @Test
    public void correctBoardWhenApplyingSameBoardMove() {

        BitBoard board = new BitBoard.Builder(4, 0, 0)
                .withWhitePiece(PieceType.KING, 0, 0)
                .withBlackPiece(PieceType.KING, 3, 3)
                .build();

        Move move = Move.of(board.getLocationContents(0, 0), new Point4D(0, 0, 0, 0), new Point4D(0, 0, 0, 1));

        BitBoard newBoard = board.applyMove(0, 1, move);
//        Log.debug("Test", newBoard);

        assertEquals(0, newBoard.getLocationContents(0, 0));
        assertEquals(BitPiece.encode(Color.WHITE, PieceType.KING, true), newBoard.getLocationContents(0, 1));
    }

    @Test
    public void correctBoardWhenApplyingJumpMove() {

        BitBoard board0 = new BitBoard.Builder(4, 0, 0)
                .withWhitePiece(PieceType.KING, 0, 0)
                .withBlackPiece(PieceType.KING, 3, 3)
                .build();

        BitBoard board1 = new BitBoard.Builder(4, 0, 0)
                .withWhitePiece(PieceType.KING, 0, 0)
                .withBlackPiece(PieceType.KING, 3, 3)
                .build();

        Move move = Move.of(board0.getLocationContents(0, 0), new Point4D(0, 0, 0, 0), new Point4D(0, 0, 0, 1));

        BitBoard newBoard0 = board0.applyMove(0, 1, move);
        BitBoard newBoard1 = board1.applyMove(1, 1, move);

//        Log.debug("Test", newBoard0);
//        Log.debug("Test");
//        Log.debug("Test", newBoard1);

        assertEquals(0, newBoard0.getLocationContents(0, 0));
        assertEquals(0, newBoard0.getLocationContents(0, 1));

        assertEquals(BitPiece.encode(Color.WHITE, PieceType.KING, false), newBoard1.getLocationContents(0, 0));
        assertEquals(BitPiece.encode(Color.WHITE, PieceType.KING, true), newBoard1.getLocationContents(0, 1));
    }

    @Test
    public void correctWhenApplyingCapturingMove() {

        BitBoard board = new BitBoard.Builder(4, 0, 0)
                .withWhitePiece(PieceType.KING, 0, 0)
                .withBlackPiece(PieceType.KING, 3, 3)
                .build();

        Move move = Move.of(board.getLocationContents(0, 0), new Point4D(0, 0, 0, 0), new Point4D(0, 0, 3, 3));

        BitBoard newBoard = board.applyMove(0, 1, move);
//        Log.debug("Test", newBoard);

        assertEquals(0, newBoard.getLocationContents(0, 0));
        assertEquals(BitPiece.encode(Color.WHITE, PieceType.KING, true), newBoard.getLocationContents(3, 3));
    }

    @Test
    public void correctWhenApplyingNoopMove() {

        BitBoard board = new BitBoard.Builder(4, 0, 0)
                .withWhitePiece(PieceType.KING, 0, 0)
                .withBlackPiece(PieceType.KING, 3, 3)
                .build();

        BitBoard newBoard = board.applyMove(0, 1, Move.noop(0, 0));
//        Log.debug("Test", newBoard);

        assertEquals(board.toString(), newBoard.toString());
    }
}