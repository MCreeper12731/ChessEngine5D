package com.github.mcreeper12731.game;

import com.github.mcreeper12731.game.models.*;
import com.github.mcreeper12731.game.models.Move;
import com.github.mcreeper12731.game.models.pieces.PieceType;
import com.github.mcreeper12731.game.presets.Preset;
import com.github.mcreeper12731.utility.Log;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class GameApiTest {

    private static Game presetGame() {
        Game game = new Game(
                new Multiverse.Builder(4)
                        .withTimeline(new Timeline.Builder(0)
                                .withBoard(new Board.Builder(4, 0, 0)
                                        .withWhitePiece(PieceType.QUEEN, 0, 0)
                                        .withBlackPiece(PieceType.QUEEN, 3, 3)
                                        .build()
                                ).build()
                        ).build()
        );

        game.applyMovesAndFinalizeTurn(List.of(
                new Move.Builder(game)
                        .withFrom(0, 0, 0,0)
                        .withTo(0, 0, 0, 1)
                        .build()
        ));

        game.applyMovesAndFinalizeTurn(List.of(
                new Move.Builder(game)
                        .withFrom(0, 1, 3, 3)
                        .withTo(0, 1, 3, 2)
                        .build()
        ));

        game.applyMovesAndFinalizeTurn(List.of(
                new Move.Builder(game)
                        .withFrom(0, 2, 0, 1)
                        .withTo(0, 0, 0, 1)
                        .build()
        ));

        game.applyMovesAndFinalizeTurn(List.of(
                new Move.Builder(game)
                        .withFrom(1, 1, 3, 3)
                        .withTo(1, 1, 3, 2)
                        .build()
        ));

        game.applyMovesAndFinalizeTurn(List.of(
                new Move.Builder(game)
                        .withFrom(1, 2, 0, 1)
                        .withTo(0, 0, 0, 1)
                        .build()
        ));

        return game;
    }

    @Test
    public void presetGameCorrect() {
        Game game = presetGame();
        Multiverse multiverse = game.getMultiverse();

        assertEquals(3, multiverse.getTimelineLs().size());
        assertEquals(2, multiverse.getActiveTimelineLs().size());
    }


    @Test
    public void moveAddAndActivateTimeline() {
        Game game = presetGame();
        Multiverse multiverse = game.getMultiverse();

        Move move = new Move.Builder(game)
                .withFrom(0, 3, 3, 2)
                .withTo(0, 1, 3, 1)
                .build();

        assertTrue(game.doesMoveAddTimeline(move));
        assertNotEquals(0, game.doesMoveActivateTimeline(move));

        game.applyMove(move);

        assertEquals(4, multiverse.getTimelineLs().size());
        assertEquals(4, multiverse.getActiveTimelineLs().size());
    }

    @Test
    public void mandatoryTimelineLsOnPresentShiftDueToTimelineActivation() {

        Game game = Preset.CUSTOM_COMPLEX_POSITION.getGame();

        game.applyMove(
                new Move.Builder(game)
                        .withFrom(0, 4, 4, 1)
                        .withTo(0, 2, 2, 1)
                        .build()
        );

        assertEquals(1, game.getMandatoryTimelineLs().size());
        assertEquals(-2, game.getMandatoryTimelineLs().getFirst());

        game.applyMove(
                new Move.Builder(game)
                        .withFrom(-2, 2, 0, 1)
                        .withTo(-2, 2, 0, 0)
                        .build()
        );

        assertEquals(0, game.getMandatoryTimelineLs().size());
    }

    @Test
    public void movedPieceDestinationCorrect() {

        Game game = Preset.CUSTOM_COMPLEX_POSITION.getGame();

        List<Move> moves = game.getMultiverse().getLocationContents(0, 4, 4, 1).getAvailableMoves(game.getMultiverse(), new Point4D(0, 4, 4, 1));

        for (Move move : moves) {
            Point4D destination = game.getMovedPieceDestination(move);

            Log.debug("Test", "Applied move", move);
            Log.debug("Test", "Piece ended up at", destination);

            game.applyMove(move);

            assertNotNull(game.getMultiverse().getLocationContents(destination));
            assertEquals(
                    PieceType.KNIGHT,
                    game.getMultiverse().getLocationContents(destination).type()
            );
            assertEquals(
                    Color.WHITE,
                    game.getMultiverse().getLocationContents(destination).color()
            );

            game.undoMoveFromCurrentTurn();
        }
    }

    @Test
    public void playedTurnsCorrectOrder() {

        Game game = Preset.CHECKMATE_PRACTICE_QUEEN.getGame();

        game.applyMovesAndFinalizeTurn(List.of(
                new Move.Builder(game)
                        .withFrom(0, 0, 0, 0)
                        .withTo(0, 0, 1, 1)
                        .build()
        ));

        game.applyMovesAndFinalizeTurn(List.of(
                new Move.Builder(game)
                        .withFrom(0, 1, 4, 5)
                        .withTo(0, 1, 4, 4)
                        .build()
        ));

        game.applyMovesAndFinalizeTurn(List.of(
                new Move.Builder(game)
                        .withFrom(0, 2, 1, 1)
                        .withTo(0, 2, 0, 0)
                        .build()
        ));

        game.clearTurnHistory();

        game.applyMovesAndFinalizeTurn(List.of(
                new Move.Builder(game)
                        .withFrom(0, 3, 4, 4)
                        .withTo(0, 3, 4, 5)
                        .build()
        ));

        game.applyMove(
                new Move.Builder(game)
                        .withFrom(0, 4, 0, 0)
                        .withTo(0, 4, 1, 1)
                        .build()
        );

        List<List<Move>> turns = game.getTurns();

        Log.debug("Test", turns);

        assertEquals(Color.WHITE, turns.getFirst().getFirst().color());
        assertEquals(Color.BLACK, turns.get(1).getFirst().color());
        assertEquals(Color.WHITE, turns.get(2).getFirst().color());
        assertEquals(Color.BLACK, turns.get(3).getFirst().color());
        assertEquals(Color.WHITE, turns.get(4).getFirst().color());
    }
}
