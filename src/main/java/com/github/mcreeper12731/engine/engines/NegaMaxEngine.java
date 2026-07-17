package com.github.mcreeper12731.engine.engines;

import com.github.mcreeper12731.engine.evaluators.NegaMaxEvaluator;
import com.github.mcreeper12731.game.Game;
import com.github.mcreeper12731.game.models.Move;

import java.util.List;

public class NegaMaxEngine implements Engine {

    private final NegaMaxEvaluator strategy;

    public NegaMaxEngine() {
        this.strategy = new NegaMaxEvaluator();
    }

    @Override
    public List<Move> nextTurn(Game game) {
        return this.strategy.findBestTurn(game).moves();
    }
}
