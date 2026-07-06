package com.github.mcreeper12731.game.models.scored;

import com.github.mcreeper12731.game.Game;
import com.github.mcreeper12731.game.models.Board;
import com.github.mcreeper12731.game.models.Move;
import com.github.mcreeper12731.game.models.Point4D;
import com.github.mcreeper12731.game.movegeneration.MoveGenerator;
import com.github.mcreeper12731.game.models.pieces.Piece;
import com.github.mcreeper12731.game.models.pieces.PieceType;

import java.util.*;

public record ScoredBoard(Board board, List<Integer> danger, List<Point4D> enemies) {

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

    public ScoredBoard(Board board, Game game) {
        this(board, new ArrayList<>(board.size() * board.size()), new ArrayList<>());
        for (int i = 0; i < board.size() * board.size(); i++) {
            this.danger.add(0);
        }
        this.init(game);
    }

    public void init(Game game) {

        Move noopMove = new Move.Builder()
                .withNoop(board.l(), board.t())
                .build();
        game.applyMove(noopMove);

        List<Board> opponentBoards = game.getPlayableBoards(game.getPlayerTurn().other());
        for (Board board : opponentBoards) {
            List<Move> opponentMoves = MoveGenerator.probableMoves(board, game);
            for (Move move : opponentMoves) {
                if (move.noop()) continue;
                Piece piece = game.getMultiverse().getLocationContents(move.to());
                if (piece.type() == PieceType.KING)
                    registerEnemy(move);
                registerDanger(move);
            }
        }
        game.undoMoveFromCurrentTurn();
    }

    private void registerEnemy(Move move) {
        if (this.enemies.contains(move.from())) return;

        this.enemies.add(move.from());
    }

    private void registerDanger(Move move) {
        Point4D location = move.to();
        if (location.l() != this.board.l() || (location.t() != this.board.t() && location.t() != this.board.t() + 1)) return;

        this.danger.set(
                location.x() + location.y() * board.size(),
                this.danger.get(location.x() + location.y() * board.size()) + 1
        );
    }

    @SuppressWarnings("Duplicates")
    public List<ScoredMove> scoreMoves(Game game) {
        List<ScoredMove> scoredMoves = new ArrayList<>();

        List<Move> boardMoves = MoveGenerator.probableMoves(this.board, game);
        for (Move move : boardMoves) {
            int score = 0;
            if (move.noop()) {
                scoredMoves.add(new ScoredMove(move, score));
                continue;
            }

            if (move.fromType() == PieceType.KING) score -= 100;

            // jump costs
            if (game.doesMoveAddTimeline(move)) score += JUMP_COST;
            else if (game.doesMoveAddInactiveTimeline(move)) score += JUMP_INACTIVE_COST;

            // take enemy reward
            boolean takesEnemy = this.enemies.stream().anyMatch(location -> location.equals(move.to()));
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

            Board nextBoard = this.board.applyMove(this.board.l(), this.board.t() + 1, move);

            int kingCount = 0;
            for (int i = 0; i < nextBoard.size() * nextBoard.size(); i++) {
                Piece piece = nextBoard.getLocationContents(i % nextBoard.size(), i / nextBoard.size());

                if (piece.color() != this.board.getPlayerTurn()) continue;
                score += switch (piece.type()) {
                    case KING -> {
                        kingCount++;
                        int scr = 0;
                        scr += this.danger().get(i) * KING_DANGER_COST;
                        if (kingCount > 0) scr += MANY_KINGS_COST;
                        yield scr;
                    }
                    case QUEEN -> this.danger().get(i) * QUEEN_DANGER_COST;
                    case ROOK -> this.danger().get(i) * ROOK_DANGER_COST;
                    case BISHOP -> this.danger().get(i) * BISHOP_DANGER_COST;
                    case KNIGHT -> this.danger().get(i) * KNIGHT_DANGER_COST;
                    case PAWN -> this.danger().get(i) * PAWN_DANGER_COST;

                    case UNICORN -> this.danger().get(i) * UNICORN_DANGER_COST;
                    case DRAGON -> this.danger().get(i) * DRAGON_DANGER_COST;
                    case PRINCESS -> this.danger().get(i) * PRINCESS_DANGER_COST;
                    case BRAWN -> this.danger().get(i) * BRAWN_DANGER_COST;

                    case EMPTY -> 0;
                };
            }

            if (score < -95_000) continue;

            scoredMoves.add(new ScoredMove(move, score));
        }

        scoredMoves.sort(Comparator.naturalOrder());

        return scoredMoves;
    }

    public List<Integer> danger() {
        return Collections.unmodifiableList(this.danger);
    }

    public List<Point4D> enemies() {
        return Collections.unmodifiableList(this.enemies);
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();

        //builder.append(board);

        for (int y = this.board.size() - 1; y >= 0; y--) {
            for (int x = 0; x < this.board.size(); x++) {
                int index = x + y * board.size();
                builder.append(this.danger.get(index)).append(" ");
                if (index % this.board.size() == this.board.size() - 1) builder.append("\n");
            }
        }

        builder.append(enemies);
        return builder.toString();
    }
}
