package com.github.mcreeper12731.application;

import com.github.mcreeper12731.application.components.ViewComponent;
import com.github.mcreeper12731.application.controllers.*;
import com.github.mcreeper12731.engine.engines.Engine;
import com.github.mcreeper12731.engine.engines.NegaMaxEngine;
import com.github.mcreeper12731.engine.engines.RandomSelectionEngine;
import com.github.mcreeper12731.game.Game;
import com.github.mcreeper12731.game.models.Color;
import javafx.scene.Scene;

public interface GraphicsApplication {

    default Controller getController(String controllerType, Color controllerColor, boolean headless) {
        Engine engine = null;
        switch (controllerType) {
            case "player" -> {
                return !headless ?
                        new PlayerController(this, controllerColor) :
                        new HeadlessPlayerController(this, controllerColor);
            }
            case "engine_random" -> engine = new RandomSelectionEngine();
            case "engine_negamax" -> engine = new NegaMaxEngine();
        }
        if (engine != null) {
            return !headless ?
                    new EngineController(this, controllerColor, engine) :
                    new HeadlessEngineController(this, controllerColor, engine);
        }
        throw new IllegalArgumentException("Controller of type " + controllerType + " does not exist!");
    }

    Controller getActiveController();

    Game getGame();

    ViewComponent getView();

    Scene getScene();

    void updateCurrentPlayer();

}
