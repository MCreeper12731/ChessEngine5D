package com.github.mcreeper12731.engine.finders;

import com.github.mcreeper12731.engine.config.NegamaxStrategyConfig;
import com.github.mcreeper12731.engine.evaluators.Evaluator;
import com.github.mcreeper12731.game.models.Color;
import com.github.mcreeper12731.game.models.scored.ScoredTurn;
import com.github.mcreeper12731.game.Game;
import com.github.mcreeper12731.game.movegeneration.MoveGenerator;
import com.github.mcreeper12731.game.models.Move;
import com.github.mcreeper12731.utility.Log;

import java.util.Iterator;
import java.util.List;

public class NegaMaxStrategy {
    private static final int POSITIVE_INFINITY = Integer.MAX_VALUE;
    private static final int NEGATIVE_INFINITY = Integer.MIN_VALUE + 1;

    private final NegamaxStrategyConfig config;
    private final Evaluator evaluator;

    public int startTimelineCount;
    public int maxTimelinesReached;
    public long nodesSearched;
    public boolean stoppedByNodeLimit;

    public NegaMaxStrategy(NegamaxStrategyConfig config, Evaluator evaluator) {
        this.config = config;
        this.evaluator = evaluator;
    }

    public NegaMaxStrategy() {
        this.config = NegamaxStrategyConfig.fromConfig();
        this.evaluator = new Evaluator();
    }

    public ScoredTurn findBestTurn(Game game) {
        startTimelineCount = game.getMultiverse().getTimelines().size();
        maxTimelinesReached = 0;
        nodesSearched = 0;
        stoppedByNodeLimit = false;
        long prevNodesSearched;

        List<Move> bestTurn = null;
        double bestScore = NEGATIVE_INFINITY;
        int currentDepth = 1;

        while (currentDepth <= config.maxDepth()) {
            startTimelineCount = game.getMultiverse().getTimelines().size();
            nodesSearched = 0;
            prevNodesSearched = 0;

            bestTurn = null;
            bestScore = NEGATIVE_INFINITY;

            Iterator<List<Move>> turns = MoveGenerator.getIterativeTurnIterator(game);

            while (turns.hasNext()) {
                List<Move> turn = turns.next();

                game.applyMoves(turn);
                if (!game.isCurrentTurnFinalizable()) {
                    game.undoAllMovesFromCurrentTurn();
                    continue;
                }
                game.finalizeTurn();

                if (config.debugLevel() >= 5) {
                    Log.debug("AlphaBeta", "Exploring: " + turn);
                }

                double score = -negamax(
                        game,
                        currentDepth - 1,
                        NEGATIVE_INFINITY,
                        POSITIVE_INFINITY,
                        game.getPlayerTurn() == Color.WHITE ? 1 : -1
                );

                game.undoTurn();

                if (config.debugLevel() >= 3) {
                    Log.debug("AlphaBeta", "Root candidate score=" + score + ": " + turn + ", spent nodes=" + (nodesSearched - prevNodesSearched));
                }
                prevNodesSearched = nodesSearched;

                if (score >= bestScore) {
                    bestScore = score;
                    bestTurn = turn;
                }

                if (Math.abs(bestScore) == 1_000_000 || stoppedByNodeLimit) {
                    break;
                }
            }

            if (Math.abs(bestScore) == 1_000_000 || stoppedByNodeLimit) {
                break;
            }

            currentDepth += 2;
        }

        if (config.debugLevel() >= 2) {
            Log.debug("AlphaBeta", "AlphaBeta nodes searched: " + nodesSearched);
            Log.debug("AlphaBeta", "Stopped by node limit: " + stoppedByNodeLimit);
        }
        if (config.debugLevel() >= 1) {
            Log.print("AlphaBeta", "Found turn at depth:", currentDepth);
            Log.print("AlphaBeta", "Best score:", bestScore);
            Log.print("AlphaBeta", "Best turn:", bestTurn);
        }

        return new ScoredTurn(bestTurn, bestScore, nodesSearched);
    }

    private double negamax(Game game, int depth, double alpha, double beta, int color) {
        nodesSearched++;

        if (game.getMultiverse().getTimelines().size() > maxTimelinesReached)
            maxTimelinesReached = game.getMultiverse().getTimelines().size();

        if (nodesSearched >= config.maxNodes()) {
            stoppedByNodeLimit = true;
            return color * evaluator.evaluate(game);
        }

        if (depth == 0 || this.isTerminal(game)) {
            return color * evaluator.evaluate(game);
        }

        double best = NEGATIVE_INFINITY;
        Iterator<List<Move>> turnsIterator = MoveGenerator.getIterativeTurnIterator(game);
        if (!turnsIterator.hasNext()) {
            return 0.1;
        }

        while (turnsIterator.hasNext()) {
            List<Move> turn = turnsIterator.next();

            game.applyMoves(turn);
            if (!game.isCurrentTurnFinalizable()) {
                game.undoAllMovesFromCurrentTurn();
                continue;
            }
            if (game.getMultiverse().getTimelines().size() > startTimelineCount + config.maxAdditionalTimelines()) {
                game.undoAllMovesFromCurrentTurn();
                continue;
            }
            game.finalizeTurn();

            double score = -negamax(game, depth - 1, -beta, -alpha, -color);

            if (config.debugLevel() >= 10 && score > best) {
                Log.debug(" ".repeat(config.maxDepth() - depth) + "AlphaBeta", "Exploring turn:", "best score:", score);
            }

            game.undoTurn();
            best = Math.max(best, score);
            alpha = Math.max(alpha, score);
            if (alpha >= beta || this.stoppedByNodeLimit) break;
        }
        return best;
    }

    private boolean isTerminal(Game game) {
        return game.isGameOver();
    }
}
