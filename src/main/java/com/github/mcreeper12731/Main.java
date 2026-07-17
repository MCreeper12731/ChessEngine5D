package com.github.mcreeper12731;

import com.github.mcreeper12731.bitgame.BitGame;
import com.github.mcreeper12731.engine.evaluators.BitNegaMaxEvaluator;
import com.github.mcreeper12731.game.Game;
import com.github.mcreeper12731.game.presets.Preset;
import com.github.mcreeper12731.utility.Config;

public class Main {
    public static void main(String[] args) {

        System.out.println(Config.fromFile("negamax"));

        Game game = Preset.STANDARD.getGame();
        BitGame bitGame = new BitGame(game);

        BitNegaMaxEvaluator negaMaxStrategy = new BitNegaMaxEvaluator();
        negaMaxStrategy.findBestTurn(bitGame);
    }
}