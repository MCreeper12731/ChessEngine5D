package com.github.mcreeper12731.game.moves;

import com.github.mcreeper12731.game.models.Color;

import java.util.*;

public class Turn implements Comparable<Turn> {

    private final List<Move> moves;
    private final Color playedBy;
    private final int penalty;

    public Turn(List<Move> moves) {
        this.moves = List.copyOf(moves);
        this.playedBy = moves.getFirst().pieceColor();
        int penalty = 0;
        for (Move move : moves) penalty += move.penalty();
        this.penalty = penalty / moves.size();
    }

    public List<Move> getMoves() {
        return moves;
    }

    public Color getPlayedBy() {
        return playedBy;
    }

    @Override
    public boolean equals(Object other) {
        if (other == null || getClass() != other.getClass()) return false;
        Turn turn = (Turn) other;
        return Objects.equals(moves, turn.moves);
    }

    @Override
    public int hashCode() {
        return Objects.hash(moves);
    }

    @Override
    public String toString() {
        return moves + "";
    }

    @Override
    public int compareTo(Turn turn) {
        return penalty - turn.penalty;
    }
}
