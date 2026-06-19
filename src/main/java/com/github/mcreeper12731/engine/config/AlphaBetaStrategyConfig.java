package com.github.mcreeper12731.engine.config;

public record AlphaBetaStrategyConfig(int maxDepth, long maxNodes, int debugLevel) {

    public AlphaBetaStrategyConfig {
        if (maxDepth < 1) {
            throw new IllegalArgumentException("maxDepth must be at least 1");
        }

        if (maxNodes < 1) {
            throw new IllegalArgumentException("maxNodes must be at least 1");
        }
    }
}
