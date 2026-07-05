package com.github.mcreeper12731.application.controllers;

import com.github.mcreeper12731.application.GraphicsApplication;
import com.github.mcreeper12731.application.components.TileComponent;
import com.github.mcreeper12731.game.models.Color;

public abstract class Controller {

    protected final GraphicsApplication application;
    protected final Color playingAs;

    public Controller(GraphicsApplication application, Color playingAs) {
        this.application = application;
        this.playingAs = playingAs;
    }

    public abstract void onTurnStart();

    public abstract void onTurnEnd();

    public abstract void handleTileComponentClick(TileComponent tileComponent);

    public void updateView() {
        application.getView().draw();
    }
}
