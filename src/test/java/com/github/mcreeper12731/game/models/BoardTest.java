package com.github.mcreeper12731.game.models;

import com.github.mcreeper12731.game.Game;
import com.github.mcreeper12731.game.pieces.PieceType;
import com.github.mcreeper12731.game.presets.Preset;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BoardTest {

    @Test
    public void boardFilledWithEmptyPieces() {

        Board board = new Board.Builder(8, 0, 0)
                .build();

        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                assertNotNull(board.getLocationContents(x, y));
                assertEquals(PieceType.EMPTY, board.getLocationContents(x, y).type());
            }
        }
    }

    @Test
    public void boardCorrectlyCopied() {

        Board board = new Board.Builder(8, 0, 0)
                .withWhitePiece(PieceType.QUEEN, 0, 0)
                .withBlackPiece(PieceType.QUEEN, 7, 7)
                .build();

        assertEquals(2, board.getPieces().size());
        assertEquals(PieceType.QUEEN, board.getLocationContents(0, 0).type());
        assertEquals(Color.WHITE, board.getLocationContents(0, 0).color());

        assertEquals(PieceType.QUEEN, board.getLocationContents(7, 7).type());
        assertEquals(Color.BLACK, board.getLocationContents(7, 7).color());

        Move move = new Move.Builder()
                .withPiece(board.getLocationContents(0, 0))
                .withTo(0, 0, 1, 0)
                .build();

        Board newBoard = new Board.Builder(board, 0, 0, move)
                .build();

        assertEquals(2, newBoard.getPieces().size());
        assertEquals(PieceType.QUEEN, newBoard.getLocationContents(1, 0).type());
        assertEquals(Color.WHITE, newBoard.getLocationContents(1, 0).color());

        assertEquals(PieceType.QUEEN, newBoard.getLocationContents(7, 7).type());
        assertEquals(Color.BLACK, newBoard.getLocationContents(7, 7).color());
    }

    @Test
    public void boardCorrectlyCopiedWhenTimeTravel() {

        Board board0T0 = new Board.Builder(8, 0, 0)
                .withWhitePiece(PieceType.QUEEN, 0, 0)
                .withBlackPiece(PieceType.QUEEN, 7, 7)
                .build();

        Move move = new Move.Builder()
                .withPiece(board0T0.getLocationContents(0, 0))
                .withTo(0, 0, 1, 0)
                .build();
        Board board0T1 = new Board.Builder(board0T0, 0, 1, move)
                .build();

        move = new Move.Builder()
                .withPiece(board0T1.getLocationContents(7, 7))
                .withTo(0, 1, 6, 7)
                .build();
        Board board0T2 = new Board.Builder(board0T1, 0, 2, move)
                .build();

        move = new Move.Builder()
                .withPiece(board0T2.getLocationContents(1, 0))
                .withTo(0, 0, 1, 1)
                .build();

        Board board0T3 = new Board.Builder(board0T2, 0, 3, move)
                .build();

        assertEquals(3, board0T3.t());
        assertEquals(0, board0T3.l());
        assertEquals(1, board0T3.getPieces().size());
        assertEquals(PieceType.QUEEN, board0T3.getLocationContents(6, 7).type());

        Board board1T1 = new Board.Builder(board0T0, 1, 1, move)
                .build();

        assertEquals(1, board1T1.t());
        assertEquals(1, board1T1.l());
        assertEquals(3, board1T1.getPieces().size());
        assertEquals(PieceType.QUEEN, board1T1.getLocationContents(0, 0).type());
        assertEquals(PieceType.QUEEN, board1T1.getLocationContents(1, 1).type());
        assertEquals(PieceType.QUEEN, board1T1.getLocationContents(7, 7).type());
    }

    @Test
    public void boardCorrectlyCopiedWhenNoopMove() {

        Game game = Preset.STANDARD.getGame();
        game.applyMove(
                new Move.Builder()
                        .withNoop(0, 0)
                        .build()
        );

        assertEquals(1, game.getMultiverse().getTimeline(0).getLastT());
        Board originalBoard = game.getMultiverse().getBoard(0, 0);
        Board copiedBoard = game.getMultiverse().getBoard(0, 1);

        for (int i = 0; i < originalBoard.size(); i++) {
            assertEquals(originalBoard.getLocationContents(i % 8, i / 8).type(), copiedBoard.getLocationContents(i % 8, i / 8).type());
            assertEquals(originalBoard.getLocationContents(i % 8, i / 8).color(), copiedBoard.getLocationContents(i % 8, i / 8).color());
            assertEquals(originalBoard.getLocationContents(i % 8, i / 8).location().l(), copiedBoard.getLocationContents(i % 8, i / 8).location().l());
            assertEquals(originalBoard.getLocationContents(i % 8, i / 8).location().x(), copiedBoard.getLocationContents(i % 8, i / 8).location().x());
            assertEquals(originalBoard.getLocationContents(i % 8, i / 8).location().y(), copiedBoard.getLocationContents(i % 8, i / 8).location().y());

            assertNotEquals(originalBoard.getLocationContents(i % 8, i / 8).location().t(), copiedBoard.getLocationContents(i % 8, i / 8).location().t());
        }
    }

    @Test
    public void boardCorrectlyCopiedWhenCapture() {

        Board board = new Board.Builder(4, 0, 0)
                .withWhitePiece(PieceType.QUEEN, 0, 0)
                .withBlackPiece(PieceType.KING, 3, 0)
                .build();

        Move move = new Move.Builder()
                .withPiece(board.getLocationContents(0, 0))
                .withTo(0, 0, 3, 0)
                .build();

        Board nextBoard = new Board.Builder(board, 0, 1, move)
                .build();

        assertEquals(new Point4D(0, 1, 3, 0), nextBoard.getLocationContents(3, 0).location());
        assertEquals(PieceType.QUEEN, nextBoard.getLocationContents(3, 0).type());
        assertEquals(Color.WHITE, nextBoard.getLocationContents(3, 0).color());
        assertTrue(nextBoard.getLocationContents(3, 0).moved());
    }
}