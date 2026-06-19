package com.github.mcreeper12731.game.graphics;

import com.github.mcreeper12731.game.models.Color;
import com.github.mcreeper12731.game.graphics.components.TileComponent;
import com.github.mcreeper12731.game.models.Multiverse;
import com.github.mcreeper12731.game.models.Timeline;
import com.github.mcreeper12731.game.moves.Move;
import com.github.mcreeper12731.game.pieces.Piece;
import com.github.mcreeper12731.game.pieces.PieceType;
import com.github.mcreeper12731.utility.Log;

import java.util.List;

public class PlayerController extends Controller {

    private TileComponent selectedTile = null;

    public PlayerController(GraphicsApplication application, Color playingAs) {
        super(application, playingAs);
    }

    public void handleTileComponentClick(TileComponent clickedTile) {

        Log.debug("Graphics", "Clicked " + clickedTile.getLocation().toString());

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

        Multiverse multiverse = application.getGame().getMultiverse();
        if (selectedTile == null && multiverse.getLocationContents(clickedTile.getLocation()) == null) return;

        Timeline timeline = multiverse.getTimeline(clickedTile.getLocation().l());
        if (selectedTile == null && timeline.getLastBoard().getPlayerTurn() != clickedTile.getPiece().color()) return;
        if (selectedTile == null && timeline.getLastT() != clickedTile.getLocation().t()) return;

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

        List<Move> legalMoves = piece.getAvailableMoves(application.getGame().getMultiverse());

        for (Move move : legalMoves) {
            TileComponent tile = application.getView()
                    .getTimelineComponent(move.to().l())
                    .getBoardComponentFromTime(move.to().t())
                    .getTileComponent(move.to().x(), move.to().y());
            tile.setHighlighted(highlighted);
        }
    }

    private void attemptMove(TileComponent clickedTile) {

        Move move = new Move.Builder(this.application.getGame().getMultiverse())
                .withFrom(selectedTile.getLocation())
                .withTo(clickedTile.getLocation())
                .build();

        if (!isValidMove(move)) return;

        setHighlightedTiles(selectedTile.getPiece(), false);
        application.getGame().applyMove(move);
        selectedTile.setSelected(false);
        selectedTile = null;

        updateView();
    }

    private boolean isValidMove(Move move) {

        Multiverse multiverse = application.getGame().getMultiverse();

        Piece pieceFrom = multiverse.getLocationContents(move.from());
        if (pieceFrom == null) return false;
        if (pieceFrom.type() == PieceType.EMPTY) return false;

        Timeline timeline = multiverse.getTimeline(move.from().l());

        if (move.color() != timeline.getLastBoard().getPlayerTurn()) return false;

        Piece pieceTo = multiverse.getLocationContents(move.to());
        if (pieceTo == null) return false;
        if (pieceTo.color() == move.color()) return false;

        List<Move> legalMoves = pieceFrom.getAvailableMoves(multiverse);

        for (Move legalMove : legalMoves) {
            if (move.equals(legalMove)) return true;
        }

        return false;
    }

    @Override
    public void onTurnStart() {
        application.getScene().setOnKeyPressed(event -> {
            switch (event.getCode()) {
                case Q:
                    application.getGame().undoMoveFromCurrentTurn();
                    updateView();
                    break;
                case W:
                    application.getGame().undoTurn();
                    application.updateCurrentPlayer();
                    updateView();
                    break;
                case E:
                    Log.print("Graphics", "Player played!");
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
