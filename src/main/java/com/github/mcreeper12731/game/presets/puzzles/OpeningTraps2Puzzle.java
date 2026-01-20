package com.github.mcreeper12731.game.presets.puzzles;

import com.github.mcreeper12731.game.models.Multiverse;
import com.github.mcreeper12731.game.models.Point4D;
import com.github.mcreeper12731.game.presets.Preset;

import java.awt.*;

public class OpeningTraps2Puzzle implements Puzzle {

    @Override
    public int moveLimit() {
        return 1;
    }

    @Override
    public Multiverse createMultiverse() {

        return new Multiverse.Builder(Preset.STANDARD.getMultiverse())
                .withTurn(
                        new Point4D(0, 0, 4, 1),
                        new Point4D(0, 0, 4, 3)
                )
                .withTurn(
                        new Point4D(0, 1, 3,  6),
                        new Point4D(0, 1, 3,  4)
                )
                .withTurn(
                        new Point4D(0, 2, 4, 3),
                        new Point4D(0, 2, 3, 4)
                )
                .withTurn(
                        new Point4D(0, 3, 2, 7),
                        new Point4D(0, 3, 5, 4)
                )
                .withTurn(
                        new Point4D(0, 4, 1, 0),
                        new Point4D(0, 4, 2, 2)
                )
                .withTurn(
                       new Point4D(0, 5, 3, 7),
                       new Point4D(0, 5, 3, 4)
                )
                .withTurn(
                        new Point4D(0, 6, 2, 2),
                        new Point4D(0, 6, 3, 4)
                )
                .build();
    }
}
