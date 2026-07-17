package com.github.mcreeper12731.bitgame.movegeneration.iterators;

import com.github.mcreeper12731.bitgame.BitGame;
import com.github.mcreeper12731.bitgame.models.BitBoard;
import com.github.mcreeper12731.bitgame.movegeneration.BitMoveGenerator;
import com.github.mcreeper12731.game.Game;
import com.github.mcreeper12731.game.models.Board;
import com.github.mcreeper12731.game.models.Move;
import com.github.mcreeper12731.game.models.Multiverse;
import com.github.mcreeper12731.game.models.Timeline;
import com.github.mcreeper12731.game.models.pieces.PieceType;
import com.github.mcreeper12731.game.movegeneration.MoveGenerator;
import com.github.mcreeper12731.game.presets.Preset;
import com.github.mcreeper12731.utility.Iterators;
import com.github.mcreeper12731.utility.Log;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class BitTurnIteratorTest {

    @Test
    public void turnStatistics() {

        BitGame game = new BitGame(Preset.CUSTOM_COMPLEX_POSITION.getGame());

        Iterator<List<Move>> turns = BitMoveGenerator.getTurnsIterator(game);

        List<List<Move>> allTurns = new ArrayList<>();
        turns.forEachRemaining(allTurns::add);

        List<Set<Move>> uniquePermutations = new ArrayList<>();
        int duplicatePermutations = 0;
        int invalidTurns = 0;
        int maxT = 0;

        for (List<Move> turn : allTurns) {

            game.applyMoves(turn);
            if (game.getPresentTime() > maxT)
                maxT = game.getPresentTime();
            if (!game.isCurrentTurnFinalizable()) {
                invalidTurns++;
            }
            game.undoTurn();

            boolean allSameTimeline = true;
            for (Move move : turn) {
                if (!move.noop() && move.from().l() != move.to().l()) {
                    allSameTimeline = false;
                    break;
                }
            }
            if (allSameTimeline) {

                Set<Move> hashedTurn = new HashSet<>(turn);
                for (Set<Move> uniquePermutation : uniquePermutations) {
                    if (uniquePermutation.equals(hashedTurn)) {
//                        Log.debug("Test", "found duplicate", turn, "of turn", uniquePermutation);
                        duplicatePermutations++;
                        break;
                    }
                }

                uniquePermutations.add(hashedTurn);
                //Log.debug("Test", turn.toString());
            }
        }


        Log.debug("Test", allTurns.size());
        Log.debug("Test", "duplicate", duplicatePermutations);
        Log.debug("Test", "invalid", invalidTurns);
        Log.debug("Test", "maxT", maxT);
        Log.debug("Test", "actual turns", allTurns.size() - duplicatePermutations - invalidTurns);
    }

    @Test
    public void turnGenerationSameAsRegular() {

        Game game = Preset.STANDARD.getGame();
        List<List<Move>> turns = Iterators.consumeRemaining(MoveGenerator.getTurnsIterator(game));

        BitGame bitGame = new BitGame(game);
        List<List<Move>> bitTurns = Iterators.consumeRemaining(BitMoveGenerator.getTurnsIterator(bitGame));

//        Log.debug("Test", bitTurns);
        assertEquals(turns, bitTurns);

        game = Preset.CUSTOM_COMPLEX_POSITION.getGame();
        turns = Iterators.consumeRemaining(MoveGenerator.getTurnsIterator(game));

        bitGame = new BitGame(game);
        assertEquals(game.toString(), bitGame.toString());
//        Log.debug("Test", bitGame.getMultiverse());
        bitTurns = Iterators.consumeRemaining(BitMoveGenerator.getTurnsIterator(bitGame));
        assertEquals(new HashSet<>(game.getPlayableTimelineLs()), bitGame.getPlayableTimelineLs(bitGame.getPlayerTurn()));
        for (int l : game.getPlayableTimelineLs()) {

            Board board = game.getMultiverse().getTimeline(l).getLastBoard();
            BitBoard bitBoard = bitGame.getMultiverse().getTimeline(l).getLastBoard();
            assertEquals(board.toString(), bitBoard.toString());

            List<Move> moves = MoveGenerator.probableMoves(board, game);
            List<Move> bitMoves = BitMoveGenerator.probableMoves(bitBoard, bitGame);
            assertEquals(moves, bitMoves);

            moves = MoveGenerator.scoredMoves(board, game);
            bitMoves = BitMoveGenerator.scoredMoves(bitBoard, bitGame);
            assertEquals(moves, bitMoves);
        }

//        Log.debug("Test", turns.size());
//        Log.debug("Test", bitTurns.size());
        assertEquals(turns.size(), bitTurns.size());
        for (int i = 0; i < turns.size(); i++) {
            List<Move> turn = turns.get(i);
            List<Move> bitTurn = bitTurns.get(i);
            assertEquals(turn, bitTurn);

            if (!game.isTurnFinalizable(turn) && bitGame.isTurnFinalizable(turn)) {
                bitGame.isTurnFinalizable(turn);
                Log.debug("Test", turn);
            }
        }
        assertEquals(turns, bitTurns);
    }

    @Test
    public void turnGenerationSameAsIterative() {
        BitGame game = new BitGame(Preset.CUSTOM_COMPLEX_POSITION.getGame());

        List<List<Move>> regularTurns = Iterators.consumeRemaining(BitMoveGenerator.getTurnsIterator(game));
        List<List<Move>> iterativeTurns = Iterators.consumeRemaining(BitMoveGenerator.getIterativeTurnIterator(game));

        assertEquals(regularTurns.size(), iterativeTurns.size());
    }
}