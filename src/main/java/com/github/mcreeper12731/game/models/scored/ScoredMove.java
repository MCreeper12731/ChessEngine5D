package com.github.mcreeper12731.game.models.scored;

import com.github.mcreeper12731.game.models.Move;

public record ScoredMove(Move move, double score) implements Comparable<ScoredMove> {

    @Override
    public int compareTo(ScoredMove other) {
        return Double.compare(other.score, this.score);
    }
}
