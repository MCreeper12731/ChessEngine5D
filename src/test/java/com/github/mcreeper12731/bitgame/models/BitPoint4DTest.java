package com.github.mcreeper12731.bitgame.models;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BitPoint4DTest {

    private void assertCorrectPackingAndUnpacking(int l, int t, int x, int y) {
        long packed = BitPoint4D.of(l, t, x, y);
//        Log.debug("Test", packed, Long.toBinaryString(packed));
        assertEquals(l, BitPoint4D.l(packed));
        assertEquals(t, BitPoint4D.t(packed));
        assertEquals(x, BitPoint4D.x(packed));
        assertEquals(y, BitPoint4D.y(packed));
    }

    @Test
    public void packUnpackCorrect() {

        assertCorrectPackingAndUnpacking(0, 0, 0, 0);
        assertCorrectPackingAndUnpacking(10, 0, 0, 0);
        assertCorrectPackingAndUnpacking(-10, 0, 0, 0);
        assertCorrectPackingAndUnpacking(-10, 100, 5, 3);
    }

}