package com.github.mcreeper12731.bitgame.movegeneration;

import com.github.mcreeper12731.bitgame.BitGame;
import com.github.mcreeper12731.bitgame.models.BitBoard;
import com.github.mcreeper12731.bitgame.models.BitTimeline;
import com.github.mcreeper12731.bitgame.models.scored.ScoredBitBoard;
import com.github.mcreeper12731.bitgame.movegeneration.iterators.BitBoardMoveIterator;
import com.github.mcreeper12731.bitgame.movegeneration.iterators.BitIterativeTurnIterator;
import com.github.mcreeper12731.bitgame.movegeneration.iterators.BitTurnIterator;
import com.github.mcreeper12731.game.Game;
import com.github.mcreeper12731.game.models.Board;
import com.github.mcreeper12731.game.models.Move;
import com.github.mcreeper12731.game.models.Timeline;
import com.github.mcreeper12731.game.models.scored.ScoredBoard;
import com.github.mcreeper12731.game.models.scored.ScoredMove;
import com.github.mcreeper12731.game.movegeneration.iterators.BoardMoveIterator;
import com.github.mcreeper12731.game.movegeneration.iterators.IterativeTurnIterator;
import com.github.mcreeper12731.game.movegeneration.iterators.TurnIterator;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class BitMoveGenerator {

    public static Iterator<List<Move>> getIterativeTurnIterator(BitGame game) {

        List<List<Move>> boardsMoves = new ArrayList<>();
        for (BitTimeline timeline : game.getMultiverse().getTimelines()) {
            BitBoard board = timeline.getLastBoard();
            if (board.getPlayerTurn() != game.getPlayerTurn()) continue;
            List<Move> moves = scoredMoves(timeline.getLastBoard(), game);
            boardsMoves.add(moves);
        }
        return new BitIterativeTurnIterator(game, boardsMoves);
    }

    public static Iterator<List<Move>> getTurnsIterator(BitGame game) {
        return new BitTurnIterator(game);
    }

    public static Supplier<Iterator<Move>> scoredMovesSupplier(BitBoard board, BitGame game) {
        List<Move> moves = scoredMoves(board, game);
        moves.addFirst(
                new Move.Builder()
                        .withNoop()
                        .build()
        );
        return moves::iterator;
    }

    public static Supplier<Iterator<Move>> probableMovesSupplier(Board board, Game game) {

        return () -> new BoardMoveIterator(board, game.getMultiverse());
    }

    public static List<Move> scoredMoves(BitBoard board, BitGame game) {
        ScoredBitBoard scoredBoard = new ScoredBitBoard(board, game);
        return scoredBoard.scoreMoves(game).stream().map(ScoredMove::move).collect(Collectors.toList());
    }

    public static List<Move> probableMoves(BitBoard board, BitGame game) {
        BitBoardMoveIterator boardMoveIterator = new BitBoardMoveIterator(board, game);
        List<Move> result = new ArrayList<>();
        boardMoveIterator.forEachRemaining(result::add);
        return result;
    }
}
