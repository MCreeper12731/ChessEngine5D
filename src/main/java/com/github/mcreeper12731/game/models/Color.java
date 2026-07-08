package com.github.mcreeper12731.game.models;

public enum Color {
    WHITE,
    BLACK;

    public Color other() {
        return this == Color.WHITE ? Color.BLACK : Color.WHITE;
    }

    public static Color of(int ordinal) {
        return switch (ordinal) {
            case -1 -> null;
            case 0 -> WHITE;
            case 1 -> BLACK;
            default -> throw new IllegalArgumentException("Invalid color ordinal " + ordinal);
        };
    }
}
