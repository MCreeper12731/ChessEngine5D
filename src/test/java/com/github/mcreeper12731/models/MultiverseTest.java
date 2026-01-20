package com.github.mcreeper12731.models;

import com.github.mcreeper12731.game.models.Color;
import com.github.mcreeper12731.game.models.Multiverse;
import com.github.mcreeper12731.game.models.Timeline;
import com.github.mcreeper12731.game.pieces.PieceType;
import com.github.mcreeper12731.game.moves.Move;
import com.github.mcreeper12731.game.presets.Preset;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

class MultiverseTest {
    @Test
    void oddTimelineInitializationTest() {

        Multiverse multiverse = new Multiverse.Builder(0)
                .withTimeline(new Timeline.Builder(-2).build())
                .withTimeline(new Timeline.Builder(-1).build())
                .withTimeline(new Timeline.Builder(0).build())
                .withTimeline(new Timeline.Builder(1).build())
                .withTimeline(new Timeline.Builder(2).build())
                .build();

        for (int i = -2; i < 3; i++) {
            Assertions.assertEquals(i, multiverse.getTimeline(i).getId());
        }

        List<Double> indices = multiverse.getTimelineIndices();
        Assertions.assertEquals(5, indices.size());

        for (double i : indices) {
            Assertions.assertEquals(i, multiverse.getTimeline(i).getId());
        }

        Assertions.assertEquals(-2, multiverse.getBottomTimeline());
        Assertions.assertEquals(2, multiverse.getTopTimeline());
    }

    @Test
    void oddTimelineInitializationTest2() {

        Multiverse multiverse = new Multiverse.Builder(0)
                .withTimeline(new Timeline.Builder(0).build())
                .build();

        List<Double> indices = multiverse.getTimelineIndices();
        Assertions.assertEquals(1, indices.size());


        for (double i : indices) {
            Assertions.assertEquals(i, multiverse.getTimeline(i).getId());
        }

        Assertions.assertEquals(0, multiverse.getBottomTimeline());
        Assertions.assertEquals(0, multiverse.getTopTimeline());
    }

    @Test
    void evenTimelineInitializationTest() {

        Multiverse multiverse = new Multiverse.Builder(0)
                .withTimeline(new Timeline.Builder(-1).build())
                .withTimeline(new Timeline.Builder(-0.5).build())
                .withTimeline(new Timeline.Builder(0.5).build())
                .withTimeline(new Timeline.Builder(1).build())
                .build();

        List<Double> indices = multiverse.getTimelineIndices();
        Assertions.assertEquals(4, indices.size());

        for (double i : indices) {
            Assertions.assertEquals(i, multiverse.getTimeline(i).getId());
        }

        Assertions.assertEquals(-1, multiverse.getBottomTimeline());
        Assertions.assertEquals(1, multiverse.getTopTimeline());
    }

    @Test
    void evenTimelineInitializationTest2() {

        Multiverse multiverse = new Multiverse.Builder(0)
                .withTimeline(new Timeline.Builder(-0.5).build())
                .withTimeline(new Timeline.Builder(0.5).build())
                .build();

        List<Double> indices = multiverse.getTimelineIndices();
        Assertions.assertEquals(2, indices.size());

        for (double i : indices) {
            Assertions.assertEquals(i, multiverse.getTimeline(i).getId());
        }

        Assertions.assertEquals(-0.5, multiverse.getBottomTimeline());
        Assertions.assertEquals(0.5, multiverse.getTopTimeline());
    }

    /*@Test
    void timelineSplittingMoveTest() {

        Multiverse multiverse = Preset.TEST1.getMultiverse();

        multiverse.applyMove(new Move(
                Color.WHITE, PieceType.KING,
                0, 0, 0, 0,
                0, 0, 1, 0
        ));

        multiverse.applyMove(new Move(
                Color.BLACK, PieceType.KING,
                0, 1, 1, 1,
                0, 1, 0, 1
        ));

        multiverse.applyMove(new Move(
                Color.WHITE, PieceType.KING,
                0, 2, 1, 0,
                0, 0, 0, 0
        ));

        Assertions.assertEquals(1, multiverse.getTopTimeline());
        Assertions.assertEquals(0, multiverse.getBottomTimeline());

        Timeline timeline0 = multiverse.getTimeline(0);
        Timeline timeline1 = multiverse.getTimeline(1);

        Assertions.assertEquals(4, timeline0.size());
        Assertions.assertEquals(1, timeline1.size());

        Assertions.assertNull(timeline0.getLastBoard().getPiece(0, 0));
        Assertions.assertNull(timeline0.getLastBoard().getPiece(1, 0));
        Assertions.assertEquals(PieceType.KING, timeline1.getLastBoard().getPiece(0, 0).type());

        Assertions.assertEquals(1, timeline1.getFirstTimeCoordinate());

        multiverse.applyMove(new Move(
                Color.BLACK, PieceType.KING,
                0, 3, 0, 1,
                0, 1, 1, 1
        ));

        Assertions.assertEquals(1, multiverse.getTopTimeline());
        Assertions.assertEquals(-1, multiverse.getBottomTimeline());

        Timeline timeline1n = multiverse.getTimeline(-1);

        Assertions.assertEquals(5, timeline0.size());
        Assertions.assertEquals(1, timeline1n.size());

        Assertions.assertNull(timeline0.getLastBoard().getPiece(1, 1));
        Assertions.assertNull(timeline0.getLastBoard().getPiece(0, 1));
        Assertions.assertEquals(PieceType.KING, timeline1n.getLastBoard().getPiece(1, 1).type());

        Assertions.assertEquals(2, timeline1n.getFirstTimeCoordinate());
    }

    @Test
    void timelineSplittingMoveTest2() {

        Multiverse multiverse = Preset.TEST1.getMultiverse();

        multiverse.applyMove(new Move(
                Color.WHITE, PieceType.KING,
                0, 0, 0, 0,
                0, 0, 1, 0
        ));

        multiverse.applyMove(new Move(
                Color.BLACK, PieceType.KING,
                0, 1, 1, 1,
                0, 1, 0, 1
        ));

        multiverse.applyMove(new Move(
                Color.WHITE, PieceType.KING,
                0, 2, 1, 0,
                0, 0, 0, 0
        ));

        multiverse.applyMove(new Move(
                Color.BLACK, PieceType.KING,
                1, 1, 1, 1,
                1, 1, 0, 1
        ));

        Timeline timeline1 = multiverse.getTimeline(1);

        Assertions.assertNull(timeline1.getLastBoard().getPiece(1, 1));
        Assertions.assertEquals(2, timeline1.size());
    }

    @Test
    void nonSplittingMoveTest() {

        Multiverse multiverse = Preset.TEST1.getMultiverse();

        multiverse.applyMove(new Move(
                Color.WHITE, PieceType.KING,
                0, 0, 0, 0,
                0, 0, 1, 0
        ));

        multiverse.applyMove(new Move(
                Color.BLACK, PieceType.KING,
                0, 1, 1, 1,
                0, 1, 0, 1
        ));

        multiverse.applyMove(new Move(
                Color.WHITE, PieceType.KING,
                0, 2, 1, 0,
                0, 0, 0, 0
        ));

        multiverse.applyMove(new Move(
                Color.BLACK, PieceType.KING,
                1, 1, 1, 1,
                0, 3, 0, 1
        ));

        Timeline timeline0 = multiverse.getTimeline(0);
        Timeline timeline1 = multiverse.getTimeline(1);

        Assertions.assertEquals(5, timeline0.size());
        Assertions.assertEquals(2, timeline1.size());

        Assertions.assertNull(timeline1.getLastBoard().getPiece(1, 1));
        Assertions.assertEquals(PieceType.KING, timeline0.getLastBoard().getPiece(0, 1).type());

    }

    @Test
    void activeTimelinesTest() {

        Multiverse multiverse = Preset.TEST1.getMultiverse();

        multiverse.applyMove(new Move(
                Color.WHITE, PieceType.KING,
                0, 0, 0, 0,
                0, 0, 1, 0
        ));

        multiverse.applyMove(new Move(
                Color.BLACK, PieceType.KING,
                0, 1, 1, 1,
                0, 1, 0, 1
        ));

        multiverse.applyMove(new Move(
                Color.WHITE, PieceType.KING,
                0, 2, 1, 0,
                0, 0, 0, 0
        ));

        multiverse.applyMove(new Move(
                Color.BLACK, PieceType.KING,
                1, 1, 1, 1,
                1, 1, 0, 1
        ));

        multiverse.applyMove(new Move(
                Color.WHITE, PieceType.KING,
                1, 2, 0, 0,
                0, 0, 0, 0
        ));

        Timeline timeline0 = multiverse.getTimeline(0);
        Timeline timeline1 = multiverse.getTimeline(1);
        Timeline timeline2 = multiverse.getTimeline(2);

        Assertions.assertTrue(timeline0.isActive());
        Assertions.assertTrue(timeline1.isActive());

        Assertions.assertFalse(timeline2.isActive());

        Assertions.assertEquals(3, multiverse.getPresentTime());
    }*/
}