package com.github.mcreeper12731.utility;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ReducedListViewTest {

    @Test
    public void correctElementWhenIndexStartAndIndexEnd() {

        List<Integer> list = List.of(0, 1, 2, 3, 4, 5, 6, 7, 8, 9);

        ReducedListView<Integer> reducedList = new ReducedListView<>(list, 2, 7);
        /*      first                 out
        (0, 1, || 2, 3, 4, 5, 6 ||, 7, 8, 9)
          out                last
         */

        assertEquals(5, reducedList.size());
        assertThrows(IndexOutOfBoundsException.class, () -> reducedList.get(-2));
        assertEquals(2, reducedList.getFirst());
        assertEquals(6, reducedList.getLast());
        assertThrows(IndexOutOfBoundsException.class, () -> reducedList.get(5));
        reducedList.forEach(integer -> {
            assertNotEquals(0, integer);
            assertNotEquals(1, integer);
            assertNotEquals(7, integer);
            assertNotEquals(8, integer);
            assertNotEquals(9, integer);
        });
    }
}