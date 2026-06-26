package com.github.mcreeper12731.game.movegeneration.iterators;

import com.github.mcreeper12731.game.Game;
import com.github.mcreeper12731.game.models.Board;
import com.github.mcreeper12731.game.models.Move;
import com.github.mcreeper12731.game.movegeneration.MoveGenerator;

import java.util.*;
import java.util.function.Supplier;

public final class TurnIterator implements Iterator<List<Move>> {

    private final Game game;
    private final boolean minimal;

    private final List<Supplier<Iterator<Move>>> moveIteratorSuppliers;
    private final List<Iterator<Move>> moveIterators;
    private final List<Move> currentMoves;

    private final List<Move> anchoredPartialTurn;

    private final Deque<List<Move>> generatedTurns = new ArrayDeque<>();

    public TurnIterator(Game game, boolean minimal) {

        this.game = game;
        this.minimal = minimal;

        this.moveIteratorSuppliers = new ArrayList<>();

        for (int l : minimal ? this.game.getMandatoryTimelineLs() : this.game.getPlayableTimelineLs()) {
            Board board = this.game.getMultiverse().getTimeline(l).getLastBoard();

            this.moveIteratorSuppliers.add(MoveGenerator.scoredMovesSupplier(board, this.game));
        }

        this.moveIterators = new ArrayList<>();
        this.currentMoves = new ArrayList<>();

        for (Supplier<Iterator<Move>> moveIteratorSupplier : moveIteratorSuppliers) {
            Iterator<Move> iterator = moveIteratorSupplier.get();


            this.moveIterators.add(iterator);
            this.currentMoves.add(iterator.next());
        }

        this.anchoredPartialTurn = new ArrayList<>();

        this.step();
    }

    private TurnIterator(Game game, boolean minimal, List<Move> anchoredPartialTurn) {
        this(game, minimal);

        this.anchoredPartialTurn.addAll(anchoredPartialTurn);
    }

    private void step() {

        while (true) {

            if (!this.advanceCombination()) {
                return;
            }

            List<Move> candidateTurn = new ArrayList<>(this.currentMoves);

            if (isLegalTurn(candidateTurn)) {
                this.generatedTurns.add(candidateTurn);
                return;
            }
        }
    }

    private boolean advanceCombination() {
        for (int index = this.moveIterators.size() - 1; index >= 0; index--) {
            Iterator<Move> iterator = this.moveIterators.get(index);

            if (iterator.hasNext()) {
                Move move = iterator.next();
                this.currentMoves.set(index, move);

                if (!this.game.doesMoveAddTimeline(move)) {
                    this.resetFollowingIterators(index + 1);
                    return true;
                }

                // Handle if a move added a timeline as this might have activated a timeline
                if (!this.game.doesMoveActivateTimeline(move) && !this.game.doesMoveRewindPresent(move)) {
                    // No new timeline activated, proceed as usual
                    currentMoves.set(index, move);
                    return true;
                }

                // New timeline activated, anchor move that activated it and pre-generate all turns with this move
                List<Move> anchoredPartialTurn = new ArrayList<>(this.anchoredPartialTurn);
                anchoredPartialTurn.add(move);
                this.game.applyMove(move);
                TurnIterator turnIterator = new TurnIterator(this.game, this.minimal, anchoredPartialTurn);

                if (!turnIterator.hasNext()) {
                    this.game.undoMoveFromCurrentTurn();
                    currentMoves.set(index, move);
                    return true;
                }

                turnIterator.forEachRemaining(turn -> {
                    turn.addFirst(move);
                    this.generatedTurns.add(turn);
                });
                this.game.undoMoveFromCurrentTurn();
                return false;
            }
        }

        return false;
    }

    private void resetFollowingIterators(int startIndex) {
        for (int index = startIndex; index < this.moveIterators.size(); index++) {
            Iterator<Move> iterator = this.moveIteratorSuppliers.get(index).get();

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
            if (this.game.getMultiverse().getTimeline(move.to().l()).getLastT() == move.to().t())
                covered.add(move.to().l());
        }

        return covered;
    }

    @Override
    public boolean hasNext() {
        return !this.generatedTurns.isEmpty();
    }

    @Override
    public List<Move> next() {
        if (this.generatedTurns.isEmpty()) {
            throw new NoSuchElementException();
        }

        List<Move> result = this.generatedTurns.pop();
        this.step();
        return result;
    }
}