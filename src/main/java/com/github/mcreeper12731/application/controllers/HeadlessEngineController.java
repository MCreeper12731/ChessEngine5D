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

public class HeadlessEngineController extends Controller {

    private final Engine engine;

    public HeadlessEngineController(GraphicsApplication application, Color playingAs, Engine engine) {
        super(application, playingAs);
        this.engine = engine;
    }

    @Override
    public void onTurnStart() {
        //Log.debug("HeadlessApplication", "Engine turn");
        Game game = this.application.getGame();

        if (game.isGameOver()) {
            return;
        }
        List<Move> turn = this.engine.nextTurn(game);
        game.applyMoves(turn);
        Log.print("HeadlessApplication", "Engine played:", turn);
        this.application.updateCurrentPlayer();
    }

    @Override
    public void onTurnEnd() {

    }

    @Override
    public void handleTileComponentClick(TileComponent tileComponent) { }
}
