package com.github.mcreeper12731.game.logic;

import com.github.mcreeper12731.evaluators.GameEvaluator;
import com.github.mcreeper12731.game.models.ScoredBoard;
import com.github.mcreeper12731.evaluators.ScoredMove;
import com.github.mcreeper12731.game.models.Board;
import com.github.mcreeper12731.game.models.Multiverse;
import com.github.mcreeper12731.game.moves.Move;
import com.github.mcreeper12731.game.pieces.Piece;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class MoveGenerator {

    public static Iterator<List<Move>> getTurnsIterator(Game game) {
        return new TurnIterator(game, true);
    }

    public static Iterator<List<Move>> getSortedTurnsIterator(Game game, GameEvaluator evaluator) {
        return new SortedTurnIterator(getTurnsIterator(game), game, evaluator);
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
        Multiverse multiverse = game.getMultiverse();
        List<Move> moves = new ArrayList<>();
        
        for (int x = 0; x < multiverse.getBoardSize(); x++) {
            for (int y = 0; y < multiverse.getBoardSize(); y++) {
                Piece piece = board.getLocationContents(x, y);
                if (piece.color() != board.getPlayerTurn()) continue;
                moves.addAll(piece.getAvailableMoves(multiverse));
            }
        }
        return moves;
    }
    
    public static List<Move> scoredMoves(Board board, Game game) {
        ScoredBoard scoredBoard = new ScoredBoard(board, game);
        return scoredBoard.scoreMoves(probableMoves(board, game), game).stream().map(ScoredMove::move).collect(Collectors.toList());
    }
    
    
}
