package com.github.mcreeper12731.game.presets;

import com.github.mcreeper12731.game.models.Multiverse;
import com.github.mcreeper12731.game.presets.custom.JustQueensInvasionPreset;
import com.github.mcreeper12731.game.presets.custom.Test1Preset;
import com.github.mcreeper12731.game.presets.focused.JustQueensPreset;
import com.github.mcreeper12731.game.presets.focused.JustRooksPreset;
import com.github.mcreeper12731.game.presets.puzzles.*;

public enum Preset {

    STANDARD(new StandardPreset()),
    JUST_QUEENS(new JustQueensPreset()),
    JUST_QUEENS_INVASION(new JustQueensInvasionPreset()),
    JUST_ROOKS(new JustRooksPreset()),
    PUZZLE_ROOK1(new RookTactics1Puzzle()),
    PUZZLE_ROOK2(new RookTactics2Puzzle()),
    PUZZLE_ROOK3(new RookTactics3Puzzle()),
    PUZZLE_ROOK4(new RookTactics4Puzzle()),
    PUZZLE_ROOK5(new RookTactics5Puzzle()),
    PUZZLE_QUEEN1(new QueenTactics1Puzzle()),
    PUZZLE_QUEEN3(new QueenTactics3Puzzle()),
    PUZZLE_QUEEN4(new QueenTactics4Puzzle()),
    PUZZLE_KNIGHT6(new KnightTactics6Puzzle()),
    PUZZLE_BACKRANK4(new BackrankBasics4Puzzle()),
    PUZZLE_OPENING_TRAP2(new OpeningTraps2Puzzle()),
    PUZZLE_OPENING_TRAP4(new OpeningTraps4Puzzle()),
    TEST1(new Test1Preset());

    public static Preset fromString(String stringValue) {
        return Preset.valueOf(stringValue.toUpperCase());
    }

    private final GamePreset gamePreset;

    Preset(GamePreset gamePreset) {
        this.gamePreset = gamePreset;
    }

    public Multiverse getMultiverse() {
        return this.gamePreset.createMultiverse();
    }

}
