package com.github.mcreeper12731.game.graphics.components;

import com.github.mcreeper12731.MainApplication;
import com.github.mcreeper12731.game.graphics.GraphicsApplication;
import com.github.mcreeper12731.game.graphics.PlayerController;
import com.github.mcreeper12731.game.graphics.GraphicsConfig;
import com.github.mcreeper12731.game.models.Multiverse;
import javafx.application.Application;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Pane;

import java.util.*;

public class ViewComponent extends ScrollPane {

    private final GraphicsApplication application;
    private final Pane displayContainer = new Pane();
    private final Map<Double, TimelineComponent> timelineComponents = new HashMap<>();

    private double scale = 1.0;

    public ViewComponent(GraphicsApplication application) {

        this.application = application;

        setContent(displayContainer);
        setPannable(true);

        for (double timelineIndex : application.getMultiverse().getTimelineIndices()) {
            addTimelineComponent(timelineIndex);
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

    private void addTimelineComponent(double timelineIndex) {

        TimelineComponent timelineComponent = new TimelineComponent(
                application.getMultiverse().getTimeline(timelineIndex),
                application
        );

        timelineComponents.put(timelineIndex, timelineComponent);
        displayContainer.getChildren().add(timelineComponent);
    }

    private void removeTimelineComponent(double timelineIndex) {

        TimelineComponent timelineComponent = timelineComponents.get(timelineIndex);
        timelineComponent.erase();

        timelineComponents.remove(timelineIndex);
        displayContainer.getChildren().remove(timelineComponent);
    }

    public TimelineComponent getTimelineComponent(double timelineIndex) {
        return timelineComponents.get(timelineIndex);
    }

    public void draw() {

        Set<Double> modelIndices = new HashSet<>(application.getMultiverse().getTimelineIndices());
        Set<Double> viewIndices = new HashSet<>(timelineComponents.keySet());
        modelIndices.removeAll(viewIndices);

        for (double timelineIndex : modelIndices) {
            addTimelineComponent(timelineIndex);
        }

        modelIndices = new HashSet<>(application.getMultiverse().getTimelineIndices());
        viewIndices.removeAll(modelIndices);

        for (double timelineIndex : viewIndices) {
            removeTimelineComponent(timelineIndex);
        }

        for (TimelineComponent component : timelineComponents.values()) {
            component.draw();
        }
    }
}
