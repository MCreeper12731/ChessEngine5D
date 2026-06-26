package com.github.mcreeper12731.game.movegeneration;

import com.github.mcreeper12731.engine.evaluators.Evaluator;
import com.github.mcreeper12731.game.Game;
import com.github.mcreeper12731.game.models.Timeline;
import com.github.mcreeper12731.game.models.scored.ScoredBoard;
import com.github.mcreeper12731.game.models.scored.ScoredMove;
import com.github.mcreeper12731.game.models.Board;
import com.github.mcreeper12731.game.models.Move;
import com.github.mcreeper12731.game.movegeneration.iterators.BoardMoveIterator;
import com.github.mcreeper12731.game.movegeneration.iterators.IterativeTurnIterator;
import com.github.mcreeper12731.game.movegeneration.iterators.SortedTurnIterator;
import com.github.mcreeper12731.game.movegeneration.iterators.TurnIterator;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class MoveGenerator {

    public static Iterator<List<Move>> getTurnsIterator(Game game) {
        return new TurnIterator(game, true);
    }

    public static Iterator<List<Move>> getSortedTurnsIterator(Game game, Evaluator evaluator) {
        return new SortedTurnIterator(getTurnsIterator(game), game, evaluator);
    }

    public static Iterator<List<Move>> getIterativeTurnIterator(Game game) {

        List<List<Move>> boardsMoves = new ArrayList<>();
        for (Timeline timeline : game.getMultiverse().getTimelines()) {
            List<Move> moves = scoredMoves(timeline.getLastBoard(), game);
            boardsMoves.add(moves);
        }
        return new IterativeTurnIterator(game, boardsMoves);
    }
    
    public static Supplier<Iterator<Move>> probableMovesSupplier(Board board, Game game) {

        return () -> new BoardMoveIterator(board, game.getMultiverse());
    }

    public static Supplier<Iterator<Move>> scoredMovesSupplier(Board board, Game game) {
        List<Move> moves = scoredMoves(board, game);
        moves.addFirst(
                new Move.Builder()
                        .withNoop()
                        .build()
        );
        return moves::iterator;
    }
    
    public static List<Move> probableMoves(Board board, Game game) {
        BoardMoveIterator boardMoveIterator = new BoardMoveIterator(board, game.getMultiverse());
        List<Move> result = new ArrayList<>();
        boardMoveIterator.forEachRemaining(result::add);
        return result;
    }
    
    public static List<Move> scoredMoves(Board board, Game game) {
        ScoredBoard scoredBoard = new ScoredBoard(board, game);
        return scoredBoard.scoreMoves(game).stream().map(ScoredMove::move).collect(Collectors.toList());
    }
    
    
}
