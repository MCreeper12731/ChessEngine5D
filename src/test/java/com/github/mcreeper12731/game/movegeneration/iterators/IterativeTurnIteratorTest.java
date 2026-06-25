package com.github.mcreeper12731.game.movegeneration.iterators;

import com.github.mcreeper12731.MainApplication;
import com.github.mcreeper12731.game.logic.Game;
import com.github.mcreeper12731.game.models.Board;
import com.github.mcreeper12731.game.models.Move;
import com.github.mcreeper12731.game.models.Timeline;
import com.github.mcreeper12731.game.models.scored.ScoredBoard;
import com.github.mcreeper12731.game.movegeneration.MoveGenerator;
import com.github.mcreeper12731.game.presets.Preset;
import com.github.mcreeper12731.utility.Log;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class IterativeTurnIteratorTest {

    @Test
    public void testSimpleTurnGeneration() {

        Game game = Preset.CUSTOM_SIMPLE_POSITION.getGame();
        List<List<Move>> timelineMoves = new ArrayList<>();
        for (Timeline timeline : game.getMultiverse().getTimelines()) {
            List<Move> moves = MoveGenerator.scoredMoves(timeline.getLastBoard(), game);
            timelineMoves.add(moves);
        }

        Iterator<List<Move>> turnsIterator = new IterativeTurnIterator(game, timelineMoves);

        while (turnsIterator.hasNext()) {
            List<Move> turn = turnsIterator.next();
            game.applyMovesFromTurnStart(turn);
            assertTrue(game.isCurrentTurnFinalizable());
            game.undoAllMovesFromCurrentTurn();
        }
    }

    @Test
    public void testMultiboardTurnGeneration() {

        Game game = Preset.CUSTOM_COMPLEX_POSITION.getGame();
        List<List<Move>> timelineMoves = new ArrayList<>();
        for (Timeline timeline : game.getMultiverse().getTimelines()) {
            List<Move> moves = MoveGenerator.scoredMoves(timeline.getLastBoard(), game);
            timelineMoves.add(moves);
        }

        Iterator<List<Move>> turnsIterator = new IterativeTurnIterator(game, timelineMoves);

        while (turnsIterator.hasNext()) {
            List<Move> turn = turnsIterator.next();

            Log.debug("Test", turn);

            game.applyMovesFromTurnStart(turn);
            assertTrue(game.isCurrentTurnFinalizable());
            game.undoAllMovesFromCurrentTurn();
        }
    }

    @Test
    public void orderGenerationCorrect() {

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

        ScoredBoard board = new ScoredBoard(game.getMultiverse().getBoard(0, 2), game);
        Log.debug("Test", board.scoreMoves(game));

        List<List<Move>> boardsMoves = new ArrayList<>();
        for (Timeline timeline : game.getMultiverse().getTimelines()) {
            List<Move> moves = MoveGenerator.scoredMoves(timeline.getLastBoard(), game);
            boardsMoves.add(moves);
        }
        Log.debug("Test", boardsMoves);

        Iterator<List<Move>> turnIterator = MoveGenerator.getIterativeTurnIterator(game);

        while (turnIterator.hasNext()) {
            Log.debug("Test", turnIterator.next());
        }
    }

    @Test
    public void generatesAllTurns() {

        Game game = Preset.PUZZLE_QUEEN_1.getGame();

        game.applyMovesAndFinalizeTurn(List.of(
                new Move.Builder(game)
                        .withPiece(game.getMultiverse().getLocationContents(0, 2, 2, 0))
                        .withTo(0, 2, 2, 1)
                        .build()
        ));

        game.applyMovesAndFinalizeTurn(List.of(
                new Move.Builder(game)
                        .withPiece(game.getMultiverse().getLocationContents(0, 3, 3, 2))
                        .withTo(0, 3, 3, 3)
                        .build()
        ));

        Move moveSeeking = new Move.Builder(game.getMultiverse())
                .withFrom(0, 4, 2, 1)
                .withTo(0, 2, 3, 2)
                .build();

        //MainApplication.launchWithGame(game);

        Board board = game.getMultiverse().getBoard(0, 4);
        List<Move> probableMoves = MoveGenerator.probableMoves(board, game);
        assertTrue(probableMoves.contains(moveSeeking));

        List<Move> scoredMoves = MoveGenerator.scoredMoves(board, game);
        assertTrue(scoredMoves.contains(moveSeeking));
        Log.debug("Test", scoredMoves.indexOf(moveSeeking));

        Iterator<List<Move>> turnIterator = MoveGenerator.getIterativeTurnIterator(game);
        List<List<Move>> allTurns = new ArrayList<>();
        turnIterator.forEachRemaining(turn -> {
            Log.debug("Test", turn);
            allTurns.add(turn);
        });

        assertTrue(allTurns.contains(List.of(moveSeeking)));
    }
}