package com.github.mcreeper12731.bitapplication.controllers;

import com.github.mcreeper12731.application.GraphicsApplication;
import com.github.mcreeper12731.bitapplication.GraphicsBitApplication;
import com.github.mcreeper12731.bitgame.BitGame;
import com.github.mcreeper12731.engine.engines.BitEngine;
import com.github.mcreeper12731.engine.engines.Engine;
import com.github.mcreeper12731.game.models.Move;
import com.github.mcreeper12731.application.components.TileComponent;
import com.github.mcreeper12731.game.Game;
import com.github.mcreeper12731.game.models.Color;
import com.github.mcreeper12731.utility.Log;
import javafx.application.Platform;

import java.util.List;

public class HeadlessBitEngineController extends BitController {

    private final BitEngine engine;

    public HeadlessBitEngineController(GraphicsBitApplication application, Color playingAs, BitEngine engine) {
        super(application, playingAs);
        this.engine = engine;
    }

    @Override
    public void onTurnStart() {
        Log.debug("HeadlessBitApplication", "Engine turn");
        BitGame game = this.application.getGame();

        if (game.isGameOver()) {
            Log.print("HeadlessBitApplication", "Game over,", game.getWinner(), "won!");
            return;
        }
        List<Move> turn = this.engine.nextTurn(game);
        game.applyMoves(turn);
        Log.print("HeadlessBitApplication", "Engine played:", turn);
        this.application.updateCurrentPlayer();
    }

    @Override
    public void onTurnEnd() {

    }
}
