package com.github.mcreeper12731;

import com.github.mcreeper12731.game.presets.Preset;

public class LaunchConfig {

    public static final Preset PRESET = Preset.CHECKMATE_PRACTICE_PAWN;
    public static final String CONTROLLER_1 = "player";
    public static final String CONTROLLER_2 = "engine";

    public static final int MAX_NODES = 2_000_000;
    public static final int MAX_DEPTH = 9;
    public static final int DEBUG_LEVEL = 0;
    public static final int MAX_ADDITIONAL_TIMELINES = 1;

}
