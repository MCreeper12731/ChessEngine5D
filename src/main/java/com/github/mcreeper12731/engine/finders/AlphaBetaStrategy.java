package com.github.mcreeper12731.engine.finders;

import com.github.mcreeper12731.engine.config.AlphaBetaStrategyConfig;
import com.github.mcreeper12731.engine.evaluators.Evaluator;
import com.github.mcreeper12731.game.models.Color;
import com.github.mcreeper12731.game.models.scored.ScoredTurn;
import com.github.mcreeper12731.game.logic.Game;
import com.github.mcreeper12731.game.movegeneration.MoveGenerator;
import com.github.mcreeper12731.game.models.Move;
import com.github.mcreeper12731.utility.Log;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class AlphaBetaStrategy {
    private static final int POSITIVE_INFINITY = 1_000_000;
    private static final int NEGATIVE_INFINITY = -1_000_000;

    private final AlphaBetaStrategyConfig config;
    private final Evaluator evaluator;

    private long nodesSearched;
    private boolean stoppedByNodeLimit;

    public AlphaBetaStrategy(AlphaBetaStrategyConfig config, Evaluator evaluator) {
        this.config = config;
        this.evaluator = evaluator;
    }

    public ScoredTurn findBestTurn(Game game) {
        nodesSearched = 0;
        stoppedByNodeLimit = false;
        long prevNodesSearched = 0;

        List<Move> bestTurn = null;
        int bestScore = NEGATIVE_INFINITY;

        Iterator<List<Move>> turns = MoveGenerator.getIterativeTurnIterator(game);

        while (turns.hasNext()) {
            List<Move> turn = turns.next();

            if (config.debugLevel() >= 5) {
                Log.debug("AlphaBeta", "Exploring: " + turn);
            }

            game.applyMovesAndFinalizeTurn(turn);

            int score = -negamax(
                    game,
                    config.maxDepth() - 1,
                    NEGATIVE_INFINITY,
                    POSITIVE_INFINITY,
                    game.getPlayerTurn() == Color.WHITE ? 1 : -1
            );

            game.undoTurn();

            if (config.debugLevel() >= 1) {
                Log.debug("AlphaBeta", "Root candidate score=" + score + ": " + turn + ", spent nodes=" + (nodesSearched - prevNodesSearched));
            }
            prevNodesSearched = nodesSearched;

            if (score > bestScore) {
                bestScore = score;
                bestTurn = new ArrayList<>(turn);
            }

            if (stoppedByNodeLimit) {
                break;
            }

            if (score > 900_000) {
                break;
            }
        }

        if (config.debugLevel() >= 2) {
            Log.debug("AlphaBeta", "AlphaBeta nodes searched: " + nodesSearched);
            Log.debug("AlphaBeta", "Stopped by node limit: " + stoppedByNodeLimit);
            Log.print("AlphaBeta", "Best score: " + bestScore);
            Log.print("AlphaBeta", "Best turn: " + bestTurn);
        }

        return new ScoredTurn(bestTurn, bestScore, nodesSearched);
    }

    private int negamax(Game game, int depth, int alpha, int beta, int color) {
        nodesSearched++;

        if (nodesSearched >= config.maxNodes()) {
            stoppedByNodeLimit = true;
            return color * evaluator.evaluate(game);
        }

        if (depth == 0 || this.isTerminal(game)) {
            return color * evaluator.evaluate(game);
        }

        int best = NEGATIVE_INFINITY;
        Iterator<List<Move>> turnsIterator = MoveGenerator.getIterativeTurnIterator(game);
        while (turnsIterator.hasNext()) {
            List<Move> turn = turnsIterator.next();
            game.applyMovesAndFinalizeTurn(turn);
            // Validation check - skip invalid turns
            if (!game.isCurrentTurnFinalizable()) {
                game.undoTurn();
                continue;
            }
            int score = -negamax(game, depth - 1, -beta, -alpha, -color);
            game.undoTurn();
            best = Math.max(best, score);
            alpha = Math.max(alpha, score);
            if (alpha >= beta) break;
        }
        return best;
    }

    private boolean isTerminal(Game game) {
        return game.isGameOver();
    }
}
