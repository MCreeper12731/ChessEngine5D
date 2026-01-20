package com.github.mcreeper12731.game.moves;

import com.github.mcreeper12731.game.models.Board;
import com.github.mcreeper12731.game.models.Multiverse;
import com.github.mcreeper12731.game.models.Timeline;
import com.github.mcreeper12731.game.pieces.Piece;

import java.util.*;

public class MoveGenerator {

    public static Queue<Turn> generateAllTurns(Multiverse multiverse) {

        Queue<Turn> allTurns = new PriorityQueue<>();

        if (multiverse.isGameOver()) return allTurns;

        Map<Double, Queue<Move>> allMoves = generateAllMoves(multiverse);

        // System.out.println(allMoves);

        // System.out.printf("Generating turns from %d timelines and %d moves%n", allMoves.size(), currentMoves);
        generateAllTurns(
                multiverse,
                allMoves,
                new HashSet<>(),
                new ArrayList<>(),
                allTurns
        );

        return allTurns;
    }

    private static Map<Double, Queue<Move>> generateAllMoves(Multiverse multiverse) {

        Map<Double, Queue<Move>> allMoves = new HashMap<>();

        for (double timelineId : multiverse.getActiveTimelineIndices()) {
            Timeline timeline = multiverse.getTimeline(timelineId);
            // if (timeline.getLastTimeCoordinate() > multiverse.getPresentTime()) continue;

            Board board = timeline.getLastBoard();
            Set<Piece> pieces = board.getPieces();
            for (Piece piece : pieces) {

                if (piece.color() == multiverse.getPlayerTurn()) {
                    List<Move> moves = piece.getAvailableMoves(multiverse);
                    allMoves.computeIfAbsent(timeline.getId(), k -> new PriorityQueue<>()).addAll(moves);
                }
            }
        }
        return allMoves;
    }

    private static void generateAllTurns(
            Multiverse multiverse,
            Map<Double, Queue<Move>> remainingMoves,
            Set<Double> usedTimelines,
            List<Move> currentTurn,
            Queue<Turn> allTurns
    ) {

        if (multiverse.isCurrentTurnFinalizable() && !currentTurn.isEmpty()) {
            allTurns.add(new Turn(currentTurn));
            return;
        }

        for (double timeline : remainingMoves.keySet()) {

            if (usedTimelines.contains(timeline)) continue;

            for (Move move : remainingMoves.get(timeline)) {

                if (usedTimelines.contains(move.toTimeline())) continue;

                multiverse.applyMove(move);
                currentTurn.add(move);

                usedTimelines.add(timeline);
                if (move.fromTimeline() != move.toTimeline()) usedTimelines.add(move.toTimeline());

                generateAllTurns(multiverse, remainingMoves, usedTimelines, currentTurn, allTurns);

                multiverse.undoMoveFromCurrentTurn();
                currentTurn.remove(currentTurn.size() - 1);
                usedTimelines.remove(timeline);
                if (move.fromTimeline() != move.toTimeline()) usedTimelines.remove(move.toTimeline());
            }
        }
    }
}
