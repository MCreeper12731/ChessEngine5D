package com.github.mcreeper12731.game.models;

import com.github.mcreeper12731.game.moves.*;
import com.github.mcreeper12731.game.pieces.Piece;
import com.github.mcreeper12731.game.pieces.PieceType;
import com.github.mcreeper12731.utility.Coordinate;

import java.util.*;

public class Multiverse {

    private final int boardSize;
    private final boolean isEven;
    private final Map<Double, Timeline> timelines = new HashMap<>();
    private final Stack<MoveEffect> currentTurnMoveEffects = new Stack<>();
    private final Stack<TurnEffect> turnEffects = new Stack<>();
    private final List<TurnPlayedListener> turnListeners = new ArrayList<>();

    private double bottomTimeline; // Value decrements - current lowest numbered timeline
    private double topTimeline; // Value increments - current highest numbered timeline
    private int presentTime;
    private Color playerTurn;
    private boolean lastMoveChangedTurns;
    private boolean gameOver = false;
    private Color winner = null;

    private Multiverse(
            int boardSize,
            boolean isEven,
            Map<Double, Timeline> timelines,
            double bottomTimeline,
            double topTimeline,
            Color playerTurn
    ) {
        this.boardSize = boardSize;
        this.isEven = isEven;
        this.timelines.putAll(timelines);
        this.bottomTimeline = bottomTimeline;
        this.topTimeline = topTimeline;
        this.presentTime = 0;
        this.playerTurn = playerTurn;
        this.lastMoveChangedTurns = false;
        this.winner = null;
    }

    public void addTimeline(double timelineId, Timeline timeline) {
        timelines.put(timelineId, timeline);
        if (timelineId > 0) {
            topTimeline = timelineId;
        }
        if (timelineId < 0) {
            bottomTimeline = timelineId;
        }
    }

    public Timeline getTimeline(double id) {
        return timelines.get(id);
    }

    public Optional<Piece> getPiece(Point4D location) {
        return getPiece(location.timeline(), location.time(), location.x(), location.y());
    }

    public Optional<Piece> getPiece(double timeline, int time, int x, int y) {
        return Optional.ofNullable(
                timelines.get(timeline).getBoardByTime(time).getPiece(x, y)
        );
    }

    public List<Double> getTimelineIndices() {
        List<Double> indices = new LinkedList<>();
        for (double i = this.getBottomTimeline(); i <= this.getTopTimeline(); i++) {
            if (this.isEven && i == 0) {
                indices.add(-0.5);
                indices.add(0.5);
                continue;
            }
            indices.add(i);
        }
        return indices;
    }

    public List<Double> getActiveTimelineIndices() {
        List<Double> indices = new LinkedList<>();

        double topActiveTimeline = topTimeline;
        double bottomActiveTimeline = bottomTimeline;

        if (topActiveTimeline > Math.abs(bottomActiveTimeline) + 1) {
            topActiveTimeline = Math.abs(bottomActiveTimeline) + 1;
        } else if (Math.abs(bottomActiveTimeline) > topActiveTimeline + 1) {
            bottomActiveTimeline = -(topActiveTimeline + 1);
        }

        for (double i = bottomActiveTimeline; i <= topActiveTimeline; i++) {
            if (isEven && i == 0) {
                indices.add(-0.5);
                indices.add(0.5);
                continue;
            }
            indices.add(i);
        }
        return indices;
    }

    public boolean isTimelineActive(double id) {
        return getTimeline(id).isActive();
    }

    public List<Timeline> getTimelines() {
        List<Timeline> timelines = new LinkedList<>();
        for (double index : getTimelineIndices()) {
            timelines.add(this.timelines.get(index));
        }
        return timelines;
    }

    public void applyAndFinalizeTurn(Turn turn, boolean silent) {

        applyTurn(turn);
        finalizeTurn(silent);
    }

    public void applyTurn(Turn turn) {

        if (!currentTurnMoveEffects.isEmpty())
            throw new RuntimeException("Cannot apply full turn as a turn is in progress!");

        for (Move move : turn.getMoves()) {
            applyMove(move);
        }
    }

    public void finalizeTurn(boolean silent) {

        if (!isCurrentTurnFinalizable())
            throw new RuntimeException("Cannot finalize turn - current moves do not complete a valid turn!");

        TurnEffect turnEffect = new TurnEffect(currentTurnMoveEffects);
        if (!silent) turnListeners.forEach(listener -> listener.onTurnPlayed(turnEffect.getTurn()));

        turnEffects.add(turnEffect);
        currentTurnMoveEffects.clear();

        updatePresent();
        nextPlayerTurn();
    }

    public void undoTurn() {

        if (!currentTurnMoveEffects.isEmpty()) {
            for (MoveEffect moveEffect : currentTurnMoveEffects) {
                undoMove(moveEffect);
            }
            return;
        }

        if (turnEffects.isEmpty()) return;

        TurnEffect turnEffect = turnEffects.pop();

        for (int i = turnEffect.size() - 1; i >= 0; i--) {
            undoMove(turnEffect.getMoveEffect(i));
        }
    }

    public void applyMove(Move move) {
        MoveEffect moveEffect = addMove(move);
        currentTurnMoveEffects.add(moveEffect);
    }

    private MoveEffect addMove(Move move) {

        Timeline fromTimeline = this.timelines.get(move.fromTimeline());

        Timeline toTimeline = this.timelines.get(move.toTimeline());
        Board toBoard = toTimeline.getBoardByTime(move.toTime());

        MoveEffect moveEffect = new MoveEffect(
                move,
                bottomTimeline,
                topTimeline,
                presentTime,
                playerTurn,
                lastMoveChangedTurns,
                gameOver,
                winner,
                getActiveTimelineIndices()
        );
        moveEffect.addTimelineOfAddedBoard(move.fromTimeline());

        if (move.fromTimeline() == move.toTimeline() && move.fromTime() == move.toTime()) {
            // Handling in-board moves

            fromTimeline.applyMove(move);

        } else if (move.toTime() != toTimeline.getLastTimeCoordinate()) { //TODO: if something breaks check this
            // Handling in- and cross-timeline, splitting moves

            fromTimeline.applyMove(move);

            double newTimelineId = nextTimelineId(move.pieceColor());
            int startingTime = move.toTime() + 1;

            Board newBoard = toBoard.copyWithProgressedTurn(
                    newTimelineId,
                    startingTime
            );
            newBoard.setPieceFromMoving(move.toX(), move.toY(), move.pieceColor(), move.pieceType());

            Timeline newTimeline = new Timeline.Builder(newTimelineId)
                    .withStartTime(startingTime)
                    .withBoard(newBoard)
                    .withActive(false)
                    .build();

            addTimeline(newTimelineId, newTimeline);
            moveEffect.setAddedTimeline(newTimelineId);
            moveEffect.addTimelineOfAddedBoard(newTimelineId);

        } else {
            // Handling cross-timeline, non-splitting moves

            fromTimeline.applyMove(move);
            toTimeline.applyMove(move);
            moveEffect.addTimelineOfAddedBoard(move.toTimeline());
        }

        getPiece(move.to())
                .ifPresent(piece -> {
                    if (piece.type() == PieceType.KING) {
                        gameOver = true;
                        winner = piece.color().other();
                    }
                });

        return moveEffect;
    }

    public void undoMoveFromCurrentTurn() {

        if (currentTurnMoveEffects.isEmpty()) return;

        undoMove(currentTurnMoveEffects.pop());
    }

    private void undoMove(MoveEffect moveEffect) {

        bottomTimeline = moveEffect.getPrevBottomTimeline();
        topTimeline = moveEffect.getPrevTopTimeline();
        presentTime = moveEffect.getPrevPresentTime();
        playerTurn = moveEffect.getPrevPlayerTurn();
        lastMoveChangedTurns = moveEffect.getPrevLastMoveChangedTurns();
        gameOver = moveEffect.getPrevGameOver();
        winner = moveEffect.getPrevWinner();

        for (double timelineIndex : getTimelineIndices()) {
            getTimeline(timelineIndex).setActive(
                    moveEffect.getPrevActiveTimelineIndices().contains(timelineIndex)
            );
        }

        for (double timelineOfAddedBoard : moveEffect.getTimelinesOfAddedBoards()) {
            timelines.get(timelineOfAddedBoard).removeLastBoard();
        }

        if (moveEffect.isAddedTimeline()) {
            timelines.remove(moveEffect.getAddedTimeline());
        }
    }

    private void updatePresent() {

        int minTime = Integer.MAX_VALUE;

        for (double timelineIndex : getActiveTimelineIndices()) {

            Timeline timeline = getTimeline(timelineIndex);

            // Very inefficient activation of timelines - perhaps at some point TODO: move this to addTimeline() with appropriate checks
            timeline.setActive(true);

            int lastTimelineTime = timeline.getLastBoard().getTime();
            if (lastTimelineTime < minTime) minTime = lastTimelineTime;
        }

        presentTime = minTime;
    }

    public boolean isCurrentTurnFinalizable() {

        for (double timelineIndex : getActiveTimelineIndices()) {

            Board lastBoard = getTimeline(timelineIndex).getLastBoard();

            if (
                    lastBoard.getPlayerTurn() == this.playerTurn &&
                    lastBoard.getTime() <= this.presentTime
            ) {
                lastMoveChangedTurns = false;
                return false;
            }
        }

        lastMoveChangedTurns = true;
        return true;
    }

    private double nextTimelineId(Color playerColor) {
        return switch (playerColor) {
            case WHITE -> getTopTimeline() == 0.5 ? 1 : getTopTimeline() + 1;
            case BLACK -> getBottomTimeline() == -0.5 ? -1 : getBottomTimeline() - 1;
        };
    }

    public boolean isLocationValid(Point4D point) {
        Timeline timeline = getTimeline(point.timeline());
        if (timeline == null) return false;
        if (point.time() < timeline.getFirstTimeCoordinate() || point.time() > timeline.getLastTimeCoordinate()) return false;
        if (point.x() < 0 || point.x() >= boardSize) return false;
        if (point.y() < 0 || point.y() >= boardSize) return false;

        return true;
    }

    @Override
    public String toString() {

        StringBuilder stringBuilder = new StringBuilder();

        for (Timeline t : timelines.values()) {
            stringBuilder.append(t.getId() < 0 ? "-" : "").append("L").append((int) t.getId()).append(":").append("\n");
            for (int i = 0; i < t.size(); i++) {
                stringBuilder.append("T").append(Coordinate.timeFromListToGame(i + t.getFirstTimeCoordinate())).append(" - ").append(Coordinate.colorFromListToGame(i + t.getFirstTimeCoordinate())).append(":").append("\n");
                stringBuilder.append(t.getBoardByIndex(i).toString()).append("\n");
                stringBuilder.append("\n");
            }
        }

        return stringBuilder.toString();
    }

    public void addListener(TurnPlayedListener listener) {
        turnListeners.add(listener);
    }

    public int getBoardSize() {
        return boardSize;
    }

    public double getBottomTimeline() {
        return bottomTimeline;
    }

    public double getTopTimeline() {
        return topTimeline;
    }

    public double getPresentTime() {
        return presentTime;
    }

    public Color getPlayerTurn() {
        return playerTurn;
    }

    public void setPlayerTurn(Color playerTurn) {
        this.playerTurn = playerTurn;
    }

    public void nextPlayerTurn() {
        setPlayerTurn(playerTurn.other());
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public Optional<Color> getWinner() {
        return Optional.ofNullable(winner);
    }

    public static class Builder {

        private final int boardSize;
        private final Map<Double, Timeline> timelines = new HashMap<>();
        private final List<List<List<Point4D>>> turns = new ArrayList<>();
        private double bottomTimeline = 0;
        private double topTimeline = 0;
        private Color startingPlayer = Color.WHITE;

        public Builder(int boardSize) {
            this.boardSize = boardSize;
        }

        public Builder(Multiverse multiverse) {
            this.boardSize = multiverse.boardSize;
            this.timelines.putAll(multiverse.timelines);
            this.bottomTimeline = multiverse.bottomTimeline;
            this.topTimeline = multiverse.topTimeline;
            this.startingPlayer = multiverse.playerTurn;
        }

        public Builder withTimeline(Timeline timeline) {
            timelines.put(timeline.getId(), timeline);

            if (timeline.getId() < bottomTimeline)
                bottomTimeline = timeline.getId();

            if (timeline.getId() > topTimeline)
                topTimeline = timeline.getId();

            return this;
        }

        public Builder withTurn(Point4D from, Point4D to) {
            turns.add(List.of(List.of(from, to)));
            return this;
        }

        public Builder withTurn(List<List<Point4D>> movePoints) {
            turns.add(movePoints);
            return this;
        }

        public Builder withStartingPlayer(Color playerColor) {
            startingPlayer = playerColor;
            return this;
        }

        public Multiverse build() {
            Multiverse multiverse = new Multiverse(
                    boardSize,
                    timelines.size() % 2 == 0,
                    timelines,
                    bottomTimeline,
                    topTimeline,
                    startingPlayer
            );

            MoveFactory factory = new MoveFactory(multiverse);
            MoveValidator moveValidator = new MoveValidator(multiverse);

            turns.forEach(turn -> {
                turn.forEach(movePoints -> {
                    if (movePoints.size() != 2) throw new RuntimeException("A move cannot contain more than 2 points");

                    Move move = factory.build(movePoints.get(0), movePoints.get(1));
                    if (!moveValidator.isValid(move))
                        throw new RuntimeException("Invalid move when building of the multiverse: " + move + "!");

                    multiverse.applyMove(move);
                });
                multiverse.finalizeTurn(true);
            });

            multiverse.turnEffects.clear();
            return multiverse;
        }
    }
}
