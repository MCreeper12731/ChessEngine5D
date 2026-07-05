package com.github.mcreeper12731.game.logic;

import com.github.mcreeper12731.MainApplication;
import com.github.mcreeper12731.game.Game;
import com.github.mcreeper12731.game.models.Board;
import com.github.mcreeper12731.game.models.Multiverse;
import com.github.mcreeper12731.game.models.Timeline;
import com.github.mcreeper12731.game.models.Move;
import com.github.mcreeper12731.game.movegeneration.iterators.BoardMoveIterator;
import com.github.mcreeper12731.game.movegeneration.iterators.TurnIterator;
import com.github.mcreeper12731.game.pieces.PieceType;
import com.github.mcreeper12731.game.presets.Preset;
import com.github.mcreeper12731.utility.Iterators;
import com.github.mcreeper12731.utility.Log;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TurnIteratorTest {

    @Test
    public void generatesAllSameBoardTurns() {

        Game game = new Game(new Multiverse.Builder(4)
                .withTimeline(new Timeline.Builder(0)
                        .withBoard(new Board.Builder(4, 0, 0)
                                .withWhitePiece(PieceType.PAWN, 0, 0)
                                .withBlackPiece(PieceType.PAWN, 3, 3)
                                .build()
                        ).build()
                ).build()
        );

        Iterator<List<Move>> iterator = new TurnIterator(game);

        List<List<Move>> moves = Iterators.consumeRemaining(iterator);
        Log.debug("Test", moves);

        assertEquals(2, moves.size());
    }

    @Test
    public void generatesAllTimeJumpTurns() {

        Game game = new Game(new Multiverse.Builder(3)
                .withTimeline(new Timeline.Builder(0)
                        .withBoard(new Board.Builder(3, 0, 0)
                                .withWhitePiece(PieceType.ROOK, 0, 0)
                                .withBlackPiece(PieceType.ROOK, 2, 2)
                                .build()
                        ).build()
                ).build()
        );

        assertEquals(4, game.getMultiverse().getLocationContents(0, 0, 0, 0).getAvailableMoves(game.getMultiverse()).size());
        Iterator<List<Move>> fullIterator = new TurnIterator(game);
        Iterator<List<Move>> iterator = new TurnIterator(game);

        List<List<Move>> allMoves = new ArrayList<>();
        fullIterator.forEachRemaining(allMoves::add);
        iterator.forEachRemaining(allMoves::add);

        assertEquals(8, allMoves.size());

        game.applyMovesAndFinalizeTurn(List.of(
                new Move.Builder(game)
                        .withFrom(0, 0, 0, 0)
                        .withTo(0, 0, 1, 0)
                        .build()
        ));

        assertEquals(4, game.getMultiverse().getLocationContents(0, 1, 2, 2).getAvailableMoves(game.getMultiverse()).size());
        fullIterator = new TurnIterator(game);
        iterator = new TurnIterator(game);

        allMoves = new ArrayList<>();
        fullIterator.forEachRemaining(allMoves::add);
        iterator.forEachRemaining(allMoves::add);

        assertEquals(8, allMoves.size());

        game.applyMovesAndFinalizeTurn(List.of(
                new Move.Builder(game)
                        .withFrom(0, 1, 2, 2)
                        .withTo(0, 1, 2, 1)
                        .build()
        ));

        assertEquals(5, game.getMultiverse().getLocationContents(0, 2, 1, 0).getAvailableMoves(game.getMultiverse()).size());

        Iterator<Move> boardIterator = new BoardMoveIterator(game.getMultiverse().getBoard(0, 2), game.getMultiverse());
        List<Move> allBoardMoves = new ArrayList<>();
        boardIterator.forEachRemaining(allBoardMoves::add);

        assertEquals(6, allBoardMoves.size()); // 5 + 1 because of noop

        fullIterator = new TurnIterator(game);
        iterator = new TurnIterator(game);

        allMoves = new ArrayList<>();
        fullIterator.forEachRemaining(allMoves::add);
        iterator.forEachRemaining(allMoves::add);

        assertEquals(10, allMoves.size());
    }

    @Test
    public void minimalTurnGenerationWhenActivatingTimelinesTest() {

        Game game = Preset.CUSTOM_COMPLEX_POSITION.getGame();
        game.clearTurnHistory();

        Iterator<List<Move>> turnsIterator = new TurnIterator(game);
        while (turnsIterator.hasNext()) {
            List<Move> turn = turnsIterator.next();
            // Log.debug("Test", turn);

            game.applyMovesFromTurnStart(turn);
            assertTrue(game.isCurrentTurnFinalizable());
            game.undoTurn();
        }
    }

    @Test
    public void simpleTurnGenerationTest() {

        Multiverse multiverse = new Multiverse.Builder(5)
                .withTimeline(new Timeline.Builder(0)
                        .withBoard(new Board.Builder(5, 0, 0)
                                .withWhitePiece(PieceType.PAWN, 2, 0)
                                .build()
                        ).build()
                ).build();

        Game game = new Game(multiverse);

        Iterator<List<Move>> turnsIterator = new TurnIterator(game);
        while (turnsIterator.hasNext()) {
            List<Move> turn = turnsIterator.next();

            game.applyMovesFromTurnStart(turn);
            assertTrue(game.isCurrentTurnFinalizable());
            game.undoTurn();
        }
    }

    @Test
    public void fullTurnGenerationTest() {

        Game game = Preset.PUZZLE_KNIGHT_6.getGame();

        game.applyMovesAndFinalizeTurn(List.of(
                new Move.Builder(game.getMultiverse())
                        .withFrom(0, 0, 0, 0)
                        .withTo(0, 0, 0, 1)
                        .build()
        ));

        game.applyMovesAndFinalizeTurn(List.of(
                new Move.Builder(game.getMultiverse())
                        .withFrom(0, 1, 4, 4)
                        .withTo(0, 1, 3, 3)
                        .build()
        ));

        game.applyMovesAndFinalizeTurn(List.of(
                new Move.Builder(game.getMultiverse())
                        .withFrom(0, 2, 2, 0)
                        .withTo(0, 2, 4, 1)
                        .build()
        ));

        game.applyMovesAndFinalizeTurn(List.of(
                new Move.Builder(game.getMultiverse())
                        .withFrom(0, 3, 3, 3)
                        .withTo(0, 1, 3, 3)
                        .build()
        ));

        game.applyMovesAndFinalizeTurn(List.of(
                new Move.Builder(game.getMultiverse())
                        .withFrom(-1, 2, 2, 0)
                        .withTo(0, 2, 4, 0)
                        .build()
        ));

        game.clearTurnHistory();

        Iterator<List<Move>> turnsIterator = new TurnIterator(game);
        while (turnsIterator.hasNext()) {
            List<Move> turn = turnsIterator.next();

            game.applyMovesFromTurnStart(turn);
            assertTrue(game.isCurrentTurnFinalizable());
            game.undoTurn();
        }
    }
}
