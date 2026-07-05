package com.github.mcreeper12731.graphics.components;

import com.github.mcreeper12731.graphics.GraphicsApplication;
import com.github.mcreeper12731.graphics.GraphicsConfig;
import com.github.mcreeper12731.game.pieces.Piece;
import com.github.mcreeper12731.game.models.Point4D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class TileComponent extends Canvas {

    private final Piece piece;
    private final Point4D location;
    private final Color color;
    private final Color annotated;
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

        Color annotated = null;
        /*List<List<Move>> turns = application.getGame().getTurns();
        for (List<Move> turn : turns) {
            for (Move move : turn) {
                if (
                        move.from().add(0, 1, 0, 0).equals(location) &&
                        !application.getGame().getMovedPieceDestination(move).equals(move.to().add(0, 1, 0, 0))
                ) {
                    annotated = GraphicsConfig.Color.TILE_CONTAINS_JUMP_MOVE;
                    break;
                }

                if (
                        move.from().add(0, 1, 0, 0).equals(location) ||
                        move.to().add(0, 1, 0, 0).equals(location)
                ) {
                    annotated = GraphicsConfig.Color.TILE_CONTAINS_MOVE;
                    break;
                }

                if (application.getGame().getMovedPieceDestination(move).equals(location)) {
                    annotated = GraphicsConfig.Color.TILE_CONTAINS_JUMP_MOVE;
                    break;
                }
            }
        }*/
        this.annotated = annotated;

        this.setOnMouseClicked(event -> application.getCurrentController().handleTileComponentClick(this));
    }

    public void draw() {

        GraphicsContext gc = getGraphicsContext2D();
        gc.setFill(color);
        gc.fillRect(0, 0, GraphicsConfig.TILE_SIZE, GraphicsConfig.TILE_SIZE);
        if (highlighted) gc.setFill(GraphicsConfig.Color.TILE_HIGHLIGHTED);
        else if (annotated != null) gc.setFill(annotated);
        //else if (piece != null && !piece.moved()) gc.setFill(Color.CORAL);
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
                '}';
    }
}
