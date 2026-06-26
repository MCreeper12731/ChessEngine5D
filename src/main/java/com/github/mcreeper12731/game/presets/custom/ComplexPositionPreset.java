package com.github.mcreeper12731.game.presets.custom;

import com.github.mcreeper12731.game.Game;
import com.github.mcreeper12731.game.models.Move;
import com.github.mcreeper12731.game.presets.GamePreset;
import com.github.mcreeper12731.game.presets.Preset;

import java.util.List;

public class ComplexPositionPreset implements GamePreset {

    @Override
    public Game createGame() {
        Game game = Preset.PUZZLE_KNIGHT_6.getGame();

        Move move = new Move.Builder(game.getMultiverse())
                .withFrom(0, 0, 0, 0)
                .withTo(0, 0, 0, 1)
                .build();
        game.applyMovesAndFinalizeTurn(List.of(move));

        move = new Move.Builder(game.getMultiverse())
                .withFrom(0, 1, 4, 4)
                .withTo(0, 1, 4, 3)
                .build();
        game.applyMovesAndFinalizeTurn(List.of(move));

        move = new Move.Builder(game.getMultiverse())
                .withFrom(0, 2, 2, 0)
                .withTo(0, 2, 4, 1)
                .build();
        game.applyMovesAndFinalizeTurn(List.of(move));

        move = new Move.Builder(game.getMultiverse())
                .withFrom(0, 3, 4, 3)
                .withTo(0, 1, 4, 3)
                .build();
        game.applyMovesAndFinalizeTurn(List.of(move));

        move = new Move.Builder(game.getMultiverse())
                .withFrom(-1, 2, 2, 0)
                .withTo(-1, 2, 4, 1)
                .build();
        game.applyMovesAndFinalizeTurn(List.of(move));

        move = new Move.Builder(game.getMultiverse())
                .withFrom(-1, 3, 4, 3)
                .withTo(0, 1, 4, 3)
                .build();
        game.applyMovesAndFinalizeTurn(List.of(move));

        return game;
    }
}
