package com.github.mcreeper12731.game.moves;

import java.util.HashMap;
import java.util.Map;

public class TurnNode {

    private final Turn turn;
    private final Map<Turn, TurnNode> responses = new HashMap<>();
    private int score;

    public TurnNode(Turn turn) {
        this.turn = turn;
    }

    public Turn getTurn() {
        return turn;
    }

    public void addResponse(Turn enemyTurn, TurnNode nodeTree) {
        responses.put(enemyTurn, nodeTree);
    }

    public TurnNode getResponse(Turn turn) {
        return responses.get(turn);
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public boolean isLeaf() {
        return responses.isEmpty();
    }

    public void print() {
        print(0);
    }

    private void print(int depth) {

        System.out.println(" ".repeat(depth) + turn);
        for (Map.Entry<Turn, TurnNode> entry : responses.entrySet()) {
            System.out.println(" ".repeat(depth + 2) + entry.getKey());
            entry.getValue().print(depth + 4);
        }
    }
}
