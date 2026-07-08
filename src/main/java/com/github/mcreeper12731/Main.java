package com.github.mcreeper12731;

import com.github.mcreeper12731.bitgame.BitGame;
import com.github.mcreeper12731.bitgame.movegeneration.BitMoveGenerator;
import com.github.mcreeper12731.engine.finders.BitNegaMaxStrategy;
import com.github.mcreeper12731.engine.finders.NegaMaxStrategy;
import com.github.mcreeper12731.game.Game;
import com.github.mcreeper12731.game.models.Move;
import com.github.mcreeper12731.game.movegeneration.MoveGenerator;
import com.github.mcreeper12731.game.presets.Preset;
import com.github.mcreeper12731.utility.Iterators;
import com.github.mcreeper12731.utility.Log;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {

        Game game = Preset.STANDARD.getGame();
        BitGame bitGame = new BitGame(game);

        List<Double> timingsRegular = new ArrayList<>();
        List<Double> timingsBit = new ArrayList<>();

        var negaMax = new NegaMaxStrategy();
        var negaMaxBit = new BitNegaMaxStrategy();

        for (int i = 0; i < 3; i++) {

            double startTime = System.nanoTime();
            negaMax.findBestTurn(game);
            timingsRegular.add(System.nanoTime() - startTime);

            double startTimeBit = System.nanoTime();
            negaMaxBit.findBestTurn(bitGame);
            timingsBit.add(System.nanoTime() - startTimeBit);
        }

        Log.print("Main", timingsRegular);
        Log.print("Main", timingsBit);
    }
}