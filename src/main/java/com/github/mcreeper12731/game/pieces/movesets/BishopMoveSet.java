package com.github.mcreeper12731.game.pieces.movesets;

import com.github.mcreeper12731.game.models.Multiverse;
import com.github.mcreeper12731.game.models.Point4D;
import com.github.mcreeper12731.game.moves.Move;
import com.github.mcreeper12731.game.pieces.Piece;

import java.util.List;

public class BishopMoveSet implements MoveSet {

    private final List<Point4D> directions = MoveSet.getDirections(2);

    @Override
    public List<Move> generateMoves(Multiverse multiverse, Piece piece) {

        return MoveSet.generateSlidingMoves(multiverse, piece, directions, Integer.MAX_VALUE);
    }
}
