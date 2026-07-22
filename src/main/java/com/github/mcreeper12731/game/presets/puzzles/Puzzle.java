package com.github.mcreeper12731.game.presets.puzzles;

import com.github.mcreeper12731.game.presets.GamePreset;
import com.github.mcreeper12731.game.presets.Preset;

import java.util.Optional;

public interface Puzzle extends GamePreset {

    static Optional<Puzzle> fromPreset(Preset preset) {
        return Optional.ofNullable(
                switch (preset) {
                    case PUZZLE_ROOK_TACTICS_1 -> new RookTactics1Puzzle();
                    case PUZZLE_ROOK_TACTICS_2 -> new RookTactics2Puzzle();
                    case PUZZLE_ROOK_TACTICS_3 -> new RookTactics3Puzzle();
                    case PUZZLE_ROOK_TACTICS_4 -> new RookTactics4Puzzle();
                    case PUZZLE_ROOK_TACTICS_5 -> new RookTactics5Puzzle();
                    case PUZZLE_QUEEN_TACTICS_1 -> new QueenTactics1Puzzle();
                    case PUZZLE_QUEEN_TACTICS_3 -> new QueenTactics3Puzzle();
                    case PUZZLE_QUEEN_TACTICS_4 -> new QueenTactics4Puzzle();
                    case PUZZLE_KNIGHT_6 -> new KnightTactics6Puzzle();
                    case PUZZLE_BACKRANK_4 -> new BackrankBasics4Puzzle();
                    case PUZZLE_OPENING_TRAPS_2 -> new OpeningTraps2Puzzle();
                    case PUZZLE_OPENING_TRAPS_4 -> new OpeningTraps4Puzzle();
                    default -> null;
                });
    }

    int moveLimit();

}
