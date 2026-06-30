package com.github.mcreeper12731.engine.config;

import com.github.mcreeper12731.LaunchConfig;

public record NegamaxStrategyConfig(int maxDepth, long maxNodes, int debugLevel, int maxAdditionalTimelines) {

    public NegamaxStrategyConfig {
        if (maxDepth < 1) {
            throw new IllegalArgumentException("maxDepth must be at least 1");
        }

        if (maxNodes < 1) {
            throw new IllegalArgumentException("maxNodes must be at least 1");
        }

        if (maxAdditionalTimelines < 0) {
            throw new IllegalArgumentException("maxAdditionalTimelines must be at least 0");
        }
    }

    public static NegamaxStrategyConfig fromConfig() {
        return new NegamaxStrategyConfig(LaunchConfig.MAX_DEPTH, LaunchConfig.MAX_NODES, LaunchConfig.DEBUG_LEVEL, LaunchConfig.MAX_ADDITIONAL_TIMELINES);
    }
}
