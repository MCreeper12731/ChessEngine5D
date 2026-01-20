package com.github.mcreeper12731.game.moves;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class SearchNode {

    private final Turn turn;
    private final List<SearchNode> children = new ArrayList<>();
    private int score;

    public SearchNode(Turn turn) {
        this.turn = turn;
    }

    public Turn getTurn() {
        return turn;
    }

    public Optional<SearchNode> getBestChild(boolean maximizing) {
        if (isLeaf()) return Optional.empty();

        SearchNode best = null;

        for (SearchNode child : children) {
            if (best == null) best = child;
            else if (maximizing && child.getScore() > best.getScore()) best = child;
            else if (!maximizing && child.getScore() < best.getScore()) best = child;
        }

        return Optional.ofNullable(best);
    }

    public List<SearchNode> getChildren() {
        return children;
    }

    public void addChild(SearchNode node) {
        children.add(node);
    }

    public boolean isLeaf() {
        return children.isEmpty();
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
