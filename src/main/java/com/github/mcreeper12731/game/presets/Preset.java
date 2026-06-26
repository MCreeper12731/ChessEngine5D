package com.github.mcreeper12731.game.presets;

import com.github.mcreeper12731.game.presets.custom.SimplePositionPreset;
import com.github.mcreeper12731.game.Game;
import com.github.mcreeper12731.game.presets.custom.ComplexPositionPreset;
import com.github.mcreeper12731.game.presets.custom.TestCastlingPreset;
import com.github.mcreeper12731.game.presets.custom.JustQueensInvasionPreset;
import com.github.mcreeper12731.game.presets.custom.Test1Preset;
import com.github.mcreeper12731.game.presets.focused.JustDragonsPreset;
import com.github.mcreeper12731.game.presets.focused.JustQueensPreset;
import com.github.mcreeper12731.game.presets.focused.JustRooksPreset;
import com.github.mcreeper12731.game.presets.puzzles.*;

public enum Preset {

    STANDARD(new StandardPreset()),
    JUST_QUEENS(new JustQueensPreset()),
    JUST_QUEENS_INVASION(new JustQueensInvasionPreset()),
    JUST_ROOKS(new JustRooksPreset()),
    JUST_DRAGONS(new JustDragonsPreset()),
    PUZZLE_ROOK_1(new RookTactics1Puzzle()),
    PUZZLE_ROOK_2(new RookTactics2Puzzle()),
    PUZZLE_ROOK_3(new RookTactics3Puzzle()),
    PUZZLE_ROOK_4(new RookTactics4Puzzle()),
    PUZZLE_ROOK_5(new RookTactics5Puzzle()),
    PUZZLE_QUEEN_1(new QueenTactics1Puzzle()),
    PUZZLE_QUEEN_3(new QueenTactics3Puzzle()),
    PUZZLE_QUEEN_4(new QueenTactics4Puzzle()),
    PUZZLE_KNIGHT_6(new KnightTactics6Puzzle()),
    PUZZLE_BACKRANK_4(new BackrankBasics4Puzzle()),
    PUZZLE_OPENING_TRAP_2(new OpeningTraps2Puzzle()),
    PUZZLE_OPENING_TRAP_4(new OpeningTraps4Puzzle()),
    CUSTOM_TEST_1(new Test1Preset()),
    CUSTOM_TEST_CASTLING(new TestCastlingPreset()),
    CUSTOM_COMPLEX_POSITION(new ComplexPositionPreset()),
    CUSTOM_SIMPLE_POSITION(new SimplePositionPreset());

    public static Preset fromString(String stringValue) {
        return Preset.valueOf(stringValue.toUpperCase());
    }

    private final GamePreset gamePreset;

    Preset(GamePreset gamePreset) {
        this.gamePreset = gamePreset;
    }

    public Game getGame() {
        return this.gamePreset.createGame();
    }

}
