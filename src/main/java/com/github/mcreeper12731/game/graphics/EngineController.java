package com.github.mcreeper12731.game.graphics;

import com.github.mcreeper12731.game.graphics.components.TileComponent;
import com.github.mcreeper12731.game.models.Color;
import com.github.mcreeper12731.game.models.Move;

import java.util.List;

public class EngineController extends Controller {


    public EngineController(GraphicsApplication application, Color playingAs) {
        super(application, playingAs);

    }

    @Override
    public void onTurnStart() {
        application.getScene().setOnKeyPressed(event -> {
            switch (event.getCode()) {
                case Q:
                    break;
                default:
                    break;
            }
        });
    }

    @Override
    public void onTurnEnd() {
        application.getScene().setOnKeyPressed(null);
    }

    @Override
    public void handleTileComponentClick(TileComponent tileComponent) { }
}
