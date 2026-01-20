package com.github.mcreeper12731.evaluators;

import com.github.mcreeper12731.game.models.Board;
import com.github.mcreeper12731.game.models.Color;
import com.github.mcreeper12731.game.models.Multiverse;
import com.github.mcreeper12731.game.models.Timeline;
import com.github.mcreeper12731.game.pieces.PieceType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EvaluatorTest {

    @Test
    void stateEvaluationTest1() {

        Multiverse multiverse = new Multiverse.Builder(4)
                .withTimeline(
                        new Timeline.Builder(0)
                                .withBoard(
                                        new Board.Builder(4, 0, 0, Color.WHITE)
                                                .withWhitePiece(PieceType.KING, 0, 0)
                                                .withWhitePiece(PieceType.QUEEN, 1, 0)
                                                .withBlackPiece(PieceType.QUEEN, 2, 3)
                                                .withBlackPiece(PieceType.KING, 3, 3)
                                                .build()
                                )
                                .build()
                )
                .build();

        int whiteScore = Evaluator.evaluate(multiverse, Color.WHITE);
        int blackScore = Evaluator.evaluate(multiverse, Color.BLACK);

        assertEquals(0, whiteScore);
        assertEquals(0, blackScore);
    }

    @Test
    void stateEvaluationTest2() {

        Multiverse multiverse = new Multiverse.Builder(4)
                .withTimeline(
                        new Timeline.Builder(0)
                                .withBoard(
                                        new Board.Builder(4, 0, 0, Color.WHITE)
                                                .withWhitePiece(PieceType.KING, 0, 0)
                                                .withWhitePiece(PieceType.QUEEN, 1, 0)
                                                .withBlackPiece(PieceType.KING, 3, 3)
                                                .build()
                                )
                                .build()
                )
                .build();

        int whiteScore = Evaluator.evaluate(multiverse, Color.WHITE);
        int blackScore = Evaluator.evaluate(multiverse, Color.BLACK);

        assertEquals(Evaluator.PIECE_VALUE.get(PieceType.QUEEN), whiteScore);
        assertEquals(-Evaluator.PIECE_VALUE.get(PieceType.QUEEN), blackScore);
    }
}