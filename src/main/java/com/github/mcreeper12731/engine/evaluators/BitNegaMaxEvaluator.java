package com.github.mcreeper12731.engine.evaluators;

import com.github.mcreeper12731.bitgame.movegeneration.BitMoveGenerator;
import com.github.mcreeper12731.bitgame.BitGame;
import com.github.mcreeper12731.game.Game;
import com.github.mcreeper12731.game.models.Color;
import com.github.mcreeper12731.game.models.Move;
import com.github.mcreeper12731.game.models.scored.ScoredTurn;
import com.github.mcreeper12731.game.movegeneration.MoveGenerator;
import com.github.mcreeper12731.utility.Config;
import com.github.mcreeper12731.utility.Log;

import java.util.Iterator;
import java.util.List;
import java.util.function.Function;

public class BitNegaMaxEvaluator {
    private static final int POSITIVE_INFINITY = Integer.MAX_VALUE;
    private static final int NEGATIVE_INFINITY = Integer.MIN_VALUE + 1;

    private final int debugLevel;
    private final int maxDepth;
    private final int maxNodes;
    private final int maxAdditionalTimelines;
    private final Function<BitGame, Iterator<List<Move>>> moveGenerator;
    private final BitEvaluator evaluator;

    public int startTimelineCount;
    public int maxTimelinesReached;
    public long nodesSearched;
    public boolean stoppedByNodeLimit;

    public BitNegaMaxEvaluator() {
        this(Config.fromFile("negamax"), new BitStaticEvaluator());
    }

    public BitNegaMaxEvaluator(Config config) {
        this(config, new BitStaticEvaluator());
    }

    public BitNegaMaxEvaluator(Config config, BitEvaluator evaluator) {
        Config fileConfig = Config.fromFile("negamax");
        this.debugLevel = config.getOrDefault("debug_level", fileConfig.getInt("debug_level"));
        this.maxDepth = config.getOrDefault("max_depth", fileConfig.getInt("max_depth"));
        this.maxNodes = config.getOrDefault("max_nodes", fileConfig.getInt("max_nodes"));
        this.maxAdditionalTimelines = config.getOrDefault("max_additional_timelines", fileConfig.getInt("max_additional_timelines"));
        this.moveGenerator = config.getOrDefault("move_ordering", "ordered").equals("ordered") ?
                BitMoveGenerator::getIterativeTurnIterator :
                BitMoveGenerator::getTurnsIterator;

        this.evaluator = evaluator;
    }

    public ScoredTurn findBestTurn(BitGame game) {
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

            Iterator<List<Move>> turns = this.moveGenerator.apply(game);

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

    private double negamax(BitGame game, int depth, double alpha, double beta, int color) {
        nodesSearched++;

        if (game.getMultiverse().getTimelines().size() > maxTimelinesReached)
            maxTimelinesReached = game.getMultiverse().getTimelines().size();

        if (nodesSearched >= this.maxNodes) {
            stoppedByNodeLimit = true;
            return color * evaluator.evaluateGameState(game);
        }

        if (depth == 0 || game.isGameOver()) {
            return color * evaluator.evaluateGameState(game);
        }

        double best = NEGATIVE_INFINITY;
        Iterator<List<Move>> turnsIterator = this.moveGenerator.apply(game);
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
}
