package com.github.mcreeper12731.game.exceptions;

public class CannotMoveFromTileException extends RuntimeException {
    public CannotMoveFromTileException() {
        super("Cannot move from this tile!");
    }
}
