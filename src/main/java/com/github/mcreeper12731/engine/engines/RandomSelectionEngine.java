package com.github.mcreeper12731.engine.engines;

import com.github.mcreeper12731.game.Game;
import com.github.mcreeper12731.game.models.Move;
import com.github.mcreeper12731.game.movegeneration.MoveGenerator;
import com.github.mcreeper12731.utility.Iterators;

import java.util.List;
import java.util.Random;

public class RandomSelectionEngine implements Engine {

    private final Random random;

    public RandomSelectionEngine() {
        random = new Random();
    }

    public RandomSelectionEngine(int seed) {
        random = new Random(seed);
    }

    public List<Move> nextTurn(Game game) {
        List<List<Move>> availableTurns = Iterators.consumeRemaining(MoveGenerator.getTurnsIterator(game));
        return availableTurns.get(random.nextInt(availableTurns.size()));
    }
}
