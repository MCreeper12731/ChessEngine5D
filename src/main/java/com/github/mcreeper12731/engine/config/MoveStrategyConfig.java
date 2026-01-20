package com.github.mcreeper12731.engine.config;

import com.github.mcreeper12731.engine.finders.BruteForceStrategy;
import com.github.mcreeper12731.game.models.Color;

import java.util.List;

public sealed interface MoveStrategyConfig permits BruteForceStrategyConfig, MinimaxStrategyConfig {

    static MoveStrategyConfig fromParams(List<String> params) {
        return switch (params.get(0)) {
            case "brute_force" ->
                new BruteForceStrategyConfig(
                        Integer.parseInt(params.get(1))
                );
            case "minimax" ->
                new MinimaxStrategyConfig(
                        Integer.parseInt(params.get(1))
                );
            default ->
                throw new RuntimeException("Solver " + params.get(0) + " not found!");
        };
    }

    static MoveStrategyConfig fromParams(String... params) {
        return fromParams(List.of(params));
    }

    String solverName();

}
