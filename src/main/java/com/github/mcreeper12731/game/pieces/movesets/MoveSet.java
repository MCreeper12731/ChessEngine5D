package com.github.mcreeper12731.game.pieces.movesets;

import com.github.mcreeper12731.game.models.Multiverse;
import com.github.mcreeper12731.game.models.Move;
import com.github.mcreeper12731.game.pieces.Piece;

import java.util.Iterator;

public interface MoveSet {

    Iterator<Move> iterator(Multiverse multiverse, Piece piece);
}
