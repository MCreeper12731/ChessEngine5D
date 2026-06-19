package com.github.mcreeper12731.evaluators;

import com.github.mcreeper12731.game.moves.Move;

import java.util.List;

public record ScoredTurn(List<Move> moves, int score, long nodesSearched) implements Comparable<ScoredTurn> {

    public ScoredTurn(List<Move> moves, int score) {
        this(moves, score, 0);
    }

    @Override
    public int compareTo(ScoredTurn o) {
        return Integer.compare(this.score, o.score);
    }

}
