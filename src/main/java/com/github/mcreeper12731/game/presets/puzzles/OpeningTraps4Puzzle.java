package com.github.mcreeper12731.game.presets.puzzles;

import com.github.mcreeper12731.game.logic.Game;
import com.github.mcreeper12731.game.moves.Move;
import com.github.mcreeper12731.game.presets.Preset;

import java.util.List;

public class OpeningTraps4Puzzle implements Puzzle {

    @Override
    public int moveLimit() {
        return 2;
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
                .withFrom(0, 1, 2, 6)
                .withTo(0, 1, 2, 4)
                .build()
        ));

        game.applyMovesAndFinalizeTurn(List.of(new Move.Builder(game.getMultiverse())
                .withFrom(0, 2, 3, 0)
                .withTo(0, 2, 7, 4)
                .build()
        ));

        game.applyMovesAndFinalizeTurn(List.of(new Move.Builder(game.getMultiverse())
                .withFrom(0, 3, 3, 6)
                .withTo(0, 3, 3, 5)
                .build()
        ));

        game.applyMovesAndFinalizeTurn(List.of(new Move.Builder(game.getMultiverse())
                .withFrom(0, 4, 5, 0)
                .withTo(0, 4, 1, 4)
                .build()
        ));

        game.applyMovesAndFinalizeTurn(List.of(new Move.Builder(game.getMultiverse())
                .withFrom(0, 5, 1, 7)
                .withTo(0, 5, 2, 5)
                .build()
        ));

        game.applyMovesAndFinalizeTurn(List.of(new Move.Builder(game.getMultiverse())
                .withFrom(0, 6, 3, 1)
                .withTo(0, 6, 3, 2)
                .build()
        ));

        game.applyMovesAndFinalizeTurn(List.of(new Move.Builder(game.getMultiverse())
                .withFrom(0, 7, 6, 7)
                .withTo(0, 7, 5, 5)
                .build()
        ));

        return game;
    }
}
