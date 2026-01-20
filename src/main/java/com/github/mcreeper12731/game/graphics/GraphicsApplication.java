package com.github.mcreeper12731.game.graphics;

import com.github.mcreeper12731.game.graphics.components.ViewComponent;
import com.github.mcreeper12731.game.models.Multiverse;
import javafx.scene.Scene;

public interface GraphicsApplication {

    Controller getCurrentController();

    Multiverse getMultiverse();

    ViewComponent getView();

    Scene getScene();

    void updateCurrentPlayer();

}
