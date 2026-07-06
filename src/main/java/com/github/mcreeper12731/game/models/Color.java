package com.github.mcreeper12731.game.models;

public enum Color {
    WHITE,
    BLACK;

    public Color other() {
        return this == Color.WHITE ? Color.BLACK : Color.WHITE;
    }
}
