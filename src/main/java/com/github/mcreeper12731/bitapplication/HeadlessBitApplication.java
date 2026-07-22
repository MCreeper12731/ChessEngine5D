package com.github.mcreeper12731.bitapplication;

import com.github.mcreeper12731.LaunchConfig;
import com.github.mcreeper12731.application.GraphicsApplication;
import com.github.mcreeper12731.application.components.ViewComponent;
import com.github.mcreeper12731.application.controllers.Controller;
import com.github.mcreeper12731.bitapplication.controllers.BitController;
import com.github.mcreeper12731.bitgame.BitGame;
import com.github.mcreeper12731.game.models.Color;
import com.github.mcreeper12731.utility.Log;
import javafx.scene.Scene;

public class HeadlessBitApplication implements GraphicsBitApplication {

    private final BitController controller1;
    private final BitController controller2;
    private final BitGame game;

    public HeadlessBitApplication() {
        this(LaunchConfig.PRESET.getBitGame(), LaunchConfig.CONTROLLER_WHITE, LaunchConfig.CONTROLLER_BLACK);
    }

    public HeadlessBitApplication(BitGame game, String controller1Type, String controller2Type) {
        this.game = game;
        this.controller1 = getController(controller1Type, Color.WHITE, true);
        this.controller2 = getController(controller2Type, Color.BLACK, true);
    }

    public void run() {
        Log.print("HeadlessBitApplication", "Starting game");

        if (this.game.getPlayerTurn() == Color.WHITE)
            this.controller1.onTurnStart();
        else
            this.controller2.onTurnStart();
    }

    @Override
    public BitGame getGame() {
        return this.game;
    }

    @Override
    public ViewComponent getView() {
        return null;
    }

    @Override
    public Scene getScene() {
        return null;
    }

    @Override
    public void updateCurrentPlayer() {
        if (!this.game.isCurrentTurnFinalizable()) {
            Log.print("HeadlessBitApplication", "Current turn is not finalizable!");
            return;
        }
        getActiveController().onTurnEnd();
        this.game.finalizeTurn();

        if (this.game.isGameOver()) {
            Log.print("HeadlessBitApplication", "Game over! Winner is", this.game.getWinner());
            return;
        }

        getActiveController().onTurnStart();
    }

    @Override
    public BitController getActiveController() {
        return switch (this.game.getPlayerTurn()) {
            case WHITE -> this.controller1;
            case BLACK -> this.controller2;
        };
    }
}