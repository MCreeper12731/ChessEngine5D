package com.github.mcreeper12731.engine.evaluators;

import com.github.mcreeper12731.game.Game;
import com.github.mcreeper12731.game.models.Move;
import com.github.mcreeper12731.game.presets.Preset;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class EvaluatorImplTest {

    @Test
    public void isMateEvaluatedCorrectly() {

        Game game = Preset.PUZZLE_QUEEN_1.getGame();

        game.applyMovesAndFinalizeTurn(List.of(
                new Move.Builder(game)
                        .withPiece(game.getMultiverse().getLocationContents(0, 2, 2, 0))
                        .withTo(0, 2, 2, 1)
                        .build()
        ));

        game.applyMovesAndFinalizeTurn(List.of(
                new Move.Builder(game)
                        .withPiece(game.getMultiverse().getLocationContents(0, 3, 3, 2))
                        .withTo(0, 3, 3, 3)
                        .build()
        ));

        game.applyMovesAndFinalizeTurn(List.of(
                new Move.Builder(game)
                        .withPiece(game.getMultiverse().getLocationContents(0, 4, 2, 1))
                        .withTo(0, 2, 3, 2)
                        .build()
        ));

        Evaluator evaluator = new EvaluatorImpl();
        double score = evaluator.evaluate(game);
        assertTrue(score > 900000);
    }

}