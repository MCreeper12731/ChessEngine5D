package com.github.mcreeper12731.game.pieces.movesets;

import com.github.mcreeper12731.game.logic.Game;
import com.github.mcreeper12731.game.models.*;
import com.github.mcreeper12731.game.models.Move;
import com.github.mcreeper12731.game.pieces.Piece;
import com.github.mcreeper12731.game.pieces.PieceType;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MoveSetTest {

    private static void assertMoveCount(Multiverse multiverse, Piece piece, int expectedMoveCount) {
        assertNotNull(piece);

        List<Move> allPieceMoves = piece.getAvailableMoves(multiverse);
        assertEquals(expectedMoveCount, allPieceMoves.size());
    }

    @Test
    void kingMoveDirectionsTest1() {

        Multiverse multiverse = new Multiverse.Builder(10)
                .withTimeline(
                        new Timeline.Builder(0)
                                .withBoard(
                                        new Board.Builder(10, 0, 0)
                                                .withWhitePiece(PieceType.KING, 3, 3)
                                                .withBlackPiece(PieceType.QUEEN, 3, 4)
                                                .build()
                                ).build()
                ).build();

        Piece piece = multiverse.getLocationContents(0, 0, 3, 3);
        assertMoveCount(multiverse, piece, 8);
    }

    @Test
    void kingMoveDirectionsTest2() {

        Multiverse multiverse = new Multiverse.Builder(10)
                .withTimeline(
                        new Timeline.Builder(0)
                                .withBoard(
                                        new Board.Builder(10, 0, 0)
                                                .withWhitePiece(PieceType.KING, 3, 3)
                                                .withBlackPiece(PieceType.QUEEN, 3, 4)
                                                .withWhitePiece(PieceType.QUEEN, 3, 2)
                                                .withWhitePiece(PieceType.ROOK, 2, 3)
                                                .build()
                                ).build()
                ).build();

        Piece piece = multiverse.getLocationContents(0, 0, 3, 3);
        assertMoveCount(multiverse, piece, 6);
    }

    @Test
    void knightMoveDirectionsTest1() {

        Multiverse multiverse = new Multiverse.Builder(10)
                .withTimeline(
                        new Timeline.Builder(0)
                                .withBoard(
                                        new Board.Builder(10, 0, 0)
                                                .withWhitePiece(PieceType.KNIGHT, 3, 3)
                                                .build()
                                ).build()
                ).build();

        Piece piece = multiverse.getLocationContents(0, 0, 3, 3);
        assertMoveCount(multiverse, piece, 8);
    }

    @Test
    void rookMoveDirectionTest1() {

        Multiverse multiverse = new Multiverse.Builder(10)
                .withTimeline(
                        new Timeline.Builder(0)
                                .withBoard(
                                        new Board.Builder(10, 0, 0)
                                                .withWhitePiece(PieceType.ROOK, 3, 3)
                                                .build()
                                ).build()
                ).build();

        Piece piece = multiverse.getLocationContents(0, 0, 3, 3);
        assertMoveCount(multiverse, piece, 18);
    }

    @Test
    void rookMoveDirectionTest2() {
        Multiverse multiverse = new Multiverse.Builder(10)
                .withTimeline(
                        new Timeline.Builder(0)
                                .withBoard(
                                        new Board.Builder(10, 0, 0)
                                                .withWhitePiece(PieceType.ROOK, 3, 3)
                                                .withWhitePiece(PieceType.PAWN, 3, 7)
                                                .withBlackPiece(PieceType.PAWN, 7, 3)
                                                .build()
                                ).build()
                ).build();

        Piece piece = multiverse.getLocationContents(0, 0, 3, 3);
        assertMoveCount(multiverse, piece, 13);
    }

    @Test
    void pawnMoveDirectionTest1() {

        Multiverse multiverse = new Multiverse.Builder(10)
                .withTimeline(
                        new Timeline.Builder(0)
                                .withBoard(
                                        new Board.Builder(10, 0, 0)
                                                .withWhitePiece(PieceType.PAWN, 1, 1)
                                                .withBlackPiece(PieceType.PAWN, 2, 3)
                                                .build()
                                ).build()
                ).build();

        Piece whitePawn = multiverse.getLocationContents(0, 0, 1, 1);
        assertMoveCount(multiverse, whitePawn, 2);

        Game game = new Game(multiverse);
        game.applyMove(
                new Move.Builder()
                        .withPiece(whitePawn)
                        .withTo(new Point4D(0, 0, 1, 3))
                        .build()
        );

        Piece blackPawn = multiverse.getLocationContents(0, 1, 2, 3);
        assertMoveCount(multiverse, blackPawn, 3);
    }

    @Test
    void pawnMoveDirectionTest2() {

        Multiverse multiverse = new Multiverse.Builder(10)
                .withTimeline(
                        new Timeline.Builder(0)
                                .withBoard(
                                        new Board.Builder(10, 0, 0)
                                                .withWhitePiece(PieceType.PAWN, 1, 1)
                                                .withBlackPiece(PieceType.PAWN, 1, 2)
                                                .withWhitePiece(PieceType.PAWN, 3, 1)
                                                .withBlackPiece(PieceType.PAWN, 3, 2)
                                                .withWhitePiece(PieceType.PAWN, 7, 1)
                                                .withBlackPiece(PieceType.PAWN, 8, 2)
                                                .build()
                                ).build()
                ).withTimeline(
                        new Timeline.Builder(-1)
                                .withBoard(
                                        new Board.Builder(10, -1, 0)
                                                .withWhitePiece(PieceType.PAWN, 5, 1)
                                                .build()
                                ).build()
                ).withTimeline(
                        new Timeline.Builder(-2)
                                .withBoard(
                                        new Board.Builder(10, -2, 0)
                                                .withWhitePiece(PieceType.PAWN, 1, 1)
                                                .build()
                                ).build()
                ).build();

        Piece pawn1 = multiverse.getLocationContents(0, 0, 1, 1);
        assertMoveCount(multiverse, pawn1, 1);

        Piece pawn2 = multiverse.getLocationContents(0, 0, 3, 1);
        assertMoveCount(multiverse, pawn2, 2);

        Piece pawn3 = multiverse.getLocationContents(-1, 0, 5, 1);
        assertMoveCount(multiverse, pawn3, 3);

        Piece pawn4 = multiverse.getLocationContents(0, 0, 1, 2);
        assertMoveCount(multiverse, pawn4, 0);

        Piece pawn5 = multiverse.getLocationContents(0, 0, 3, 2);
        assertMoveCount(multiverse, pawn5, 0);

        Piece pawn6 = multiverse.getLocationContents(0, 0, 7, 1);
        assertMoveCount(multiverse, pawn6, 5);
    }
}