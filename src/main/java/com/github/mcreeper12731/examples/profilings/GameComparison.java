package com.github.mcreeper12731.examples.profilings;

import com.github.mcreeper12731.bitgame.BitGame;
import com.github.mcreeper12731.engine.evaluators.BitNegaMaxEvaluator;
import com.github.mcreeper12731.engine.evaluators.NegaMaxEvaluator;
import com.github.mcreeper12731.game.Game;
import com.github.mcreeper12731.game.presets.Preset;
import com.github.mcreeper12731.utility.Log;

import java.util.ArrayList;
import java.util.List;

public class GameComparison {

    public static void main(String[] args) {

        Game game = Preset.STANDARD.getGame();
        List<Long> profilings = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            long start = System.nanoTime();
            new NegaMaxEvaluator().findBestTurn(game);
            profilings.add((System.nanoTime() - start) / 1_000_000);
        }
        Log.print("ComparisonExample", "Game", profilings);

        BitGame bitGame = new BitGame(game);
        profilings = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            long start = System.nanoTime();
            new BitNegaMaxEvaluator().findBestTurn(bitGame);
            profilings.add((System.nanoTime() - start) / 1_000_000);
        }
        Log.print("ComparisonExample", "BitGame", profilings);
    }
}
