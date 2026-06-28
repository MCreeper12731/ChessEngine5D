package com.github.mcreeper12731.game.graphics;

import com.github.mcreeper12731.engine.config.NegamaxStrategyConfig;
import com.github.mcreeper12731.engine.evaluators.EvaluatorImpl;
import com.github.mcreeper12731.engine.finders.NegaMaxStrategy;
import com.github.mcreeper12731.game.graphics.components.TileComponent;
import com.github.mcreeper12731.game.Game;
import com.github.mcreeper12731.game.models.Color;
import com.github.mcreeper12731.game.models.scored.ScoredTurn;
import com.github.mcreeper12731.utility.Log;
import javafx.application.Platform;

public class EngineController extends Controller {

    private final NegaMaxStrategy moveStrategy;

    public EngineController(GraphicsApplication application, Color playingAs, NegamaxStrategyConfig config) {
        super(application, playingAs);
        moveStrategy = new NegaMaxStrategy(
                config,
                new EvaluatorImpl()
        );
    }

    @Override
    public void onTurnStart() {
        Log.debug("Graphics", "Engine turn");
        Game game = this.application.getGame();

        if (game.isGameOver()) {
            return;
        }

        Thread searchThread = new Thread(() -> {
            ScoredTurn turn = moveStrategy.findBestTurn(game);

            Platform.runLater(() -> {

                game.applyMoves(turn.moves());
                Log.print("Graphics", "Engine played:", turn.moves(), "score:", turn.score(), "nodes searched:", turn.nodesSearched());
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
