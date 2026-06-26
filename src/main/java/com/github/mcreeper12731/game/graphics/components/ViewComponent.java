package com.github.mcreeper12731.game.graphics.components;

import com.github.mcreeper12731.game.graphics.GraphicsApplication;
import com.github.mcreeper12731.game.graphics.GraphicsConfig;
import com.github.mcreeper12731.game.models.Color;
import com.github.mcreeper12731.game.models.Timeline;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Pane;

import java.util.*;

public class ViewComponent extends ScrollPane {

    private final GraphicsApplication application;
    private final Pane displayContainer = new Pane();
    private final Map<Integer, TimelineComponent> timelineComponents = new HashMap<>();

    private double scale = 1.0;

    public ViewComponent(GraphicsApplication application) {

        this.application = application;

        setContent(displayContainer);
        setPannable(true);

        for (int l : application.getGame().getMultiverse().getTimelineLs()) {
            addTimelineComponent(l);
        }

        setVvalue(GraphicsConfig.CENTER_OFFSET);

        displayContainer.setFocusTraversable(true);
        displayContainer.setOnScroll(event -> {
            if (event.isControlDown()) {
                double zoomFactor = 1.2;
                if (event.getDeltaY() < 0) {
                    scale /= zoomFactor;
                } else {
                    scale *= zoomFactor;
                }

                scale = Math.max(0.1, Math.min(scale, 5.0));

                displayContainer.setScaleX(scale);
                displayContainer.setScaleY(scale);

                event.consume();
            }
        });
    }

    private void addTimelineComponent(int l) {

        Timeline timeline = application.getGame().getMultiverse().getTimeline(l);
        if (timeline == null) return;

        TimelineComponent timelineComponent = new TimelineComponent(
                timeline,
                application
        );

        timelineComponents.put(l, timelineComponent);
        displayContainer.getChildren().add(timelineComponent);
    }

    private void removeTimelineComponent(int timelineIndex) {

        TimelineComponent timelineComponent = timelineComponents.get(timelineIndex);
        timelineComponent.erase();

        timelineComponents.remove(timelineIndex);
        displayContainer.getChildren().remove(timelineComponent);
    }

    public TimelineComponent getTimelineComponent(int timelineIndex) {
        return timelineComponents.get(timelineIndex);
    }

    public void draw() {

        if (application.getGame().getWinner() != null) {
            displayContainer.setBackground(new Background(new BackgroundFill(
                    application.getGame().getWinner() == Color.WHITE ?
                            javafx.scene.paint.Color.GREEN :
                            javafx.scene.paint.Color.RED,
                    null,
                    null
            )));
        } else {
            displayContainer.setBackground(new Background(new BackgroundFill(
                    javafx.scene.paint.Color.WHITE,
                    null,
                    null
            )));
        }

        Set<Integer> modelIndices = new HashSet<>(application.getGame().getMultiverse().getTimelineLs());
        Set<Integer> viewIndices = new HashSet<>(timelineComponents.keySet());
        modelIndices.removeAll(viewIndices);

        for (int timelineIndex : modelIndices) {
            addTimelineComponent(timelineIndex);
        }

        modelIndices = new HashSet<>(application.getGame().getMultiverse().getTimelineLs());
        viewIndices.removeAll(modelIndices);

        for (int timelineIndex : viewIndices) {
            removeTimelineComponent(timelineIndex);
        }

        for (TimelineComponent component : timelineComponents.values()) {
            component.draw();
        }

        scrollToBoardTime(application.getGame().getPresentTime());
    }

    private void scrollToBoardTime(int boardTime) {
        TimelineComponent timelineComponent = timelineComponents.get(0);
        if (timelineComponent != null) {
            // Calculate position based on board size and time
            double boardWidth = application.getGame().getMultiverse().getBoardSize() * GraphicsConfig.TILE_SIZE;
            double timeSpacing = GraphicsConfig.TIME_SPACING;
            double xPosition = boardTime * (boardWidth + timeSpacing);

            double scrollValue = xPosition / (displayContainer.getWidth() - getWidth());
            setHvalue(Math.max(0, Math.min(1, scrollValue)));
        }
    }
}
