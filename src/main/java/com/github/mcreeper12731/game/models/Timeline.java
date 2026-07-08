package com.github.mcreeper12731.game.models;

import java.util.ArrayList;
import java.util.List;

public class Timeline {

    private final int l;
    private final int startTime;
    private final List<Board> boards;

    private Timeline(Builder builder) {
        this.l = builder.l;
        this.startTime = builder.startTime;
        this.boards = builder.boards;
    }

    public void addBoard(Board board) {
        this.boards.add(board);
    }

    public int getL() {
        return this.l;
    }

    public int size() {
        return this.boards.size();
    }

    public int getFirstT() {
        return this.startTime;
    }

    public int getLastT() {
        return this.startTime + this.size() - 1;
    }

    public Board getBoardFromIndex(int index) {
        if (index < 0 || index >= this.boards.size()) return null;

        return this.boards.get(index);
    }

    public Board getBoardFromT(int time) {
        return getBoardFromIndex(time - startTime);
    }

    public Board getLastBoard() {
        return this.boards.getLast();
    }

    public void removeLastBoard() {
        this.boards.removeLast();
    }

    public static class Builder {

        private final int l;
        private final List<Board> boards = new ArrayList<>();

        private int startTime = 0;

        public Builder(int l) {
            this.l = l;
        }

        public Builder withBoard(Board board) {
            boards.add(board);
            return this;
        }

        public Builder withStartTime(int startTime) {
            this.startTime = startTime;
            return this;
        }

        public Timeline build() {
            return new Timeline(this);
        }
    }
}
