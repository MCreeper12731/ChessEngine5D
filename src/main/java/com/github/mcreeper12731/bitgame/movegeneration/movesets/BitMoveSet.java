package com.github.mcreeper12731.bitgame.movegeneration.movesets;

import com.github.mcreeper12731.bitgame.BitGame;
import com.github.mcreeper12731.bitgame.models.BitMultiverse;
import com.github.mcreeper12731.game.models.Move;
import com.github.mcreeper12731.game.models.Multiverse;
import com.github.mcreeper12731.game.models.Point4D;

import java.util.Iterator;

public interface BitMoveSet {

    Iterator<Move> iterator(BitGame game, Point4D pieceLocation);
}
