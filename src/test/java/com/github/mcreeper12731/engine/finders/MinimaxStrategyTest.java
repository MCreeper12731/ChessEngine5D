package com.github.mcreeper12731.engine.finders;

import com.github.mcreeper12731.engine.ChessEngine;
import com.github.mcreeper12731.engine.config.MoveStrategyConfig;
import com.github.mcreeper12731.game.models.*;
import com.github.mcreeper12731.game.moves.Turn;
import com.github.mcreeper12731.game.pieces.PieceType;
import com.github.mcreeper12731.game.presets.puzzles.OpeningTraps4Puzzle;
import com.github.mcreeper12731.game.presets.puzzles.Puzzle;
import com.github.mcreeper12731.game.presets.puzzles.RookTactics1Puzzle;
import com.github.mcreeper12731.game.presets.puzzles.RookTactics2Puzzle;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MinimaxStrategyTest {

    @Test
    void minimaxTurnTest1() {

        Multiverse multiverse = new Multiverse.Builder(4)
                .withTimeline(
                        new Timeline.Builder(0)
                                .withBoard(
                                        new Board.Builder(4, 0, 0, Color.WHITE)
                                                .withWhitePiece(PieceType.QUEEN, 0, 0)
                                                .withWhitePiece(PieceType.KING, 0,1)
                                                .withBlackPiece(PieceType.KING, 3, 3)
                                                .build()
                                )
                                .build()
                )
                .build();

        ChessEngine engine = new ChessEngine(
                multiverse,
                Color.WHITE,
                MoveStrategyConfig.fromParams("minimax", "1")
        );

        Turn engineTurn = engine.getTurn();

        assertEquals(1, engineTurn.getMoves().size());
        assertEquals(
                new Point4D(0, 0, 3, 3),
                engineTurn.getMoves().getFirst().to()
        );
    }

    @Test
    void minimaxTurnTest2() {

        Puzzle puzzle = new RookTactics1Puzzle();

        Multiverse multiverse = puzzle.createMultiverse();

        ChessEngine engine = new ChessEngine(
                multiverse,
                Color.WHITE,
                MoveStrategyConfig.fromParams("minimax", String.valueOf(puzzle.moveLimit()))
        );

        /*Turn engineTurn = engine.getTurn();

        assertEquals(1, engineTurn.getMoves().size());
        assertEquals(
                new Point4D(0, 4, 4, 4),
                engineTurn.getMoves().getFirst().to()
        );*/
    }

    @Test
    void minimaxTurnTest3() {

        Puzzle puzzle = new RookTactics2Puzzle();

        Multiverse multiverse = puzzle.createMultiverse();

        ChessEngine engine = new ChessEngine(
                multiverse,
                Color.WHITE,
                MoveStrategyConfig.fromParams("minimax", String.valueOf(puzzle.moveLimit()))
        );

        Turn engineTurn = engine.getTurn();

        System.out.println(engineTurn);
    }
}