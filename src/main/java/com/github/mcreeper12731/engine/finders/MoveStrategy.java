package com.github.mcreeper12731.engine.finders;

import com.github.mcreeper12731.game.models.Move;

import java.util.List;

public interface MoveStrategy {

    List<Move> nextTurn();

    void opponentTurn(List<Move> turn);

}
