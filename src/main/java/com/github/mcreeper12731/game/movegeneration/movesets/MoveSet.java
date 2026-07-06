package com.github.mcreeper12731.game.movegeneration.movesets;

import com.github.mcreeper12731.game.models.Multiverse;
import com.github.mcreeper12731.game.models.Move;
import com.github.mcreeper12731.game.models.Point4D;

import java.util.Iterator;

public interface MoveSet {

    Iterator<Move> iterator(Multiverse multiverse, Point4D pieceLocation);
}
