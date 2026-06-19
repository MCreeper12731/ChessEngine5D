package com.github.mcreeper12731.game.graphics;

import com.github.mcreeper12731.game.graphics.components.ViewComponent;
import com.github.mcreeper12731.game.logic.Game;
import javafx.scene.Scene;

public interface GraphicsApplication {

    Controller getCurrentController();

    Game getGame();

    ViewComponent getView();

    Scene getScene();

    void updateCurrentPlayer();

}
