package com.github.mcreeper12731.bitgame.models;

import java.util.ArrayList;
import java.util.List;

public class BitTimeline {

    private final int l;
    private final int startTime;
    private final List<BitBoard> boards;

    private BitTimeline(Builder builder) {
        this.l = builder.l;
        this.startTime = builder.startTime;
        this.boards = builder.boards;
    }

    public void addBoard(BitBoard board) {
        this.boards.add(board);
    }

    public int getL() {
        return this.l;
    }

    public int size() {
        return this.boards.size();
    }

    public int getFirstTimeCoordinate() {
        return this.startTime;
    }

    public int getLastT() {
        return this.startTime + this.size() - 1;
    }

    public BitBoard getBoardFromIndex(int index) {
        if (index < 0 || index >= this.boards.size()) return null;

        return this.boards.get(index);
    }

    public BitBoard getBoardFromT(int time) {
        return getBoardFromIndex(time - startTime);
    }

    public BitBoard getLastBoard() {
        return this.boards.getLast();
    }

    public void removeLastBoard() {
        this.boards.removeLast();
    }

    public static class Builder {

        private final int l;
        private final List<BitBoard> boards = new ArrayList<>();

        private int startTime = 0;

        public Builder(int l) {
            this.l = l;
        }

        public Builder withBoard(BitBoard board) {
            boards.add(board);
            return this;
        }

        public Builder withStartTime(int startTime) {
            this.startTime = startTime;
            return this;
        }

        public BitTimeline build() {
            return new BitTimeline(this);
        }
    }

}
