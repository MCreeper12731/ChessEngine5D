package com.github.mcreeper12731.evaluators;

import com.github.mcreeper12731.game.logic.Game;
import com.github.mcreeper12731.game.moves.Move;

public record ScoredMove(Move move, double score) implements Comparable<ScoredMove> {

    @Override
    public int compareTo(ScoredMove other) {
        return Double.compare(other.score, this.score);
    }
}
