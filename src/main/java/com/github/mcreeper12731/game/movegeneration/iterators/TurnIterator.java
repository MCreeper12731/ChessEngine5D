package com.github.mcreeper12731.game.movegeneration.iterators;

import com.github.mcreeper12731.game.Game;
import com.github.mcreeper12731.game.models.Board;
import com.github.mcreeper12731.game.models.Move;
import com.github.mcreeper12731.game.movegeneration.MoveGenerator;

import java.util.*;
import java.util.function.Supplier;

public final class TurnIterator implements Iterator<List<Move>> {

    private final Game game;

    private final List<Supplier<Iterator<Move>>> moveIteratorSuppliers;
    private final List<Iterator<Move>> moveIterators;
    private final List<Move> currentMoves;

    private final Deque<List<Move>> generatedTurns;

    public TurnIterator(Game game) {

        this.game = game;

        this.moveIteratorSuppliers = new ArrayList<>();

        for (int l : this.game.getPlayableTimelineLs()) {
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

        this.generatedTurns = new ArrayDeque<>();

        this.step();
    }

    private void step() {

        while (true) {

            if (!this.advanceCombination()) {
                return;
            }

            List<Move> candidateTurn = new ArrayList<>(this.currentMoves);

            if (game.isTurnFinalizable(candidateTurn)) {
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
                this.resetFollowingIterators(index + 1);
                return true;
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