package com.github.mcreeper12731.bitgame;

import com.github.mcreeper12731.game.Game;
import com.github.mcreeper12731.game.models.Move;
import com.github.mcreeper12731.game.models.Point4D;
import com.github.mcreeper12731.game.movegeneration.MoveGenerator;
import com.github.mcreeper12731.game.presets.Preset;
import com.github.mcreeper12731.utility.Log;
import org.junit.jupiter.api.Test;

import java.util.Iterator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BitGameTest {

    @Test
    public void gameSameAsRegular() {

        Game game = Preset.CUSTOM_COMPLEX_POSITION.getGame();
        BitGame bitGame = new BitGame(game);

        Log.debug("Test", game);
        Log.debug("Test", bitGame);

        assertEquals(game.toString(), bitGame.toString());
        assertEquals(game.getMultiverse().toString(), bitGame.getMultiverse().toString());
    }

    @Test
    public void undoTurnWhenSimplePreset() {

        BitGame game = Preset.CUSTOM_TEST_CASTLING.getBitGame();

        Move move = Move.of(game, 0, 0, 0, 0, 0, 1);

        int prevHashCode = game.hashCode();

        game.applyMovesAndFinalizeTurn(List.of(move));
        game.undoTurn();

        assertEquals(prevHashCode, game.hashCode());

    }

    @Test
    public void undoTurnTestComplex() {

        Game game = Preset.PUZZLE_KNIGHT_6.getGame();

        Move move = new Move.Builder(game.getMultiverse())
                .withFrom(0, 0, 0, 0)
                .withTo(0, 0, 0, 1)
                .build();
        game.applyMovesAndFinalizeTurn(List.of(move));

        move = new Move.Builder(game.getMultiverse())
                .withFrom(0, 1, 4, 4)
                .withTo(0, 1, 4, 3)
                .build();
        game.applyMovesAndFinalizeTurn(List.of(move));

        move = new Move.Builder(game.getMultiverse())
                .withFrom(0, 2, 2, 0)
                .withTo(0, 2, 4, 1)
                .build();
        game.applyMovesAndFinalizeTurn(List.of(move));

        move = new Move.Builder(game.getMultiverse())
                .withFrom(0, 3, 4, 3)
                .withTo(0, 1, 4, 3)
                .build();
        game.applyMovesAndFinalizeTurn(List.of(move));

        move = new Move.Builder(game.getMultiverse())
                .withFrom(-1, 2, 2, 0)
                .withTo(-1, 2, 4, 1)
                .build();
        game.applyMovesAndFinalizeTurn(List.of(move));

        move = new Move.Builder(game.getMultiverse())
                .withFrom(-1, 3, 4, 3)
                .withTo(0, 1, 4, 3)
                .build();
        game.applyMovesAndFinalizeTurn(List.of(move));

        Iterator<List<Move>> turnsIterator = MoveGenerator.getTurnsIterator(game);
        while (turnsIterator.hasNext()) {
            List<Move> turn = turnsIterator.next();

            int multiverseHashCodeBefore = game.getMultiverse().hashCode();
            int gameHashCodeBefore = game.hashCode();

            game.applyMovesAndFinalizeTurn(turn);
            game.undoTurn();

            assertEquals(multiverseHashCodeBefore, game.getMultiverse().hashCode());
            assertEquals(gameHashCodeBefore, game.hashCode());
        }

    }

    @Test
    public void undoTurnCorrectWhenDeactivatingTimeline() {

        BitGame game = Preset.CHECKMATE_PRACTICE_QUEEN.getBitGame();

        game.applyMovesAndFinalizeTurn(List.of(
                Move.of(game, 0, 0, 0, 0, 0, 0)
        ));

        game.applyMovesAndFinalizeTurn(List.of(
                Move.of(game, 0, 1, 4, 5, 4, 5)
        ));

        game.applyMovesAndFinalizeTurn(List.of(
                Move.of(game, 0, 2, 0, 0, 0, 0, 0, 0)
        ));

        game.applyMovesAndFinalizeTurn(List.of(
                Move.of(game, 1, 1, 4, 5, 4, 5)
        ));

        int hash = game.hashCode();

        game.applyMovesAndFinalizeTurn(List.of(
                Move.of(game, 1, 2, 0, 0, 0, 0, 0, 0)
        ));

        game.applyMove(Move.of(game, 0, 3, 4, 5, 0, 1, 4, 5));

        game.undoAllMovesFromCurrentTurn();
        game.undoTurn();

        assertEquals(hash, game.hashCode());
    }

}