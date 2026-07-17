package com.github.mcreeper12731.engine.engines;

import com.github.mcreeper12731.engine.evaluators.NegaMaxEvaluator;
import com.github.mcreeper12731.game.Game;
import com.github.mcreeper12731.game.models.Move;

import java.util.List;

public class NegaMaxEngine implements Engine {

    private final NegaMaxEvaluator negaMaxEvaluator;

    public NegaMaxEngine() {
        this.negaMaxEvaluator = new NegaMaxEvaluator();
    }

    @Override
    public List<Move> nextTurn(Game game) {
        return this.negaMaxEvaluator.findBestTurn(game).moves();
    }
}
