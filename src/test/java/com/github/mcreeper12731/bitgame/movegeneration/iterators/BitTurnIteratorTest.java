package com.github.mcreeper12731.bitgame.movegeneration.iterators;

import com.github.mcreeper12731.application.MainApplication;
import com.github.mcreeper12731.bitgame.BitGame;
import com.github.mcreeper12731.bitgame.movegeneration.BitMoveGenerator;
import com.github.mcreeper12731.game.Game;
import com.github.mcreeper12731.game.models.Move;
import com.github.mcreeper12731.game.movegeneration.MoveGenerator;
import com.github.mcreeper12731.game.presets.Preset;
import com.github.mcreeper12731.utility.Iterators;
import com.github.mcreeper12731.utility.Log;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BitTurnIteratorTest {

    @Test
    public void turnGenerationSameAsRegular() {

        Game game = Preset.STANDARD.getGame();
        List<List<Move>> turns = Iterators.consumeRemaining(MoveGenerator.getTurnsIterator(game));

        BitGame bitGame = new BitGame(game);
        List<List<Move>> bitTurns = Iterators.consumeRemaining(BitMoveGenerator.getTurnsIterator(bitGame));

        assertEquals(turns, bitTurns);

//        Log.print("Main", bitTurns);

        game = Preset.CUSTOM_COMPLEX_POSITION.getGame();
        turns = Iterators.consumeRemaining(MoveGenerator.getTurnsIterator(game));

        bitGame = new BitGame(game);
//        Log.debug("Test", bitGame.getMultiverse());
        bitTurns = Iterators.consumeRemaining(BitMoveGenerator.getTurnsIterator(bitGame));

        assertEquals(turns, bitTurns);
    }
}