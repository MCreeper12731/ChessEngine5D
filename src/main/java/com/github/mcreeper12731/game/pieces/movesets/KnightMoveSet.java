package com.github.mcreeper12731.game.pieces.movesets;

import com.github.mcreeper12731.game.models.Multiverse;
import com.github.mcreeper12731.game.models.Point4D;
import com.github.mcreeper12731.game.moves.Move;
import com.github.mcreeper12731.game.moves.MoveValidator;
import com.github.mcreeper12731.game.pieces.Piece;

import java.util.ArrayList;
import java.util.List;

public class KnightMoveSet implements MoveSet {

    private MoveValidator moveValidator = null;

    @Override
    public List<Move> generateMoves(Multiverse multiverse, Piece piece) {

        if (moveValidator == null)
            moveValidator = new MoveValidator(multiverse);

        List<Point4D> directions = MoveSet.getDirections(List.of(2, 1));
        List<Move> moves = new ArrayList<>();

        for (Point4D direction : directions) {

            Point4D toLocation = piece.location().add(direction);
            if (!moveValidator.isToCorrect(piece.color(), toLocation)) continue;

            Move move = new Move(piece, toLocation);
            moves.add(move);
        }

        return moves;
    }
}
