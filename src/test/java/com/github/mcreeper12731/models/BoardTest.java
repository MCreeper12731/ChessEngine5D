package com.github.mcreeper12731.models;

import com.github.mcreeper12731.game.models.Board;
import com.github.mcreeper12731.game.models.Color;
import com.github.mcreeper12731.game.models.Multiverse;
import com.github.mcreeper12731.game.models.Timeline;
import com.github.mcreeper12731.game.pieces.PieceType;
import com.github.mcreeper12731.game.moves.Move;
import com.github.mcreeper12731.game.presets.Preset;
import com.github.mcreeper12731.utility.Coordinate;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BoardTest {

    /*@Test
    void boardIndexTest() {

        Multiverse multiverse = Preset.JUST_QUEENS.getMultiverse();

        multiverse.applyAndFinalizeTurn(new Move(
                Color.WHITE, PieceType.QUEEN,
                0, 0, 4, 0,
                0, 0, 3, 0
        ));

        multiverse.applyAndFinalizeTurn(new Move(
                Color.WHITE, PieceType.QUEEN,
                0, 0, 3, 0,
                0, 0, 4, 0
        ));

        multiverse.applyAndFinalizeTurn(new Move(
                Color.WHITE, PieceType.QUEEN,
                0, 0, 4, 0,
                0, 0, 3, 0
        ));

        multiverse.applyAndFinalizeTurn(new Move(
                Color.WHITE, PieceType.QUEEN,
                0, 0, 3, 0,
                0, 0, 4, 0
        ));

        multiverse.applyAndFinalizeTurn(new Move(
                Color.WHITE, PieceType.QUEEN,
                0, 0, 4, 0,
                0, 0, 3, 0
        ));

        multiverse.applyAndFinalizeTurn(new Move(
                Color.WHITE, PieceType.QUEEN,
                0, 0, 3, 0,
                0, 0, 4, 0
        ));

        Timeline timeline = multiverse.getTimeline(0);

        for (int time = 0; time < timeline.size(); time++) {

            Board board = timeline.getBoardByIndex(time);

            assertEquals(time, board.getTime());
            assertEquals(Coordinate.colorFromListToGame(time), board.getPlayerTurn());
        }
    }*/
}