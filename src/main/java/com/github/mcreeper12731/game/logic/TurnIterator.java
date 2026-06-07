package com.github.mcreeper12731.game.logic;

import com.github.mcreeper12731.game.models.Board;
import com.github.mcreeper12731.game.moves.Move;

import java.util.*;
import java.util.function.Supplier;

public final class TurnIterator implements Iterator<List<Move>> {

    private final Game game;
    private final List<Supplier<Iterator<Move>>> moveIteratorSuppliers;
    private final List<Iterator<Move>> moveIterators;
    private final List<Move> currentMoves;

    private List<Move> nextTurn;
    private boolean initialized;

    public TurnIterator(Game game, boolean minimal) {

        this.game = game;
        this.moveIteratorSuppliers = new ArrayList<>();

        for (int l : minimal ? this.game.getMandatoryTimelineLs() : this.game.getMultiverse().getTimelineIndices()) {
            Board board = this.game.getMultiverse().getTimeline(l).getLastBoard();

            this.moveIteratorSuppliers.add(MoveGeneratorNew.probableMovesSupplier(board, this.game.getMultiverse()));
        }

        this.moveIterators = new ArrayList<>();
        this.currentMoves = new ArrayList<>();

        for (int i = 0; i < moveIteratorSuppliers.size(); i++) {
            this.moveIterators.add(null);
            this.currentMoves.add(null);
        }

        this.step();
    }

    private void step() {
        this.nextTurn = null;

        while (true) {
            if (!this.initialized) {
                if (!this.initializeFirstCombination()) {
                    return;
                }

                this.initialized = true;
            } else {
                if (!this.advanceCombination()) {
                    return;
                }
            }

            List<Move> candidateTurn = List.copyOf(this.currentMoves);

            if (isLegalTurn(candidateTurn)) {
                this.nextTurn = candidateTurn;
                return;
            }
        }
    }

    private boolean initializeFirstCombination() {
        for (int index = 0; index < this.moveIteratorSuppliers.size(); index++) {
            Iterator<Move> iterator = this.moveIteratorSuppliers.get(index).get();

            if (!iterator.hasNext()) {
                return false;
            }

            this.moveIterators.set(index, iterator);
            this.currentMoves.set(index, iterator.next());
        }

        return true;
    }

    private boolean advanceCombination() {
        for (int index = this.moveIterators.size() - 1; index >= 0; index--) {
            Iterator<Move> iterator = this.moveIterators.get(index);

            if (iterator.hasNext()) {
                this.currentMoves.set(index, iterator.next());

                this.resetFollowingIterators(index + 1);
                return true;
            }
        }

        return false;
    }

    private void resetFollowingIterators(int startIndex) {
        for (int index = startIndex; index < this.moveIterators.size(); index++) {
            Iterator<Move> iterator = this.moveIteratorSuppliers.get(index).get();

            if (!iterator.hasNext()) {
                throw new IllegalStateException("Move iterator unexpectedly had no moves.");
            }

            this.moveIterators.set(index, iterator);
            this.currentMoves.set(index, iterator.next());
        }
    }

    private boolean isLegalTurn(List<Move> turn) {
        Set<Integer> covered = new HashSet<>();
        List<Integer> mandatoryTimelines = this.game.getMandatoryTimelineLs();

        for (Move move : turn) {
            List<Integer> moveCoverage = this.coveredTimelines(move);

            for (Integer timeline : moveCoverage) {
                if (!mandatoryTimelines.contains(timeline)) {
                    continue;
                }

                if (!covered.add(timeline)) {
                    return false;
                }
            }
        }

        for (Integer timeline : mandatoryTimelines) {
            if (!covered.contains(timeline))
                return false;
        }
        return true;
    }

    private List<Integer> coveredTimelines(Move move) {
        List<Integer> covered = new ArrayList<>();

        if (move.noop()) {
            return covered;
        }

        covered.add(move.from().l());

        if (move.to().l() != move.from().l()) {
            if (this.game.getMultiverse().getTimeline(move.to().l()).getLastTimeCoordinate() == move.to().t())
                covered.add(move.to().l());
        }

        return covered;
    }

    @Override
    public boolean hasNext() {
        return this.nextTurn != null;
    }

    @Override
    public List<Move> next() {
        if (this.nextTurn == null) {
            throw new NoSuchElementException();
        }

        List<Move> result = this.nextTurn;
        this.step();
        return result;
    }
}