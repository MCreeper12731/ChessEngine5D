package com.github.mcreeper12731.bitgame;

import com.github.mcreeper12731.bitgame.models.BitBoard;
import com.github.mcreeper12731.bitgame.models.BitMultiverse;
import com.github.mcreeper12731.bitgame.models.pieces.BitPiece;
import com.github.mcreeper12731.bitgame.models.BitTimeline;
import com.github.mcreeper12731.game.Game;
import com.github.mcreeper12731.game.models.*;
import com.github.mcreeper12731.game.models.pieces.Piece;
import com.github.mcreeper12731.game.models.pieces.PieceType;
import com.github.mcreeper12731.utility.Log;

import java.util.*;

public class BitGame {

    public final static int NUMBER_OF_TYPES = PieceType.values().length - 1; // Omit PieceType.EMPTY
    public final static int EFFECTIVELY_NULL = -1;
    public final static int WHITE = Color.WHITE.ordinal();
    public final static int BLACK = Color.BLACK.ordinal();

    private final BitMultiverse multiverse;
    private final Stack<MoveEffect> currentTurnMoveEffects = new Stack<>();
    private final Stack<Stack<MoveEffect>> turnEffects = new Stack<>();
    private final TreeMap<Integer, Integer> lastTimesOfTimelines = new TreeMap<>();

    private final Stack<Stack<MoveEffect>> archivedTurnEffects = new Stack<>();

    private Color playerTurn = Color.WHITE;
    private boolean gameOver = false;
    private Color winner = null;

    public BitGame(BitMultiverse multiverse) {
        this.multiverse = multiverse;

        this.initialize();
    }

    public BitGame(Game game) {

        Multiverse multiverse = game.getMultiverse();

        BitMultiverse.Builder multiverseBuilder = new BitMultiverse.Builder(multiverse.getBoardSize());
        for (Timeline timeline : multiverse.getTimelines()) {

            BitTimeline bitTimeline = new BitTimeline(timeline.getL(), timeline.getFirstT());
            for (int t = timeline.getFirstT(); t <= timeline.getLastT(); t++) {

                Board board = timeline.getBoardFromT(t);
                BitBoard.Builder boardBuilder = new BitBoard.Builder(multiverse.getBoardSize(), board.l(), board.t());
                for (int i = 0; i < board.size() * board.size(); i++) {
                    int x = i % board.size();
                    int y = i / board.size();
                    Piece piece = board.getLocationContents(x, y);
                    if (piece.type() == PieceType.EMPTY) continue;
                    boardBuilder.withPiece(piece.color(), piece.type(), x, y);

                }
                bitTimeline.addBoard(boardBuilder.build());

            }
            multiverseBuilder.withTimeline(bitTimeline);

        }
        this.multiverse = multiverseBuilder.build();
        this.currentTurnMoveEffects.addAll(game.getCurrentTurnMoveEffects());
        this.turnEffects.addAll(game.getTurnEffects());
        this.archivedTurnEffects.addAll(game.getArchivedTurnEffects());
        this.playerTurn = game.getPlayerTurn();
        this.gameOver = game.isGameOver();
        this.winner = game.getWinner();

        this.initialize();
    }

    private void initialize() {
        for (BitTimeline timeline : this.multiverse.getTimelines()) {
            if (!this.multiverse.isTimelineActive(timeline)) continue;
            this.lastTimesOfTimelines.put(timeline.getLastT(), this.lastTimesOfTimelines.getOrDefault(timeline.getLastT(), 0) + 1);
        }
    }

    // Turn-related logic

    public void applyMovesAndFinalizeTurn(List<Move> turn) {

        this.applyMovesFromTurnStart(turn);
        this.finalizeTurn();
    }

    public void applyMovesFromTurnStart(List<Move> turn) {

        if (!this.currentTurnMoveEffects.isEmpty())
            throw new RuntimeException("Cannot apply full turn as a turn is in progress!");

        this.applyMoves(turn);
    }

    @SuppressWarnings("unchecked")
    public void finalizeTurn() {

        if (!this.isCurrentTurnFinalizable()) {
            System.out.println(this);
            throw new RuntimeException("Cannot finalize turn - current moves do not complete a valid turn!");
        }

        Stack<MoveEffect> turnEffect = (Stack<MoveEffect>) currentTurnMoveEffects.clone();

        this.turnEffects.add(turnEffect);
        this.currentTurnMoveEffects.clear();

        this.playerTurn = this.playerTurn.other();
    }

    // Move-related logic

    public void applyMoves(List<Move> turn) {
        for (Move move : turn) {
            this.applyMove(move);
        }
    }

    public void applyMove(Move move) {

        if (move.noop() && (move.from() == null || move.to() == null)) return;

        BitTimeline fromTimeline = this.multiverse.getTimeline(move.from().l());
        BitTimeline toTimeline = this.multiverse.getTimeline(move.to().l());

        if (toTimeline == null) {
            Log.debug("Game", this);
            Log.debug("Game","Attempted move: " + move);
            throw new RuntimeException("Cannot apply move - timeline does not exist!");
        }

        BitBoard toBoard = toTimeline.getBoardFromT(move.to().t());

        if (toBoard == null) {
            Log.debug("Game",this);
            Log.debug("Game", "Attempted move: " + move);
            throw new RuntimeException("Cannot apply move - contents does not exist!");
        }

        MoveEffect moveEffect = new MoveEffect(
                move,
                this.playerTurn,
                this.gameOver,
                this.winner,
                move.from().l()
        );

        // Subtract one from fromTimeline's current lastT as it will need to be updated
        this.removeLastT(fromTimeline);

        if (move.from().l() == move.to().l() && move.from().t() == move.to().t()) {
            // Handling in-board moves

            this.applyMoveToTimeline(fromTimeline, move);

        } else if (move.to().t() != toTimeline.getLastT()) {
            // Handling timeline splitting moves

            this.applyMoveToTimeline(fromTimeline, move);

            int newTimelineL = this.playerTurn == Color.WHITE ?
                    this.multiverse.getTopTimelineL() + 1 :
                    this.multiverse.getBotTimelineL() - 1;

            int startingTime = move.to().t() + 1;

            BitBoard newBoard = toBoard.applyMove(newTimelineL, startingTime, move);

            BitTimeline newTimeline = new BitTimeline(newTimelineL, startingTime);
            newTimeline.addBoard(newBoard);

            this.multiverse.addTimeline(newTimeline);
            if (this.multiverse.isTimelineActive(newTimeline)) {
                this.addLastT(newTimeline);

                // Does the addition of the new timeline activate an existing one?
                if (this.playerTurn == Color.WHITE) {
                    int oppositeTimelineLDelta1 = -(newTimelineL + 1);
                    if (
                            oppositeTimelineLDelta1 >= this.multiverse.getBotTimelineL() &&
                            this.multiverse.isTimelineActive(oppositeTimelineLDelta1)
                    ) this.addLastT(this.multiverse.getTimeline(oppositeTimelineLDelta1));
                } else {
                    int oppositeTimelineLDelta1 = -(newTimelineL - 1);
                    if (
                            oppositeTimelineLDelta1 <= this.multiverse.getTopTimelineL() &&
                                    this.multiverse.isTimelineActive(oppositeTimelineLDelta1)
                    ) this.addLastT(this.multiverse.getTimeline(oppositeTimelineLDelta1));
                }
            }

            moveEffect.setAddedTimelineL(newTimelineL);
            moveEffect.setSecondaryAddedBoardL(newTimelineL);

        } else {
            // Handling cross-timeline, non-splitting moves

            this.applyMoveToTimeline(fromTimeline, move);

            this.removeLastT(toTimeline);
            this.applyMoveToTimeline(toTimeline, move);
            this.addLastT(toTimeline);

            moveEffect.setSecondaryAddedBoardL(move.to().l());
        }

        byte pieceAtLocation = this.multiverse.getLocationContents(move.to());
        if (pieceAtLocation > 0) {
            int pieceType = BitPiece.typeOrdinal(pieceAtLocation);
            if (pieceType == PieceType.KING.ordinal()) {
                int pieceColor = BitPiece.colorOrdinal(pieceAtLocation);
                this.gameOver = true;
                this.winner = Color.of(pieceColor).other();
            }
        }

        this.addLastT(fromTimeline);
        this.currentTurnMoveEffects.add(moveEffect);
    }

    public void applyMoveToTimeline(BitTimeline timeline, Move move) {

        BitBoard lastBoard = timeline.getLastBoard();
        BitBoard nextBoard = lastBoard.applyMove(timeline.getL(), timeline.getLastT() + 1, move);

        timeline.addBoard(nextBoard);
    }

    // Undo related logic

    public void undoTurn() {

        Stack<MoveEffect> turnEffect;

        if (!currentTurnMoveEffects.isEmpty()) {
            turnEffect = currentTurnMoveEffects;
        } else {
            if (turnEffects.isEmpty()) return;
            turnEffect = turnEffects.pop();
        }

        while (!turnEffect.isEmpty())
            this.undoMove(turnEffect.pop());
    }

    public void undoAllMovesFromCurrentTurn() {
        while (!currentTurnMoveEffects.isEmpty())
            this.undoMove(currentTurnMoveEffects.pop());
    }

    public void undoMoveFromCurrentTurn() {

        if (this.currentTurnMoveEffects.isEmpty()) return;

        this.undoMove(this.currentTurnMoveEffects.pop());
    }

    private void undoMove(MoveEffect moveEffect) {

        // Remove added boards
        BitTimeline primaryTimeline = this.multiverse.getTimeline(moveEffect.getPrimaryAddedBoardL());
        this.removeLastT(primaryTimeline);
        primaryTimeline.removeLastBoard();
        this.addLastT(primaryTimeline);

        if (moveEffect.getSecondaryAddedBoardL() != null) {
            BitTimeline secondaryTimeline = this.multiverse.getTimeline(moveEffect.getSecondaryAddedBoardL());
            if (this.multiverse.isTimelineActive(secondaryTimeline))
                this.removeLastT(secondaryTimeline);
            secondaryTimeline.removeLastBoard();
            if (this.multiverse.isTimelineActive(secondaryTimeline) && secondaryTimeline.size() > 0)
                this.addLastT(secondaryTimeline);
        }

        // Remove timeline if it was added
        if (moveEffect.isAddedTimeline()) {
            this.multiverse.removeLastTimeline(moveEffect.getAddedTimeline() >= 0);
        }

        // Restore mutable fields
        this.playerTurn = moveEffect.getPrevPlayerTurn();
        this.gameOver = moveEffect.getPrevGameOver();
        this.winner = moveEffect.getPrevWinner();
    }

    // State polling methods and methods to predict game state without simulating moves

    public boolean isCurrentTurnFinalizable() {
        if (this.isGameOver()) return true;
        for (BitTimeline timeline : this.multiverse.getTimelines()) {
            if (timeline.getLastT() > this.getPresentTime()) continue;
            if (timeline.getLastBoard().getPlayerTurn() != this.playerTurn) continue;
            
            return false;
        }
        return true;
    }

    public List<Integer> getPlayableTimelineLs() {
        List<Integer> ls = new ArrayList<>();

        for (int timelineIndex : this.multiverse.getActiveTimelineLs()) {
            BitTimeline timeline = this.multiverse.getTimeline(timelineIndex);
            if (timeline.getLastBoard().getPlayerTurn() != this.playerTurn) continue;

            ls.add(timelineIndex);
        }

        return ls;
    }

    public List<BitBoard> getPlayableBoards(Color color) {
        List<BitBoard> boards = new ArrayList<>();

        for (BitTimeline timeline : this.multiverse.getTimelines()) {
            BitBoard board = timeline.getLastBoard();
            if (board.getPlayerTurn() != color) continue;

            boards.add(board);
        }

        return boards;
    }

    public boolean doesMoveAddTimeline(Move move) {
        return doesMoveAddTimeline(move, false);
    }

    public boolean doesMoveAddTimeline(Move move, boolean timelineHasBeenPlayedOn) {
        if (move.noop()) return false;
        int toTimelineLastT = this.multiverse.getTimeline(move.to().l()).getLastT() + (timelineHasBeenPlayedOn ? 1 : 0);

        return move.to().t() != toTimelineLastT;
    }

    public boolean doesMoveAddInactiveTimeline(Move move) {
        if (!this.doesMoveAddTimeline(move)) return false;

        int addedTimelineL = move.color() == Color.WHITE ?
                this.multiverse.getTopTimelineL() + 1 :
                this.multiverse.getBotTimelineL() - 1;

        return this.multiverse.isTimelineActive(addedTimelineL);
    }

    public Integer doesMoveActivateTimeline(Move move) {
        return this.doesMoveActivateTimeline(move, false);
    }

    public Integer doesMoveActivateTimeline(Move move, boolean hasTimelineBeenPlayedOn) {
        if (!this.doesMoveAddTimeline(move, hasTimelineBeenPlayedOn)) return null;

        int addedTimelineL = move.color() == Color.WHITE ?
                this.multiverse.getTopTimelineL() + 1 :
                this.multiverse.getBotTimelineL() - 1;

        int oppositeTimelineL =
                (Math.abs(addedTimelineL) + 1)
                        * (addedTimelineL > 0 ? -1 : 1)
                        - (this.multiverse.isEven() ? 1 : 0);

        if (this.multiverse.getTimeline(oppositeTimelineL) == null) return null;

        int oppositeTimelineLWithDelta1 = oppositeTimelineL > 0 ? oppositeTimelineL - 1 : oppositeTimelineL + 1;

        if (this.multiverse.isTimelineActive(oppositeTimelineLWithDelta1) && !this.multiverse.isTimelineActive(oppositeTimelineL))
            return oppositeTimelineL;

        return null;
    }

    public boolean doesMoveRewindPresent(Move move) {
        if (!this.doesMoveAddTimeline(move)) return false;

        int addedTimelineL = move.color() == Color.WHITE ?
                this.multiverse.getTopTimelineL() + 1 :
                this.multiverse.getBotTimelineL() - 1;

        if (!this.multiverse.isTimelineActive(addedTimelineL)) return false;

        int addedTimelineLStartTime = move.to().t() + 1;

        return this.getPresentTime() >= addedTimelineLStartTime;
    }

    public Point4D getMovedPieceDestination(Move move) {
        if (move.from().l() == move.to().l() && move.from().t() == move.to().t())
            // In-board moves
            return move.to().add(0, 1, 0, 0);

        BitTimeline fromTimeline = this.multiverse.getTimeline(move.from().l());
        BitTimeline toTimeline = this.multiverse.getTimeline(move.to().l());
        if (move.to().t() != toTimeline.getLastT()) {
            int isCheckAfterMove = move.from().t() == fromTimeline.getLastT() ? 1 : 0;
            int newTimelineL = this.playerTurn == Color.WHITE ?
                    this.multiverse.getTopTimelineL() + isCheckAfterMove :
                    this.multiverse.getBotTimelineL() - isCheckAfterMove;
            return new Point4D(
                    newTimelineL,
                    move.to().t() + 1,
                    move.to().x(),
                    move.to().y()
            );
        }

        return move.to().add(0, 1, 0, 0);
    }

    public List<List<Move>> getTurns() {
        List<List<MoveEffect>> turnEffects = new ArrayList<>(this.archivedTurnEffects);
        turnEffects.addAll(this.turnEffects);
        turnEffects.add(new ArrayList<>(this.currentTurnMoveEffects));
        return turnEffects.stream().map(turnEffect -> turnEffect.stream().map(MoveEffect::getMove).toList()).toList();
    }

    /**
     * A very dense method, checking if a turn would be finalizable without applying it. Should be complete, but an edge case may still exist
     * @param turn the turn to check
     * @return true if the turn would be finalizable, false otherwise
     */
    public boolean isTurnFinalizable(List<Move> turn) {

        boolean[] consumedTimelines = new boolean[this.multiverse.getTimelines().size()];
        boolean[] mandatoryTimelines = new boolean[this.multiverse.getTimelines().size()];
        for (BitTimeline timeline : this.multiverse.getTimelines()) {
            if (timeline.getLastT() > this.getPresentTime()) continue;
            if (!this.multiverse.isTimelineActive(timeline)) continue;

            int shiftedL = timeline.getL() - this.multiverse.getBotTimelineL();
            mandatoryTimelines[shiftedL] = true;
        }

        for (Move move : turn) {
            if (move.noop()) continue;
            int shiftedFromL = move.from().l() - this.multiverse.getBotTimelineL();
            int shiftedToL = move.to().l() - this.multiverse.getBotTimelineL();

            int toType = BitPiece.typeOrdinal(this.multiverse.getLocationContents(move.to()));

            if (toType == PieceType.KING.ordinal())
                // Always allow finalization of king capture turns
                return true;

            if (consumedTimelines[shiftedFromL])
                // A piece cannot move from a timeline already played on
                return false;

            Integer activatedTimeline = this.doesMoveActivateTimeline(move, consumedTimelines[shiftedToL]);
            if (activatedTimeline != null) {
                BitTimeline timeline = this.multiverse.getTimeline(activatedTimeline);
                int shiftedActivatedTimelineL = activatedTimeline - this.multiverse.getBotTimelineL();
                if (shiftedActivatedTimelineL >= 0 && shiftedActivatedTimelineL < mandatoryTimelines.length && timeline.getLastT() <= this.getPresentTime())
                    mandatoryTimelines[shiftedActivatedTimelineL] = true;
            }

            BitTimeline toTimeline = this.multiverse.getTimeline(move.to().l());

            consumedTimelines[shiftedFromL] = true;
            if (move.to().t() == toTimeline.getLastT()) consumedTimelines[shiftedToL] = true;
        }

        // Check if all mandatory timelines have been consumed
        for (BitTimeline timeline : this.multiverse.getTimelines()) {
            int l = timeline.getL() - this.multiverse.getBotTimelineL();
            if (!mandatoryTimelines[l]) continue;
            if (!consumedTimelines[l]) {
                // If a mandatory timeline hasn't been played on, the turn is not finalizable
                return false;
            }
        }

        return true;
    }

    private void removeLastT(BitTimeline timeline) {
        int count = this.lastTimesOfTimelines.get(timeline.getLastT());
        if (count == 1) this.lastTimesOfTimelines.remove(timeline.getLastT());
        else this.lastTimesOfTimelines.put(timeline.getLastT(), count - 1);
    }

    private void addLastT(BitTimeline timeline) {
        int count = this.lastTimesOfTimelines.getOrDefault(timeline.getLastT(), 0);
        this.lastTimesOfTimelines.put(timeline.getLastT(), count + 1);
    }

    public BitMultiverse getMultiverse() {
        return this.multiverse;
    }

    public Color getPlayerTurn() {
        return this.playerTurn;
    }

    public int getPresentTime() {
        return this.lastTimesOfTimelines.firstKey();
    }

    public int getCalculatedPresentTime() {
        return this.lastTimesOfTimelines.firstKey();
    }

    public boolean isGameOver() {
        return this.gameOver;
    }

    public Color getWinner() {
        return this.winner;
    }

    public void clearTurnHistory() {

        this.archivedTurnEffects.addAll(turnEffects);

        this.turnEffects.clear();
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (!(other instanceof Game game)) return false;
        return this.hashCode() == game.hashCode();
    }

    @Override
    public int hashCode() {
        return Objects.hash(multiverse, currentTurnMoveEffects, turnEffects, playerTurn, gameOver, winner);
    }

    @Override
    public String toString() {
        StringBuilder representation = new StringBuilder();
        representation.append("All turns played:\n");
        for (List<MoveEffect> moveEffect : this.turnEffects) {
            representation.append("  ").append(moveEffect).append("\n");
        }
        representation.append("Current turn: ").append(this.currentTurnMoveEffects).append("\n");
        representation.append("presentTime=").append(getPresentTime()).append("\n");
        representation.append("playerTurn=").append(playerTurn).append("\n");
        representation.append("gameOver=").append(gameOver).append("\n");
        representation.append("winner=").append(winner).append("\n");

        return representation.toString();
    }
}
