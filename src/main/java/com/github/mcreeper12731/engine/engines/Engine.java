package com.github.mcreeper12731.engine.engines;

import com.github.mcreeper12731.game.Game;
import com.github.mcreeper12731.game.models.Move;

import java.util.List;

public interface Engine {

    List<Move> nextTurn(Game game);
}
