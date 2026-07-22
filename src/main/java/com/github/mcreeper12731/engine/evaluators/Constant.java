package com.github.mcreeper12731.engine.evaluators;

import com.github.mcreeper12731.game.models.pieces.PieceType;

public class Constant {

    public static final int JUMP_COST              = -2;
    public static final int JUMP_INACTIVE_COST     = -50;
    public static final int TAKE_ENEMY_REWARD      = 20;
    public static final int KING_DANGER_COST       = -100_000;

    public static final int ROOK_DANGER_COST       = -3;
    public static final int KNIGHT_DANGER_COST     = -4;
    public static final int BISHOP_DANGER_COST     = -5;
    public static final int QUEEN_DANGER_COST      = -10;
    public static final int UNICORN_DANGER_COST    = -2;
    public static final int DRAGON_DANGER_COST     = -2;
    public static final int PRINCESS_DANGER_COST   = -8;
    public static final int BRAWN_DANGER_COST      = -2;
    public static final int PAWN_DANGER_COST      = -1;

    public static final int TAKE_ROOK_REWARD       = 3;
    public static final int TAKE_KNIGHT_REWARD     = 4;
    public static final int TAKE_BISHOP_REWARD     = 5;
    public static final int TAKE_PRINCESS_REWARD   = 8;
    public static final int TAKE_QUEEN_REWARD      = 10;
    public static final int TAKE_UNICORN_REWARD    = 2;
    public static final int TAKE_DRAGON_REWARD     = 2;
    public static final int TAKE_PAWN_REWARD      = 1;
    public static final int TAKE_BRAWN_REWARD     = 1;

    public static final int MANY_KINGS_COST        = -6;

    public static class PieceValue {

        public static final int KING = 100_000;
        public static final int QUEEN = 900;
        public static final int ROOK = 500;
        public static final int BISHOP = 300;
        public static final int KNIGHT = 300;
        public static final int PAWN = 100;
        public static final int UNICORN = 200;
        public static final int DRAGON = 50;
        public static final int PRINCESS = 800;
        public static final int BRAWN = 150;

        public static int fromType(PieceType pieceType) {
            return switch (pieceType) {
                case KING -> KING;
                case QUEEN -> QUEEN;
                case ROOK -> ROOK;
                case BISHOP -> BISHOP;
                case KNIGHT -> KNIGHT;
                case PAWN -> PAWN;
                case UNICORN -> UNICORN;
                case DRAGON -> DRAGON;
                case PRINCESS -> PRINCESS;
                case BRAWN -> BRAWN;

                default -> 0;
            };
        }

        public static int fromOrdinal(int ordinal) {
            return fromType(PieceType.of(ordinal));
        }
    }
}
