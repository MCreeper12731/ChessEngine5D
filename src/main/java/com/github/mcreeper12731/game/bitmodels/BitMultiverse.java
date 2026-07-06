package com.github.mcreeper12731.game.bitmodels;

import com.github.mcreeper12731.game.models.Multiverse;
import com.github.mcreeper12731.game.models.Point4D;
import com.github.mcreeper12731.game.models.Timeline;
import com.github.mcreeper12731.utility.listviews.CompoundListView;
import com.github.mcreeper12731.utility.listviews.MappedListView;
import com.github.mcreeper12731.utility.listviews.ReducedListView;

import java.util.*;

public class BitMultiverse {

    private final int boardSize;
    private final boolean isEven;
    // Timelines with L={-1, -2, ...}
    private final List<BitTimeline> negativeTimelines = new ArrayList<>();
    // Timelines with L={0, 1, 2, ...}
    private final List<BitTimeline> positiveTimelines = new ArrayList<>();

    private BitMultiverse(
            Builder builder
    ) {
        this.boardSize = builder.boardSize;
        this.isEven = builder.isEven;
        this.negativeTimelines.addAll(builder.negativeTimelines);
        this.positiveTimelines.addAll(builder.positiveTimelines);
    }

    public void addTimeline(BitTimeline timeline) {
        if (timeline.getL() >= 0) {
            positiveTimelines.add(timeline);
            return;
        }
        negativeTimelines.add(timeline);
    }

    public BitTimeline getTimeline(int l) {
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

    public BitBoard getBoard(int l, int t) {
        BitTimeline timeline = this.getTimeline(l);
        if (timeline == null) return null;

        return timeline.getBoardFromT(t);
    }

    public byte getLocationContents(Point4D location) {
        return this.getLocationContents(location.l(), location.t(), location.x(), location.y());
    }

    public byte getLocationContents(int l, int t, int x, int y) {
        BitTimeline timeline = this.getTimeline(l);
        if (timeline == null) return 0;

        BitBoard board = timeline.getBoardFromT(t);
        if (board == null) return 0;

        return board.getLocationContents(x, y);
    }

    public List<Integer> getTimelineLs() {
        return new MappedListView<>(this.getTimelines(), BitTimeline::getL);
    }

    public List<Integer> getActiveTimelineLs() {
        return new MappedListView<>(this.getActiveTimelines(), BitTimeline::getL);
    }

    public boolean isTimelineActive(BitTimeline timeline) {
        return this.isTimelineActive(timeline.getL());
    }

    public boolean isTimelineActive(int id) {
        if (id < 0 && id < -this.getTopTimelineL() - (this.isEven() ? 2 : 1)) return false;
        if (id > 0 && id > -this.getBotTimelineL() + (this.isEven() ? 0 : 1)) return false;
        return true;
    }

    public List<BitTimeline> getTimelines() {
        return new CompoundListView<>(this.negativeTimelines.reversed(), this.positiveTimelines);
    }

    public List<BitTimeline> getActiveTimelines() {
        int botL = this.getBotTimelineL();
        int topL = this.getTopTimelineL();

        if (-botL - (this.isEven ? 1 : 0) > topL) {
            botL = -topL - (this.isEven ? 2 : 1);
        } else if (-botL < topL) {
            topL = -botL + (this.isEven ? 0 : 1);
        }

        botL += this.negativeTimelines.size();
        topL += this.negativeTimelines.size();

        return new ReducedListView<>(this.getTimelines(), botL, topL + 1);
    }

    public int getBotTimelineL() {
        return -this.negativeTimelines.size();
    }

    public int getTopTimelineL() {
        return this.positiveTimelines.size() - 1;
    }

    public boolean isEven() {
        return this.isEven;
    }

    public int getBoardSize() {
        return this.boardSize;
    }

    @Override
    public String toString() {

        StringBuilder stringBuilder = new StringBuilder();

        for (BitTimeline timeline : this.getTimelines()) {
            stringBuilder.append(timeline.getL() >= 0 ? "" : "-").append("L").append(Math.abs(timeline.getL())).append(":").append("\n");
            for (int i = 0; i < timeline.size(); i++) {
                stringBuilder.append("T").append((i + timeline.getFirstTimeCoordinate()) / 2 + 1).append(" - ").append(timeline.getBoardFromIndex(i).getPlayerTurn()).append(":").append("\n");
                stringBuilder.append(timeline.getBoardFromIndex(i).toString()).append("\n");
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
        return Objects.hash(this.boardSize, this.negativeTimelines, this.positiveTimelines);
    }

    public static class Builder {

        private final int boardSize;
        private final List<BitTimeline> negativeTimelines = new ArrayList<>();
        private final List<BitTimeline> positiveTimelines = new ArrayList<>();

        private boolean isEven = false;

        public Builder(int boardSize) {
            this.boardSize = boardSize;
        }

        public Builder withTimeline(BitTimeline timeline) {

            if (timeline.getL() >= 0) {
                this.positiveTimelines.add(timeline);
            } else {
                this.negativeTimelines.add(timeline);
            }
            return this;
        }

        public Builder withEven() {
            this.isEven = true;
            return this;
        }

        public BitMultiverse build() {

            this.negativeTimelines.sort((timeline, other) -> other.getL() - timeline.getL());
            this.positiveTimelines.sort(Comparator.comparingInt(BitTimeline::getL));

            List<Integer> negativeTimelineLs = this.negativeTimelines.stream().map(BitTimeline::getL).toList();
            List<Integer> positiveTimelineLs = this.positiveTimelines.stream().map(BitTimeline::getL).toList();

            if (new HashSet<>(this.negativeTimelines).size() != this.negativeTimelines.size() ||
                    new HashSet<>(this.positiveTimelines).size() != this.positiveTimelines.size()) throw new IllegalArgumentException("Timelines must be unique");

            for (int i = 0; i < negativeTimelineLs.size(); i++) {
                if (negativeTimelineLs.get(i) != -i - 1) throw new IllegalArgumentException("Negative timelines must be in order and start at -1");
            }
            for (int i = 0; i < positiveTimelineLs.size(); i++) {
                if (positiveTimelineLs.get(i) != i) throw new IllegalArgumentException("Positive timelines must be in order and start at 0");
            }

            return new BitMultiverse(this);
        }
    }

}
