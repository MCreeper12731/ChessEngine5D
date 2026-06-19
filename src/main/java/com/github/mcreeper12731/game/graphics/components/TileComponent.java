package com.github.mcreeper12731.game.graphics.components;

import com.github.mcreeper12731.game.graphics.GraphicsApplication;
import com.github.mcreeper12731.game.graphics.PlayerController;
import com.github.mcreeper12731.game.graphics.GraphicsConfig;
import com.github.mcreeper12731.game.pieces.Piece;
import com.github.mcreeper12731.game.models.Point4D;
import com.github.mcreeper12731.utility.Log;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class TileComponent extends Canvas {

    private final Piece piece;
    private final Point4D location;
    private final Color color;
    private boolean selected = false;
    private boolean highlighted = false;

    public TileComponent(Piece piece, Point4D location, int boardHeight, GraphicsApplication application) {
        super(GraphicsConfig.TILE_SIZE, GraphicsConfig.TILE_SIZE);
        this.piece = piece;
        this.location = location;

        this.color =
                (location.x() + boardHeight - location.y() - 1) % 2 == 0 ?
                GraphicsConfig.Color.TILE_LIGHT :
                GraphicsConfig.Color.TILE_DARK;

        this.setLayoutX(location.x() * GraphicsConfig.TILE_SIZE);
        this.setLayoutY((boardHeight - location.y() - 1) * GraphicsConfig.TILE_SIZE);

        this.setOnMouseClicked(event -> application.getCurrentController().handleTileComponentClick(this));
    }

    public void draw() {

        GraphicsContext gc = getGraphicsContext2D();
        if (selected) gc.setFill(color.darker());
        else if (highlighted) gc.setFill(GraphicsConfig.Color.TILE_HIGHLIGHTED);
        else if (piece != null && !piece.moved()) gc.setFill(Color.CORAL);
        else gc.setFill(color);
        gc.fillRect(0, 0, GraphicsConfig.TILE_SIZE, GraphicsConfig.TILE_SIZE);

        if (piece == null) return;

        gc.setFill(GraphicsConfig.Color.fromPlayerColor(piece.color()));
        gc.setFont(new Font(GraphicsConfig.TILE_SIZE - 10));
        gc.fillText(piece.type().name, 5, GraphicsConfig.TILE_SIZE - 10);
    }

    public void erase() {

        GraphicsContext gc = getGraphicsContext2D();
        gc.setFill(Color.WHITESMOKE);
        gc.fillRect(0, 0, GraphicsConfig.TILE_SIZE, GraphicsConfig.TILE_SIZE);
    }

    public Piece getPiece() {
        return piece;
    }

    public Point4D getLocation() {
        return location;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
        draw();
    }

    public void setHighlighted(boolean highlighted) {
        this.highlighted = highlighted;
        draw();
    }

    @Override
    public String toString() {
        return "TileComponent{" +
                "piece=" + piece +
                ", location=" + location +
                ", color=" + color +
                ", selected=" + selected +
                '}';
    }
}
