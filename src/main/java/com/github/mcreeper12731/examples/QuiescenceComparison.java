package com.github.mcreeper12731.examples;

import com.github.mcreeper12731.bitgame.BitGame;
import com.github.mcreeper12731.engine.evaluators.BitNegaMaxEvaluator;
import com.github.mcreeper12731.game.presets.Preset;
import com.github.mcreeper12731.utility.Config;
import com.github.mcreeper12731.utility.Log;

public class QuiescenceComparison {

    public static void main(String[] args) {

        BitGame game = new BitGame(Preset.JUST_QUEENS.getGame());

        BitNegaMaxEvaluator negaMaxStatic = new BitNegaMaxEvaluator(
                new Config()
                        .with("max_nodes", 1_000_000)
                        .with("max_depth", 5)
                        .with("evaluator", "static")
        );
        negaMaxStatic.findBestTurn(game);
        Log.debug("MoveOrderingComparison", "Static evaluation:", negaMaxStatic.nodesSearched);

        for (int quiescenceDepth = 0; quiescenceDepth < 10; quiescenceDepth++) {
            BitNegaMaxEvaluator negaMaxQuiescence = new BitNegaMaxEvaluator(
                    new Config()
                            .with("max_nodes", 1_000_000)
                            .with("max_depth", 5)
                            .with("evaluator", "quiescence")
                            .with("max_quiescence_depth", quiescenceDepth)
            );

            negaMaxQuiescence.findBestTurn(game);
            Log.debug("MoveOrderingComparison", String.format("Quiescence %d evaluation:", quiescenceDepth), negaMaxQuiescence.nodesSearched);
        }
    }
}
