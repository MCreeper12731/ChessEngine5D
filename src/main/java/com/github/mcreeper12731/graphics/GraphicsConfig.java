package com.github.mcreeper12731.graphics;

import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;

import java.util.function.Function;

public class GraphicsConfig {

    public static final int TILE_SIZE = 40;
    public static final int TIMELINE_SPACING = 50;
    public static final int BOARD_PADDING = TIMELINE_SPACING;
    public static final int TIME_SPACING = 150;
    public static final int CENTER_OFFSET = 5000;

    public static class Color {

        public static javafx.scene.paint.Color fromPlayerColor(com.github.mcreeper12731.game.models.Color playerColor) {

            return switch (playerColor) {
                case BLACK -> javafx.scene.paint.Color.BLACK;
                case WHITE -> javafx.scene.paint.Color.LIGHTGRAY;
                case null -> javafx.scene.paint.Color.TRANSPARENT;
            };
        }

        public static final javafx.scene.paint.Color PRESENT = javafx.scene.paint.Color.LIGHTBLUE;

        public static final javafx.scene.paint.Color TIMELINE_ACTIVE = javafx.scene.paint.Color.YELLOWGREEN;
        public static final javafx.scene.paint.Color TIMELINE_INACTIVE = javafx.scene.paint.Color.LIGHTGRAY;
        public static final javafx.scene.paint.Color TILE_LIGHT = javafx.scene.paint.Color.WHITE.darker();
        public static final javafx.scene.paint.Color TILE_DARK = javafx.scene.paint.Color.GRAY;
        public static final javafx.scene.paint.Color TILE_HIGHLIGHTED = javafx.scene.paint.Color.rgb(0, 127, 0, 0.5);
        public static final javafx.scene.paint.Color TILE_CONTAINS_MOVE = javafx.scene.paint.Color.rgb(0, 47, 127, 0.3);
        public static final javafx.scene.paint.Color TILE_CONTAINS_JUMP_MOVE = javafx.scene.paint.Color.rgb(127, 0, 127, 0.3);

    }
}
