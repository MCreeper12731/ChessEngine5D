package com.github.mcreeper12731.engine.finders;

import com.github.mcreeper12731.MainApplication;
import com.github.mcreeper12731.engine.config.NegamaxStrategyConfig;
import com.github.mcreeper12731.engine.evaluators.EvaluatorImpl;
import com.github.mcreeper12731.game.Game;
import com.github.mcreeper12731.game.models.Move;
import com.github.mcreeper12731.game.models.scored.ScoredTurn;
import com.github.mcreeper12731.game.presets.Preset;
import com.github.mcreeper12731.utility.Log;
import org.junit.jupiter.api.Test;

import java.util.List;

class NegaMaxStrategyTest {

    @Test
    public void puzzleRook2() {

        Game game = Preset.PUZZLE_ROOK_2.getGame();

        NegamaxStrategyConfig config = new NegamaxStrategyConfig(
                3,
                Integer.MAX_VALUE,
                5
        );

        NegaMaxStrategy strategy = new NegaMaxStrategy(
                config,
                new EvaluatorImpl()
        );

        ScoredTurn result = strategy.findBestTurn(game);

        Log.print("Test", result);
        Log.debug("Test", "Max timelines", strategy.maxTimelinesReached);
        game.applyMovesAndFinalizeTurn(result.moves());
        MainApplication.launchWithGame(game);
    }

    @Test
    public void puzzleKnight6() {

        Game game = Preset.PUZZLE_KNIGHT_6.getGame();

        game.applyMovesAndFinalizeTurn(List.of(
                new Move.Builder(game)
                        .withFrom(0, 0, 0, 0)
                        .withTo(0, 0, 0, 1)
                        .build()
        ));

        game.applyMovesAndFinalizeTurn(List.of(
                new Move.Builder(game)
                        .withFrom(0, 1, 4, 4)
                        .withTo(0, 1, 3, 3)
                        .build()
        ));

        /*game.applyMovesAndFinalizeTurn(List.of(
                new Move.Builder(game)
                        .withFrom(0, 2, 2, 0)
                        .withTo(0, 2, 4, 1)
                        .build()
        ));

        /*game.applyMovesAndFinalizeTurn(List.of(
                new Move.Builder(game)
                        .withFrom(0, 3, 3, 3)
                        .withTo(0, 3, 3, 4)
                        .build()
        ));*/

        NegamaxStrategyConfig config = new NegamaxStrategyConfig(
                5,
                100_000,
                7
        );

        NegaMaxStrategy strategy = new NegaMaxStrategy(
                config,
                new EvaluatorImpl()
        );

        ScoredTurn result = strategy.findBestTurn(game);

        Log.print("Test", result);
        Log.debug("Test", "Max timelines", strategy.maxTimelinesReached);
        game.applyMovesAndFinalizeTurn(result.moves());
        MainApplication.launchWithGame(game);
    }

    @Test
    public void puzzleOpeningTrap2() {

        Game game = Preset.PUZZLE_OPENING_TRAP_2.getGame();

        NegamaxStrategyConfig config = new NegamaxStrategyConfig(
                3,
                1_000_000,
                7
        );

        NegaMaxStrategy strategy = new NegaMaxStrategy(
                config,
                new EvaluatorImpl()
        );

        ScoredTurn result = strategy.findBestTurn(game);

        Log.print("Test", result);
        game.applyMovesAndFinalizeTurn(result.moves());
        MainApplication.launchWithGame(game);
    }

}