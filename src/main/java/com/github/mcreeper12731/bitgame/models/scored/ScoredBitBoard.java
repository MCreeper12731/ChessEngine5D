package com.github.mcreeper12731.bitgame.models.scored;

import com.github.mcreeper12731.bitgame.BitGame;
import com.github.mcreeper12731.bitgame.models.BitBoard;
import com.github.mcreeper12731.bitgame.models.pieces.BitPiece;
import com.github.mcreeper12731.bitgame.movegeneration.BitMoveGenerator;
import com.github.mcreeper12731.engine.evaluators.Evaluator;
import com.github.mcreeper12731.game.Game;
import com.github.mcreeper12731.game.models.Board;
import com.github.mcreeper12731.game.models.Move;
import com.github.mcreeper12731.game.models.Point4D;
import com.github.mcreeper12731.game.models.pieces.Piece;
import com.github.mcreeper12731.game.models.pieces.PieceType;
import com.github.mcreeper12731.game.models.scored.ScoredMove;
import com.github.mcreeper12731.game.movegeneration.MoveGenerator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public record ScoredBitBoard(BitBoard board, List<Integer> danger, List<Point4D> enemies) {

    public ScoredBitBoard(BitBoard board, BitGame game) {
        this(board, new ArrayList<>(board.size() * board.size()), new ArrayList<>());
        for (int i = 0; i < board.size() * board.size(); i++) {
            this.danger.add(0);
        }
        this.init(game);
    }

    public void init(BitGame game) {

        Move noopMove = new Move.Builder()
                .withNoop(board.l(), board.t())
                .build();
        game.applyMove(noopMove);

        for (int l : game.getPlayableTimelineLs(game.getPlayerTurn().other())) {
            BitBoard board = game.getMultiverse().getTimeline(l).getLastBoard();
            List<Move> opponentMoves = BitMoveGenerator.probableMoves(board, game);
            for (Move move : opponentMoves) {
                if (move.noop()) continue;
                byte piece = game.getMultiverse().getLocationContents(move.to());
                if (BitPiece.typeOrdinal(piece) == PieceType.KING.ordinal())
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
        if (location.l() != this.board.l() || (location.t() != this.board.t() && location.t() != this.board.t() + 1))
            return;

        this.danger.set(
                location.x() + location.y() * board.size(),
                this.danger.get(location.x() + location.y() * board.size()) + 1
        );
    }

    @SuppressWarnings("Duplicates")
    public List<ScoredMove> scoreMoves(BitGame game) {
        List<ScoredMove> scoredMoves = new ArrayList<>();

        Evaluator evaluator = new Evaluator();

        List<Move> boardMoves = BitMoveGenerator.probableMoves(this.board, game);
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
                int index = x + y * this.board.size();
                builder.append(this.danger.get(index)).append(" ");
                if (index % this.board.size() == this.board.size() - 1) builder.append("\n");
            }
        }

        builder.append(this.enemies);
        return builder.toString();
    }
}
