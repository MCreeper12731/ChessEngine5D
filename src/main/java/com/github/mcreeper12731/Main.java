package com.github.mcreeper12731;

import com.github.mcreeper12731.engine.config.AlphaBetaStrategyConfig;
import com.github.mcreeper12731.engine.finders.AlphaBetaStrategy;
import com.github.mcreeper12731.game.models.scored.ScoredTurn;
import com.github.mcreeper12731.engine.evaluators.TestEvaluator;
import com.github.mcreeper12731.game.logic.Game;
import com.github.mcreeper12731.game.models.Move;
import com.github.mcreeper12731.game.presets.Preset;
import com.github.mcreeper12731.utility.Log;

import java.util.List;

public class Main {
    public static void main(String[] args) {

        Game game = Preset.PUZZLE_KNIGHT_6.getGame();

        game.applyMovesAndFinalizeTurn(List.of(
                new Move.Builder(game.getMultiverse())
                        .withFrom(0, 0, 0, 0)
                        .withTo(0, 0, 0, 1)
                        .build()
        ));

        game.applyMovesAndFinalizeTurn(List.of(
                new Move.Builder(game.getMultiverse())
                        .withFrom(0, 1, 4, 4)
                        .withTo(0, 1, 3, 3)
                        .build()
        ));

        game.applyMovesAndFinalizeTurn(List.of(
                new Move.Builder(game.getMultiverse())
                        .withFrom(0, 2, 2, 0)
                        .withTo(0, 2, 4, 1)
                        .build()
        ));

        game.applyMovesAndFinalizeTurn(List.of(
                new Move.Builder(game.getMultiverse())
                        .withFrom(0, 3, 3, 3)
                        .withTo(0, 3, 3, 4)
                        .build()
        ));

        //MainApplication.launchWithGame(game);

        game.clearTurnHistory();

        AlphaBetaStrategyConfig config = new AlphaBetaStrategyConfig(
                5,
                Integer.MAX_VALUE,
                5
        );

        AlphaBetaStrategy strategy = new AlphaBetaStrategy(
                config,
                new TestEvaluator()
        );

        ScoredTurn result = strategy.findBestTurn(game);

        Log.print("Main", result);
        MainApplication.launchWithGame(game);
    }
}