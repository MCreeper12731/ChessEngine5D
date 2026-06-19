package com.github.mcreeper12731.game.models;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MultiverseTest {

    @Test
    public void multiverseConstructionTest1() {

        Multiverse multiverse = new Multiverse.Builder(0)
                .withTimeline(
                        new Timeline.Builder(-2).build()
                )
                .withTimeline(
                        new Timeline.Builder(-1).build()
                )
                .withTimeline(
                        new Timeline.Builder(0).build()
                )
                .withTimeline(
                        new Timeline.Builder(1).build()
                )
                .build();

        List<Integer> timelineLs = multiverse.getTimelineLs();
        assertEquals(List.of(-2, -1, 0, 1), timelineLs);

        List<Timeline> timelines = multiverse.getTimelines();

        assertEquals(4, timelines.size());
        assertFalse(multiverse.isEven());
        for (int i = 0; i < timelines.size(); i++) {
            assertEquals(i - 2, timelines.get(i).getL());
        }
    }

    @Test
    public void multiverseConstructionTest2() {

        Multiverse multiverse = new Multiverse.Builder(0)
                .withTimeline(
                        new Timeline.Builder(0).build()
                )
                .withTimeline(
                        new Timeline.Builder(1).build()
                )
                .withTimeline(
                        new Timeline.Builder(2).build()
                )
                .withTimeline(
                        new Timeline.Builder(3).build()
                )
                .build();

        List<Timeline> timelines = multiverse.getTimelines();

        assertEquals(4, timelines.size());
        assertFalse(multiverse.isEven());
        for (int i = 0; i < timelines.size(); i++) {
            assertEquals(i, timelines.get(i).getL());
        }
    }

    @Test
    public void multiverseConstructionTest3() {

        assertThrows(IllegalArgumentException.class, () -> {
            new Multiverse.Builder(0)
                    .withTimeline(
                            new Timeline.Builder(1).build()
                    )
                    .withTimeline(
                            new Timeline.Builder(2).build()
                    )
                    .build();
        });
    }

    @Test
    public void multiverseConstructionTest4() {

        assertThrows(IllegalArgumentException.class, () -> {
            new Multiverse.Builder(0)
                    .withTimeline(
                            new Timeline.Builder(-2).build()
                    )
                    .withTimeline(
                            new Timeline.Builder(-3).build()
                    )
                    .build();
        });
    }

    @Test
    public void multiverseConstructionTest5() {

        assertThrows(IllegalArgumentException.class, () -> {
            new Multiverse.Builder(0)
                    .withTimeline(
                            new Timeline.Builder(0).build()
                    )
                    .withTimeline(
                            new Timeline.Builder(0).build()
                    )
                    .build();
        });
    }


    @Test
    public void activeTimelinesTest1() {

        Multiverse multiverse = new Multiverse.Builder(0)
                .withTimeline(
                        new Timeline.Builder(-2).build()
                )
                .withTimeline(
                        new Timeline.Builder(-1).build()
                )
                .withTimeline(
                        new Timeline.Builder(0).build()
                )
                .build();

        List<Integer> activeTimelineLs = multiverse.getActiveTimelineLs();
        assertEquals(List.of(-1, 0), activeTimelineLs);

        activeTimelineLs.clear();
        List<Integer> inactiveTimelineLs = new ArrayList<>();
        List<Integer> timelineLs = multiverse.getTimelineLs();

        for (int timelineL : timelineLs) {
            if (multiverse.isTimelineActive(timelineL)) {
                activeTimelineLs.add(timelineL);
            } else {
                inactiveTimelineLs.add(timelineL);
            }
        }

        assertEquals(List.of(-1, 0), activeTimelineLs);
        assertEquals(List.of(-2), inactiveTimelineLs);
    }

    @Test
    public void activeTimelinesTest2() {

        Multiverse multiverse = new Multiverse.Builder(0)
                .withTimeline(
                        new Timeline.Builder(-1).build()
                )
                .withTimeline(
                        new Timeline.Builder(0).build()
                )
                .withTimeline(
                        new Timeline.Builder(1).build()
                )
                .withTimeline(
                        new Timeline.Builder(2).build()
                )
                .withTimeline(
                        new Timeline.Builder(3).build()
                )
                .build();

        List<Integer> activeTimelineLs = multiverse.getActiveTimelineLs();
        assertEquals(List.of(-1, 0, 1, 2), activeTimelineLs);

        activeTimelineLs.clear();
        List<Integer> inactiveTimelineLs = new ArrayList<>();
        List<Integer> timelineLs = multiverse.getTimelineLs();

        for (int timelineL : timelineLs) {
            if (multiverse.isTimelineActive(timelineL)) {
                activeTimelineLs.add(timelineL);
            } else {
                inactiveTimelineLs.add(timelineL);
            }
        }

        assertEquals(List.of(-1, 0, 1, 2), activeTimelineLs);
        assertEquals(List.of(3), inactiveTimelineLs);
    }

    @Test
    public void activeTimelinesTest3() {

        Multiverse multiverse = new Multiverse.Builder(0)
                .withTimeline(
                        new Timeline.Builder(-3).build()
                )
                .withTimeline(
                        new Timeline.Builder(-2).build()
                )
                .withTimeline(
                        new Timeline.Builder(-1).build()
                )
                .withTimeline(
                        new Timeline.Builder(0).build()
                )
                .even()
                .build();

        List<Integer> activeTimelineLs = multiverse.getActiveTimelineLs();
        assertEquals(List.of(-2, -1, 0), activeTimelineLs);

        activeTimelineLs.clear();
        List<Integer> inactiveTimelineLs = new ArrayList<>();
        List<Integer> timelineLs = multiverse.getTimelineLs();

        for (int timelineL : timelineLs) {
            if (multiverse.isTimelineActive(timelineL)) {
                activeTimelineLs.add(timelineL);
            } else {
                inactiveTimelineLs.add(timelineL);
            }
        }

        assertEquals(List.of(-2, -1, 0), activeTimelineLs);
        assertEquals(List.of(-3), inactiveTimelineLs);
    }

    @Test
    public void activeTimelinesTest4() {

        Multiverse multiverse = new Multiverse.Builder(0)
                .withTimeline(
                        new Timeline.Builder(-1).build()
                )
                .withTimeline(
                        new Timeline.Builder(0).build()
                )
                .withTimeline(
                        new Timeline.Builder(1).build()
                )
                .withTimeline(
                        new Timeline.Builder(2).build()
                )
                .withTimeline(
                        new Timeline.Builder(3).build()
                )
                .even()
                .build();

        List<Integer> activeTimelineLs = multiverse.getActiveTimelineLs();
        assertEquals(List.of(-1, 0, 1), activeTimelineLs);

        activeTimelineLs.clear();
        List<Integer> inactiveTimelineLs = new ArrayList<>();
        List<Integer> timelineLs = multiverse.getTimelineLs();

        for (int timelineL : timelineLs) {
            if (multiverse.isTimelineActive(timelineL)) {
                activeTimelineLs.add(timelineL);
            } else {
                inactiveTimelineLs.add(timelineL);
            }
        }

        assertEquals(List.of(-1, 0, 1), activeTimelineLs);
        assertEquals(List.of(2, 3), inactiveTimelineLs);
    }

    @Test
    public void activeTimelinesTest5() {

        Multiverse multiverse = new Multiverse.Builder(0)
                .withTimeline(
                        new Timeline.Builder(-1).build()
                )
                .withTimeline(
                        new Timeline.Builder(0).build()
                )
                .withTimeline(
                        new Timeline.Builder(1).build()
                )
                .build();

        List<Integer> activeTimelineLs = multiverse.getActiveTimelineLs();
        assertEquals(List.of(-1, 0, 1), activeTimelineLs);

        activeTimelineLs.clear();
        List<Integer> inactiveTimelineLs = new ArrayList<>();
        List<Integer> timelineLs = multiverse.getTimelineLs();

        for (int timelineL : timelineLs) {
            if (multiverse.isTimelineActive(timelineL)) {
                activeTimelineLs.add(timelineL);
            } else {
                inactiveTimelineLs.add(timelineL);
            }
        }

        assertEquals(List.of(-1, 0, 1), activeTimelineLs);
        assertEquals(List.of(), inactiveTimelineLs);
    }
}