package com.github.mcreeper12731.engine.finders;

import com.github.mcreeper12731.MainApplication;
import com.github.mcreeper12731.engine.config.NegamaxStrategyConfig;
import com.github.mcreeper12731.engine.evaluators.EvaluatorImpl;
import com.github.mcreeper12731.game.Game;
import com.github.mcreeper12731.game.models.Move;
import com.github.mcreeper12731.game.models.scored.ScoredTurn;
import com.github.mcreeper12731.game.movegeneration.MoveGenerator;
import com.github.mcreeper12731.game.presets.Preset;
import com.github.mcreeper12731.utility.Log;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class NegaMaxStrategyTest {

    @Test
    public void puzzleRook2() {

        Game game = Preset.PUZZLE_ROOK_2.getGame();

        NegaMaxStrategy strategy = new NegaMaxStrategy(
                NegamaxStrategyConfig.fromConfig(),
                new EvaluatorImpl()
        );

        ScoredTurn result = strategy.findBestTurn(game);

        Log.print("Test", result);
        Log.debug("Test", "Max timelines", strategy.maxTimelinesReached);
        game.applyMovesAndFinalizeTurn(result.moves());
        //MainApplication.launchWithGame(game);
    }

    @Test
    public void puzzleKnight6() {

        Game game = Preset.PUZZLE_KNIGHT_6.getGame();

        /*game.applyMovesAndFinalizeTurn(List.of(
                new Move.Builder(game)
                        .withFrom(0, 0, 0, 0)
                        .withTo(0, 0, 0, 1)
                        .build()
        ));

        game.applyMovesAndFinalizeTurn(List.of(
                new Move.Builder(game)
                        .withFrom(0, 1, 4, 4)
                        .withTo(0, 1, 3, 3)
                        .build()
        ));

        /*game.applyMovesAndFinalizeTurn(List.of(
                new Move.Builder(game)
                        .withFrom(0, 2, 2, 0)
                        .withTo(0, 2, 4, 1)
                        .build()
        ));

        /*game.applyMovesAndFinalizeTurn(List.of(
                new Move.Builder(game)
                        .withFrom(0, 3, 3, 3)
                        .withTo(0, 3, 3, 4)
                        .build()
        ));*/

        NegamaxStrategyConfig config = new NegamaxStrategyConfig(
                7,
                10_000_000,
                7,
                100
        );

        NegaMaxStrategy strategy = new NegaMaxStrategy(
                config,
                new EvaluatorImpl()
        );

        ScoredTurn result = strategy.findBestTurn(game);

        Log.print("Test", result);
        Log.debug("Test", "Max timelines", strategy.maxTimelinesReached);
        game.applyMovesAndFinalizeTurn(result.moves());
        //MainApplication.launchWithGame(game);
    }

    @Test
    public void puzzleOpeningTrap2() {

        Game game = Preset.PUZZLE_OPENING_TRAP_2.getGame();

        NegaMaxStrategy strategy = new NegaMaxStrategy(
                NegamaxStrategyConfig.fromConfig(),
                new EvaluatorImpl()
        );

        ScoredTurn result = strategy.findBestTurn(game);

        Log.print("Test", result);
        game.applyMovesAndFinalizeTurn(result.moves());
        MainApplication.launchWithGame(game);
    }

    @Test
    public void checkmatePracticeRook() {

        Game game = Preset.CHECKMATE_PRACTICE_ROOK.getGame();

        game.applyMove(new Move.Builder(game)
                .fromStringAndBuild("(0,0;0,0)->(0,0;0,1)")
        );
        game.finalizeTurn();
        game.applyMove(new Move.Builder(game)
                .fromStringAndBuild("(0,1;5,5)->(0,1;0,5)")
        );
        game.finalizeTurn();
        game.applyMove(new Move.Builder(game)
                .fromStringAndBuild("(0,2;0,1)->(0,2;1,1)")
        );
        game.finalizeTurn();
        game.applyMove(new Move.Builder(game)
                .fromStringAndBuild("(0,3;0,5)->(0,3;0,4)")
        );
        game.finalizeTurn();
        game.applyMove(new Move.Builder(game)
                .fromStringAndBuild("(0,4;1,1)->(0,4;1,2)")
        );
        game.finalizeTurn();
        game.applyMove(new Move.Builder(game)
                .fromStringAndBuild("(0,5;0,4)->(0,5;0,5)")
        );
        game.finalizeTurn();

        NegaMaxStrategy strategy = new NegaMaxStrategy(
                NegamaxStrategyConfig.fromConfig(),
                new EvaluatorImpl()
        );

        ScoredTurn turn = strategy.findBestTurn(game);
        Log.debug("Test", turn);
        game.applyMovesAndFinalizeTurn(turn.moves());

        MainApplication.launchWithGame(game);
    }

    @Test
    public void checkmatePracticeRook2() {

        Game game = Preset.CHECKMATE_PRACTICE_ROOK.getGame();

        game.applyMove(
                new Move.Builder(game)
                        .fromStringAndBuild("(0,0;0,0)->(0,0;0,1)")
        );
        game.finalizeTurn();

        game.applyMove(
                new Move.Builder(game)
                        .fromStringAndBuild("(0,1;5,5)->(0,1;0,5)")
        );
        game.finalizeTurn();

        game.applyMove(
                new Move.Builder(game)
                        .fromStringAndBuild("(0,2;0,1)->(0,0;0,1)")
        );
        game.finalizeTurn();

        game.applyMove(
                new Move.Builder(game)
                        .fromStringAndBuild("(1,1;5,5)->(1,1;0,5)")
        );
        game.finalizeTurn();

        Iterator<List<Move>> turns = MoveGenerator.getIterativeTurnIterator(game);

        assertTrue(turns.hasNext());

        NegaMaxStrategy strategy = new NegaMaxStrategy(
                NegamaxStrategyConfig.fromConfig(),
                new EvaluatorImpl()
        );

        ScoredTurn foundTurn = strategy.findBestTurn(game);

        Log.debug("Test", foundTurn);
    }

    @Test
    public void checkmatePracticeRook3() {

        Game game = Preset.CHECKMATE_PRACTICE_ROOK.getGame();

        {
            game.applyMove(
                    new Move.Builder(game)
                            .fromStringAndBuild("(0,0;0,0)->(0,0;1,0)")
            );
            game.finalizeTurn();

            game.applyMove(
                    new Move.Builder(game)
                            .fromStringAndBuild("(0,1;5,5)->(0,1;4,5)")
            );
            game.finalizeTurn();


            game.applyMove(
                    new Move.Builder(game)
                            .fromStringAndBuild("(0,2;1,0)->(0,2;2,0)")
            );
            game.finalizeTurn();


            game.applyMove(
                    new Move.Builder(game)
                            .fromStringAndBuild("(0,3;4,5)->(0,3;0,5)")
            );
            game.finalizeTurn();


            game.applyMove(
                    new Move.Builder(game)
                            .fromStringAndBuild("(0,4;2,0)->(0,4;3,0)")
            );
            game.finalizeTurn();


            game.applyMove(
                    new Move.Builder(game)
                            .fromStringAndBuild("(0,5;0,5)->(0,5;0,0)")
            );
            game.finalizeTurn();


            game.applyMove(
                    new Move.Builder(game)
                            .fromStringAndBuild("(0,6;3,0)->(0,6;4,0)")
            );
            game.finalizeTurn();


            game.applyMove(
                    new Move.Builder(game)
                            .fromStringAndBuild("(0,7;0,0)->(0,7;1,0)")
            );
            game.finalizeTurn();
        }

        // MainApplication.launchWithGame(game);

        NegaMaxStrategy strategy = new NegaMaxStrategy(
                NegamaxStrategyConfig.fromConfig(),
                new EvaluatorImpl()
        );

        ScoredTurn turn = strategy.findBestTurn(game);

        Log.debug("Test", turn);
    }

    @Test
    public void checkmatePracticeRook4() {

        Game game = Preset.CHECKMATE_PRACTICE_ROOK.getGame();

        {
            game.applyMovesAndFinalizeTurn(List.of(
                    new Move.Builder(game)
                            .fromStringAndBuild("(0,0;0,0)->(0,0;1,1)")
            ));
            game.applyMovesAndFinalizeTurn(List.of(
                    new Move.Builder(game)
                            .fromStringAndBuild("(0,1;5,5)->(0,1;1,5)")
            ));
            game.applyMovesAndFinalizeTurn(List.of(
                    new Move.Builder(game)
                            .fromStringAndBuild("(0,2;1,1)->(0,0;1,1)")
            ));
            game.applyMovesAndFinalizeTurn(List.of(
                    new Move.Builder(game)
                            .fromStringAndBuild("(0,3;1,5)->(0,3;1,1)"),
                    new Move.Builder(game)
                            .fromStringAndBuild("(1,1;5,5)->(1,1;5,4)")
            ));
            game.applyMovesAndFinalizeTurn(List.of(
                    new Move.Builder(game)
                            .fromStringAndBuild("(1,2;1,1)->(1,2;2,0)")
            ));
            game.applyMovesAndFinalizeTurn(List.of(
                    new Move.Builder(game)
                            .fromStringAndBuild("(1,3;5,4)->(1,3;5,0)")
            ));
            game.applyMovesAndFinalizeTurn(List.of(
                    new Move.Builder(game)
                            .fromStringAndBuild("(1,4;0,0)->(0,4;1,1)")
            ));
        }

        Iterator<List<Move>> moveIterator = MoveGenerator.getIterativeTurnIterator(game);
        List<List<Move>> turns = new ArrayList<>();
        while (moveIterator.hasNext()) turns.add(moveIterator.next());

        Log.debug("Test", turns);
    }

    @Test
    public void checkmatePracticeQueen() {

        Game game = Preset.CHECKMATE_PRACTICE_QUEEN.getGame();

        {
            game.applyMovesAndFinalizeTurn(List.of(
                    new Move.Builder(game)
                            .fromStringAndBuild("(0,0;0,0)->(0,0;1,1)")
            ));
            game.applyMovesAndFinalizeTurn(List.of(
                    new Move.Builder(game)
                            .fromStringAndBuild("(0,1;4,5)->(0,1;4,4)")
            ));
            game.applyMovesAndFinalizeTurn(List.of(
                    new Move.Builder(game)
                            .fromStringAndBuild("(0,2;1,1)->(0,0;2,1)")
            ));
            game.applyMovesAndFinalizeTurn(List.of(
                    new Move.Builder(game)
                            .fromStringAndBuild("(1,1;4,5)->(1,1;0,1)"),
                    new Move.Builder(game)
                            .fromStringAndBuild("(0,3;4,4)->(0,1;4,3)")
            ));
        }

        Iterator<List<Move>> iterator = MoveGenerator.getIterativeTurnIterator(game);
        List<List<Move>> turns = new ArrayList<>();
        while (iterator.hasNext()) turns.add(iterator.next());
        Log.debug("Test", turns.size());


        NegaMaxStrategy strategy = new NegaMaxStrategy(
                NegamaxStrategyConfig.fromConfig(),
                new EvaluatorImpl()
        );

        ScoredTurn turn = strategy.findBestTurn(game);
        Log.debug("Test", turn);

        MainApplication.launchWithGame(game);
    }
}
