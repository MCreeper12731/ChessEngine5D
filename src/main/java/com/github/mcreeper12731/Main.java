package com.github.mcreeper12731;

import com.github.mcreeper12731.engine.finders.NegaMaxStrategy;
import com.github.mcreeper12731.game.Game;
import com.github.mcreeper12731.game.bitmodels.BitGame;
import com.github.mcreeper12731.game.models.scored.ScoredTurn;
import com.github.mcreeper12731.game.presets.Preset;
import com.github.mcreeper12731.utility.Log;

import java.util.Arrays;

public class Main {
    public static void main(String[] args) {

        BitGame game = new BitGame(Preset.PUZZLE_KNIGHT_6.getGame());

        Log.debug("Test", game.getMultiverse().getTimeline(0));

        Log.print("Main", game.getMultiverse());
    }
}