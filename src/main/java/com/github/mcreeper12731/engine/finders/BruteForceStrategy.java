package com.github.mcreeper12731.engine.finders;

import com.github.mcreeper12731.engine.ChessEngine;
import com.github.mcreeper12731.game.models.Multiverse;
import com.github.mcreeper12731.game.moves.Move;
import com.github.mcreeper12731.game.moves.MoveGenerator;
import com.github.mcreeper12731.game.moves.Turn;
import com.github.mcreeper12731.game.moves.TurnNode;
import com.github.mcreeper12731.game.pieces.Piece;
import com.github.mcreeper12731.game.pieces.PieceType;

import java.util.Optional;
import java.util.Queue;

public class BruteForceStrategy implements MoveStrategy {

    private final ChessEngine chessEngine;
    private final Multiverse startingMultiverse;
    private final int plyLimit;

    private TurnNode mateTree;

    public BruteForceStrategy(ChessEngine chessEngine, int moveLimit) {
        this.chessEngine = chessEngine;
        this.startingMultiverse = chessEngine.getMultiverse();
        this.plyLimit = moveLimit * 2;
    }

    @Override
    public Turn nextTurn() {

        if (mateTree == null) {
            mateTree = buildMateTree(startingMultiverse, plyLimit, null);
            if (mateTree == null) {
                System.out.println("Brute force strategy did not find a mate in " + plyLimit / 2 + " moves!");
                return null;
            }
        }
        return mateTree.getTurn();
    }

    @Override
    public void opponentTurn(Turn turn) {
        mateTree = mateTree.getResponse(turn);
    }

    public TurnNode buildMateTree(Multiverse multiverse, int depth, Turn prevTurn) {

        Optional<Turn> kingCaptureTurn = getKingCaptureTurn(multiverse);

        if (chessEngine.getPlayingAs() == multiverse.getPlayerTurn() && kingCaptureTurn.isPresent()) {
            // System.out.println(" ".repeat(plyCount - depth) + " - CHECKMATED");
            return new TurnNode(kingCaptureTurn.get());
        }
        if (depth == 0) {
            // System.out.println(" ".repeat(plyCount - depth) + " - NOT CHECKMATED");
            return null;
        }

        Queue<Turn> turns = MoveGenerator.generateAllTurns(multiverse);

        if (multiverse.getPlayerTurn() == chessEngine.getPlayingAs()) {
            // Engine playing

            while (!turns.isEmpty()) {

                if (plyLimit == depth) System.out.println("Turns left: " + turns.size());

                Turn turn = turns.poll();

                // System.out.printf(" ".repeat(plyCount - depth) + "%s plays: %s%n", multiverse.getPlayerTurn().name().charAt(0), turn);
                multiverse.applyAndFinalizeTurn(turn, true);

                TurnNode mateTree = buildMateTree(multiverse, depth - 1, turn);
                if (mateTree != null) {
                    multiverse.undoTurn();
                    return mateTree;
                }

                multiverse.undoTurn();
            }

            return null;
        } else {
            // Opponent playing

            TurnNode root = new TurnNode(prevTurn);

            while (!turns.isEmpty()) {

                Turn turn = turns.poll();
                if (isKingCapture(multiverse, turn)) return null;

                // System.out.printf(" ".repeat(plyCount - depth) + "%s plays: %s%n", multiverse.getPlayerTurn().name().charAt(0), turn);
                multiverse.applyAndFinalizeTurn(turn, true);

                TurnNode mateTree = buildMateTree(multiverse, depth - 1, turn);
                if (mateTree == null) {
                    multiverse.undoTurn();
                    return null;
                }

                root.addResponse(turn, mateTree);

                multiverse.undoTurn();
            }

            return root;
        }
    }

    public boolean isKingCapture(Multiverse multiverse, Turn turn) {
        for (Move move : turn.getMoves()) {
            if (
                    multiverse.getPiece(move.to())
                            .filter(piece -> piece.type() == PieceType.KING)
                            .isPresent()
            ) return true;
        }
        return false;
    }

    public Optional<Turn> getKingCaptureTurn(Multiverse multiverse) {

        for (Turn turn : MoveGenerator.generateAllTurns(multiverse)) {
            for (Move move : turn.getMoves()) {

                Piece piece = multiverse
                        .getTimeline(move.toTimeline())
                        .getBoardByTime(move.toTime())
                        .getPiece(move.toX(), move.toY());

                if (piece != null && piece.type() == PieceType.KING) {
                    // System.out.print(" ".repeat(deco) + move);
                    return Optional.of(turn);
                }
            }
        }

        return Optional.empty();
    }
}
