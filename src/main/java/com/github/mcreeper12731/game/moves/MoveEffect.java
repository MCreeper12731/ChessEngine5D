package com.github.mcreeper12731.game.moves;

import com.github.mcreeper12731.game.models.Color;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MoveEffect {

    private final Move move;
    private final double prevBottomTimeline;
    private final double prevTopTimeline;
    private final int prevPresentTime;
    private final Color prevPlayerTurn;
    private final boolean prevLastMoveChangedTurns;
    private final boolean prevGameOver;
    private final Color prevWinner;
    private final List<Double> prevActiveTimelineIndices;

    private final List<Double> timelinesOfAddedBoards = new ArrayList<>();
    private Double addedTimeline = null;

    public MoveEffect(
            Move move,
            double prevBottomTimeline,
            double prevTopTimeline,
            int prevPresentTime,
            Color prevPlayerTurn,
            boolean prevLastMoveChangedTurns,
            boolean prevGameOver,
            Color prevWinner,
            List<Double> prevActiveTimelineIndices) {
        this.move = move;
        this.prevBottomTimeline = prevBottomTimeline;
        this.prevTopTimeline = prevTopTimeline;
        this.prevPresentTime = prevPresentTime;
        this.prevPlayerTurn = prevPlayerTurn;
        this.prevLastMoveChangedTurns = prevLastMoveChangedTurns;
        this.prevGameOver = prevGameOver;
        this.prevWinner = prevWinner;
        this.prevActiveTimelineIndices = Collections.unmodifiableList(prevActiveTimelineIndices);
    }

    public void setAddedTimeline(double addedTimeline) {
        this.addedTimeline = addedTimeline;
    }

    public void addTimelineOfAddedBoard(double timelineOfAddedBoard) {
        timelinesOfAddedBoards.add(timelineOfAddedBoard);
    }

    public Move getMove() {
        return move;
    }

    public boolean isAddedTimeline() {
        return addedTimeline != null;
    }

    public Double getAddedTimeline() {
        return addedTimeline;
    }

    public double getPrevBottomTimeline() {
        return prevBottomTimeline;
    }

    public double getPrevTopTimeline() {
        return prevTopTimeline;
    }

    public int getPrevPresentTime() {
        return prevPresentTime;
    }

    public Color getPrevPlayerTurn() {
        return prevPlayerTurn;
    }

    public boolean getPrevLastMoveChangedTurns() {
        return prevLastMoveChangedTurns;
    }

    public List<Double> getTimelinesOfAddedBoards() {
        return timelinesOfAddedBoards;
    }

    public boolean getPrevGameOver() {
        return prevGameOver;
    }

    public Color getPrevWinner() {
        return prevWinner;
    }

    public List<Double> getPrevActiveTimelineIndices() {
        return prevActiveTimelineIndices;
    }
}
