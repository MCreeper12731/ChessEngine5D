package com.github.mcreeper12731.bitgame.models;

import java.util.ArrayList;
import java.util.List;

public class BitTimeline {

    private final int l;
    private final int startTime;
    private final List<BitBoard> boards;

    public BitTimeline(int l, int startTime) {
        this.l = l;
        this.startTime = startTime;
        this.boards = new ArrayList<>();
    }

    private BitTimeline(int l, int startTime, List<BitBoard> boards) {
        this.l = l;
        this.startTime = startTime;
        this.boards = boards;
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

    public int getFirstT() {
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

    @Override
    public String toString() {
        return this.l + "";
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
            return new BitTimeline(
                    this.l, this.startTime, this.boards
            );
        }
    }

}
