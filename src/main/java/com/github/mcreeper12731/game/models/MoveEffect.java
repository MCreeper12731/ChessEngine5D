package com.github.mcreeper12731.game.models;

public class MoveEffect {

    private final Move move;
    private final Color prevPlayerTurn;
    private final boolean prevGameOver;
    private final Color prevWinner;
    private final int primaryAddedBoardL;

    private Integer secondaryAddedBoardL = null;
    private Integer addedTimelineL = null;

    public MoveEffect(
            Move move,
            Color prevPlayerTurn,
            boolean prevGameOver,
            Color prevWinner,
            int primaryAddedBoardL
    ) {
        this.move = move;
        this.prevPlayerTurn = prevPlayerTurn;
        this.prevGameOver = prevGameOver;
        this.prevWinner = prevWinner;
        this.primaryAddedBoardL = primaryAddedBoardL;
    }

    public void setAddedTimelineL(int addedTimelineL) {
        this.addedTimelineL = addedTimelineL;
    }

    public void setSecondaryAddedBoardL(int secondaryAddedBoardL) {
        this.secondaryAddedBoardL = secondaryAddedBoardL;
    }

    public Move getMove() {
        return this.move;
    }

    public boolean isAddedTimeline() {
        return this.addedTimelineL != null;
    }

    public Integer getAddedTimeline() {
        return this.addedTimelineL;
    }

    public Color getPrevPlayerTurn() {
        return this.prevPlayerTurn;
    }

    public int getPrimaryAddedBoardL() {
        return this.primaryAddedBoardL;
    }

    public Integer getSecondaryAddedBoardL() {
        return this.secondaryAddedBoardL;
    }

    public boolean getPrevGameOver() {
        return this.prevGameOver;
    }

    public Color getPrevWinner() {
        return this.prevWinner;
    }

    @Override
    public String toString() {
        return move.toString();
    }
}
