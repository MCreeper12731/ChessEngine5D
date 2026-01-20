package com.github.mcreeper12731.game.moves;

import com.github.mcreeper12731.game.models.Multiverse;
import com.github.mcreeper12731.game.models.Point4D;
import com.github.mcreeper12731.game.pieces.PieceType;

public class MoveFactory {

    private final Multiverse multiverse;

    public MoveFactory(Multiverse multiverse) {
        this.multiverse = multiverse;
    }

    public Move build(Point4D from, Point4D to) {
        return multiverse.getPiece(from)
                .map(piece -> switch (piece.type()) {
                    case PAWN -> new Move(
                            piece,
                            to,
                            to.y() == 0 || to.y() == multiverse.getBoardSize() - 1 ? PieceType.QUEEN : null
                    );
                    default -> new Move(piece, to);
                })
                .orElseThrow(() -> new RuntimeException("Cannot generate move, no piece found at " + from + "!"));
    }
}
