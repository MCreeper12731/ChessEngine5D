package com.github.mcreeper12731.game.models;

import com.github.mcreeper12731.game.moves.Move;
import com.github.mcreeper12731.game.pieces.PieceType;

import java.util.ArrayList;
import java.util.List;

public class Timeline {

    private final double id;
    private final int startTime;
    private final List<Board> boards = new ArrayList<>();

    private boolean active;

    private Timeline(double id, int startTime, List<Board> boards, boolean active) {
        this.id = id;
        this.startTime = startTime;
        this.boards.addAll(boards);

        this.active = active;
    }

    public void applyMove(Move move) {
        Board lastBoard = getLastBoard();
        Board nextBoard = lastBoard.copyWithProgressedTurn(
                this.id,
                this.getLastTimeCoordinate() + 1
        );

        if (move.fromTimeline() == this.id) {
            nextBoard.removePiece(move.fromX(), move.fromY());
        }

        if (move.toTimeline() == this.id && lastBoard.getTime() == move.toTime()) {
            nextBoard.setPieceFromMoving(
                    move.toX(),
                    move.toY(),
                    move.pieceColor(),
                    move.promotionResult() == null ? move.pieceType() : move.promotionResult()
            );
        }
        this.boards.add(nextBoard);
    }

    public double getId() {
        return id;
    }

    public int size() {
        return boards.size();
    }

    public int getFirstTimeCoordinate() {
        return startTime;
    }

    public int getLastTimeCoordinate() {
        return startTime + size() - 1;
    }

    public Board getBoardByIndex(int index) {
        return boards.get(index);
    }

    public Board getBoardByTime(int time) {
        return boards.get(time - startTime);
    }

    public Board getLastBoard() {
        return boards.get(boards.size() - 1);
    }

    public void removeLastBoard() {
        boards.remove(boards.size() - 1);
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isActive() {
        return this.active;
    }

    public static class Builder {

        private final double index;
        private final List<Board> boards = new ArrayList<>();

        private int startTime = 0;
        private boolean isActive = true;

        public Builder(double index) {
            this.index = index;
        }

        public Builder withBoard(Board board) {
            boards.add(board);
            return this;
        }

        public Builder withStartTime(int startTime) {
            this.startTime = startTime;
            return this;
        }

        public Builder withActive(boolean isActive) {
            this.isActive = isActive;
            return this;
        }

        public Timeline build() {
            return new Timeline(
                    index,
                    startTime,
                    boards,
                    isActive
            );
        }
    }
}
