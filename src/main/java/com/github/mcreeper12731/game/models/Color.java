package com.github.mcreeper12731.game.models;

public enum Color {
    BLACK,
    WHITE;

    public Color other() {
        return this == Color.WHITE ? Color.BLACK : Color.WHITE;
    }
}
