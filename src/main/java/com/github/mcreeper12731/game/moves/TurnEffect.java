package com.github.mcreeper12731.game.moves;

import java.util.ArrayList;
import java.util.List;

public record TurnEffect(List<MoveEffect> moveEffects) {

    public TurnEffect(List<MoveEffect> moveEffects) {
        this.moveEffects = new ArrayList<>(moveEffects);
    }

    public Turn getTurn() {
        List<Move> moves = new ArrayList<>();
        moveEffects.forEach(moveEffect -> moves.add(moveEffect.getMove()));
        return new Turn(moves);
    }

    public MoveEffect getMoveEffect(int index) {
        return moveEffects.get(index);
    }

    public int size() {
        return moveEffects.size();
    }

}
