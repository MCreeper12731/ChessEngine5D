package com.github.mcreeper12731.engine.evaluators;

import com.github.mcreeper12731.bitgame.movegeneration.BitMoveGenerator;
import com.github.mcreeper12731.bitgame.BitGame;
import com.github.mcreeper12731.game.models.Color;
import com.github.mcreeper12731.game.models.Move;
import com.github.mcreeper12731.game.models.scored.ScoredTurn;
import com.github.mcreeper12731.utility.Config;
import com.github.mcreeper12731.utility.Log;

import java.util.Iterator;
import java.util.List;
import java.util.function.Function;

public class BitNegaMaxEvaluator {
    private static final int POSITIVE_INFINITY = Integer.MAX_VALUE;
    private static final int NEGATIVE_INFINITY = Integer.MIN_VALUE + 1;

    // Configuration values
    private final int debugLevel;
    private final int maxDepth;
    private final int maxNodes;
    private final int maxAdditionalTimelines;
    private final Function<BitGame, Iterator<List<Move>>> turnsGenerator;
    private final EvaluatorType evaluator;
    // The nature of 5D chess means that the quiescence search will not stop until it hits the node limit
    // as every move generates copies of boards and thus pieces to capture.
    private final int maxQuiescenceDepth;

    public int startTimelineCount;
    public int maxTimelinesReached;
    public long nodesSearched;
    public boolean stoppedByNodeLimit;

    public BitNegaMaxEvaluator() {
        this(Config.fromFile("negamax"));
    }

    public BitNegaMaxEvaluator(Config config) {
        Config fileConfig = Config.fromFile("negamax");
        this.debugLevel = config.getOrDefault("debug_level", fileConfig.getInt("debug_level"));
        this.maxDepth = config.getOrDefault("max_depth", fileConfig.getInt("max_depth"));
        this.maxNodes = config.getOrDefault("max_nodes", fileConfig.getInt("max_nodes"));
        this.maxAdditionalTimelines = config.getOrDefault("max_additional_timelines", fileConfig.getInt("max_additional_timelines"));
        this.turnsGenerator = config.getOrDefault("move_ordering", fileConfig.getString("move_ordering")).equals("ordered") ?
                bitGame -> BitMoveGenerator.getIterativeTurnIterator(bitGame, true) :
                bitGame -> BitMoveGenerator.getIterativeTurnIterator(bitGame, false);
        this.evaluator = switch (config.getOrDefault("evaluator", fileConfig.getString("evaluator"))) {
            case "static" -> EvaluatorType.STATIC;
            case "quiescence" -> EvaluatorType.QUIESCENCE;
            default -> throw new IllegalArgumentException("Invalid evaluator type");
        };
        this.maxQuiescenceDepth = config.getOrDefault("max_quiescence_depth", fileConfig.getInt("max_quiescence_depth"));
    }

    public ScoredTurn findBestTurn(BitGame game) {
        startTimelineCount = game.getMultiverse().getTimelines().size();
        maxTimelinesReached = 0;
        nodesSearched = 0;
        stoppedByNodeLimit = false;
        long prevNodesSearched;

        List<Move> bestTurn = null;
        int bestScore = NEGATIVE_INFINITY;
        int currentDepth = 1;

        while (currentDepth <= this.maxDepth) {
            startTimelineCount = game.getMultiverse().getTimelines().size();
            nodesSearched = 0;
            stoppedByNodeLimit = false;
            prevNodesSearched = 0;

            bestTurn = null;
            bestScore = NEGATIVE_INFINITY;

            Iterator<List<Move>> turns = this.turnsGenerator.apply(game);

            while (turns.hasNext()) {
                List<Move> turn = turns.next();

                game.applyMoves(turn);
                if (!game.isCurrentTurnFinalizable()) {
                    Log.print("BitAlphaBeta", "Found non-finalizable turn!");
                    game.isCurrentTurnFinalizable();
                    game.undoAllMovesFromCurrentTurn();
                    continue;
                }
                game.finalizeTurn();

                if (this.debugLevel >= 5) {
                    Log.debug("AlphaBeta", "Exploring: " + turn);
                }

                int score = -negamax(
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

    private int negamax(BitGame game, int depth, int alpha, int beta, int color) {

        if (game.getMultiverse().getTimelines().size() > this.maxTimelinesReached)
            this.maxTimelinesReached = game.getMultiverse().getTimelines().size();

        if (nodesSearched >= this.maxNodes) {
            stoppedByNodeLimit = true;
            return BitEvaluator.evaluateGameState(game);
        }

        if (depth == 0 || game.isGameOver()) {
            return switch (evaluator) {
                case STATIC -> BitEvaluator.evaluateGameState(game);
                case QUIESCENCE -> quiescence(game, this.maxQuiescenceDepth, alpha, beta, color);
                default -> throw new IllegalStateException("Unknown evaluator: " + evaluator);
            };
        }
        this.nodesSearched++;

        int best = NEGATIVE_INFINITY;
        Iterator<List<Move>> turnsIterator = this.turnsGenerator.apply(game);
        if (!turnsIterator.hasNext()) {
            return 0;
        }

        while (turnsIterator.hasNext()) {
            if (this.stoppedByNodeLimit) break;
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

            int score = -negamax(game, depth - 1, -beta, -alpha, -color);

            if (this.debugLevel >= 10 && score > best) {
                Log.debug(" ".repeat(this.maxDepth - depth) + "AlphaBeta", "Exploring turn:", "best score:", score);
            }

            game.undoTurn();
            best = Math.max(best, score);
            alpha = Math.max(alpha, score);
            if (alpha >= beta) break;
        }
        return best;
    }

    private int quiescence(BitGame game, int depth, int alpha, int beta, int color) {

        int evaluation = BitEvaluator.evaluateGameState(game);

        if (this.nodesSearched >= this.maxNodes) {
            this.stoppedByNodeLimit = true;
            return evaluation;
        }

        if (depth == 0) {
            return evaluation;
        }

        this.nodesSearched++;

        if (evaluation >= beta)
            return beta;

        if (evaluation >= alpha)
            alpha = evaluation;

        int best = evaluation;
        Iterator<List<Move>> turnsIterator = this.turnsGenerator.apply(game);
        while (turnsIterator.hasNext()) {
            if (this.stoppedByNodeLimit) break;
            List<Move> turn = turnsIterator.next();

            boolean isCapture = false;
            for (Move move : turn) {
                if (move.to() == null) continue;
                if (game.getMultiverse().getLocationContents(move.to()) != 0 || move.enPassant() != null) {
                    isCapture = true;
                    break;
                }
            }
            if (!isCapture) continue;
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

            int score = -quiescence(game, depth - 1, -beta, -alpha, -color);

            game.undoTurn();
            best = Math.max(best, score);
            alpha = Math.max(alpha, score);
            if (alpha >= beta) break;
        }
        return best;
    }
}
