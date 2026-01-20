package com.github.mcreeper12731.evaluators;

import com.github.mcreeper12731.game.models.Board;
import com.github.mcreeper12731.game.models.Color;
import com.github.mcreeper12731.game.models.Multiverse;
import com.github.mcreeper12731.game.models.Timeline;
import com.github.mcreeper12731.game.pieces.Piece;
import com.github.mcreeper12731.game.pieces.PieceType;

import java.util.Map;

public class Evaluator {

    public static final Map<PieceType, Integer> PIECE_VALUE = Map.of(
            PieceType.KING, 100000,
            PieceType.QUEEN, 900,
            PieceType.ROOK, 500,
            PieceType.BISHOP, 333,
            PieceType.KNIGHT, 300,
            PieceType.PAWN, 100
    );

    public static int evaluate(Multiverse multiverse, Color playerColor) {
        int score = 0;

        for (Timeline timeline : multiverse.getTimelines()) {
            Board board = timeline.getLastBoard();

            for (Piece piece : board.getPieces()) {
                if (piece.color() == playerColor) {
                    score += PIECE_VALUE.get(piece.type());
                } else {
                    score -= PIECE_VALUE.get(piece.type());
                }
            }
        }

        return score;
    }
}
