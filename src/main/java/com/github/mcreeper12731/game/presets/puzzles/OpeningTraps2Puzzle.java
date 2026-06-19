package com.github.mcreeper12731.game.presets.puzzles;

import com.github.mcreeper12731.game.logic.Game;
import com.github.mcreeper12731.game.models.Move;
import com.github.mcreeper12731.game.presets.Preset;

import java.util.List;

public class OpeningTraps2Puzzle implements Puzzle {

    @Override
    public int moveLimit() {
        return 1;
    }

    @Override
    public Game createGame() {

        Game game = Preset.STANDARD.getGame();

        game.applyMovesAndFinalizeTurn(List.of(new Move.Builder(game.getMultiverse())
                .withFrom(0, 0, 4, 1)
                .withTo(0, 0, 4, 3)
                .build()
        ));

        game.applyMovesAndFinalizeTurn(List.of(new Move.Builder(game.getMultiverse())
                .withFrom(0, 1, 3, 6)
                .withTo(0, 1, 3, 4)
                .build()
        ));

        game.applyMovesAndFinalizeTurn(List.of(new Move.Builder(game.getMultiverse())
                .withFrom(0, 2, 4, 3)
                .withTo(0, 2, 3, 4)
                .build()
        ));

        game.applyMovesAndFinalizeTurn(List.of(new Move.Builder(game.getMultiverse())
                .withFrom(0, 3, 2, 7)
                .withTo(0, 3, 5, 4)
                .build()
        ));

        game.applyMovesAndFinalizeTurn(List.of(new Move.Builder(game.getMultiverse())
                .withFrom(0, 4, 1, 0)
                .withTo(0, 4, 2, 2)
                .build()
        ));

        game.applyMovesAndFinalizeTurn(List.of(new Move.Builder(game.getMultiverse())
                .withFrom(0, 5, 3, 7)
                .withTo(0, 5, 3, 4)
                .build()
        ));

        game.applyMovesAndFinalizeTurn(List.of(new Move.Builder(game.getMultiverse())
                .withFrom(0, 6, 2, 2)
                .withTo(0, 6, 3, 4)
                .build()
        ));

        return game;
    }
}
