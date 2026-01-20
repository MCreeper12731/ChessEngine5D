package com.github.mcreeper12731.game.graphics.components;

import com.github.mcreeper12731.game.graphics.GraphicsApplication;
import com.github.mcreeper12731.game.graphics.PlayerController;
import com.github.mcreeper12731.game.graphics.GraphicsConfig;
import com.github.mcreeper12731.game.models.Board;
import com.github.mcreeper12731.game.models.Point4D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

public class BoardComponent extends Pane {

    private final Board board;
    private final GraphicsApplication application;
    private final Canvas canvas;
    private final TileComponent[][] tileComponents;

    public BoardComponent(Board board, GraphicsApplication application) {

        this.board = board;
        this.application = application;

        this.canvas = new Canvas();
        this.canvas.setWidth(board.getSize() * GraphicsConfig.TILE_SIZE + GraphicsConfig.BOARD_PADDING);
        this.canvas.setHeight(board.getSize() * GraphicsConfig.TILE_SIZE + GraphicsConfig.BOARD_PADDING);
        this.canvas.setLayoutX(-GraphicsConfig.BOARD_PADDING / 2.0);
        this.canvas.setLayoutY(-GraphicsConfig.BOARD_PADDING / 2.0);
        this.canvas.toBack();
        this.getChildren().add(canvas);

        this.setLayoutX(
                GraphicsConfig.TIME_SPACING +
                board.getTime() *
                (board.getSize() * GraphicsConfig.TILE_SIZE + GraphicsConfig.TIME_SPACING)
        );
        this.setLayoutY(0);

        this.tileComponents = new TileComponent[board.getSize()][board.getSize()];

        for (int x = 0; x < board.getSize(); x++) {
            for (int y = 0; y < board.getSize(); y++) {
                TileComponent component = new TileComponent(board.getPiece(x, y), new Point4D(board.getTimeline(), board.getTime(), x, y), board.getSize(), application);
                this.tileComponents[y][x] = component;
                this.getChildren().add(component);
            }
        }
    }

    public TileComponent getTileComponent(int x, int y) {
        return tileComponents[y][x];
    }

    public void draw() {

        if (application.getMultiverse().getTimeline(board.getTimeline()).getLastTimeCoordinate() < board.getTime()) {
            erase();
            return;
        }

        for (int x = 0; x < board.getSize(); x++) {
            for (int y = 0; y < board.getSize(); y++) {
                tileComponents[y][x].draw();
            }
        }

        drawDecorations();
    }

    public void erase() {
        for (int x = 0; x < board.getSize(); x++) {
            for (int y = 0; y < board.getSize(); y++) {
                tileComponents[y][x].erase();
            }
        }

        eraseDecorations();
    }

    private void drawDecorations() {

        GraphicsContext gc = this.canvas.getGraphicsContext2D();
        if (this.board.getTime() == this.application.getMultiverse().getPresentTime()) {
            if (application.getMultiverse().getTimeline(this.board.getTimeline()).isActive())
                gc.setFill(GraphicsConfig.Color.PRESENT);
            else
                gc.setFill(GraphicsConfig.Color.PRESENT.darker());
        } else {
            gc.setFill(Color.WHITESMOKE);
        }
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
    }

    private void eraseDecorations() {
        GraphicsContext gc = this.canvas.getGraphicsContext2D();
        gc.setFill(Color.WHITESMOKE);
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
    }

}
