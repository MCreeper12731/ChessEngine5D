package com.github.mcreeper12731.game.moves;

import com.github.mcreeper12731.game.models.Multiverse;
import com.github.mcreeper12731.game.models.Point4D;
import com.github.mcreeper12731.game.models.Timeline;
import com.github.mcreeper12731.game.models.Color;
import com.github.mcreeper12731.game.pieces.Piece;

import java.util.List;

public class MoveValidator {

    private final Multiverse multiverse;

    public MoveValidator(Multiverse multiverse) {
        this.multiverse = multiverse;
    }

    public boolean isValid(Move move) {

        return isFromCorrect(move.from()) &&
                isPieceColorCorrect(move.pieceColor(), move.from()) &&
                isToCorrect(move.pieceColor(), move.to()) &&
                isLegalMove(move);
    }

    public boolean isFromCorrect(Point4D from) {

        // Check if 'from' location exists and is last in its timeline
        Timeline timeline = this.multiverse.getTimeline(from.timeline());

        if (timeline == null) return false;
        if (from.time() != timeline.getLastTimeCoordinate()) return false;

        if (
                from.x() < 0 ||
                from.x() >= this.multiverse.getBoardSize() ||
                from.y() < 0 ||
                from.y() >= this.multiverse.getBoardSize()
        ) return false;

        return true;
    }

    public boolean isPieceColorCorrect(Color pieceColor, Point4D from) {

        Timeline timeline = this.multiverse.getTimeline(from.timeline());

        return pieceColor == timeline.getLastBoard().getPlayerTurn();

    }

    public boolean isToCorrect(Color pieceColor, Point4D to) {

        // Check if 'to' location exists and is not occupied by a piece of same color
        if (
                to.x() < 0 ||
                        to.x() >= this.multiverse.getBoardSize() ||
                        to.y() < 0 ||
                        to.y() >= this.multiverse.getBoardSize()
        ) return false;

        Timeline timeline = multiverse.getTimeline(to.timeline());

        if (timeline == null) return false;
        if (to.time() < timeline.getFirstTimeCoordinate() || to.time() > timeline.getLastTimeCoordinate()) return false;
        if (pieceColor != timeline.getBoardByTime(to.time()).getPlayerTurn()) return false;
        if (multiverse.getPiece(to).isPresent() && multiverse.getPiece(to).get().color() == pieceColor) return false;

        return true;
    }

    private boolean isLegalMove(Move move) {

        Piece piece = multiverse.getTimeline(move.fromTimeline()).getLastBoard().getPiece(move.fromX(), move.fromY());

        List<Move> legalMoves = piece.getAvailableMoves(multiverse);

        for (Move legalMove : legalMoves) {
            if (move.equals(legalMove)) return true;
        }

        return false;
    }





}
