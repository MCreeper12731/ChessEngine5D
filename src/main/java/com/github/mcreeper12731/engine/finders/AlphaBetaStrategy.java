package com.github.mcreeper12731.engine.finders;

import com.github.mcreeper12731.engine.config.AlphaBetaStrategyConfig;
import com.github.mcreeper12731.evaluators.GameEvaluator;
import com.github.mcreeper12731.evaluators.ScoredTurn;
import com.github.mcreeper12731.game.logic.Game;
import com.github.mcreeper12731.game.logic.MoveGenerator;
import com.github.mcreeper12731.game.moves.Move;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class AlphaBetaStrategy {
    private static final int POSITIVE_INFINITY = 1_000_000_000;
    private static final int NEGATIVE_INFINITY = -POSITIVE_INFINITY;

    private final AlphaBetaStrategyConfig config;
    private final GameEvaluator evaluator;

    private long nodesSearched;
    private boolean stoppedByNodeLimit;

    public AlphaBetaStrategy(AlphaBetaStrategyConfig config, GameEvaluator evaluator) {
        this.config = config;
        this.evaluator = evaluator;
    }

    public ScoredTurn findBestTurn(Game game) {
        nodesSearched = 0;
        stoppedByNodeLimit = false;
        long prevNodesSearched = 0;

        List<Move> bestTurn = null;
        int bestScore = NEGATIVE_INFINITY;

        Iterator<List<Move>> turns = MoveGenerator.getSortedTurnsIterator(game, this.evaluator);

        while (turns.hasNext()) {
            List<Move> turn = turns.next();

            if (config.debugLevel() >= 5) {
                System.out.println("Exploring: " + turn);
            }

            game.applyMovesAndFinalizeTurn(turn);

            int score = alphaBeta(
                    game,
                    config.maxDepth() - 1,
                    NEGATIVE_INFINITY,
                    POSITIVE_INFINITY,
                    false
            );

            game.undoTurn();

            if (config.debugLevel() >= 1) {
                System.out.println("Root candidate score=" + score + ": " + turn + ", spent nodes=" + (nodesSearched - prevNodesSearched));
            }
            prevNodesSearched = nodesSearched;

            if (score > bestScore) {
                bestScore = score;
                bestTurn = new ArrayList<>(turn);
            }

            if (stoppedByNodeLimit) {
                break;
            }
        }

        if (config.debugLevel() >= 2) {
            System.out.println("AlphaBeta nodes searched: " + nodesSearched);
            System.out.println("Stopped by node limit: " + stoppedByNodeLimit);
            System.out.println("Best score: " + bestScore);
            System.out.println("Best turn: " + bestTurn);
        }

        return new ScoredTurn(bestTurn, bestScore, nodesSearched);
    }

    private int alphaBeta(
            Game game,
            int depth,
            int alpha,
            int beta,
            boolean maximizingPlayer
    ) {
        nodesSearched++;

        if (nodesSearched >= config.maxNodes()) {
            stoppedByNodeLimit = true;
            return evaluator.evaluate(game);
        }

        if (depth == 0 || isTerminal(game)) {
            return evaluator.evaluate(game);
        }

        if (maximizingPlayer) {
            int bestScore = NEGATIVE_INFINITY;

            Iterator<List<Move>> turns = depth >= config.maxDepth() - 1
                    ? MoveGenerator.getSortedTurnsIterator(game, evaluator)
                    : MoveGenerator.getTurnsIterator(game);
            while (turns.hasNext()) {
                List<Move> turn = turns.next();

                if (config.debugLevel() >= 10) {
                    System.out.print(" ".repeat(2 * config.maxDepth() - 2 * depth) + "(" + depth + ") Exploring: ");
                    System.out.println(turn);
                }
                game.applyMovesAndFinalizeTurn(turn);

                int score = alphaBeta(
                        game,
                        depth - 1,
                        alpha,
                        beta,
                        false
                );

                game.undoTurn();

                bestScore = Math.max(bestScore, score);
                alpha = Math.max(alpha, bestScore);

                if (alpha >= beta || stoppedByNodeLimit) {
                    break;
                }
            }

            return bestScore;
        } else {
            int bestScore = POSITIVE_INFINITY;

            Iterator<List<Move>> turns = depth >= config.maxDepth() - 1
                    ? MoveGenerator.getSortedTurnsIterator(game, evaluator)
                    : MoveGenerator.getTurnsIterator(game);
            while (turns.hasNext()) {
                List<Move> turn = turns.next();

                if (config.debugLevel() >= 10) {
                    System.out.print(" ".repeat(2 * config.maxDepth() - 2 * depth) + "(" + depth + ") Exploring: ");
                    System.out.println(turn);
                }
                game.applyMovesAndFinalizeTurn(turn);

                int score = alphaBeta(
                        game,
                        depth - 1,
                        alpha,
                        beta,
                        true
                );

                game.undoTurn();

                bestScore = Math.min(bestScore, score);
                beta = Math.min(beta, bestScore);

                if (alpha >= beta || stoppedByNodeLimit) {
                    break;
                }
            }

            return bestScore;
        }
    }

    private boolean isTerminal(Game game) {
        return game.isGameOver();
    }
}
