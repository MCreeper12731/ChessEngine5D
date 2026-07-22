package com.github.mcreeper12731.engine.evaluators;

import com.github.mcreeper12731.bitgame.BitGame;
import com.github.mcreeper12731.bitgame.models.BitBoard;
import com.github.mcreeper12731.bitgame.models.BitMultiverse;
import com.github.mcreeper12731.bitgame.models.BitTimeline;
import com.github.mcreeper12731.bitgame.models.pieces.BitPiece;
import com.github.mcreeper12731.bitgame.models.scored.ScoredBitBoard;
import com.github.mcreeper12731.bitgame.movegeneration.movesets.BitSlidingMoveSet;
import com.github.mcreeper12731.game.models.Board;
import com.github.mcreeper12731.game.models.Color;
import com.github.mcreeper12731.game.models.Move;
import com.github.mcreeper12731.game.models.Point4D;
import com.github.mcreeper12731.game.models.pieces.PieceType;
import com.github.mcreeper12731.game.movegeneration.movesets.MoveDirections;
import com.github.mcreeper12731.utility.Iterators;

import static com.github.mcreeper12731.engine.evaluators.Constant.*;
import static com.github.mcreeper12731.engine.evaluators.Constant.BISHOP_DANGER_COST;
import static com.github.mcreeper12731.engine.evaluators.Constant.BRAWN_DANGER_COST;
import static com.github.mcreeper12731.engine.evaluators.Constant.DRAGON_DANGER_COST;
import static com.github.mcreeper12731.engine.evaluators.Constant.KING_DANGER_COST;
import static com.github.mcreeper12731.engine.evaluators.Constant.KNIGHT_DANGER_COST;
import static com.github.mcreeper12731.engine.evaluators.Constant.MANY_KINGS_COST;
import static com.github.mcreeper12731.engine.evaluators.Constant.PAWN_DANGER_COST;
import static com.github.mcreeper12731.engine.evaluators.Constant.PRINCESS_DANGER_COST;
import static com.github.mcreeper12731.engine.evaluators.Constant.QUEEN_DANGER_COST;
import static com.github.mcreeper12731.engine.evaluators.Constant.ROOK_DANGER_COST;
import static com.github.mcreeper12731.engine.evaluators.Constant.TAKE_BISHOP_REWARD;
import static com.github.mcreeper12731.engine.evaluators.Constant.TAKE_BRAWN_REWARD;
import static com.github.mcreeper12731.engine.evaluators.Constant.TAKE_DRAGON_REWARD;
import static com.github.mcreeper12731.engine.evaluators.Constant.TAKE_KNIGHT_REWARD;
import static com.github.mcreeper12731.engine.evaluators.Constant.TAKE_PAWN_REWARD;
import static com.github.mcreeper12731.engine.evaluators.Constant.TAKE_PRINCESS_REWARD;
import static com.github.mcreeper12731.engine.evaluators.Constant.TAKE_QUEEN_REWARD;
import static com.github.mcreeper12731.engine.evaluators.Constant.TAKE_ROOK_REWARD;
import static com.github.mcreeper12731.engine.evaluators.Constant.TAKE_UNICORN_REWARD;
import static com.github.mcreeper12731.engine.evaluators.Constant.UNICORN_DANGER_COST;

public class BitEvaluator {

    public static int evaluateGameState(BitGame game) {

        if (game.isGameOver())
            return 1_000_000 * (game.getPlayerTurn() == game.getWinner() ? 1 : -1);

        int score = 0;

        /*int playerMobility = pieceMobility(game, game.getPlayerTurn());
        int opponentMobility = pieceMobility(game, game.getPlayerTurn().other());
        score += (playerMobility - opponentMobility);

        int playerPieceScore = pieceScore(game, game.getPlayerTurn());
        int opponentPieceScore = pieceScore(game, game.getPlayerTurn());
        score += (playerPieceScore - opponentPieceScore);
*/
        return score;
    }

    private static int pieceScore(BitGame game, Color player) {

        int score = 0;
        for (BitTimeline timeline : game.getMultiverse().getTimelines()) {
            BitBoard board = timeline.getLastBoard();
            for (int x = 0; x < board.size(); x++) {
                for (int y = 0; y < board.size(); y++) {
                    byte piece = board.getLocationContents(x, y);
                    if (piece == 0) continue;
                    if (BitPiece.colorOrdinal(piece) != player.ordinal()) continue;
                    int typeOrdinal = BitPiece.typeOrdinal(piece);
                    score += Constant.PieceValue.fromOrdinal(typeOrdinal);
                }
            }
        }

        return score;
    }

    private static int pieceMobility(BitGame game, Color player) {

        int score = 0;
        for (BitTimeline timeline : game.getMultiverse().getTimelines()) {
            BitBoard board = timeline.getLastBoard();
            for (int x = 0; x < board.size(); x++) {
                for (int y = 0; y < board.size(); y++) {
                    byte piece = board.getLocationContents(x, y);
                    if (piece == 0) continue;
                    if (BitPiece.colorOrdinal(piece) != player.ordinal()) continue;
                    Point4D location = new Point4D(board.l(), board.t(), x, y);
                    int typeOrdinal = BitPiece.typeOrdinal(piece);
                    int moveCount;
                    if (typeOrdinal == PieceType.KING.ordinal()) {
                        moveCount = Iterators.consumeRemaining(
                                new BitSlidingMoveSet(MoveDirections.DIRECTIONS_1234_DIM).iterator(game, location)
                        ).size();
                    } else {
                        moveCount = Iterators.consumeRemaining(
                                BitPiece.getMoveIterator(game, location)
                        ).size();
                    }

                    score += moveCount + Constant.PieceMobilityWeight.fromOrdinal(
                            BitPiece.typeOrdinal(piece)
                    );
                }
            }
        }

        return score;
    }

    // Move heuristic - TODO
    public static int evaluateMove(Move move, BitGame game, ScoredBitBoard board) {
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
