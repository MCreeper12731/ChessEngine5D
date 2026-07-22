package com.github.mcreeper12731.examples;

import com.github.mcreeper12731.bitapplication.HeadlessBitApplication;
import com.github.mcreeper12731.bitgame.BitGame;
import com.github.mcreeper12731.game.presets.Preset;

public class HeadlessApplicationExample {

    public static void main(String[] args) {

        BitGame game = Preset.CHECKMATE_PRACTICE_QUEEN.getBitGame();
        HeadlessBitApplication application = new HeadlessBitApplication(game, "engine_negamax", "engine_negamax");

        application.run();

    }
}
