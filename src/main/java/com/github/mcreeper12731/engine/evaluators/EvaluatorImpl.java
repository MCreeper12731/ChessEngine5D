package com.github.mcreeper12731.engine.evaluators;

import com.github.mcreeper12731.game.Game;
import com.github.mcreeper12731.game.models.Board;
import com.github.mcreeper12731.game.models.Color;
import com.github.mcreeper12731.game.models.Multiverse;
import com.github.mcreeper12731.game.models.Timeline;
import com.github.mcreeper12731.game.pieces.Piece;
import com.github.mcreeper12731.game.pieces.PieceType;

public class EvaluatorImpl implements Evaluator {

    @Override
    public double evaluate(Game game) {
        double score = 0;
        Multiverse multiverse = game.getMultiverse();

        if (game.getWinner() == Color.WHITE) return 1_000_000;
        if (game.getWinner() == Color.BLACK) return -1_000_000;

        for (Timeline timeline : multiverse.getTimelines()) {
            if (timeline.getL() < 0) score += 2;
            if (timeline.getL() > 0) score -= 2;

            Board board = timeline.getLastBoard();
            for (Piece piece : board.getPieces()) {
                if (piece.color() == Color.WHITE) score += pieceScore(piece.type());
                if (piece.color() == Color.BLACK) score -= pieceScore(piece.type());
            }
        }

        return score;
    }

    public double pieceScore(PieceType type) {

        return switch(type) {
            case EMPTY -> 0;
            case KING -> 1_000;
            case QUEEN -> 9;
            case ROOK -> 5;
            case BISHOP -> 3;
            case KNIGHT -> 3;
            case PAWN -> 1;
            case UNICORN -> 2;
            case DRAGON -> 0.5;
            case PRINCESS -> 8;
            case BRAWN -> 1.5;
        };
    }
}
