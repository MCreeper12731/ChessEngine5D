package com.github.mcreeper12731.game.models;

import com.github.mcreeper12731.game.pieces.Piece;
import com.github.mcreeper12731.utility.CompoundListView;
import com.github.mcreeper12731.utility.Log;

import java.util.*;

public class Multiverse {

    private final int boardSize;
    private final boolean isEven;
    // Timelines with L={-1, -2, ...}
    private final List<Timeline> negativeTimelines = new ArrayList<>();
    // Timelines with L={0, 1, 2, ...}
    private final List<Timeline> positiveTimelines = new ArrayList<>();

    private Multiverse(
            int boardSize,
            boolean isEven,
            List<Timeline> negativeTimelines,
            List<Timeline> positiveTimelines
    ) {
        this.boardSize = boardSize;
        this.isEven = isEven;
        this.negativeTimelines.addAll(negativeTimelines);
        this.positiveTimelines.addAll(positiveTimelines);
    }

    public void addTimeline(Timeline timeline) {
        if (timeline.getL() >= 0) {
            positiveTimelines.add(timeline);
            return;
        }
        negativeTimelines.add(timeline);
    }

    public Timeline getTimeline(int l) {
        if (l >= 0) {
            if (l >= positiveTimelines.size()) return null;
            return positiveTimelines.get(l);
        }
        if (l < -negativeTimelines.size()) return null;
        return negativeTimelines.get(-l - 1);
    }

    public void removeLastTimeline(boolean positive) {
        if (positive) {
            positiveTimelines.removeLast();
            return;
        }
        negativeTimelines.removeLast();
    }

    public Board getBoard(int l, int t) {
        Timeline timeline = this.getTimeline(l);
        if (timeline == null) return null;

        return timeline.getBoardByT(t);
    }

    /***
     *
     * @param location
     * @return the piece at the given 4D location. Returns a piece with PieceType.NONE if there is no piece at the given location. Returns null if the location is not in the multiverse.
     */
    public Piece getLocationContents(Point4D location) {
        return getLocationContents(location.l(), location.t(), location.x(), location.y());
    }

    /***
     * Gets the piece at the given 4D location
     * @param l
     * @param t
     * @param x
     * @param y
     * @return the piece at the given 4D location. Returns a piece with PieceType.NONE if there is no piece at the given location. Returns null if the location is not in the multiverse.
     */
    public Piece getLocationContents(int l, int t, int x, int y) {
        Timeline timeline = this.getTimeline(l);
        if (timeline == null) return null;

        Board board = timeline.getBoardByT(t);
        if (board == null) return null;

        return board.getLocationContents(x, y);
    }

    /**
     * Deprecated - iterate over Multiverse::getTimelines() and get L from Timeline::getL()
     * @return list of all timeline Ls
     */
    @Deprecated
    public List<Integer> getTimelineLs() {
        List<Integer> indices = new ArrayList<>();
        int botL = getBotTimelineL();
        int topL = getTopTimelineL();

        for (int i = botL; i <= topL; i++) {
            indices.add(i);
        }

        return indices;
    }

    public List<Integer> getActiveTimelineLs() {
        List<Integer> indices = new ArrayList<>();
        int botL = getBotTimelineL();
        int topL = getTopTimelineL();

        if (-botL - (this.isEven ? 1 : 0) > topL) {
            botL = -topL - (this.isEven ? 2 : 1);
        } else if (-botL < topL) {
            topL = -botL + (this.isEven ? 0 : 1);
        }

        for (int i = botL; i <= topL; i++) {
            indices.add(i);
        }

        return indices;
    }

    public boolean isTimelineActive(int id) {
        if (id < 0 && id < -this.getTopTimelineL() - (this.isEven() ? 2 : 1)) return false;
        if (id > 0 && id > -this.getBotTimelineL() + (this.isEven() ? 0 : 1)) return false;
        return true;
    }

    public List<Timeline> getTimelines() {
        return new CompoundListView<>(this.negativeTimelines.reversed(), this.positiveTimelines);
    }

    public int getBotTimelineL() {
        return -this.negativeTimelines.size();
    }

    public int getTopTimelineL() {
        return this.positiveTimelines.size() - 1;
    }

    public int getTimelineCount() {
        return this.negativeTimelines.size() + this.positiveTimelines.size();
    }

    public boolean isEven() {
        return this.isEven;
    }

    public int getBoardSize() {
        return boardSize;
    }

    @Override
    public String toString() {

        StringBuilder stringBuilder = new StringBuilder();

        for (Timeline timeline : this.getTimelines()) {
            stringBuilder.append(timeline.getL() >= 0 ? "" : "-").append("L").append(Math.abs(timeline.getL())).append(":").append("\n");
            for (int i = 0; i < timeline.size(); i++) {
                stringBuilder.append("T").append((i + timeline.getFirstTimeCoordinate()) / 2 + 1).append(" - ").append(timeline.getBoardByIndex(i).getPlayerTurn()).append(":").append("\n");
                stringBuilder.append(timeline.getBoardByIndex(i).toString()).append("\n");
                stringBuilder.append("\n");
            }
        }

        return stringBuilder.toString();
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (!(other instanceof Multiverse)) return false;
        return this.hashCode() == other.hashCode();
    }

    @Override
    public int hashCode() {
        return Objects.hash(boardSize, negativeTimelines, positiveTimelines);
    }

    public static class Builder {

        private final int boardSize;
        private final List<Timeline> negativeTimelines = new ArrayList<>();
        private final List<Timeline> positiveTimelines = new ArrayList<>();

        private boolean isEven = false;

        public Builder(int boardSize) {
            this.boardSize = boardSize;
        }

        public Builder(Multiverse multiverse) {
            this.boardSize = multiverse.boardSize;
            this.negativeTimelines.addAll(multiverse.negativeTimelines);
            this.positiveTimelines.addAll(multiverse.positiveTimelines);
        }

        public Builder withTimeline(Timeline timeline) {

            if (timeline.getL() >= 0) {
                this.positiveTimelines.add(timeline);
            } else {
                this.negativeTimelines.add(timeline);
            }
            return this;
        }

        @Deprecated
        public Builder withTurn(Point4D from, Point4D to) {
            Log.debug("WARNING", "Using deprecated withTurn() - check preset");
            return this;
        }

        public Builder even() {
            this.isEven = true;
            return this;
        }

        public Multiverse build() {

            this.negativeTimelines.sort((timeline, other) -> other.getL() - timeline.getL());
            this.positiveTimelines.sort(Comparator.comparingInt(Timeline::getL));

            List<Integer> negativeTimelineLs = this.negativeTimelines.stream().map(Timeline::getL).toList();
            List<Integer> positiveTimelineLs = this.positiveTimelines.stream().map(Timeline::getL).toList();

            if (new HashSet<>(this.negativeTimelines).size() != this.negativeTimelines.size() ||
                    new HashSet<>(this.positiveTimelines).size() != this.positiveTimelines.size()) throw new IllegalArgumentException("Timelines must be unique");

            for (int i = 0; i < negativeTimelineLs.size(); i++) {
                if (negativeTimelineLs.get(i) != -i - 1) throw new IllegalArgumentException("Negative timelines must be in order and start at -1");
            }
            for (int i = 0; i < positiveTimelineLs.size(); i++) {
                if (positiveTimelineLs.get(i) != i) throw new IllegalArgumentException("Positive timelines must be in order and start at 0");
            }

            return new Multiverse(
                    this.boardSize,
                    this.isEven,
                    this.negativeTimelines,
                    this.positiveTimelines
            );
        }
    }
}
