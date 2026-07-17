package com.github.mcreeper12731.engine.evaluators;

import com.github.mcreeper12731.game.models.Color;
import com.github.mcreeper12731.game.models.scored.ScoredTurn;
import com.github.mcreeper12731.game.Game;
import com.github.mcreeper12731.game.movegeneration.MoveGenerator;
import com.github.mcreeper12731.game.models.Move;
import com.github.mcreeper12731.utility.Config;
import com.github.mcreeper12731.utility.Log;

import java.util.Iterator;
import java.util.List;

public class NegaMaxEvaluator {
    private static final int POSITIVE_INFINITY = Integer.MAX_VALUE;
    private static final int NEGATIVE_INFINITY = Integer.MIN_VALUE + 1;

    private final int debugLevel;
    private final int maxDepth;
    private final int maxNodes;
    private final int maxAdditionalTimelines;
    private final Evaluator evaluator;

    public int startTimelineCount;
    public int maxTimelinesReached;
    public long nodesSearched;
    public boolean stoppedByNodeLimit;

    public NegaMaxEvaluator() {
        this(Config.fromFile("negamax"), new StaticEvaluator());
    }

    public NegaMaxEvaluator(Config config) {
        this(config, new StaticEvaluator());
    }

    public NegaMaxEvaluator(Config config, Evaluator evaluator) {
        this.debugLevel = config.getOrDefault("debug_level", 0);
        this.maxDepth = config.getOrDefault("max_depth", 9);
        this.maxNodes = config.getOrDefault("max_nodes", 1_000_000);
        this.maxAdditionalTimelines = config.getOrDefault("max_additional_timelines", 1);

        this.evaluator = evaluator;
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

        while (currentDepth <= this.maxDepth) {
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
                    Log.print("AlphaBeta", "Found non-finalizable turn!");
                    game.undoAllMovesFromCurrentTurn();
                    continue;
                }
                game.finalizeTurn();

                if (this.debugLevel >= 5) {
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

                if (this.debugLevel >= 3) {
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

        if (this.debugLevel >= 2) {
            Log.debug("AlphaBeta", "AlphaBeta nodes searched: " + nodesSearched);
            Log.debug("AlphaBeta", "Stopped by node limit: " + stoppedByNodeLimit);
        }
        if (this.debugLevel >= 1) {
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

        if (nodesSearched >= this.maxNodes) {
            stoppedByNodeLimit = true;
            return color * evaluator.evaluateGameState(game);
        }

        if (depth == 0 || this.isTerminal(game)) {
            return color * evaluator.evaluateGameState(game);
        }

        double best = NEGATIVE_INFINITY;
        Iterator<List<Move>> turnsIterator = MoveGenerator.getIterativeTurnIterator(game);
        if (!turnsIterator.hasNext()) {
            return -0.000001;
        }

        while (turnsIterator.hasNext()) {
            List<Move> turn = turnsIterator.next();

            game.applyMoves(turn);
            if (!game.isCurrentTurnFinalizable()) {
                game.undoAllMovesFromCurrentTurn();
                continue;
            }
            if (game.getMultiverse().getTimelines().size() > startTimelineCount + this.maxAdditionalTimelines) {
                game.undoAllMovesFromCurrentTurn();
                continue;
            }
            game.finalizeTurn();

            double score = -negamax(game, depth - 1, -beta, -alpha, -color);

            if (this.debugLevel >= 10 && score > best) {
                Log.debug(" ".repeat(this.maxDepth - depth) + "AlphaBeta", "Exploring turn:", "best score:", score);
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
