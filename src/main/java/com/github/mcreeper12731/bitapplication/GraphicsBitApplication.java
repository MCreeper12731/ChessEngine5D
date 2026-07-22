package com.github.mcreeper12731.bitapplication;

import com.github.mcreeper12731.application.components.ViewComponent;
import com.github.mcreeper12731.application.controllers.*;
import com.github.mcreeper12731.bitapplication.controllers.BitController;
import com.github.mcreeper12731.bitapplication.controllers.HeadlessBitEngineController;
import com.github.mcreeper12731.bitapplication.controllers.HeadlessBitPlayerController;
import com.github.mcreeper12731.bitgame.BitGame;
import com.github.mcreeper12731.engine.engines.*;
import com.github.mcreeper12731.game.Game;
import com.github.mcreeper12731.game.models.Color;
import javafx.scene.Scene;

public interface GraphicsBitApplication {

    default BitController getController(String controllerType, Color controllerColor, boolean headless) {
        BitEngine engine = null;
        switch (controllerType) {
            case "player" -> {
                return new HeadlessBitPlayerController(this, controllerColor);
            }
            case "engine_random" -> engine = new RandomSelectionBitEngine();
            case "engine_negamax" -> engine = new NegaMaxBitEngine();
        }
        if (engine != null) {
            return new HeadlessBitEngineController(this, controllerColor, engine);
        }
        throw new IllegalArgumentException("Controller of type " + controllerType + " does not exist!");
    }

    BitController getActiveController();

    BitGame getGame();

    ViewComponent getView();

    Scene getScene();

    void updateCurrentPlayer();

}
