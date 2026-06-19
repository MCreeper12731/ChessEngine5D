package com.github.mcreeper12731.game.pieces;

import com.github.mcreeper12731.game.models.Point4D;

import java.util.List;
import java.util.stream.Stream;

public class MoveDirections {
    public static final List<Point4D> DIRECTIONS_1_DIM = List.of(
            // y
            new Point4D(0, 0, 0, 1),
            new Point4D(0, 0, 0, -1),

            // x
            new Point4D(0, 0, 1, 0),
            new Point4D(0, 0, -1, 0),

            // l
            new Point4D(1, 0, 0, 0),
            new Point4D(-1, 0, 0, 0),

            // t
            new Point4D(0, 2, 0, 0),
            new Point4D(0, -2, 0, 0)
    );

    public static final List<Point4D> DIRECTIONS_2_DIM = List.of(
            // x + y
            new Point4D(0, 0, 1, 1),
            new Point4D(0, 0, -1, 1),
            new Point4D(0, 0, 1, -1),
            new Point4D(0, 0, -1, -1),

            // l + x
            new Point4D(1, 0, 1, 0),
            new Point4D(-1, 0, 1, 0),
            new Point4D(1, 0, -1, 0),
            new Point4D(-1, 0, -1, 0),

            // l + y
            new Point4D(1, 0, 0, 1),
            new Point4D(-1, 0, 0, 1),
            new Point4D(1, 0, 0, -1),
            new Point4D(-1, 0, 0, -1),

            // l + t
            new Point4D(1, 2, 0, 0),
            new Point4D(-1, 2, 0, 0),
            new Point4D(1, -2, 0, 0),
            new Point4D(-1, -2, 0, 0),

            // t + x
            new Point4D(0, 2, 1, 0),
            new Point4D(0, -2, 1, 0),
            new Point4D(0, 2, -1, 0),
            new Point4D(0, -2, -1, 0),

            // t + y
            new Point4D(0, 2, 0, 1),
            new Point4D(0, -2, 0, 1),
            new Point4D(0, 2, 0, -1),
            new Point4D(0, -2, 0, -1)
    );

    public static final List<Point4D> DIRECTIONS_3_DIM = List.of(
            // l + x + y
            new Point4D(1, 0, 1, 1),
            new Point4D(-1, 0, 1, 1),
            new Point4D(1, 0, -1, 1),
            new Point4D(-1, 0, -1, 1),
            new Point4D(1, 0, 1, -1),
            new Point4D(-1, 0, 1, -1),
            new Point4D(1, 0, -1, -1),
            new Point4D(-1, 0, -1, -1),

            // l + t + x
            new Point4D(1, 2, 1, 0),
            new Point4D(-1, 2, 1, 0),
            new Point4D(1, -2, 1, 0),
            new Point4D(-1, -2, 1, 0),
            new Point4D(1, 2, -1, 0),
            new Point4D(-1, 2, -1, 0),
            new Point4D(1, -2, -1, 0),
            new Point4D(-1, -2, -1, 0),

            // l + t + y
            new Point4D(1, 2, 0, 1),
            new Point4D(-1, 2, 0, 1),
            new Point4D(1, -2, 0, 1),
            new Point4D(-1, -2, 0, 1),
            new Point4D(1, 2, 0, -1),
            new Point4D(-1, 2, 0, -1),
            new Point4D(1, -2, 0, -1),
            new Point4D(-1, -2, 0, -1),

            // t + x + y
            new Point4D(0, 2, 1, 1),
            new Point4D(0, -2, 1, 1),
            new Point4D(0, 2, -1, 1),
            new Point4D(0, -2, -1, 1),
            new Point4D(0, 2, 1, -1),
            new Point4D(0, -2, 1, -1),
            new Point4D(0, 2, -1, -1),
            new Point4D(0, -2, -1, -1)
    );

    public static final List<Point4D> DIRECTIONS_4_DIM = List.of(
            new Point4D(1, 2, 1, 1),
            new Point4D(-1, 2, 1, 1),
            new Point4D(1, -2, 1, 1),
            new Point4D(-1, -2, 1, 1),

            new Point4D(1, 2, -1, 1),
            new Point4D(-1, 2, -1, 1),
            new Point4D(1, -2, -1, 1),
            new Point4D(-1, -2, -1, 1),

            new Point4D(1, 2, 1, -1),
            new Point4D(-1, 2, 1, -1),
            new Point4D(1, -2, 1, -1),
            new Point4D(-1, -2, 1, -1),

            new Point4D(1, 2, -1, -1),
            new Point4D(-1, 2, -1, -1),
            new Point4D(1, -2, -1, -1),
            new Point4D(-1, -2, -1, -1)
    );

    public static final List<Point4D> DIRECTIONS_12_DIM = Stream.of(
            MoveDirections.DIRECTIONS_1_DIM,
            MoveDirections.DIRECTIONS_2_DIM
    ).flatMap(List::stream).toList();

    public static final List<Point4D> DIRECTIONS_1234_DIM = Stream.of(
            MoveDirections.DIRECTIONS_1_DIM,
            MoveDirections.DIRECTIONS_2_DIM,
            MoveDirections.DIRECTIONS_3_DIM,
            MoveDirections.DIRECTIONS_4_DIM
    ).flatMap(List::stream).toList();

    public static final List<Point4D> DIRECTIONS_KNIGHT = List.of(
            // x + y
            // x=2, y=1
            new Point4D(0, 0, 2, 1),
            new Point4D(0, 0, -2, 1),
            new Point4D(0, 0, 2, -1),
            new Point4D(0, 0, -2, -1),
            // x=1, y=2
            new Point4D(0, 0, 1, 2),
            new Point4D(0, 0, -1, 2),
            new Point4D(0, 0, 1, -2),
            new Point4D(0, 0, -1, -2),

            // l + x
            // l=2, x=1
            new Point4D(2, 0, 1, 0),
            new Point4D(-2, 0, 1, 0),
            new Point4D(2, 0, -1, 0),
            new Point4D(-2, 0, -1, 0),
            // l=1, x=2
            new Point4D(1, 0, 2, 0),
            new Point4D(-1, 0, 2, 0),
            new Point4D(1, 0, -2, 0),
            new Point4D(-1, 0, -2, 0),

            // l + y
            // l=2, y=1
            new Point4D(2, 0, 0, 1),
            new Point4D(-2, 0, 0, 1),
            new Point4D(2, 0, 0, -1),
            new Point4D(-2, 0, 0, -1),
            // l=1, y=2
            new Point4D(1, 0, 0, 2),
            new Point4D(-1, 0, 0, 2),
            new Point4D(1, 0, 0, -2),
            new Point4D(-1, 0, 0, -2),

            // l + t
            // l=2, t=1
            new Point4D(2, 2, 0, 0),
            new Point4D(-2, 2, 0, 0),
            new Point4D(2, -2, 0, 0),
            new Point4D(-2, -2, 0, 0),
            // l=1, t=2
            new Point4D(1, 4, 0, 0),
            new Point4D(-1, 4, 0, 0),
            new Point4D(1, -4, 0, 0),
            new Point4D(-1, -4, 0, 0),

            // t + x
            // t=2, x=1
            new Point4D(0, 4, 1, 0),
            new Point4D(0, -4, 1, 0),
            new Point4D(0, 4, -1, 0),
            new Point4D(0, -4, -1, 0),
            // t=1, x=2
            new Point4D(0, 2, 2, 0),
            new Point4D(0, -2, 2, 0),
            new Point4D(0, 2, -2, 0),
            new Point4D(0, -2, -2, 0),

            // t + y
            // t=2, y=1
            new Point4D(0, 4, 0, 1),
            new Point4D(0, -4, 0, 1),
            new Point4D(0, 4, 0, -1),
            new Point4D(0, -4, 0, -1),
            // t=1, y=2
            new Point4D(0, 2, 0, 2),
            new Point4D(0, -2, 0, 2),
            new Point4D(0, 2, 0, -2),
            new Point4D(0, -2, 0, -2)

    );

    private static List<Point4D> reverseForwardness(List<Point4D> points) {
        return points.stream().map(point4D -> new Point4D(-point4D.l(), point4D.t(), point4D.x(), -point4D.y())).toList();
    }

    public static final List<Point4D> DIRECTIONS_WHITE_PAWN_MOVES = List.of(
            new Point4D(0, 0, 0, 1),
            new Point4D(-1, 0, 0, 0)
    );

    public static final List<Point4D> DIRECTIONS_WHITE_PAWN_CAPTURES = List.of(
            new Point4D(0, 0, -1, 1),
            new Point4D(0, 0, 1, 1),
            new Point4D(-1, -2, 0, 0),
            new Point4D(-1, 2, 0, 0)
    );

    public static final List<Point4D> DIRECTIONS_BLACK_PAWN_MOVES = reverseForwardness(DIRECTIONS_WHITE_PAWN_MOVES);
    public static final List<Point4D> DIRECTIONS_BLACK_PAWN_CAPTURES = reverseForwardness(DIRECTIONS_WHITE_PAWN_CAPTURES);

    public static final List<Point4D> DIRECTIONS_WHITE_BRAWN_CAPTURES =
            DIRECTIONS_2_DIM.stream().filter(point4D -> point4D.l() < 0 && point4D.y() >= 0 || point4D.l() <= 0 && point4D.y() > 0).toList();

    public static final List<Point4D> DIRECTIONS_BLACK_BRAWN_CAPTURES = reverseForwardness(DIRECTIONS_WHITE_BRAWN_CAPTURES);
}