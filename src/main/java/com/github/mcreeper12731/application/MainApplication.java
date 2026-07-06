package com.github.mcreeper12731.application;

import com.github.mcreeper12731.LaunchConfig;
import com.github.mcreeper12731.application.controllers.Controller;
import com.github.mcreeper12731.application.components.ViewComponent;
import com.github.mcreeper12731.game.Game;
import com.github.mcreeper12731.game.models.Color;
import com.github.mcreeper12731.game.presets.Preset;
import com.github.mcreeper12731.utility.Log;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApplication extends Application implements GraphicsApplication {

    private static Game staticGameForLaunch;
    
    private ViewComponent view;
    private Controller controller1;
    private Controller controller2;
    private Preset preset;
    private Game game;
    private Scene scene;

    @Override
    public void init() {
        if (staticGameForLaunch != null) {
            this.game = staticGameForLaunch;
            staticGameForLaunch = null;
            this.controller1 = getController("player", Color.WHITE, false);
            this.controller2 = getController("player", Color.BLACK, false);
        } else {
            this.preset = LaunchConfig.PRESET;
            this.game = preset.getGame();
            this.controller1 = getController(LaunchConfig.CONTROLLER_WHITE, Color.WHITE, false);
            this.controller2 = getController(LaunchConfig.CONTROLLER_BLACK, Color.BLACK, false);
        }
        this.view = new ViewComponent(this);
        this.scene = new Scene(view);

        this.scene.setOnKeyPressed(event -> {
            switch (event.getCode()) {
                case R:
                    getView().draw();
                    break;
            }
        });
    }

    @Override
    public void start(Stage primaryStage) {

        String title = "5D Chess Visualizer";
        primaryStage.setTitle(title);
        primaryStage.setScene(this.scene);
        primaryStage.setMaximized(true);
        primaryStage.show();

        this.view.draw();

        if (this.game.getPlayerTurn() == Color.WHITE)
            this.controller1.onTurnStart();
        else
            this.controller2.onTurnStart();
    }

    public Game getGame() {
        return this.game;
    }

    public ViewComponent getView() {
        return view;
    }

    public Scene getScene() {
        return scene;
    }

    public void updateCurrentPlayer() {
        if (!this.game.isCurrentTurnFinalizable()) {
            Log.print("Application", "Current turn is not finalizable!");
            return;
        }
        getActiveController().onTurnEnd();
        this.game.finalizeTurn();
        getActiveController().onTurnStart();
    }

    public Controller getActiveController() {
        return switch (this.game.getPlayerTurn()) {
            case WHITE -> this.controller1;
            case BLACK -> this.controller2;
        };
    }

    public static void launchWithGame(Game game) {
        staticGameForLaunch = game;
        launch();
    }
}
