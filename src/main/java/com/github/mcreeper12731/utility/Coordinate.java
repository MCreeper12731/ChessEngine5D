package com.github.mcreeper12731.utility;

import com.github.mcreeper12731.game.models.Color;

public class Coordinate {

    public static int timeFromGameToList(Color playerColor, int gameTimeCoordinate) {
        /*
         * List indices:
         *     0     1         2     3         4     5
         * +-------------+ +-------------+ +-------------+
         * | +---+ +---+ | | +---+ +---+ | | +---+ +---+ |
         * | | W | | B | | | | W | | B | | | | W | | B | |
         * | +---+ +---+ | | +---+ +---+ | | +---+ +---+ |
         * +-------------+ +-------------+ +-------------+
         *       T1              T2              T3
         * */
        return (gameTimeCoordinate - 1) * 2
                + (playerColor == Color.WHITE ? 0 : 1);
    }

    public static int timeFromListToGame(int listTimeIndex) {
        return listTimeIndex / 2 + 1;
    }

    public static Color colorFromListToGame(int listTimeIndex) {
        return listTimeIndex % 2 == 0 ? Color.WHITE : Color.BLACK;
    }

    public static int timelineFromIdToGame(double timelineId) {
        return timelineId == -0.5 || timelineId == 0.5 ? 0 : (int) timelineId;
    }

}
