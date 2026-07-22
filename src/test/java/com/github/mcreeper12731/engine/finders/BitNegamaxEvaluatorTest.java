package com.github.mcreeper12731.engine.finders;

import com.github.mcreeper12731.bitgame.BitGame;
import com.github.mcreeper12731.bitgame.models.BitBoard;
import com.github.mcreeper12731.bitgame.models.BitMultiverse;
import com.github.mcreeper12731.bitgame.models.BitTimeline;
import com.github.mcreeper12731.engine.evaluators.BitNegaMaxEvaluator;
import com.github.mcreeper12731.game.Game;
import com.github.mcreeper12731.game.models.Board;
import com.github.mcreeper12731.game.models.Move;
import com.github.mcreeper12731.game.models.pieces.PieceType;
import com.github.mcreeper12731.game.models.scored.ScoredTurn;
import com.github.mcreeper12731.game.presets.Preset;
import com.github.mcreeper12731.utility.Config;
import com.github.mcreeper12731.utility.Log;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.Hashtable;
import java.util.List;
import java.util.TreeMap;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.*;

public class BitNegamaxEvaluatorTest {

    @Test
    public void findsWinIn1Ply() {

        BitGame game = new BitGame(
                new BitMultiverse.Builder(3)
                        .withTimeline(
                                new BitTimeline.Builder(0)
                                        .withBoard(
                                                new BitBoard.Builder(3, 0, 0)
                                                        .withWhitePiece(PieceType.QUEEN, 0, 0)
                                                        .withWhitePiece(PieceType.KING, 1, 0)
                                                        .withBlackPiece(PieceType.KING, 2, 2)
                                                        .build()
                                        ).build()
                        ).build()
        );

        BitNegaMaxEvaluator negaMaxEvaluator = new BitNegaMaxEvaluator(
                new Config()
                        .with("max_depth", 1)
                        .with("evaluator", "static")
                        .with("debug_level", 0)
        );

        ScoredTurn turn = negaMaxEvaluator.findBestTurn(game);
//        Log.debug("Test", turn);
        assertEquals(1_000_000, turn.score());

    }

    @Test
    public void findsWinIn3Plies() {

        BitGame game = Preset.PUZZLE_ROOK_TACTICS_1.getBitGame();

        BitNegaMaxEvaluator negaMaxEvaluator = new BitNegaMaxEvaluator(
                new Config()
                        .with("max_depth", 3)
                        .with("evaluator", "static")
                        .with("debug_level", 0)
        );

        ScoredTurn turn = negaMaxEvaluator.findBestTurn(game);
//        Log.debug("Test", turn);
        assertEquals(1_000_000, turn.score());

    }

    @Test
    public void generatesValidTurn() {

        BitGame game = Preset.CHECKMATE_PRACTICE_QUEEN.getBitGame();

        game.applyMovesAndFinalizeTurn(List.of(
                Move.of(game, 0, 0, 0, 0, 1, 0)
        ));

        game.applyMovesAndFinalizeTurn(List.of(
                Move.of(game, 0, 1, 4, 5, 1, 5)
        ));

        game.applyMovesAndFinalizeTurn(List.of(
                Move.of(game, 0, 2, 1, 0, 0, 0, 1 ,0)
        ));

        BitNegaMaxEvaluator negaMaxEvaluator = new BitNegaMaxEvaluator(
                new Config()
                        .with("max_depth", 3)
                        .with("evaluator", "static")
                        .with("debug_level", 5)
        );

        new Thread(() -> {
            while (true) {
                try {
                    Field field = game.getClass().getDeclaredField("lastTimesOfTimelines"); //NoSuchFieldException
                    field.setAccessible(true);
                    TreeMap<Integer, Integer> iWantThis = (TreeMap<Integer, Integer>) field.get(game);
                    Log.debug("Test", iWantThis.toString());
                    Thread.sleep(10);
                } catch (Exception e) {
                    Log.debug("Test", e);
                }
            }
        }).start();

        ScoredTurn turn = negaMaxEvaluator.findBestTurn(game);

    }

    @Test
    public void generatesValidTurnWhenActivatingTimeline() {

        BitGame game = Preset.CHECKMATE_PRACTICE_QUEEN.getBitGame();

        game.applyMovesAndFinalizeTurn(List.of(
                Move.of(game, 0, 0, 0, 0, 0, 0)
        ));

        game.applyMovesAndFinalizeTurn(List.of(
                Move.of(game, 0, 1, 4, 5, 4, 5)
        ));

        game.applyMovesAndFinalizeTurn(List.of(
                Move.of(game, 0, 2, 0, 0, 0, 0, 0, 0)
        ));

        game.applyMovesAndFinalizeTurn(List.of(
                Move.of(game, 1, 1, 4, 5, 4, 5)
        ));

        game.applyMovesAndFinalizeTurn(List.of(
                Move.of(game, 1, 2, 0, 0, 0, 0, 0, 0)
        ));

        game.applyMove(Move.of(game, 0, 3, 4, 5, 0, 1, 4, 5));

        game.undoAllMovesFromCurrentTurn();
        game.undoTurn();

        Log.debug("Test", game);
    }
}
