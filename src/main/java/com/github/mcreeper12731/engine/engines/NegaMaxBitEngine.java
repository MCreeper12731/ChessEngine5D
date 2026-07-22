package com.github.mcreeper12731.engine.engines;

import com.github.mcreeper12731.bitgame.BitGame;
import com.github.mcreeper12731.engine.evaluators.BitNegaMaxEvaluator;
import com.github.mcreeper12731.engine.evaluators.NegaMaxEvaluator;
import com.github.mcreeper12731.game.Game;
import com.github.mcreeper12731.game.models.Move;

import java.util.List;

public class NegaMaxBitEngine implements BitEngine {

    private final BitNegaMaxEvaluator negaMaxEvaluator;

    public NegaMaxBitEngine() {
        this.negaMaxEvaluator = new BitNegaMaxEvaluator();
    }

    @Override
    public List<Move> nextTurn(BitGame game) {
        return this.negaMaxEvaluator.findBestTurn(game).moves();
    }

}
