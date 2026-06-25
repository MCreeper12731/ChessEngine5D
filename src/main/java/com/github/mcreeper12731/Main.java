package com.github.mcreeper12731;

import com.github.mcreeper12731.engine.config.AlphaBetaStrategyConfig;
import com.github.mcreeper12731.engine.finders.AlphaBetaStrategy;
import com.github.mcreeper12731.game.models.Move;
import com.github.mcreeper12731.game.models.scored.ScoredTurn;
import com.github.mcreeper12731.engine.evaluators.EvaluatorImpl;
import com.github.mcreeper12731.game.logic.Game;
import com.github.mcreeper12731.game.presets.Preset;
import com.github.mcreeper12731.utility.Log;

import java.util.List;

public class Main {
    public static void main(String[] args) {

        Game game = Preset.PUZZLE_ROOK_4.getGame();

        game.clearTurnHistory();

        AlphaBetaStrategyConfig config = new AlphaBetaStrategyConfig(
                3,
                Integer.MAX_VALUE,
                11
        );

        AlphaBetaStrategy strategy = new AlphaBetaStrategy(
                config,
                new EvaluatorImpl()
        );

        ScoredTurn result = strategy.findBestTurn(game);

        Log.print("Main", result);
        game.applyMovesAndFinalizeTurn(result.moves());
        //MainApplication.launchWithGame(game);
    }
}