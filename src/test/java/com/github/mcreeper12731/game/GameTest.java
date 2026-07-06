package com.github.mcreeper12731.game;

import com.github.mcreeper12731.game.models.Color;
import com.github.mcreeper12731.game.models.Point4D;
import com.github.mcreeper12731.game.models.Move;
import com.github.mcreeper12731.game.movegeneration.MoveGenerator;
import com.github.mcreeper12731.game.presets.Preset;
import org.junit.jupiter.api.Test;

import java.util.Iterator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GameTest {

    @Test
    public void undoTurnTestSimple() {

        Game game = Preset.CUSTOM_TEST_CASTLING.getGame();

        Move move = new Move.Builder(game.getMultiverse())
                .withFrom(new Point4D(0, 0, 0, 0))
                .withTo(new Point4D(0, 0, 0, 1))
                .build();

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

            game.applyMovesFromTurnStart(turn);
            game.undoTurn();

            assertEquals(multiverseHashCodeBefore, game.getMultiverse().hashCode());
            assertEquals(gameHashCodeBefore, game.hashCode());
        }

    }

    @Test
    public void applyMovesAndFinalizeTurnTest1() {

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

        game.applyMovesFromTurnStart(List.of(
                new Move.Builder(game.getMultiverse())
                        .withFrom(-1, 4, 0, 1)
                        .withTo(-1, 4, 0, 2)
                        .build(),
                new Move.Builder(game.getMultiverse())
                        .withFrom(0, 4, 0, 1)
                        .withTo(0, 2, 1, 1)
                        .build()
        ));

        assertEquals(2, game.getPresentTime());
        assertFalse(game.isCurrentTurnFinalizable());
    }

    @Test
    public void applyMovesAndDontFinalizeAndUndo() {

        Game game = Preset.JUST_QUEENS.getGame();

        int hashCode = game.hashCode();

        game.applyMove(
                new Move.Builder()
                        .withNoop(0, 0)
                        .build()
        );

        game.applyMove(
                new Move.Builder()
                        .withNoop(0, 1)
                        .build()
        );

        game.undoTurn();

        assertEquals(hashCode, game.hashCode());
    }

    @Test
    public void applyMoveAndDontFinalizeAndUndoComplex() {

        Game game = Preset.CUSTOM_COMPLEX_POSITION.getGame();

        int hashCode = game.hashCode();

        game.applyMove(
                new Move.Builder(game)
                        .withFrom(0, 4, 4, 1)
                        .withTo(0, 0, 3, 1)
                        .build()
        );

        game.applyMove(
                new Move.Builder()
                        .withNoop(1, 1)
                        .build()
        );

        game.undoTurn();

        assertEquals(hashCode, game.hashCode());
    }

    @Test
    public void isGameOverWhenKingCaptured() {

        Game game = Preset.PUZZLE_QUEEN_1.getGame();

        game.applyMovesAndFinalizeTurn(List.of(
                new Move.Builder(game)
                        .withPieceMinimal(game.getMultiverse().getLocationContents(0, 2, 2, 0))
                        .withFrom(0, 2, 2, 0)
                        .withTo(0, 2, 2, 1)
                        .build()
        ));

        game.applyMovesAndFinalizeTurn(List.of(
                new Move.Builder(game)
                        .withPieceMinimal(game.getMultiverse().getLocationContents(0, 3, 3, 2))
                        .withFrom(0, 3, 3, 2)
                        .withTo(0, 3, 3, 3)
                        .build()
        ));

        game.applyMovesAndFinalizeTurn(List.of(
                new Move.Builder(game)
                        .withPieceMinimal(game.getMultiverse().getLocationContents(0, 4, 2, 1))
                        .withFrom(0, 4, 2, 1)
                        .withTo(0, 2, 3, 2)
                        .build()
        ));

        assertTrue(game.isGameOver());
        assertEquals(Color.WHITE, game.getWinner());
    }
}