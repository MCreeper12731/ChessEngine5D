package com.github.mcreeper12731.examples;

import com.github.mcreeper12731.bitgame.BitGame;
import com.github.mcreeper12731.engine.finders.BitNegaMaxStrategy;
import com.github.mcreeper12731.engine.finders.NegaMaxStrategy;
import com.github.mcreeper12731.game.Game;
import com.github.mcreeper12731.game.presets.Preset;

public class ComparisonExample {

    public static void main(String[] args) {

        Game game = Preset.STANDARD.getGame();
        BitGame bitGame = new BitGame(game);

        new NegaMaxStrategy().findBestTurn(game);
        new NegaMaxStrategy().findBestTurn(game);
        new NegaMaxStrategy().findBestTurn(game);
        new NegaMaxStrategy().findBestTurn(game);
        new NegaMaxStrategy().findBestTurn(game);

        new BitNegaMaxStrategy().findBestTurn(bitGame);
        new BitNegaMaxStrategy().findBestTurn(bitGame);
        new BitNegaMaxStrategy().findBestTurn(bitGame);
        new BitNegaMaxStrategy().findBestTurn(bitGame);
        new BitNegaMaxStrategy().findBestTurn(bitGame);

    }
}
