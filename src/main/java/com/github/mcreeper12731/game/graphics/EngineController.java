package com.github.mcreeper12731.game.graphics;

import com.github.mcreeper12731.MainApplication;
import com.github.mcreeper12731.engine.ChessEngine;
import com.github.mcreeper12731.engine.config.BruteForceStrategyConfig;
import com.github.mcreeper12731.engine.config.MoveStrategyConfig;
import com.github.mcreeper12731.game.graphics.components.TileComponent;
import com.github.mcreeper12731.game.models.Color;
import com.github.mcreeper12731.game.moves.Turn;
import com.github.mcreeper12731.game.presets.puzzles.Puzzle;

import java.util.List;
import java.util.Optional;

public class EngineController extends Controller {

    private final ChessEngine chessEngine;

    public EngineController(MainApplication application, Color playingAs) {
        super(application, playingAs);

        Optional<Puzzle> potentialPuzzle = Puzzle.fromPreset(application.getPreset());
        if (potentialPuzzle.isPresent()) {
            this.chessEngine = new ChessEngine(
                    application.getMultiverse(),
                    playingAs,
                    new BruteForceStrategyConfig(potentialPuzzle.get().moveLimit())
            );
        } else {
            this.chessEngine = new ChessEngine(
                    application.getMultiverse(),
                    playingAs,
                    MoveStrategyConfig.fromParams(List.of())
            );
        }

    }

    @Override
    public void onTurnStart() {
        application.getScene().setOnKeyPressed(event -> {
            switch (event.getCode()) {
                case Q:
                    Turn turn = chessEngine.getTurn();
                    System.out.println("Engine played " + turn + "!");
                    application.getMultiverse().applyTurn(turn);
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
