package com.github.mcreeper12731.examples.profilings;

import com.github.mcreeper12731.bitgame.models.BitPoint4D;
import com.github.mcreeper12731.game.models.Point4D;
import com.github.mcreeper12731.utility.Log;

public class Point4DComparison {

    public static void main(String[] args) {

        long count = 100_000_000_000L;

        long start = System.nanoTime();
        for (long i = 0; i < count; i++) {
            Point4D point = new Point4D(0, 0, 0, 0);
            int l = point.l();
            int t = point.t();
            int x = point.x();
            int y = point.y();
        }
        long result1 = System.nanoTime() - start;
        Log.debug("Point4DComparison", result1);

        start = System.nanoTime();
        for (long i = 0; i < count; i++) {
            long point = BitPoint4D.of(0, 0, 0, 0);
            int l = BitPoint4D.l(point);
            int t = BitPoint4D.t(point);
            int x = BitPoint4D.x(point);
            int y = BitPoint4D.y(point);
        }
        long result2 = System.nanoTime() - start;
        Log.debug("Point4DComparison", result2);
        Log.debug("Diff", result2 - result1);

    }

}
