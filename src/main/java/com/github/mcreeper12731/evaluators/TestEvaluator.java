package com.github.mcreeper12731.evaluators;

import com.github.mcreeper12731.game.logic.Game;
import com.github.mcreeper12731.game.models.Board;
import com.github.mcreeper12731.game.models.Color;
import com.github.mcreeper12731.game.models.Multiverse;
import com.github.mcreeper12731.game.models.Timeline;
import com.github.mcreeper12731.game.pieces.Piece;

public class TestEvaluator implements GameEvaluator {

    @Override
    public int evaluate(Game game) {
        int score = 0;
        Multiverse multiverse = game.getMultiverse();

        if (game.getWinner() == Color.WHITE) score += 1_000_000;
        if (game.getWinner() == Color.BLACK) score -= 1_000_000;

        for (int l : multiverse.getTimelineLs()) {
            if (l < 0) score += 20;
            if (l > 0) score -= 20;
        }

        for (int timelineL : multiverse.getTimelineLs()) {
            Timeline timeline = multiverse.getTimeline(timelineL);
            Board board = timeline.getLastBoard();
            for (Piece piece : board.pieces()) {
                if (piece.color() == Color.WHITE) score += 10;
                if (piece.color() == Color.BLACK) score -= 10;
            }
        }

        return score;
    }

}
