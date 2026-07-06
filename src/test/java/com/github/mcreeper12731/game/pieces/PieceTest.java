package com.github.mcreeper12731.game.pieces;

import com.github.mcreeper12731.game.Game;
import com.github.mcreeper12731.game.models.Color;
import com.github.mcreeper12731.game.models.Point4D;
import com.github.mcreeper12731.game.models.Move;
import com.github.mcreeper12731.game.presets.Preset;
import com.github.mcreeper12731.utility.Log;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PieceTest {

    @Test
    public void movesWhenPieceDoesNotExist() {

        Game game = Preset.CUSTOM_COMPLEX_POSITION.getGame();

        Move move = new Move.Builder(game)
                .withFrom(new Point4D(0, 4, 4, 1))
                .withTo(new Point4D(0, 0, 3, 1))
                .build();

        Piece movedPiece = game.getMultiverse().getLocationContents(0, 4, 4, 1);

        Point4D newPieceLocation = game.getMovedPieceDestination(move);
        Piece artificalPiece = new Piece(
                movedPiece.color(),
                movedPiece.type(),
                newPieceLocation.add(0, 1, 0, 0),
                false
        );

        List<Move> artificialMoves = artificalPiece.getAvailableMoves(game.getMultiverse());

        game.applyMove(move);
        game.finalizeTurn();

        game.applyMove(
                new Move.Builder()
                        .withNoop(1, 1)
                        .build()
        );
        game.finalizeTurn();

        List<Move> actualMoves = game.getMultiverse().getLocationContents(1, 2, 3, 1).getAvailableMoves(game.getMultiverse());
        actualMoves = actualMoves.stream().filter(m -> m.from().l() != m.to().l() || m.from().t() != m.to().t()).toList();
        assertEquals(artificialMoves, actualMoves);
    }
}