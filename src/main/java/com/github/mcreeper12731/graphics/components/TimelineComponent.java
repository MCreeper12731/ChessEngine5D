package com.github.mcreeper12731.graphics.components;

import com.github.mcreeper12731.graphics.GraphicsApplication;
import com.github.mcreeper12731.graphics.GraphicsConfig;
import com.github.mcreeper12731.game.models.Timeline;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.util.ArrayList;
import java.util.List;

public class TimelineComponent extends Pane {

    private final Timeline timeline;
    private final Canvas canvas;
    private final GraphicsApplication application;
    private final List<BoardComponent> boardComponents = new ArrayList<>();

    public TimelineComponent(Timeline timeline, GraphicsApplication application) {

        this.timeline = timeline;
        this.application = application;
        this.canvas = new Canvas();
        this.getChildren().add(canvas);
        this.canvas.toBack();

        this.setLayoutX(0);

        this.setLayoutY(timeline.getL() * (application.getGame().getMultiverse().getBoardSize() * GraphicsConfig.TILE_SIZE + GraphicsConfig.TIMELINE_SPACING) + GraphicsConfig.CENTER_OFFSET);

        for (int time = 0; time < timeline.size(); time++) {
            addBoardComponent(time);
        }

    }

    private void addBoardComponent(int time) {
        BoardComponent board = new BoardComponent(
                timeline.getBoardByIndex(time),
                application
        );

        boardComponents.add(board);
        getChildren().add(board);
    }

    private void removeBoardComponent(int time) {
        BoardComponent board = boardComponents.get(time);
        board.erase();

        boardComponents.remove(time);
        getChildren().remove(board);
    }

    public BoardComponent getBoardComponentByIndex(int index) {
        return boardComponents.get(index);
    }

    public BoardComponent getBoardComponentByT(int time) {
        return boardComponents.get(time - timeline.getFirstTimeCoordinate());
    }

    public void draw() {

        if (application.getGame().getMultiverse().getTimeline(timeline.getL()) == null) {
            erase();
            return;
        }

        double modelTimelineLength = timeline.size();

        for (int time = boardComponents.size(); time < modelTimelineLength; time++) {
            addBoardComponent(time);
        }

        for (int time = boardComponents.size() - 1; time >= modelTimelineLength; time--) {
            removeBoardComponent(time);
        }

        double width = (this.timeline.getLastT() + 1) * (application.getGame().getMultiverse().getBoardSize() * GraphicsConfig.TILE_SIZE + GraphicsConfig.TIME_SPACING);
        double height = application.getGame().getMultiverse().getBoardSize() * GraphicsConfig.TILE_SIZE;
        this.canvas.setWidth(width);
        this.canvas.setHeight(height);

        for (BoardComponent component : boardComponents) {
            component.draw();
        }

        drawDecorations(width, height);

    }

    public void erase() {

        double modelTimelineLength = timeline.size();
        double viewTimelineLength = boardComponents.size();

        if (modelTimelineLength - viewTimelineLength > 0) {
            for (int time = boardComponents.size(); time < modelTimelineLength; time++) {
                addBoardComponent(time);
            }
        }

        double width = (this.timeline.getLastT() + 1) * (application.getGame().getMultiverse().getBoardSize() * GraphicsConfig.TILE_SIZE + GraphicsConfig.TIME_SPACING);
        double height = application.getGame().getMultiverse().getBoardSize() * GraphicsConfig.TILE_SIZE;
        this.canvas.setWidth(width);
        this.canvas.setHeight(height);

        for (BoardComponent component : boardComponents) {
            component.erase();
        }

        boardComponents.clear();
    }

    private void drawDecorations(double width, double height) {

        GraphicsContext gc = this.canvas.getGraphicsContext2D();
        gc.setFill(Color.BLUE);
        gc.setFont(new Font(18));
        gc.fillText(String.format("%dL", timeline.getL()), 20, height / 2);

        if (this.application.getGame().getMultiverse().isTimelineActive(this.timeline.getL())) gc.setFill(GraphicsConfig.Color.TIMELINE_ACTIVE);
        else gc.setFill(GraphicsConfig.Color.TIMELINE_INACTIVE);
        gc.fillRect(0, 0, width, height);
    }
}
