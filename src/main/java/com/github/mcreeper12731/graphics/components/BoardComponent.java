package com.github.mcreeper12731.graphics.components;

import com.github.mcreeper12731.graphics.GraphicsApplication;
import com.github.mcreeper12731.graphics.GraphicsConfig;
import com.github.mcreeper12731.game.models.Board;
import com.github.mcreeper12731.game.models.Point4D;
import com.github.mcreeper12731.game.models.Timeline;
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
        this.canvas.setWidth(board.size() * GraphicsConfig.TILE_SIZE + GraphicsConfig.BOARD_PADDING);
        this.canvas.setHeight(board.size() * GraphicsConfig.TILE_SIZE + GraphicsConfig.BOARD_PADDING);
        this.canvas.setLayoutX(-GraphicsConfig.BOARD_PADDING / 2.0);
        this.canvas.setLayoutY(-GraphicsConfig.BOARD_PADDING / 2.0);
        this.canvas.toBack();
        this.getChildren().add(canvas);

        this.setLayoutX(
                GraphicsConfig.TIME_SPACING +
                board.t() *
                (board.size() * GraphicsConfig.TILE_SIZE + GraphicsConfig.TIME_SPACING)
        );
        this.setLayoutY(0);

        this.tileComponents = new TileComponent[board.size()][board.size()];

        for (int x = 0; x < board.size(); x++) {
            for (int y = 0; y < board.size(); y++) {
                TileComponent component = new TileComponent(board.getLocationContents(x, y), new Point4D(board.l(), board.t(), x, y), board.size(), application);
                this.tileComponents[y][x] = component;
                this.getChildren().add(component);
            }
        }
    }

    public TileComponent getTileComponent(int x, int y) {
        return tileComponents[y][x];
    }

    public void draw() {

        Timeline timeline = application.getGame().getMultiverse().getTimeline(board.l());

        if (timeline != null && timeline.getLastT() < board.t()) {
            erase();
            return;
        }

        for (int x = 0; x < board.size(); x++) {
            for (int y = 0; y < board.size(); y++) {
                tileComponents[y][x].draw();
            }
        }

        drawDecorations();
    }

    public void erase() {
        for (int x = 0; x < board.size(); x++) {
            for (int y = 0; y < board.size(); y++) {
                tileComponents[y][x].erase();
            }
        }

        eraseDecorations();
    }

    private void drawDecorations() {

        GraphicsContext gc = this.canvas.getGraphicsContext2D();
        if (this.board.t() == this.application.getGame().getPresentTime()) {
            if (this.application.getGame().getMultiverse().isTimelineActive(this.board.l()))
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
