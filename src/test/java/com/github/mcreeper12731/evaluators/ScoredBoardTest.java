package com.github.mcreeper12731.evaluators;

import com.github.mcreeper12731.game.logic.Game;
import com.github.mcreeper12731.game.logic.MoveGenerator;
import com.github.mcreeper12731.game.models.Board;
import com.github.mcreeper12731.game.models.Multiverse;
import com.github.mcreeper12731.game.models.ScoredBoard;
import com.github.mcreeper12731.game.models.Timeline;
import com.github.mcreeper12731.game.pieces.PieceType;
import com.github.mcreeper12731.game.presets.Preset;
import com.github.mcreeper12731.utility.Log;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ScoredBoardTest {

    @Test
    public void scoredBoardWhenBoardEmpty() {

        Game game = new Game(
                new Multiverse.Builder(4)
                        .withTimeline(new Timeline.Builder(0)
                                .withBoard(new Board.Builder(4, 0, 0)
                                        .build()
                                ).build()
                        ).build()
        );

        ScoredBoard scoredBoard = new ScoredBoard(game.getMultiverse().getBoard(0, 0), game);

        assertEquals(16, scoredBoard.danger().size());
        for (int x : scoredBoard.danger()) {
            assertEquals(0, x);
        }
        assertEquals(0, scoredBoard.enemies().size());
    }

    @Test
    public void scoredBoardWhenArbitrary() {

        Game game = new Game(
                new Multiverse.Builder(4)
                        .withTimeline(new Timeline.Builder(0)
                                .withBoard(new Board.Builder(4, 0, 0)
                                        .withWhitePiece(PieceType.KING, 0, 0)
                                        .withWhitePiece(PieceType.ROOK, 3, 0)
                                        .withBlackPiece(PieceType.QUEEN, 3, 3)
                                        .withBlackPiece(PieceType.QUEEN, 2, 3)
                                        .withBlackPiece(PieceType.KNIGHT, 1, 2)
                                        .build()
                                ).build()
                        ).build()
        );

        ScoredBoard scoredBoard = new ScoredBoard(game.getMultiverse().getBoard(0, 0), game);


    }

    @Test
    public void scoredMovesWhenSimpleSetup() {

        Game game = new Game(
                new Multiverse.Builder(4)
                        .withTimeline(new Timeline.Builder(0)
                                .withBoard(new Board.Builder(4, 0, 0)
                                        .withWhitePiece(PieceType.QUEEN, 0, 0)
                                        .withBlackPiece(PieceType.KING, 3, 3)
                                        .build()
                                ).build()
                        ).build()
        );

        Board board = game.getMultiverse().getBoard(0, 0);
        ScoredBoard scoredBoard = new ScoredBoard(board, game);

        List<ScoredMove> scoredMoves = scoredBoard.scoreMoves(MoveGenerator.probableMoves(board, game), game);

        Log.debug("Test", scoredMoves.getFirst());
    }

    @Test
    public void scoredMovesWhenMateInOne() {

        Game game = Preset.PUZZLE_QUEEN_1.getGame();

        Board board = game.getMultiverse().getBoard(0, 2);
        ScoredBoard scoredBoard = new ScoredBoard(board, game);

        List<ScoredMove> scoredMoves = scoredBoard.scoreMoves(MoveGenerator.probableMoves(board, game), game);

        Log.debug("Test", scoredMoves);
    }

}