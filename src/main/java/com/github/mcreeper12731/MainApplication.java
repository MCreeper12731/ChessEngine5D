package com.github.mcreeper12731;

import com.github.mcreeper12731.game.graphics.Controller;
import com.github.mcreeper12731.game.graphics.EngineController;
import com.github.mcreeper12731.game.graphics.GraphicsApplication;
import com.github.mcreeper12731.game.graphics.PlayerController;
import com.github.mcreeper12731.game.graphics.components.ViewComponent;
import com.github.mcreeper12731.game.models.Color;
import com.github.mcreeper12731.game.models.Multiverse;
import com.github.mcreeper12731.game.presets.Preset;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.List;

public class MainApplication extends Application implements GraphicsApplication {

    private ViewComponent view;
    private Controller controller1;
    private Controller controller2;
    private Preset preset;
    private Multiverse multiverse;
    private List<String> launchArgs;
    private Scene scene;

    @Override
    public void init() {
        this.launchArgs = getParameters().getRaw();

        this.preset = Preset.fromString(LaunchConfig.PRESET);
        this.multiverse = preset.getMultiverse();
        this.controller1 = getController(LaunchConfig.CONTROLLER_1, Color.WHITE);
        this.controller2 = getController(LaunchConfig.CONTROLLER_2, Color.BLACK);
        this.view = new ViewComponent(this);
        this.scene = new Scene(view);
    }

    @Override
    public void start(Stage primaryStage) {

        if (multiverse.getPlayerTurn() == Color.WHITE)
            controller1.onTurnStart();
        else
            controller2.onTurnStart();

        primaryStage.setTitle("5D Chess Visualizer");
        primaryStage.setScene(scene);
        primaryStage.setMaximized(true);
        primaryStage.show();

        view.draw();
    }

    private Controller getController(String controllerType, Color controllerColor) {
        return switch (controllerType) {
            case "player" -> new PlayerController(this, controllerColor);
            case "engine" -> new EngineController(this, controllerColor);
            default -> throw new RuntimeException("Controller of type " + controllerType + " does not exist!");
        };
    }

    public Preset getPreset() {
        return preset;
    }

    public Multiverse getMultiverse() {
        return multiverse;
    }

    public ViewComponent getView() {
        return view;
    }

    public Scene getScene() {
        return scene;
    }

    public void updateCurrentPlayer() {
        if (!multiverse.isCurrentTurnFinalizable()) {
            System.out.println("Current turn is not finalizable!");
            return;
        }
        getCurrentController().onTurnEnd();
        multiverse.finalizeTurn(false);
        getCurrentController().onTurnStart();
    }

    public Controller getCurrentController() {
        return switch (multiverse.getPlayerTurn()) {
            case WHITE -> controller1;
            case BLACK -> controller2;
        };
    }

    public static void main(String[] args) {
        launch(args);
    }

}
