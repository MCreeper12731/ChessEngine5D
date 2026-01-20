package com.github.mcreeper12731.game.graphics;

import com.github.mcreeper12731.game.models.Color;
import com.github.mcreeper12731.game.moves.Move;
import com.github.mcreeper12731.game.graphics.components.TileComponent;
import com.github.mcreeper12731.game.moves.MoveFactory;
import com.github.mcreeper12731.game.moves.MoveValidator;
import com.github.mcreeper12731.game.pieces.Piece;

import java.util.List;

public class PlayerController extends Controller {

    private final MoveValidator moveValidator;

    private TileComponent selectedTile = null;

    public PlayerController(GraphicsApplication application, Color playingAs) {
        super(application, playingAs);
        this.moveValidator = new MoveValidator(application.getMultiverse());
    }

    public void handleTileComponentClick(TileComponent clickedTile) {

        if (clickedTile.getPiece() == null) {
            handleEmptyTileClick(clickedTile);
            return;
        }

        handleOccupiedTileClick(clickedTile);
    }

    private void handleEmptyTileClick(TileComponent clickedTile) {

        if (selectedTile == null) return;

        attemptMove(clickedTile);
    }

    private void handleOccupiedTileClick(TileComponent clickedTile) {

        if (selectedTile == null && clickedTile.getPiece().color() != playingAs) return;

        if (selectedTile == null && !moveValidator.isFromCorrect(clickedTile.getLocation())) return;
        if (selectedTile == null && !moveValidator.isPieceColorCorrect(clickedTile.getPiece().color(), clickedTile.getLocation())) return;

        if (selectedTile != null && selectedTile.getLocation() == clickedTile.getLocation()) {
            selectedTile.setSelected(false);
            setHighlightedTiles(selectedTile.getPiece(), false);
            selectedTile = null;
            return;
        }

        if (selectedTile != null && selectedTile.getPiece().color() != clickedTile.getPiece().color()) {
            attemptMove(clickedTile);
            return;
        }

        if (selectedTile != null && selectedTile.getPiece().color() == clickedTile.getPiece().color()) return;

        if (selectedTile != null) {
            selectedTile.setSelected(false);
            setHighlightedTiles(selectedTile.getPiece(), false);
        }

        selectedTile = clickedTile;
        setHighlightedTiles(selectedTile.getPiece(), true);

        selectedTile.setSelected(true);
    }

    private void setHighlightedTiles(Piece piece, boolean highlighted) {

        List<Move> legalMoves = piece.getAvailableMoves(application.getMultiverse());

        for (Move move : legalMoves) {
            TileComponent tile = application.getView()
                    .getTimelineComponent(move.toTimeline())
                    .getBoardComponentFromTime(move.toTime())
                    .getTileComponent(move.toX(), move.toY());
            tile.setHighlighted(highlighted);
        }
    }

    private void attemptMove(TileComponent clickedTile) {

        MoveFactory moveFactory = new MoveFactory(application.getMultiverse());
        Move move = moveFactory.build(
                selectedTile.getLocation(),
                clickedTile.getLocation()
        );

        if (!moveValidator.isValid(move)) return;

        setHighlightedTiles(selectedTile.getPiece(), false);
        application.getMultiverse().applyMove(move);
        selectedTile.setSelected(false);
        selectedTile = null;

        updateView();
    }

    @Override
    public void onTurnStart() {
        application.getScene().setOnKeyPressed(event -> {
            switch (event.getCode()) {
                case R:
                    application.getMultiverse().undoMoveFromCurrentTurn();
                    updateView();
                    break;
                case T:
                    application.getMultiverse().undoTurn();
                    application.updateCurrentPlayer();
                    updateView();
                    break;
                case Q:
                    System.out.println("Player played!");
                    application.updateCurrentPlayer();
                    updateView();
                    break;
                default:
                    break;
            }
        });
    }

    @Override
    public void onTurnEnd() {
        application.getScene().setOnKeyPressed(null);
    }
}
