package com.github.mcreeper12731.game.moves;

import com.github.mcreeper12731.game.models.Point4D;
import com.github.mcreeper12731.game.models.Color;
import com.github.mcreeper12731.game.pieces.Piece;
import com.github.mcreeper12731.game.pieces.PieceType;

import java.util.Objects;

public record Move(Color pieceColor, PieceType pieceType,
                   double fromTimeline, int fromTime, int fromX, int fromY,
                   double toTimeline, int toTime, int toX, int toY,
                   PieceType promotionResult
                   ) implements Comparable<Move> {

    public Move(Piece piece, Point4D to, PieceType promotionResult) {
        this(
                piece.color(), piece.type(),
                piece.location().timeline(), piece.location().time(), piece.location().x(), piece.location().y(),
                to.timeline(), to.time(), to.x(), to.y(),
                promotionResult
        );
    }

    public Move(Piece piece, Point4D to) {
        this(piece, to, null);
    }

    public Point4D from() {
        return new Point4D(fromTimeline, fromTime, fromX, fromY);
    }

    public Point4D to() {
        return new Point4D(toTimeline, toTime, toX, toY);
    }

    @Override
    public String toString() {
        return String.format("%s: %s > %s",
                pieceType.name + pieceColor.name().charAt(0),
                from(),
                to()
        );
    }

    @Override
    public boolean equals(Object other) {
        if (other == null || getClass() != other.getClass()) return false;
        Move move = (Move) other;
        return toX == move.toX && toY == move.toY && fromX == move.fromX && fromY == move.fromY && toTime == move.toTime && fromTime == move.fromTime && toTimeline == move.toTimeline && fromTimeline == move.fromTimeline && pieceColor == move.pieceColor && pieceType == move.pieceType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(pieceColor, pieceType, fromTimeline, fromTime, fromX, fromY, toTimeline, toTime, toX, toY);
    }

    @Override
    public int compareTo(Move other) {
        return penalty() - other.penalty();
    }

    public int penalty() {
        boolean sameTimeline = fromTimeline() == toTimeline();
        boolean sameTime = fromTime() == toTime();

        if (!sameTime && sameTimeline) return 1000; // Guarantees timeline splitting
        if (!sameTime || !sameTimeline) return 500; // May or may not split timelines
        return 0; // Same board move splits timelines
    }
}
