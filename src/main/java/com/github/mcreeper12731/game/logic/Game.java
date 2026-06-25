package com.github.mcreeper12731.game.logic;

import com.github.mcreeper12731.game.models.*;
import com.github.mcreeper12731.game.pieces.Piece;
import com.github.mcreeper12731.game.pieces.PieceType;

import java.util.*;

public class Game {

    private final Multiverse multiverse;
    private final Stack<MoveEffect> currentTurnMoveEffects = new Stack<>();
    private final Stack<Stack<MoveEffect>> turnEffects = new Stack<>();

    private int presentTime;
    private Color playerTurn = Color.WHITE;
    private boolean gameOver = false;
    private Color winner = null;

    public Game(Multiverse multiverse) {
        this.multiverse = multiverse;
    }

    // Turn-related logic

    public void applyMovesAndFinalizeTurn(List<Move> turn) {

        this.applyMovesFromTurnStart(turn);
        this.finalizeTurn();
    }

    public void applyMovesFromTurnStart(List<Move> turn) {

        if (!this.currentTurnMoveEffects.isEmpty())
            throw new RuntimeException("Cannot apply full turn as a turn is in progress!");

        this.applyMoves(turn);
    }

    @SuppressWarnings("unchecked")
    public void finalizeTurn() {

        if (!this.isCurrentTurnFinalizable()) {
            System.out.println(this);
            throw new RuntimeException("Cannot finalize turn - current moves do not complete a valid turn!");
        }

        Stack<MoveEffect> turnEffect = (Stack<MoveEffect>) currentTurnMoveEffects.clone();
        //if (!isSilent) this.turnListeners.forEach(listener -> listener.onTurnPlayed(turnEffect.getTurn()));

        this.turnEffects.add(turnEffect);
        this.currentTurnMoveEffects.clear();

        this.playerTurn = this.playerTurn.other();
    }

    // Move-related logic

    public void applyMoves(List<Move> turn) {
        for (Move move : turn) {
            this.applyMove(move);
        }
    }

    public void applyMove(Move move) {

        if (move.noop() && (move.from() == null || move.to() == null)) return;

        Timeline fromTimeline = this.multiverse.getTimeline(move.from().l());
        Timeline toTimeline = this.multiverse.getTimeline(move.to().l());

        if (toTimeline == null) {
            System.out.println(this);
            System.out.println("Attempted move: " + move);
            throw new RuntimeException("Cannot apply move - timeline does not exist!");
        }

        Board toBoard = toTimeline.getBoardByT(move.to().t());

        if (toBoard == null) {
            System.out.println(this);
            System.out.println("Attempted move: " + move);
            throw new RuntimeException("Cannot apply move - contents does not exist!");
        }

        MoveEffect moveEffect = new MoveEffect(
                move,
                this.playerTurn,
                this.gameOver,
                this.winner,
                move.from().l()
        );

        if (move.from().l() == move.to().l() && move.from().t() == move.to().t()) {
            // Handling in-board moves

            this.applyMoveToTimeline(fromTimeline, move);

        } else if (move.to().t() != toTimeline.getLastT()) {
            // Handling timeline splitting moves

            this.applyMoveToTimeline(fromTimeline, move);

            int newTimelineL = this.playerTurn == Color.WHITE ?
                    this.multiverse.getTopTimelineL() + 1 :
                    this.multiverse.getBotTimelineL() - 1;

            int startingTime = move.to().t() + 1;

            Board newBoard = new Board.Builder(toBoard, newTimelineL, startingTime, move)
                    .build();

            Timeline newTimeline = new Timeline.Builder(newTimelineL)
                    .withStartTime(startingTime)
                    .withBoard(newBoard)
                    .build();

            this.multiverse.addTimeline(newTimeline);

            moveEffect.setAddedTimeline(newTimelineL);
            moveEffect.setSecondaryAddedBoardL(newTimelineL);

        } else {
            // Handling cross-timeline, non-splitting moves

            this.applyMoveToTimeline(fromTimeline, move);
            this.applyMoveToTimeline(toTimeline, move);

            moveEffect.setSecondaryAddedBoardL(move.to().l());
        }

        Piece pieceAtLocation = this.multiverse.getLocationContents(move.to());
        if (pieceAtLocation != null && pieceAtLocation.type() == PieceType.KING) {
            this.gameOver = true;
            this.winner = pieceAtLocation.color().other();
        }

        this.updatePresentTime();
        this.currentTurnMoveEffects.add(moveEffect);
    }

    public void applyMoveToTimeline(Timeline timeline, Move move) {

        Board lastBoard = timeline.getLastBoard();
        Board nextBoard = new Board.Builder(lastBoard, timeline.getL(), timeline.getLastT() + 1, move).build(); // TODO: hotspot

        timeline.addBoard(nextBoard);
    }

    // Undo related logic

    public void undoTurn() {

        Stack<MoveEffect> turnEffect;

        if (!currentTurnMoveEffects.isEmpty()) {
            turnEffect = currentTurnMoveEffects;
        } else {
            if (turnEffects.isEmpty()) return;
            turnEffect = turnEffects.pop();
        }

        while (!turnEffect.isEmpty())
            this.undoMove(turnEffect.pop());
    }

    public void undoAllMovesFromCurrentTurn() {
        while (!currentTurnMoveEffects.isEmpty())
            this.undoMove(currentTurnMoveEffects.pop());
    }

    public void undoMoveFromCurrentTurn() {

        if (this.currentTurnMoveEffects.isEmpty()) return;

        this.undoMove(this.currentTurnMoveEffects.pop());
    }

    private void undoMove(MoveEffect moveEffect) {

        // Remove added boards
        this.multiverse.getTimeline(moveEffect.getPrimaryAddedBoardL()).removeLastBoard();

        if (moveEffect.getSecondaryAddedBoardL() != null)
            this.multiverse.getTimeline(moveEffect.getSecondaryAddedBoardL()).removeLastBoard();

        // Remove timeline if it was added
        if (moveEffect.isAddedTimeline()) {
            this.multiverse.removeLastTimeline(moveEffect.getAddedTimeline() >= 0);
        }

        // Restore mutable fields
        this.updatePresentTime();
        this.playerTurn = moveEffect.getPrevPlayerTurn();
        this.gameOver = moveEffect.getPrevGameOver();
        this.winner = moveEffect.getPrevWinner();
    }

    // State polling methods and methods to predict game state without simulating moves

    public boolean isCurrentTurnFinalizable() {
        return getMandatoryTimelineLs().isEmpty();
    }

    public List<Integer> getMandatoryTimelineLs() {
        List<Integer> ls = new ArrayList<>();

        for (int timelineIndex : this.multiverse.getActiveTimelineLs()) {
            Timeline timeline = this.multiverse.getTimeline(timelineIndex);
            if (timeline.getLastT() > this.presentTime) continue;
            if (timeline.getLastBoard().getPlayerTurn() != this.playerTurn) continue;

            ls.add(timelineIndex);
        }

        return ls;
    }

    public List<Integer> getPlayableTimelineLs() {
        List<Integer> ls = new ArrayList<>();

        for (int timelineIndex : this.multiverse.getActiveTimelineLs()) {
            Timeline timeline = this.multiverse.getTimeline(timelineIndex);
            if (timeline.getLastBoard().getPlayerTurn() != this.playerTurn) continue;

            ls.add(timelineIndex);
        }

        return ls;
    }

    public List<Board> getPlayableBoards(Color color) {
        List<Board> boards = new ArrayList<>();

        for (Timeline timeline : this.multiverse.getTimelines()) {
            Board board = timeline.getLastBoard();
            if (board.getPlayerTurn() != color) continue;

            boards.add(board);
        }

        return boards;
    }

    public boolean doesMoveAddTimeline(Move move) {
        if (move.noop()) return false;
        Timeline toTimeline = this.multiverse.getTimeline(move.to().l());

        return move.to().t() != toTimeline.getLastT();
    }

    public boolean doesMoveAddInactiveTimeline(Move move) {
        if (!this.doesMoveAddTimeline(move)) return false;

        int addedTimelineL = move.color() == Color.WHITE ?
                this.multiverse.getTopTimelineL() + 1 :
                this.multiverse.getBotTimelineL() - 1;

        return this.multiverse.isTimelineActive(addedTimelineL);
    }

    public boolean doesMoveActivateTimeline(Move move) {
        if (!this.doesMoveAddTimeline(move)) return false;

        int addedTimelineL = move.color() == Color.WHITE ?
                this.multiverse.getTopTimelineL() + 1 :
                this.multiverse.getBotTimelineL() - 1;

        int inactiveTimelineL =
                (Math.abs(addedTimelineL) + 1)
                * (addedTimelineL > 0 ? -1 : 1)
                - (this.multiverse.isEven() ? 1 : 0);

        if (this.multiverse.getTimeline(inactiveTimelineL) == null) return false;

        int activeTimelineL = inactiveTimelineL > 0 ? inactiveTimelineL - 1 : inactiveTimelineL + 1;

        return this.multiverse.isTimelineActive(activeTimelineL) && !this.multiverse.isTimelineActive(inactiveTimelineL);
    }

    public boolean doesMoveRewindPresent(Move move) {
        if (!this.doesMoveAddTimeline(move)) return false;

        int addedTimelineL = move.color() == Color.WHITE ?
                this.multiverse.getTopTimelineL() + 1 :
                this.multiverse.getBotTimelineL() - 1;

        if (!this.multiverse.isTimelineActive(addedTimelineL)) return false;

        int addedTimelineLStartTime = move.to().t() + 1;

        return this.presentTime >= addedTimelineLStartTime;
    }

    public Point4D getMovedPieceDestination(Move move) {
        if (move.from().l() == move.to().l() && move.from().t() == move.to().t())
            // In-board moves
            return move.to().add(0, 1, 0, 0);

        Timeline toTimeline = this.multiverse.getTimeline(move.to().l());
        if (move.to().t() != toTimeline.getLastT()) {
            int newTimelineL = this.playerTurn == Color.WHITE ?
                    this.multiverse.getTopTimelineL() + 1 :
                    this.multiverse.getBotTimelineL() - 1;
            return new Point4D(
                    newTimelineL,
                    move.to().t() + 1,
                    move.to().x(),
                    move.to().y()
            );
        }

        return move.to().add(0, 1, 0, 0);
    }

    private void updatePresentTime() {
        // Optimization: present time is always the minimum of all timeline last times.
        // When adding a board, only the modified timeline's last time changes.
        // Since we only add boards (never remove during search), the minimum
        // is either the previous presentTime or the newly added board's time.
        // However, for correctness during undo, we recalculate but avoid allocations.
        
        int minTime = Integer.MAX_VALUE;
        
        // Iterate directly over timeline lists without creating intermediate ArrayList
        for (Timeline timeline : this.multiverse.getTimelines()) {
            int lastTimelineTime = timeline.getLastT();
            if (lastTimelineTime < minTime) minTime = lastTimelineTime;
        }
        
        this.presentTime = minTime;
    }

    public Multiverse getMultiverse() {
        return this.multiverse;
    }

    public Color getPlayerTurn() {
        return this.playerTurn;
    }

    public int getPresentTime() {
        return this.presentTime;
    }

    public boolean isGameOver() {
        return this.gameOver;
    }

    public Color getWinner() {
        return this.winner;
    }

    public void clearTurnHistory() {
        this.turnEffects.clear();
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (!(other instanceof Game game)) return false;
        return this.hashCode() == game.hashCode();
    }

    @Override
    public int hashCode() {
        return Objects.hash(multiverse, currentTurnMoveEffects, turnEffects, presentTime, playerTurn, gameOver, winner);
    }

    @Override
    public String toString() {
        StringBuilder representation = new StringBuilder();
        representation.append("All turns played:\n");
        for (List<MoveEffect> moveEffect : this.turnEffects) {
            representation.append("  ").append(moveEffect).append("\n");
        }
        representation.append("Current turn: ").append(this.currentTurnMoveEffects).append("\n");
        representation.append("presentTime=").append(presentTime).append("\n");
        representation.append("playerTurn=").append(playerTurn).append("\n");
        representation.append("gameOver=").append(gameOver).append("\n");
        representation.append("winner=").append(winner).append("\n");

        return representation.toString();
    }
}
