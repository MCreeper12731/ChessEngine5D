package com.github.mcreeper12731;

import com.github.mcreeper12731.engine.finders.NegaMaxStrategy;
import com.github.mcreeper12731.game.Game;
import com.github.mcreeper12731.game.models.scored.ScoredTurn;
import com.github.mcreeper12731.game.presets.Preset;
import com.github.mcreeper12731.utility.Log;

import java.util.Arrays;

public class Main {
    public static void main(String[] args) {

        int tries = 10;
        Long[] profiling = new Long[tries];

        for (int i = 0; i < tries; i++) {
            Game game = Preset.STANDARD.getGame();

            NegaMaxStrategy strategy = new NegaMaxStrategy();

            long start = System.nanoTime();
            ScoredTurn result = strategy.findBestTurn(game);
            profiling[i] = (System.nanoTime() - start) / 1_000_000 ;

            Log.print("Main", result);
            game.applyMovesAndFinalizeTurn(result.moves());
            //MainApplication.launchWithGame(game);



        }

        Log.print("Main", profiling);
    }
}