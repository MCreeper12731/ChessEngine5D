package com.github.mcreeper12731.application.controllers;

import com.github.mcreeper12731.application.GraphicsApplication;
import com.github.mcreeper12731.engine.engines.Engine;
import com.github.mcreeper12731.game.models.Move;
import com.github.mcreeper12731.application.components.TileComponent;
import com.github.mcreeper12731.game.Game;
import com.github.mcreeper12731.game.models.Color;
import com.github.mcreeper12731.utility.Log;
import javafx.application.Platform;

import java.util.List;

public class EngineController extends Controller {

    private final Engine engine;

    public EngineController(GraphicsApplication application, Color playingAs, Engine engine) {
        super(application, playingAs);
        this.engine = engine;
    }

    @Override
    public void onTurnStart() {
        Log.debug("Application", "Engine turn");
        Game game = this.application.getGame();

        if (game.isGameOver()) {
            return;
        }

        Thread searchThread = new Thread(() -> {
            List<Move> turn = engine.nextTurn(game);

            Platform.runLater(() -> {

                game.applyMoves(turn);
                Log.print("Application", "Engine played:", turn);
                updateView();

                new Thread(() -> {
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    Platform.runLater(application::updateCurrentPlayer);
                }).start();
            });
        });

        searchThread.setDaemon(true);
        searchThread.setName("ChessEngineSearch");
        searchThread.start();
    }

    @Override
    public void onTurnEnd() {

    }

    @Override
    public void handleTileComponentClick(TileComponent tileComponent) { }
}
