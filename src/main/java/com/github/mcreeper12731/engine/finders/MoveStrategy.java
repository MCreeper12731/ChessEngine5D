package com.github.mcreeper12731.engine.finders;

import com.github.mcreeper12731.engine.ChessEngine;
import com.github.mcreeper12731.engine.config.BruteForceStrategyConfig;
import com.github.mcreeper12731.engine.config.MinimaxStrategyConfig;
import com.github.mcreeper12731.engine.config.MoveStrategyConfig;
import com.github.mcreeper12731.game.models.Color;
import com.github.mcreeper12731.game.models.Multiverse;
import com.github.mcreeper12731.game.moves.Turn;

public interface MoveStrategy {

    static MoveStrategy fromConfig(MoveStrategyConfig config, ChessEngine engine) {
        return switch (config) {
            case BruteForceStrategyConfig bfConfig -> new BruteForceStrategy(
                    engine,
                    bfConfig.moveLimit()
            );
            case MinimaxStrategyConfig mmConfig -> new MinimaxStrategy(
                    engine,
                    mmConfig.moveLimit()
            );
        };
    }

    Turn nextTurn();

    void opponentTurn(Turn turn);

}
