package com.github.mcreeper12731.engine;

import com.github.mcreeper12731.engine.finders.MoveStrategy;
import com.github.mcreeper12731.game.logic.Game;
import com.github.mcreeper12731.game.models.Color;
import com.github.mcreeper12731.game.moves.Move;

import java.util.List;

public class ChessEngine {

    private final Game game;
    private final Color playingAs;
    private final MoveStrategy finder;

    public ChessEngine(Game game, Color playingAs) {
        this.game = game;
        this.playingAs = playingAs;
        this.finder = null;
    }

    public List<Move> getTurn() {
        return finder.nextTurn();
    }

    public Game getGame() {
        return this.game;
    }

    public Color getPlayingAs() {
        return playingAs;
    }
}
