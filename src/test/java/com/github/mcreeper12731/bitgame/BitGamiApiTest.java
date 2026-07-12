package com.github.mcreeper12731.bitgame;

import com.github.mcreeper12731.application.MainApplication;
import com.github.mcreeper12731.bitgame.models.BitBoard;
import com.github.mcreeper12731.bitgame.models.BitMultiverse;
import com.github.mcreeper12731.bitgame.models.BitTimeline;
import com.github.mcreeper12731.game.Game;
import com.github.mcreeper12731.game.models.Move;
import com.github.mcreeper12731.game.presets.Preset;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class BitGamiApiTest {

    @Test
    public void isCalculatedPresentTimeCorrectWhenStatic() {

        BitGame bitGame = new BitGame(new BitMultiverse.Builder(4)
                .withTimeline(new BitTimeline.Builder(0)
                        .withBoard(new BitBoard.Builder(4, 0, 0).build())
                        .withBoard(new BitBoard.Builder(4, 0, 1).build())
                        .withBoard(new BitBoard.Builder(4, 0, 2).build())
                        .withBoard(new BitBoard.Builder(4, 0, 3).build())
                        .build()
                ).withTimeline(new BitTimeline.Builder(1)
                        .withBoard(new BitBoard.Builder(4, 1, 0).build())
                        .withBoard(new BitBoard.Builder(4, 1, 1).build())
                        .withBoard(new BitBoard.Builder(4, 1, 2).build())
                        .build()
                ).withTimeline(new BitTimeline.Builder(2)
                        .withBoard(new BitBoard.Builder(4, 2, 0).build())
                        .build()
                ).build()
        );

        assertEquals(2, bitGame.getCalculatedPresentTime());
    }

    @Test
    public void isCalculatedPresentTimeCorrectWhenApplyingMove() {
        BitGame bitGame = new BitGame(Preset.CUSTOM_COMPLEX_POSITION.getGame());

        assertEquals(4, bitGame.getCalculatedPresentTime());

        bitGame.applyMove(Move.noop(-1, 4));
        assertEquals(4, bitGame.getCalculatedPresentTime());

        bitGame.applyMove(Move.noop(0, 4));
        assertEquals(5, bitGame.getCalculatedPresentTime());
    }

    @Test
    public void isCalculatedPresentTimeCorrectWhenActivatingTimeline() {
        BitGame bitGame = new BitGame(Preset.CUSTOM_COMPLEX_POSITION.getGame());

        bitGame.applyMove(Move.of(bitGame, 0, 4, 4, 1, 0, 2, 4, 1));
        assertEquals(2, bitGame.getCalculatedPresentTime());
    }

    @Test
    public void isCalculatedPresentTimeCorrectWhenUndoMove() {
        BitGame bitGame = new BitGame(Preset.CUSTOM_COMPLEX_POSITION.getGame());

        bitGame.applyMove(Move.noop(-1, 4));
        bitGame.applyMove(Move.noop(0, 4));
        assertEquals(5, bitGame.getCalculatedPresentTime());
        bitGame.undoMoveFromCurrentTurn();
        assertEquals(4, bitGame.getCalculatedPresentTime());
        bitGame.undoMoveFromCurrentTurn();
        assertEquals(4, bitGame.getCalculatedPresentTime());
    }
}
