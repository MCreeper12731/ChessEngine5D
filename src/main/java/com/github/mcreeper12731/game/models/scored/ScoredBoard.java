package com.github.mcreeper12731.game.models.scored;

import com.github.mcreeper12731.engine.evaluators.StaticEvaluator;
import com.github.mcreeper12731.game.Game;
import com.github.mcreeper12731.game.models.Board;
import com.github.mcreeper12731.game.models.Move;
import com.github.mcreeper12731.game.models.Point4D;
import com.github.mcreeper12731.game.movegeneration.MoveGenerator;
import com.github.mcreeper12731.game.models.pieces.Piece;
import com.github.mcreeper12731.game.models.pieces.PieceType;

import java.util.*;

public record ScoredBoard(Board board, List<Integer> danger, List<Point4D> enemies) {

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

        StaticEvaluator evaluator = new StaticEvaluator();

        List<Move> boardMoves = MoveGenerator.probableMoves(this.board, game);
        boardMoves.addFirst(
                new Move.Builder()
                        .withNoop()
                        .build()
        );
        for (Move move : boardMoves) {

            int score = evaluator.evaluateMove(move, game, this);

            if (score == -100_000) continue;

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
