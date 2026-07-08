package com.github.mcreeper12731.bitgame;

import com.github.mcreeper12731.game.Game;
import com.github.mcreeper12731.game.presets.Preset;
import com.github.mcreeper12731.utility.Log;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BitGameTest {

    @Test
    public void gameSameAsRegular() {

        Game game = Preset.CUSTOM_COMPLEX_POSITION.getGame();
        BitGame bitGame = new BitGame(game);

//        Log.debug("Test", game);
//        Log.debug("Test", bitGame);

        assertEquals(game.toString(), bitGame.toString());
        assertEquals(game.getMultiverse().toString(), bitGame.getMultiverse().toString());
    }

}