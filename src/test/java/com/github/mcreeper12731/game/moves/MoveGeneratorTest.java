package com.github.mcreeper12731.game.moves;

import com.github.mcreeper12731.game.models.Color;
import com.github.mcreeper12731.game.models.Multiverse;
import com.github.mcreeper12731.game.pieces.PieceType;
import com.github.mcreeper12731.game.presets.Preset;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Queue;

class MoveGeneratorTest {

    /*@Test
    void turnGenerationTest1Test() {

        Multiverse multiverse = Preset.TEST1.getMultiverse();

        Queue<Turn> turns = MoveGenerator.generateAllTurns(multiverse);

        Assertions.assertEquals(3, turns.size());

        multiverse.applyAndFinalizeTurn(new Move(
                Color.WHITE, PieceType.KING,
                0, 0, 0, 0,
                0, 0, 0, 1
        ));

        turns = MoveGenerator.generateAllTurns(multiverse);
        Assertions.assertEquals(3, turns.size());
    }*/

    @Test
    void turnGenerationRookTactics1Test() {

        Multiverse multiverse = Preset.PUZZLE_ROOK1.getMultiverse();

        Queue<Turn> turns = MoveGenerator.generateAllTurns(multiverse);

    }
}