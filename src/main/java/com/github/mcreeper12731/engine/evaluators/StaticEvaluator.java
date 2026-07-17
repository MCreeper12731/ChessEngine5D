package com.github.mcreeper12731.engine.evaluators;

import com.github.mcreeper12731.bitgame.BitGame;
import com.github.mcreeper12731.bitgame.models.*;
import com.github.mcreeper12731.bitgame.models.pieces.BitPiece;
import com.github.mcreeper12731.bitgame.models.scored.ScoredBitBoard;
import com.github.mcreeper12731.game.Game;
import com.github.mcreeper12731.game.models.*;
import com.github.mcreeper12731.game.models.pieces.Piece;
import com.github.mcreeper12731.game.models.pieces.PieceType;
import com.github.mcreeper12731.game.models.scored.ScoredBoard;

import static com.github.mcreeper12731.engine.evaluators.Constant.*;

public class StaticEvaluator implements Evaluator {

    public double evaluateGameState(Game game) {
        double score = 0;
        Multiverse multiverse = game.getMultiverse();

        if (game.getWinner() == Color.WHITE) return 1_000_000;
        if (game.getWinner() == Color.BLACK) return -1_000_000;

        for (Timeline timeline : multiverse.getTimelines()) {
            if (timeline.getL() < 0) score += 2;
            if (timeline.getL() > 0) score -= 2;

            Board board = timeline.getLastBoard();
            for (int i = 0; i < board.size() * board.size(); i++) {
                Piece piece = board.getLocationContents(i % board.size(), i / board.size());
                if (piece.type() == PieceType.EMPTY) continue;
                if (piece.color() == Color.WHITE) score += PieceValue.fromType(piece.type());
                if (piece.color() == Color.BLACK) score -= PieceValue.fromType(piece.type());
            }
        }

        return score;
    }

    public int evaluateMove(Move move, Game game, ScoredBoard board) {
        int score = 0;
        if (move.noop()) return 0;

        if (move.fromType() == PieceType.KING) score -= 100;

        // jump costs
        if (game.doesMoveAddTimeline(move)) score += JUMP_COST;
        else if (game.doesMoveAddInactiveTimeline(move)) score += JUMP_INACTIVE_COST;

        // take enemy reward
        boolean takesEnemy = board.enemies().stream().anyMatch(location -> location.equals(move.to()));
        if (takesEnemy) score += TAKE_ENEMY_REWARD;

        // take piece rewards
        score += switch (game.getMultiverse().getLocationContents(move.to()).type()) {
            case KING -> 1_000_000;
            case QUEEN -> TAKE_QUEEN_REWARD;
            case ROOK -> TAKE_ROOK_REWARD;
            case BISHOP -> TAKE_BISHOP_REWARD;
            case KNIGHT -> TAKE_KNIGHT_REWARD;
            case PAWN -> TAKE_PAWN_REWARD;

            case UNICORN -> TAKE_UNICORN_REWARD;
            case DRAGON -> TAKE_DRAGON_REWARD;
            case PRINCESS -> TAKE_PRINCESS_REWARD;
            case BRAWN -> TAKE_BRAWN_REWARD;

            case EMPTY -> 0;
        };

        Board nextBoard = board.board().applyMove(board.board().l(), board.board().t() + 1, move);

        int kingCount = 0;
        for (int i = 0; i < nextBoard.size() * nextBoard.size(); i++) {
            Piece piece = nextBoard.getLocationContents(i % nextBoard.size(), i / nextBoard.size());

            if (piece.color() != board.board().getPlayerTurn()) continue;
            switch (piece.type()) {
                case KING -> {
                    kingCount++;
                    if (board.danger().get(i) > 0) return KING_DANGER_COST;
                    if (kingCount > 0) score += MANY_KINGS_COST;
                }
                case QUEEN -> score += board.danger().get(i) * QUEEN_DANGER_COST;
                case ROOK -> score += board.danger().get(i) * ROOK_DANGER_COST;
                case BISHOP -> score += board.danger().get(i) * BISHOP_DANGER_COST;
                case KNIGHT -> score += board.danger().get(i) * KNIGHT_DANGER_COST;
                case PAWN -> score += board.danger().get(i) * PAWN_DANGER_COST;

                case UNICORN -> score += board.danger().get(i) * UNICORN_DANGER_COST;
                case DRAGON -> score += board.danger().get(i) * DRAGON_DANGER_COST;
                case PRINCESS -> score += board.danger().get(i) * PRINCESS_DANGER_COST;
                case BRAWN -> score += board.danger().get(i) * BRAWN_DANGER_COST;

                case EMPTY -> {}
            }
        }
        return score;
    }
}
