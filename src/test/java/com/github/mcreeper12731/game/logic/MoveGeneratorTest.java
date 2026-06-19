package com.github.mcreeper12731.game.logic;

import com.github.mcreeper12731.game.models.Board;
import com.github.mcreeper12731.game.models.Timeline;
import com.github.mcreeper12731.game.models.Move;
import com.github.mcreeper12731.game.movegeneration.MoveGenerator;
import com.github.mcreeper12731.game.presets.Preset;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.*;

class MoveGeneratorTest {

    @Test
    public void probableMoveGenerationEqualsScoredMoveGeneration() {

        Game game = Preset.CUSTOM_COMPLEX_POSITION.getGame();

        int hashCodeBefore = game.hashCode();

        for (int l : game.getPlayableTimelineLs()) {

            Timeline timeline = game.getMultiverse().getTimeline(l);
            Board board = timeline.getLastBoard();

            Set<Move> probableMoves = new HashSet<>(MoveGenerator.probableMoves(board, game));
            Set<Move> scoredMoves = new HashSet<>(MoveGenerator.scoredMoves(board, game));

            assertEquals(probableMoves, scoredMoves);
        }

        assertEquals(hashCodeBefore, game.hashCode());
    }

    @Test
    public void probableMoveSupplierEqualsScoredMoveSupplier() {

        Game game = Preset.CUSTOM_COMPLEX_POSITION.getGame();

        int hashCodeBefore = game.hashCode();


        for (int l : game.getPlayableTimelineLs()) {

            Timeline timeline = game.getMultiverse().getTimeline(l);
            Board board = timeline.getLastBoard();

            Supplier<Iterator<Move>> probableMovesSupplier = MoveGenerator.probableMovesSupplier(board, game);
            Supplier<Iterator<Move>> scoredMovesSupplier = MoveGenerator.scoredMovesSupplier(board, game);

            for (int i = 0; i < 10; i++) {
                Set<Move> probableMoves = new HashSet<>();
                probableMovesSupplier.get().forEachRemaining(probableMoves::add);
                Set<Move> scoredMoves = new HashSet<>();
                scoredMovesSupplier.get().forEachRemaining(scoredMoves::add);

                assertEquals(probableMoves, scoredMoves);
            }

        }

        assertEquals(hashCodeBefore, game.hashCode());
    }

}