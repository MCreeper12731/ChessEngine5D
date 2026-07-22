package com.github.mcreeper12731.bitapplication.controllers;

import com.github.mcreeper12731.application.GraphicsApplication;
import com.github.mcreeper12731.application.components.TileComponent;
import com.github.mcreeper12731.bitapplication.GraphicsBitApplication;
import com.github.mcreeper12731.bitgame.BitGame;
import com.github.mcreeper12731.game.Game;
import com.github.mcreeper12731.game.models.Color;
import com.github.mcreeper12731.game.models.Move;
import com.github.mcreeper12731.utility.Log;

import java.util.Scanner;

public class HeadlessBitPlayerController extends BitController {

    private final Scanner scanner;

    public HeadlessBitPlayerController(GraphicsBitApplication application, Color playingAs) {
        super(application, playingAs);
        this.scanner = new Scanner(System.in);
    }

    @Override
    public void onTurnStart() {
        BitGame game = this.application.getGame();
        Log.print("HeadlessBitApplication", "Enter turn. Type \".\" when done and \"undo\" to remove the last undo");
        while (true) {
            String moveString = this.scanner.nextLine();
            if (moveString.equalsIgnoreCase(".")) {
                if (game.isCurrentTurnFinalizable()) break;
                Log.print("HeadlessBitApplication", "Current turn is not finalizable!");
                continue;
            }
            else if (moveString.equalsIgnoreCase("undo")) {
                game.undoMoveFromCurrentTurn();
                continue;
            }
            Move move;
            try {
                move = null;

                //move = new Move.Builder(this.application.getGame()) TODO
                //        .fromStringAndBuild(moveString);

            } catch (IllegalArgumentException exception) {
                Log.print("HeadlessBitApplication", "Invalid move syntax!");
                continue;
            }
            game.applyMove(move);
        }
        this.application.updateCurrentPlayer();
    }

    @Override
    public void onTurnEnd() {

    }

    @Override
    public void handleTileComponentClick(TileComponent tileComponent) {
        throw new UnsupportedOperationException("Not supported in headless mode!");
    }

}
