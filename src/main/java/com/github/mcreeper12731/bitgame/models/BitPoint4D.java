package com.github.mcreeper12731.bitgame.models;

/**
 * Currently not used as performance gained with this is negligible
 */
public class BitPoint4D {

    /**
     * Packs 4 coordinates in one long: SLLL_LLLL_LLLL_LLLL_LLLL_LLLL_LLLL |36 TTTT_TTTT_TTTT_TTTT_TTTT_TTTT_TTTT |8 XXXX |4 YYYY |0,
     * where L are bits representing the timeline coordinate, T are bits representing the time coordinate,
     * X are bits representing the x coordinate, and Y are bits representing the y coordinate. S is the sign of the timeline coordinate, where 0 is positive and 1 is negative
     * @param l
     * @param t
     * @param x
     * @param y
     * @return
     */
    public static long of(int l, int t, int x, int y) {
        return ((((long) (l >= 0 ? l : -l) & 0x7FFFFFF | (l >= 0 ? 0 : 1L << 27)) << 36) | // keep only the first 27 bits and use the 28th one as a sign flag
                ((long) t << 8) |
                ((long) x << 4) |
                ((long) y));
    }

    public static int l(long point4D) {
        return (int) ((point4D >> 36) & 0x7FFFFFF) * (point4D >= 0 ? 1 : -1);
    }

    public static int t(long point4D) {
        return (int) ((point4D >> 8) & 0xFFFFFFF);
    }

    public static int x(long point4D) {
        return (int) ((point4D >> 4) & 0xF);
    }

    public static int y(long point4D) {
        return (int) (point4D & 0xF);
    }
}