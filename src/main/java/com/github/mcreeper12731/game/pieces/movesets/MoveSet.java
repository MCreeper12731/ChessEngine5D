package com.github.mcreeper12731.game.pieces.movesets;

import com.github.mcreeper12731.game.models.Multiverse;
import com.github.mcreeper12731.game.models.Point4D;
import com.github.mcreeper12731.game.models.Timeline;
import com.github.mcreeper12731.game.moves.Move;
import com.github.mcreeper12731.game.pieces.Piece;

import java.util.*;

public interface MoveSet {

    // TODO: cleanup
    static List<Point4D> getDirections(List<Integer> values) {
        values = new ArrayList<>(values);
        List<Point4D> result = new ArrayList<>();

        int n = values.size();
        if (n == 0 || n > 4) throw new IllegalArgumentException("Input must have 1 to 4 elements");

        List<List<Integer>> permutations = new ArrayList<>();
        permute(values, 0, permutations);

        List<int[]> positionCombos = new ArrayList<>();
        generatePositionCombinations(4, n, 0, new int[n], positionCombos);

        for (List<Integer> perm : permutations) {
            for (int[] positions : positionCombos) {
                int signVariants = 1 << n;

                for (int s = 0; s < signVariants; s++) {
                    int[] coords = new int[4];

                    for (int i = 0; i < n; i++) {
                        int value = perm.get(i);
                        if (((s >> i) & 1) == 1) value = -value;
                        coords[positions[i]] = value;
                    }

                    result.add(new Point4D(coords[0], coords[1] * 2, coords[2], coords[3]));
                }
            }
        }

        return result;
    }

    private static void permute(List<Integer> list, int start, List<List<Integer>> result) {
        if (start == list.size()) {
            result.add(new ArrayList<>(list));
            return;
        }
        for (int i = start; i < list.size(); i++) {
            Collections.swap(list, i, start);
            permute(list, start + 1, result);
            Collections.swap(list, i, start);
        }
    }

    private static void generatePositionCombinations(int total, int choose, int start, int[] current, List<int[]> result) {
        if (choose == 0) {
            result.add(Arrays.copyOf(current, current.length));
            return;
        }
        for (int i = start; i <= total - choose; i++) {
            current[current.length - choose] = i;
            generatePositionCombinations(total, choose - 1, i + 1, current, result);
        }
    }

    private static void permute(int axisCount, int currentAxisCount, int currentCoordinate, Point4D currentDirection, List<Point4D> directions) {

        if (currentAxisCount == axisCount) {
            directions.add(currentDirection);
            return;
        }
        if (currentCoordinate < 0) return;

        int coordinateDelta = 1;
        if (currentCoordinate == 1) coordinateDelta = 2;

        permute(axisCount, currentAxisCount + 1, currentCoordinate - 1, currentDirection.addCoordinate(currentCoordinate, coordinateDelta), directions);
        permute(axisCount, currentAxisCount + 1, currentCoordinate - 1, currentDirection.addCoordinate(currentCoordinate, -coordinateDelta), directions);
        permute(axisCount, currentAxisCount, currentCoordinate - 1, currentDirection, directions);

    }

    static List<Point4D> getDirections(int minAxisCount, int maxAxisCount) {

        List<Point4D> directions = new ArrayList<>();
        for (int axisCount = minAxisCount; axisCount <= maxAxisCount; axisCount++) {
            permute(
                    axisCount,
                    0,
                    3,
                    new Point4D(0, 0,0, 0),
                    directions
            );
        }
        return directions;
    }

    static List<Point4D> getDirections(int preciseAxisCount) {
        return getDirections(preciseAxisCount, preciseAxisCount);
    }

    static List<Move> generateSlidingMoves(Multiverse multiverse, Piece piece, List<Point4D> directions, int maxSteps) {

        List<Move> moves = new ArrayList<>();

        for (Point4D direction : directions) {
            Point4D toLocation = piece.location();

            for (int i = 0; i < maxSteps; i++) {
                toLocation = toLocation.add(direction);

                Timeline timeline = multiverse.getTimeline(toLocation.timeline());

                if (timeline == null) break;
                if (toLocation.time() < timeline.getFirstTimeCoordinate()) break;
                if (toLocation.time() > timeline.getLastTimeCoordinate()) break;
                if (toLocation.x() < 0 || toLocation.x() >= multiverse.getBoardSize()) break;
                if (toLocation.y() < 0 || toLocation.y() >= multiverse.getBoardSize()) break;

                Piece pieceAtLocation = multiverse
                        .getTimeline(toLocation.timeline())
                        .getBoardByTime(toLocation.time())
                        .getPiece(toLocation.x(), toLocation.y());

                if (pieceAtLocation != null) {
                    if (pieceAtLocation.color() != piece.color()) {
                        moves.add(new Move(piece, toLocation));
                    }
                    break;
                }

                moves.add(new Move(piece, toLocation));
            }
        }

        return moves;
    }

    List<Move> generateMoves(Multiverse multiverse, Piece piece);

}
