package com.github.mcreeper12731.engine.config;

public record MinimaxStrategyConfig(String solverName, int moveLimit) implements MoveStrategyConfig {

    public MinimaxStrategyConfig(int moveLimit) {
        this("minimax", moveLimit);
    }
}
