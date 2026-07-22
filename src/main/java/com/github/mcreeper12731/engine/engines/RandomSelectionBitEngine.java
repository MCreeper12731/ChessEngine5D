package com.github.mcreeper12731.engine.engines;

import com.github.mcreeper12731.bitgame.BitGame;
import com.github.mcreeper12731.bitgame.movegeneration.BitMoveGenerator;
import com.github.mcreeper12731.game.Game;
import com.github.mcreeper12731.game.models.Move;
import com.github.mcreeper12731.game.movegeneration.MoveGenerator;
import com.github.mcreeper12731.utility.Iterators;

import java.util.List;
import java.util.Random;

public class RandomSelectionBitEngine implements BitEngine {

    private final Random random;

    public RandomSelectionBitEngine() {
        random = new Random();
    }

    public RandomSelectionBitEngine(int seed) {
        random = new Random(seed);
    }

    public List<Move> nextTurn(BitGame game) {
        List<List<Move>> availableTurns = Iterators.consumeRemaining(BitMoveGenerator.getTurnsIterator(game));
        return availableTurns.get(random.nextInt(availableTurns.size()));
    }
}
