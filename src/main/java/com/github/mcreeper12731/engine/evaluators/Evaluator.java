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

public class Evaluator {

    public static final int JUMP_COST              = -2;
    public static final int JUMP_INACTIVE_COST     = -50;
    public static final int TAKE_ENEMY_REWARD      = 20;
    public static final int KING_DANGER_COST       = -100_000;

    public static final int ROOK_DANGER_COST       = -3;
    public static final int KNIGHT_DANGER_COST     = -4;
    public static final int BISHOP_DANGER_COST     = -5;
    public static final int QUEEN_DANGER_COST      = -10;
    public static final int UNICORN_DANGER_COST    = -2;
    public static final int DRAGON_DANGER_COST     = -2;
    public static final int PRINCESS_DANGER_COST   = -8;
    public static final int BRAWN_DANGER_COST      = -2;
    public static final int PAWN_DANGER_COST      = -1;

    public static final int TAKE_ROOK_REWARD       = 3;
    public static final int TAKE_KNIGHT_REWARD     = 4;
    public static final int TAKE_BISHOP_REWARD     = 5;
    public static final int TAKE_PRINCESS_REWARD   = 8;
    public static final int TAKE_QUEEN_REWARD      = 10;
    public static final int TAKE_UNICORN_REWARD    = 2;
    public static final int TAKE_DRAGON_REWARD     = 2;
    public static final int TAKE_PAWN_REWARD      = 1;
    public static final int TAKE_BRAWN_REWARD     = 1;

    public static final int MANY_KINGS_COST        = -6;

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
                if (piece.color() == Color.WHITE) score += pieceScore(piece.type());
                if (piece.color() == Color.BLACK) score -= pieceScore(piece.type());
            }
        }

        return score;
    }

    public double evaluateGameState(BitGame game) {
        double score = 0;
        BitMultiverse multiverse = game.getMultiverse();

        if (game.getWinner() == Color.WHITE) return 1_000_000;
        if (game.getWinner() == Color.BLACK) return -1_000_000;

        for (BitTimeline timeline : multiverse.getTimelines()) {
            if (timeline.getL() < 0) score += 2;
            if (timeline.getL() > 0) score -= 2;

            BitBoard board = timeline.getLastBoard();
            for (int i = 0; i < board.size() * board.size(); i++) {
                byte piece = board.getLocationContents(i % board.size(), i / board.size());
                if (piece == 0) continue;
                int pieceColor = BitPiece.colorOrdinal(piece);
                PieceType pieceType = PieceType.of(BitPiece.typeOrdinal(piece));
                if (pieceColor == BitGame.WHITE) score += pieceScore(pieceType);
                if (pieceColor == BitGame.BLACK) score -= pieceScore(pieceType);
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

    public int evaluateMove(Move move, BitGame game, ScoredBitBoard board) {
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
        score += switch (BitPiece.typeOrdinal(game.getMultiverse().getLocationContents(move.to()))) {
            case 0 -> 1_000_000;
            case 1 -> TAKE_QUEEN_REWARD;
            case 2 -> TAKE_ROOK_REWARD;
            case 3 -> TAKE_BISHOP_REWARD;
            case 4 -> TAKE_KNIGHT_REWARD;
            case 5 -> TAKE_PAWN_REWARD;

            case 6 -> TAKE_UNICORN_REWARD;
            case 7 -> TAKE_DRAGON_REWARD;
            case 8 -> TAKE_PRINCESS_REWARD;
            case 9 -> TAKE_BRAWN_REWARD;

            default -> 0;
        };

        BitBoard nextBoard = board.board().applyMove(board.board().l(), board.board().t() + 1, move);

        int kingCount = 0;
        for (int i = 0; i < nextBoard.size() * nextBoard.size(); i++) {
            byte piece = nextBoard.getLocationContents(i % nextBoard.size(), i / nextBoard.size());

            if (BitPiece.colorOrdinal(piece) != board.board().getPlayerTurn().ordinal()) continue;
            switch (BitPiece.typeOrdinal(piece)) {
                case 0 -> {
                    kingCount++;
                    if (board.danger().get(i) > 0) return KING_DANGER_COST;
                    if (kingCount > 0) score += MANY_KINGS_COST;
                }
                case 1 -> score += board.danger().get(i) * QUEEN_DANGER_COST;
                case 2 -> score += board.danger().get(i) * ROOK_DANGER_COST;
                case 3 -> score += board.danger().get(i) * BISHOP_DANGER_COST;
                case 4 -> score += board.danger().get(i) * KNIGHT_DANGER_COST;
                case 5 -> score += board.danger().get(i) * PAWN_DANGER_COST;

                case 6 -> score += board.danger().get(i) * UNICORN_DANGER_COST;
                case 7 -> score += board.danger().get(i) * DRAGON_DANGER_COST;
                case 8 -> score += board.danger().get(i) * PRINCESS_DANGER_COST;
                case 9 -> score += board.danger().get(i) * BRAWN_DANGER_COST;

                default -> {}
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
