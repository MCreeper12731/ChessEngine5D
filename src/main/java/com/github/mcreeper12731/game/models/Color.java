package com.github.mcreeper12731.game.models;

public enum Color {
    BLACK,
    WHITE;

    public Color other() {
        return this == Color.WHITE ? Color.BLACK : Color.WHITE;
    }

    public static long toNumber(Color color) {
        return switch (color) {
            case WHITE -> 0;
            case BLACK -> 1;
            case null -> 3;
        };
    }

    public static Color fromNumber(int number) {
        return switch (number) {
            case 0 -> WHITE;
            case 1 -> BLACK;
            case 3 -> null;
            default -> throw new IllegalArgumentException("Invalid color number: " + number);
        };
    }
}
