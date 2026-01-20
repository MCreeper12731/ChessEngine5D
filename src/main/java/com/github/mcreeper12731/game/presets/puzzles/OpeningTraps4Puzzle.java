package com.github.mcreeper12731.game.presets.puzzles;

import com.github.mcreeper12731.game.models.Multiverse;
import com.github.mcreeper12731.game.models.Point4D;
import com.github.mcreeper12731.game.presets.Preset;

public class OpeningTraps4Puzzle implements Puzzle {

    @Override
    public int moveLimit() {
        return 2;
    }

    @Override
    public Multiverse createMultiverse() {

        return new Multiverse.Builder(Preset.STANDARD.getMultiverse())
                .withTurn(
                        new Point4D(0, 0, 4, 1),
                        new Point4D(0, 0, 4, 3)
                )
                .withTurn(
                        new Point4D(0, 1, 2, 6),
                        new Point4D(0, 1, 2, 4)
                )
                .withTurn(
                        new Point4D(0, 2, 3, 0),
                        new Point4D(0, 2, 7, 4)
                )
                .withTurn(
                        new Point4D(0, 3, 3, 6),
                        new Point4D(0, 3, 3, 5)
                )
                .withTurn(
                        new Point4D(0, 4, 5, 0),
                        new Point4D(0, 4, 1, 4)
                )
                .withTurn(
                        new Point4D(0, 5, 1, 7),
                        new Point4D(0, 5, 2, 5)
                )
                .withTurn(
                        new Point4D(0, 6, 3, 1),
                        new Point4D(0, 6, 3, 2)
                )
                .withTurn(
                        new Point4D(0, 7, 6, 7),
                        new Point4D(0, 7, 5, 5)
                )
                .build();
    }
}
