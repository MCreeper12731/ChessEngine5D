package com.github.mcreeper12731.game.logic;

import com.github.mcreeper12731.game.moves.Move;
import com.github.mcreeper12731.game.presets.Preset;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class SortedTurnIteratorTest {

    @Test
    public void generatesSameTurnsAsTurnsIterator() {

        Game game = Preset.PUZZLE_KNIGHT_6.getGame();

        Iterator<List<Move>> iterator = MoveGenerator.getTurnsIterator(game);
        Iterator<List<Move>> sortedIterator = MoveGenerator.getSortedTurnsIterator(game, g -> 0);

        Set<List<Move>> iteratorResult = new HashSet<>();
        iterator.forEachRemaining(iteratorResult::add);

        Set<List<Move>> sortedIteratorResult = new HashSet<>();
        sortedIterator.forEachRemaining(sortedIteratorResult::add);

        assertEquals(iteratorResult, sortedIteratorResult);
    }

}
