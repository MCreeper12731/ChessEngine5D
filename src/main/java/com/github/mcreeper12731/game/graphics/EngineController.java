package com.github.mcreeper12731.game.graphics;

import com.github.mcreeper12731.engine.ChessEngine;
import com.github.mcreeper12731.game.graphics.components.TileComponent;
import com.github.mcreeper12731.game.models.Color;
import com.github.mcreeper12731.game.models.Move;

import java.util.List;

public class EngineController extends Controller {

    private final ChessEngine chessEngine;

    public EngineController(GraphicsApplication application, Color playingAs) {
        super(application, playingAs);
        this.chessEngine = new ChessEngine(
                application.getGame(),
                playingAs
        );
    }

    @Override
    public void onTurnStart() {
        application.getScene().setOnKeyPressed(event -> {
            switch (event.getCode()) {
                case Q:
                    List<Move> turn = chessEngine.getTurn();
                    System.out.println("Engine played " + turn + "!");
                    application.getGame().applyMovesFromTurnStart(turn);
                    application.updateCurrentPlayer();
                    updateView();
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
