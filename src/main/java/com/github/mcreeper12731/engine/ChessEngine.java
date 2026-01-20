package com.github.mcreeper12731.engine;

import com.github.mcreeper12731.engine.config.MoveStrategyConfig;
import com.github.mcreeper12731.engine.finders.MoveStrategy;
import com.github.mcreeper12731.engine.finders.BruteForceStrategy;
import com.github.mcreeper12731.game.models.Color;
import com.github.mcreeper12731.game.models.Multiverse;
import com.github.mcreeper12731.game.moves.Turn;
import com.github.mcreeper12731.game.moves.TurnNode;
import com.github.mcreeper12731.game.moves.TurnPlayedListener;

public class ChessEngine implements TurnPlayedListener {

    private final Multiverse multiverse;
    private final Color playingAs;
    private final MoveStrategy finder;

    public ChessEngine(Multiverse multiverse, Color playingAs, MoveStrategyConfig config) {
        multiverse.addListener(this);
        this.multiverse = multiverse;
        this.playingAs = playingAs;
        this.finder = MoveStrategy.fromConfig(config, this);
    }

    public Turn getTurn() {
        return finder.nextTurn();
    }

    @Override
    public void onTurnPlayed(Turn turn) {
        if (turn.getPlayedBy() == playingAs) return;
        finder.opponentTurn(turn);
    }

    public Multiverse getMultiverse() {
        return multiverse;
    }

    public Color getPlayingAs() {
        return playingAs;
    }
}
