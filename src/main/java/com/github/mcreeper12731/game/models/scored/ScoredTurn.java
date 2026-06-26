package com.github.mcreeper12731.game.models.scored;

import com.github.mcreeper12731.game.models.Move;

import java.util.List;

public record ScoredTurn(List<Move> moves, double score, long nodesSearched) implements Comparable<ScoredTurn> {

    public ScoredTurn(List<Move> moves, double score) {
        this(moves, score, 0);
    }

    @Override
    public int compareTo(ScoredTurn o) {
        return Double.compare(this.score, o.score);
    }

}
