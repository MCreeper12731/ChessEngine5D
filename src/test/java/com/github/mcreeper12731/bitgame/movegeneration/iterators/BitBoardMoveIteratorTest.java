package com.github.mcreeper12731.bitgame.movegeneration.iterators;

import com.github.mcreeper12731.bitgame.BitGame;
import com.github.mcreeper12731.bitgame.models.BitBoard;
import com.github.mcreeper12731.bitgame.models.BitMultiverse;
import com.github.mcreeper12731.bitgame.models.BitTimeline;
import com.github.mcreeper12731.bitgame.models.pieces.BitPiece;
import com.github.mcreeper12731.game.models.Move;
import com.github.mcreeper12731.game.models.Point4D;
import com.github.mcreeper12731.game.models.pieces.PieceType;
import com.github.mcreeper12731.game.presets.Preset;
import com.github.mcreeper12731.utility.Iterators;
import com.github.mcreeper12731.utility.Log;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BitBoardMoveIteratorTest {

    @Test
    public void moveGenerationCorrect() {

        BitGame game = new BitGame(Preset.CUSTOM_SIMPLE_SINGLEBOARD.getGame());

        List<Move> moves = Iterators.consumeRemaining(BitPiece.getMoveIterator(game, new Point4D(0, 0, 0, 0)));
//        Log.debug("Test", moves);
        assertEquals(2, moves.size());
        assertEquals(List.of(Move.of(game, 0, 0, 0, 0, 0, 1), Move.of(game, 0, 0, 0, 0, 0, 2)), moves);

        game.applyMove(Move.of(game, 0, 0, 0, 0, 0, 1));
        game.finalizeTurn();

        moves = Iterators.consumeRemaining(BitPiece.getMoveIterator(game, new Point4D(0, 1, 3, 3)));
//        Log.debug("Test", moves);
        assertEquals(2, moves.size());
        assertEquals(List.of(Move.of(game, 0, 1, 3, 3, 3, 2), Move.of(game, 0, 1, 3, 3, 3, 1)), moves);

        game.applyMove(Move.of(game, 0, 1, 3, 3, 3, 2));

        moves = Iterators.consumeRemaining(BitPiece.getMoveIterator(game, new Point4D(0, 2, 0, 1)));
        assertEquals(1, moves.size());
        assertEquals(List.of(Move.of(game, 0, 2, 0, 1, 0, 2)), moves);
    }

    @Test
    public void moveGenerationCorrectWhenQueen() {

        BitGame game = new BitGame(
                new BitMultiverse.Builder(3)
                        .withTimeline(
                                new BitTimeline.Builder(0)
                                        .withBoard(
                                                new BitBoard.Builder(3, 0, 0)
                                                        .withWhitePiece(PieceType.QUEEN, 0, 0)
                                                        .withWhitePiece(PieceType.KING, 1, 0)
                                                        .withBlackPiece(PieceType.KING, 2, 2)
                                                        .build()
                                        ).build()
                        ).build()
        );

        List<Move> moves = Iterators.consumeRemaining(BitPiece.getMoveIterator(game, new Point4D(0, 0, 0, 0)));

        Log.debug("Test", moves);
        assertEquals(4, moves.size());

    }

    @Test
    public void moveGenerationCorrectWhenBishop() {

        BitGame game = new BitGame(
                new BitMultiverse.Builder(3)
                        .withTimeline(
                                new BitTimeline.Builder(0)
                                        .withBoard(
                                                new BitBoard.Builder(3, 0, 0)
                                                        .withWhitePiece(PieceType.BISHOP, 0, 0)
                                                        .withWhitePiece(PieceType.KING, 1, 0)
                                                        .withBlackPiece(PieceType.KING, 2, 2)
                                                        .build()
                                        ).build()
                        ).build()
        );

        List<Move> moves = Iterators.consumeRemaining(BitPiece.getMoveIterator(game, new Point4D(0, 0, 0, 0)));

        Log.debug("Test", moves);
        assertEquals(2, moves.size());

    }

    @Test
    public void moveGenerationCorrectWhenRook() {

        BitGame game = new BitGame(
                new BitMultiverse.Builder(3)
                        .withTimeline(
                                new BitTimeline.Builder(0)
                                        .withBoard(
                                                new BitBoard.Builder(3, 0, 0)
                                                        .withWhitePiece(PieceType.ROOK, 0, 0)
                                                        .withWhitePiece(PieceType.KING, 1, 0)
                                                        .withBlackPiece(PieceType.KING, 2, 2)
                                                        .build()
                                        ).build()
                        ).build()
        );

        List<Move> moves = Iterators.consumeRemaining(BitPiece.getMoveIterator(game, new Point4D(0, 0, 0, 0)));

        Log.debug("Test", moves);
        assertEquals(2, moves.size());

    }

}