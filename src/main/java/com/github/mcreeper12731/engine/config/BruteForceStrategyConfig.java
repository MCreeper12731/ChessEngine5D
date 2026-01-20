package com.github.mcreeper12731.engine.config;

public record BruteForceStrategyConfig(String solverName, int moveLimit) implements MoveStrategyConfig {

    public BruteForceStrategyConfig(int moveLimit) {
        this("brute_force", moveLimit);
    }
}
