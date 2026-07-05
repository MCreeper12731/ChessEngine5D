package com.github.mcreeper12731.graphics;

import com.github.mcreeper12731.graphics.components.ViewComponent;
import com.github.mcreeper12731.game.Game;
import javafx.scene.Scene;

public interface GraphicsApplication {

    Controller getCurrentController();

    Game getGame();

    ViewComponent getView();

    Scene getScene();

    void updateCurrentPlayer();

}
