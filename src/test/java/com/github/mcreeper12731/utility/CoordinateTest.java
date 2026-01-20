package com.github.mcreeper12731.utility;

import com.github.mcreeper12731.game.models.Color;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CoordinateTest {
    @Test
    void timeFromGameToListTest() {

        assertEquals(0, Coordinate.timeFromGameToList(Color.WHITE, 1));
        assertEquals(1, Coordinate.timeFromGameToList(Color.BLACK, 1));
        assertEquals(4, Coordinate.timeFromGameToList(Color.WHITE, 3));
        assertEquals(5, Coordinate.timeFromGameToList(Color.BLACK, 3));

    }

    @Test
    void timeFromListToGameTest() {

        assertEquals(1, Coordinate.timeFromListToGame(0));
        assertEquals(1, Coordinate.timeFromListToGame(1));
        assertEquals(3, Coordinate.timeFromListToGame(4));
        assertEquals(3, Coordinate.timeFromListToGame(5));

    }
}