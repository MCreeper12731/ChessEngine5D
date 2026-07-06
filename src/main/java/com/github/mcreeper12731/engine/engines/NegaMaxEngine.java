package com.github.mcreeper12731.engine.engines;

import com.github.mcreeper12731.engine.finders.NegaMaxStrategy;
import com.github.mcreeper12731.game.Game;
import com.github.mcreeper12731.game.models.Move;

import java.util.List;

public class NegaMaxEngine implements Engine {

    private final NegaMaxStrategy strategy;

    public NegaMaxEngine() {
        this.strategy = new NegaMaxStrategy();
    }

    @Override
    public List<Move> nextTurn(Game game) {
        return this.strategy.findBestTurn(game).moves();
    }
}
