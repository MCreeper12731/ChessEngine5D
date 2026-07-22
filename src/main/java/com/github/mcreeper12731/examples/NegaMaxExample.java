package com.github.mcreeper12731.examples;

import com.github.mcreeper12731.bitgame.BitGame;
import com.github.mcreeper12731.engine.evaluators.BitNegaMaxEvaluator;
import com.github.mcreeper12731.engine.evaluators.NegaMaxEvaluator;
import com.github.mcreeper12731.game.presets.Preset;
import com.github.mcreeper12731.utility.Config;
import com.github.mcreeper12731.utility.Log;

public class NegaMaxExample {

    public static void main(String[] args) {

        BitGame game = new BitGame(Preset.STANDARD.getGame());

        BitNegaMaxEvaluator evaluator = new BitNegaMaxEvaluator(
                new Config()
                        .with("max_nodes", 1_000_000)
                        .with("max_depth", 5)
                        .with("evaluator", "quiescence")
                        .with("max_quiescence_depth", 4)
        );

        Log.print("NegaMaxExample", evaluator.findBestTurn(game));
        Log.debug("NegaMaxExample", game.getPresentTime());
    }
}
