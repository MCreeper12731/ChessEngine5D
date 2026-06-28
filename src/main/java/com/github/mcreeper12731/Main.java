package com.github.mcreeper12731;

import com.github.mcreeper12731.engine.config.NegamaxStrategyConfig;
import com.github.mcreeper12731.engine.finders.NegaMaxStrategy;
import com.github.mcreeper12731.game.models.scored.ScoredTurn;
import com.github.mcreeper12731.engine.evaluators.EvaluatorImpl;
import com.github.mcreeper12731.game.Game;
import com.github.mcreeper12731.game.presets.Preset;
import com.github.mcreeper12731.utility.Log;

public class Main {
    public static void main(String[] args) {

        Game game = Preset.STANDARD.getGame();

        NegaMaxStrategy strategy = new NegaMaxStrategy(
                NegamaxStrategyConfig.fromConfig(),
                new EvaluatorImpl()
        );

        ScoredTurn result = strategy.findBestTurn(game);

        Log.print("Main", result);
        game.applyMovesAndFinalizeTurn(result.moves());
        //MainApplication.launchWithGame(game);
    }
}