package com.github.mcreeper12731.engine.evaluators;

import com.github.mcreeper12731.bitgame.BitGame;
import com.github.mcreeper12731.bitgame.models.BitBoard;
import com.github.mcreeper12731.bitgame.models.BitMultiverse;
import com.github.mcreeper12731.bitgame.models.BitTimeline;
import com.github.mcreeper12731.bitgame.models.pieces.BitPiece;
import com.github.mcreeper12731.bitgame.models.scored.ScoredBitBoard;
import com.github.mcreeper12731.game.models.Color;
import com.github.mcreeper12731.game.models.Move;
import com.github.mcreeper12731.game.models.pieces.PieceType;

import static com.github.mcreeper12731.engine.evaluators.Constant.*;

public class BitStaticEvaluator implements BitEvaluator {

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
                byte piece = board.getLocationContentsFromIndex(i);
                if (piece == 0) continue;
                int pieceColor = BitPiece.colorOrdinal(piece);
                if (pieceColor == BitGame.WHITE) score += PieceValue.fromOrdinal(BitPiece.typeOrdinal(piece));
                if (pieceColor == BitGame.BLACK) score -= PieceValue.fromOrdinal(BitPiece.typeOrdinal(piece));
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
            byte piece = nextBoard.getLocationContentsFromIndex(i);

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

}
