package com.github.mcreeper12731.game.movegeneration.iterators;

import com.github.mcreeper12731.game.Game;
import com.github.mcreeper12731.game.models.Move;
import com.github.mcreeper12731.game.models.Timeline;

import java.util.*;

public class IterativeTurnIterator implements Iterator<List<Move>> {

    private final Game game;
    private final List<List<Move>> boardsMoves;

    private final Deque<List<Move>> permutationQueue;
    private final int maxMoves;

    private int movesConsidered;
    private int maxMovesConsidered;
    private int movesetsConsidered;
    private int maxMovesetsConsidered;

    private List<Move> peeked;

    public IterativeTurnIterator(Game game, List<List<Move>> boardsMoves) {
        this.game = game;
        this.boardsMoves = new ArrayList<>();
        for (List<Move> boardMoves : boardsMoves) {
            this.boardsMoves.add(Collections.unmodifiableList(boardMoves));
        }

        this.permutationQueue = new ArrayDeque<>();
        this.maxMoves = this.boardsMoves.stream().mapToInt(List::size).max().orElse(0) + 1;

        this.movesConsidered = 0;
        this.maxMovesConsidered = 0;
        this.movesetsConsidered = 0;
        this.maxMovesetsConsidered = 0;

        this.peeked = null;
    }

    public List<Move> nextMoveSet() {
        this.movesetsConsidered++;

        if (
                (this.maxMovesetsConsidered > 0 && this.movesetsConsidered > this.maxMovesetsConsidered) ||
                (this.maxMovesConsidered > 0 && this.movesConsidered > this.maxMovesConsidered) ||
                (this.movesConsidered > this.maxMoves)
        )
            return null;

        if (!this.permutationQueue.isEmpty()) return permutationQueue.poll();

        do {
            this.movesConsidered++;

            if (this.maxMovesConsidered > 0 && this.movesConsidered > this.maxMovesConsidered) {
                return null;
            }

            List<BoardMoveIndex> newMoves = new ArrayList<>();
            for (int i = 0; i < this.boardsMoves.size(); i++) {
                if (this.boardsMoves.get(i).size() >= this.movesConsidered) {
                    newMoves.add(new BoardMoveIndex(i, this.movesConsidered - 1));
                }
            }

            this.generateCombinations(newMoves);

            if (!this.permutationQueue.isEmpty()) return this.permutationQueue.poll();

        } while (this.movesConsidered <= this.maxMoves);

        return null;
    }

    private void generateCombinations(List<BoardMoveIndex> newMoves) {

        for (BoardMoveIndex newMove: newMoves) {

            List<List<BoardMoveIndex>> preCombinations = newMove.boardIndex > 0 ?
                    this.generatePreCombinations(newMove.boardIndex, 0) :
                    Collections.singletonList(Collections.emptyList());

            if (preCombinations.isEmpty()) {
                preCombinations = Collections.singletonList(Collections.emptyList());
            }

            List<List<BoardMoveIndex>> postCombinations = newMove.boardIndex < boardsMoves.size() - 1 ?
                    this.generatePostCombinations(newMove.boardIndex, boardsMoves.size() - 1) :
                    Collections.singletonList(Collections.emptyList());

            if (postCombinations.isEmpty()) {
                postCombinations = Collections.singletonList(Collections.emptyList());
            }

            for (List<BoardMoveIndex> preCombination: preCombinations) {
                for (List<BoardMoveIndex> postCombination: postCombinations) {
                    List<Move> turn = new ArrayList<>();

                    for (BoardMoveIndex combination : preCombination) {
                        Move move = boardsMoves.get(combination.boardIndex).get(combination.moveIndex);
                        turn.add(move);
                    }

                    for (BoardMoveIndex combination : postCombination) {
                        Move move = boardsMoves.get(combination.boardIndex).get(combination.moveIndex);
                        turn.add(move);
                    }

                    Move move = boardsMoves.get(newMove.boardIndex).get(newMove.moveIndex);
                    turn.add(move);

                    this.commitCombination(turn);
                }
            }
        }
    }

    private List<List<BoardMoveIndex>> generatePreCombinations(int max, int current) {
        if (current == max - 1) {
            int limit = Math.min(boardsMoves.get(current).size(), this.movesConsidered);
            List<List<BoardMoveIndex>> preCombinations = new ArrayList<>();
            for (int n = 0; n < limit; n++) {
                preCombinations.add(Collections.singletonList(new BoardMoveIndex(current, n)));
            }
            return preCombinations;
        }

        if (boardsMoves.get(current).isEmpty()) {
            return generatePreCombinations(max, current + 1);
        }

        List<List<BoardMoveIndex>> preCombinations = new ArrayList<>();
        List<List<BoardMoveIndex>> toCombine = generatePreCombinations(max, current + 1);

        int limit = Math.min(boardsMoves.get(current).size(), this.movesConsidered);
        if (!toCombine.isEmpty()) {
            for (List<BoardMoveIndex> tc : toCombine) {
                for (int x = 0; x < limit; x++) {
                    List<BoardMoveIndex> tc2 = new ArrayList<>(tc);
                    tc2.add(new BoardMoveIndex(current, x));
                    preCombinations.add(tc2);
                }
            }
            return preCombinations;
        }

        for (int x = 0; x < limit; x++) {
            preCombinations.add(Collections.singletonList(new BoardMoveIndex(current, x)));
        }
        return preCombinations;
    }

    private List<List<BoardMoveIndex>> generatePostCombinations(int min, int current) {
        if (current == min + 1) {
            int limit = Math.min(boardsMoves.get(current).size(), movesConsidered - 1);
            List<List<BoardMoveIndex>> res = new ArrayList<>();
            for (int n = 0; n < limit; n++) {
                res.add(Collections.singletonList(new BoardMoveIndex(current, n)));
            }
            return res;
        }

        if (boardsMoves.get(current).isEmpty()) {
            return generatePostCombinations(min, current - 1);
        }

        List<List<BoardMoveIndex>> postCombinations = new ArrayList<>();
        List<List<BoardMoveIndex>> toCombine = generatePostCombinations(min, current - 1);
        int limit = Math.min(boardsMoves.get(current).size(), movesConsidered);
        if (!toCombine.isEmpty()) {
            for (List<BoardMoveIndex> tc : toCombine) {
                for (int x = 0; x < limit; x++) {
                    List<BoardMoveIndex> tc2 = new ArrayList<>(tc);
                    tc2.add(new BoardMoveIndex(current, x));
                    postCombinations.add(tc2);
                }
            }
            return postCombinations;
        }

        for (int x = 0; x < limit; x++) {
            postCombinations.add(Collections.singletonList(new BoardMoveIndex(current, x)));
        }
        return postCombinations;
    }

    private void commitCombination(List<Move> combination) {
        List<Move> normal = new ArrayList<>();
        List<Move> jumping = new ArrayList<>();
        List<Move> branching = new ArrayList<>();

        for (Move move : combination) {
            if (move.noop() || move.from().l() == move.to().l() && move.from().t() == move.to().t()) {
                normal.add(move);
            } else if (!game.doesMoveAddTimeline(move)) {
                jumping.add(move);
            } else {
                branching.add(move);
            }
        }

        List<Move> activeMoves = new ArrayList<>();
        activeMoves.addAll(jumping);
        activeMoves.addAll(branching);

        // Generate all permutations of activeMoves
        List<List<Move>> permutations = permutationsOf(activeMoves);

        for (List<Move> perm : permutations) {
            if (maxMovesetsConsidered > 0 && permutationQueue.size() > maxMovesetsConsidered) {
                break;
            }
            List<Move> turn = new ArrayList<>();
            // normal moves first
            turn.addAll(normal);
            // then the permuted active moves
            turn.addAll(perm);
            if (turn.isEmpty()) {
                break;
            }

            if (!game.isTurnFinalizable(turn)) {
                break;
            }

            permutationQueue.push(turn);
        }
    }

    @Override
    public boolean hasNext() {
        if (this.peeked != null) return true;
        this.peeked = this.nextMoveSet();
        return this.peeked != null;
    }

    @Override
    public List<Move> next() {
        if (!this.hasNext()) throw new NoSuchElementException();
        List<Move> result = peeked;
        peeked = null;
        return result;
    }

    private static <T> List<List<T>> permutationsOf(List<T> list) {
        List<List<T>> result = new ArrayList<>();
        if (list.isEmpty()) {
            result.add(Collections.emptyList());
            return result;
        }
        heapPermutation(new ArrayList<>(list), list.size(), result);
        return result;
    }

    private static <T> void heapPermutation(List<T> a, int size, List<List<T>> out) {
        if (size == 1) {
            out.add(new ArrayList<>(a));
            return;
        }
        for (int i = 0; i < size; i++) {
            heapPermutation(a, size - 1, out);
            int j = (size % 2 == 1) ? 0 : i;
            Collections.swap(a, j, size - 1);
        }
    }

    private record BoardMoveIndex(int boardIndex, int moveIndex) {}
}
