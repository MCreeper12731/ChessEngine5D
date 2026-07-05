package com.github.mcreeper12731.game.movegeneration.iterators;

import com.github.mcreeper12731.game.Game;
import com.github.mcreeper12731.game.models.*;
import com.github.mcreeper12731.game.models.scored.ScoredBoard;
import com.github.mcreeper12731.game.movegeneration.MoveGenerator;
import com.github.mcreeper12731.game.pieces.Piece;
import com.github.mcreeper12731.game.pieces.PieceType;
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

            //Log.debug("Test", turn);

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
        Log.debug("Test", "index:", scoredMoves.indexOf(moveSeeking));

        Iterator<List<Move>> turnIterator = MoveGenerator.getIterativeTurnIterator(game);
        List<List<Move>> allTurns = new ArrayList<>();
        turnIterator.forEachRemaining(turn -> {
            Log.debug("Test", turn);
            allTurns.add(turn);
        });

        Log.debug("Test", allTurns);

        assertTrue(allTurns.contains(List.of(moveSeeking)));
    }

    @Test
    public void turnStatistics() {

        Game game = new Game(
                new Multiverse.Builder(5)
                        .withTimeline(
                                new Timeline.Builder(-1)
                                        .withBoard(
                                                new Board.Builder(5, -1, 0)
                                                        .withWhitePiece(PieceType.KNIGHT, 2, 2)
                                                        .build()
                                        )
                                        .build()
                        )
                        .withTimeline(
                                new Timeline.Builder(0)
                                        .withBoard(
                                                new Board.Builder(5, 0, 0)
                                                        .withWhitePiece(PieceType.KNIGHT, 2, 2)
                                                        .build()
                                        )
                                        .build()
                        )
                        .withTimeline(
                                new Timeline.Builder(1)
                                        .withBoard(
                                                new Board.Builder(5, 1, 0)
                                                        .withWhitePiece(PieceType.KNIGHT, 2, 2)
                                                        .build()
                                        )
                                        .build()
                        )
                        .build()
        );

        Iterator<List<Move>> turns = MoveGenerator.getIterativeTurnIterator(game);

        List<List<Move>> allTurns = new ArrayList<>();
        turns.forEachRemaining(allTurns::add);

        List<Set<Move>> uniquePermutations = new ArrayList<>();
        int duplicatePermutations = 0;
        int invalidTurns = 0;
        int maxT = 0;

        for (List<Move> turn : allTurns) {

            game.applyMoves(turn);
            if (game.getPresentTime() > maxT)
                maxT = game.getPresentTime();
            if (!game.isCurrentTurnFinalizable()) {
                invalidTurns++;
            }
            game.undoTurn();

            boolean allSameTimeline = true;
            for (Move move : turn) {
                if (!move.noop() && move.from().l() != move.to().l()) {
                    allSameTimeline = false;
                    break;
                }
            }
            if (allSameTimeline) {

                Set<Move> hashedTurn = new HashSet<>(turn);
                for (Set<Move> uniquePermutation : uniquePermutations) {
                    if (uniquePermutation.equals(hashedTurn)) {
                        Log.debug("Test", "found duplicate", turn, "of turn", uniquePermutation);
                        duplicatePermutations++;
                        break;
                    }
                }

                uniquePermutations.add(hashedTurn);
                //Log.debug("Test", turn.toString());
            }
        }

        // Huge win:
        // total turns: ~9500 -> 732
        // invalid turns: ~50 -> 28
        // duplicate turns: >0 -> 0

        Log.debug("Test", allTurns.size());
        Log.debug("Test", "duplicate", duplicatePermutations);
        Log.debug("Test", "invalid", invalidTurns);
        Log.debug("Test", "maxT", maxT);
    }

    @Test
    public void doesNotGenerateMovesThatPutKingInCheck() {

        Game game = Preset.CHECKMATE_PRACTICE_QUEEN.getGame();

        Iterator<List<Move>> turnIterator = MoveGenerator.getIterativeTurnIterator(game);
        List<List<Move>> turns = new ArrayList<>();
        turnIterator.forEachRemaining(turns::add);
        Log.debug("Test", turns);

        Board board = game.getMultiverse().getBoard(0, 0);
        turns.forEach(turn -> {
            Move move = turn.getFirst();
            if (move.noop()) return;
            Log.debug("Test", move, isKingInCheck(board, move.to(), game));
            assertFalse(isKingInCheck(board, move.to(), game));
        });
    }

    private boolean isKingInCheck(Board board, Point4D toLocation, Game game) {

        ScoredBoard scoredBoard = new ScoredBoard(board, game);

        for (Piece piece : board.getPieces()) {
            if (piece.type() != PieceType.KING) continue;

            if (scoredBoard.danger().get(toLocation.x() + toLocation.y() * board.size()) > 0) {
                return true;
            }
        }
        return false;
    }
}