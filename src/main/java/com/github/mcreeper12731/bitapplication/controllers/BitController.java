package com.github.mcreeper12731.bitapplication.controllers;

import com.github.mcreeper12731.application.components.TileComponent;
import com.github.mcreeper12731.bitapplication.GraphicsBitApplication;
import com.github.mcreeper12731.game.models.Color;

public abstract class BitController {

    protected final GraphicsBitApplication application;
    protected final Color playingAs;

    public BitController(GraphicsBitApplication application, Color playingAs) {
        this.application = application;
        this.playingAs = playingAs;
    }

    public abstract void onTurnStart();

    public abstract void onTurnEnd();

    public void handleTileComponentClick(TileComponent tileComponent) {}

    public void updateView() {
        application.getView().draw();
    }
}

