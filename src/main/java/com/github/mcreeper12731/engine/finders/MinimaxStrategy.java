package com.github.mcreeper12731.engine.finders;

import com.github.mcreeper12731.engine.ChessEngine;
import com.github.mcreeper12731.evaluators.Evaluator;
import com.github.mcreeper12731.game.models.Color;
import com.github.mcreeper12731.game.models.Multiverse;
import com.github.mcreeper12731.game.moves.MoveGenerator;
import com.github.mcreeper12731.game.moves.SearchNode;
import com.github.mcreeper12731.game.moves.Turn;

import java.util.Queue;

public class MinimaxStrategy implements MoveStrategy {

    private final ChessEngine chessEngine;
    private final int plyLimit;

    public MinimaxStrategy(ChessEngine chessEngine, int moveLimit) {
        this.chessEngine = chessEngine;
        this.plyLimit = moveLimit * 2 + 1;
    }

    @Override
    public Turn nextTurn() {

        SearchNode tree = buildTree(
                chessEngine.getMultiverse(),
                null,
                plyLimit,
                true,
                Integer.MIN_VALUE,
                Integer.MAX_VALUE,
                chessEngine.getPlayingAs()
        );

        return tree.getBestChild(true).orElseThrow().getTurn();
    }

    private SearchNode buildTree(
            Multiverse multiverse,
            Turn turn,
            int depth,
            boolean maximizingPlayer,
            int alpha,
            int beta,
            Color perspective
    ) {

        SearchNode node = new SearchNode(turn);

        if (depth == 0) {
            int score = Evaluator.evaluate(multiverse, perspective);
            node.setScore(score);
            return node;
        }

        int bestScore = maximizingPlayer ? Integer.MIN_VALUE : Integer.MAX_VALUE;

        Queue<Turn> legalTurns = MoveGenerator.generateAllTurns(multiverse);
        for (Turn nextTurn : legalTurns) {

            multiverse.applyAndFinalizeTurn(nextTurn, true);

            SearchNode childNode = buildTree(
                    multiverse, nextTurn, depth - 1,
                    !maximizingPlayer, alpha, beta, perspective
            );
            multiverse.undoTurn();

            node.addChild(childNode);
            int childScore = childNode.getScore();

            if (maximizingPlayer) {
                bestScore = Math.max(bestScore, childScore);
                alpha = Math.max(alpha, childScore);
            } else {
                bestScore = Math.min(bestScore, childScore);
                beta = Math.min(beta, childScore);
            }

            if (beta <= alpha) break;
        }

        node.setScore(bestScore);
        return node;
    }

    @Override
    public void opponentTurn(Turn turn) {

    }
}
