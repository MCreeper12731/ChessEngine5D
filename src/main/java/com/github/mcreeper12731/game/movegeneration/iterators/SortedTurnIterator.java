package com.github.mcreeper12731.game.movegeneration.iterators;

import com.github.mcreeper12731.engine.evaluators.Evaluator;
import com.github.mcreeper12731.game.models.scored.ScoredTurn;
import com.github.mcreeper12731.game.Game;
import com.github.mcreeper12731.game.models.Move;

import java.util.*;

public class SortedTurnIterator implements Iterator<List<Move>> {

    private final Game game;
    private final Evaluator evaluator;
    private final Iterator<List<Move>> sourceIterator;
    private final Queue<ScoredTurn> generatedMoves;
    private final int batchSize;

    public SortedTurnIterator(Iterator<List<Move>> iterator, Game game, Evaluator evaluator) {
        this(iterator, game, evaluator, 5);
    }

    public SortedTurnIterator(Iterator<List<Move>> iterator, Game game, Evaluator evaluator, int batchSize) {
        this.game = game;
        this.evaluator = evaluator;
        this.sourceIterator = iterator;
        this.generatedMoves = new PriorityQueue<>((a, b) -> Double.compare(b.score(), a.score()));
        this.batchSize = batchSize;
        this.step();
    }

    private void step() {
        while (generatedMoves.size() < batchSize && sourceIterator.hasNext()) {
            List<Move> turn = sourceIterator.next();
            double score = evaluateTurn(turn);
            generatedMoves.add(new ScoredTurn(new ArrayList<>(turn), score));
        }
    }

    private double evaluateTurn(List<Move> turn) {
        this.game.applyMovesAndFinalizeTurn(turn);
        double score = this.evaluator.evaluate(this.game);
        this.game.undoTurn();
        return score;
    }

    @Override
    public boolean hasNext() {
        return !this.generatedMoves.isEmpty();
    }

    @Override
    public List<Move> next() {
        if (this.generatedMoves.isEmpty()) throw new java.util.NoSuchElementException();

        List<Move> result = generatedMoves.poll().moves();
        this.step();
        return result;
    }
}
