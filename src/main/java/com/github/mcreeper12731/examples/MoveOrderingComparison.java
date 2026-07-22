package com.github.mcreeper12731.examples;

import com.github.mcreeper12731.bitgame.BitGame;
import com.github.mcreeper12731.engine.evaluators.BitNegaMaxEvaluator;
import com.github.mcreeper12731.game.Game;
import com.github.mcreeper12731.game.presets.Preset;
import com.github.mcreeper12731.utility.Config;
import com.github.mcreeper12731.utility.Log;

public class MoveOrderingComparison {

    public static void main(String[] args) {

        BitGame game = new BitGame(Preset.STANDARD.getGame());

        BitNegaMaxEvaluator negaMaxNoMoveOrdering = new BitNegaMaxEvaluator(
                new Config()
                        .with("max_nodes", 9_999_999)
                        .with("max_depth", 5)
                        .with("move_ordering", "none")
        );

        BitNegaMaxEvaluator negaMaxWithMoveOrdering = new BitNegaMaxEvaluator(
                new Config()
                        .with("max_nodes", 9_999_999)
                        .with("max_depth", 5)
                        .with("move_ordering", "ordered")
        );

        negaMaxNoMoveOrdering.findBestTurn(game);
        negaMaxWithMoveOrdering.findBestTurn(game);

        Log.debug("MoveOrderingComparison", negaMaxNoMoveOrdering.nodesSearched);
        Log.debug("MoveOrderingComparison", negaMaxWithMoveOrdering.nodesSearched);
    }
}
