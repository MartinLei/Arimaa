package controller.impl;

import com.google.inject.Inject;
import controller.GameStatus;
import controller.IArimaaController;
import model.FIGURE_NAME;
import model.IFigure;
import model.IPitch;
import model.PLAYER_NAME;
import model.impl.Pitch;
import util.command.UndoManager;
import util.observer.Observable;
import util.position.Position;

import java.util.List;

public class ArimaaController extends Observable implements IArimaaController {
    private UndoManager undoManager;

    private IPitch pitch;
    private Rules rules;

    private GameStatus status;
    private String statusText;

    private boolean changePlayerEable;

    @Inject
    public ArimaaController() {
        initArimaaController();
    }

    private void initArimaaController() {
        pitch = new Pitch();
        rules = new Rules(this);
        undoManager = new UndoManager();

        status = GameStatus.CREATE;
        statusText = "New game started";

        changePlayerEable = false;
    }

    @Override
    public int getRemainingMoves() {
        return pitch.getRemainingMoves();
    }

    @Override
    public GameStatus getGameStatus() {
        return status;
    }

    @Override
    public String getStatusText() {
        return statusText;
    }

    @Override
    public void quitGame() {
        status = GameStatus.EXIT;
        statusText = "Have a nice day ;)";
        notifyObservers();
    }

    @Override
    public void changePlayer() {
        if (!changePlayerEable)
            return;

        if (status.equals(GameStatus.PUSHFIGURE) || status.equals(GameStatus.FINISH))
            return;

        pitch.changePlayer();

        changePlayerEable = false;

        status = GameStatus.CHANGEPLAYER;
        statusText = pitch.getCurrentPlayerName().toString() + " it’s your turn";
        notifyObservers();
    }

    @Override
    public void createNewGame() {
        initArimaaController();
        status = GameStatus.CREATE;
        statusText = "New game started";
        notifyObservers();
    }

    @Override
    public void undo() {
        undoManager.undoCommand();

        status = GameStatus.UNDO;
        statusText = "-";
        notifyObservers();
    }

    @Override
    public void redo() {
        undoManager.redoCommand();

        status = GameStatus.REDO;
        statusText = "-";
        notifyObservers();
    }

    @Override
    public PLAYER_NAME getCurrentPlayerName() {
        return pitch.getCurrentPlayerName();
    }

    @Override
    public String getPitchView() {
        return pitch.toString();
    }

    @Override
    public boolean moveFigure(Position from, Position to) {
        if (status.equals(GameStatus.FINISH))
            return false;

        // Preconditions
        PLAYER_NAME currenPlayerName = getCurrentPlayerName();
        boolean moved = rules.precondition(currenPlayerName, from, to);
        if (moved) {

            // Move the figure
            undoManager.doCommand(new MoveFigureCommand(pitch, from, to));

            changePlayerEable = true;

            // reduce remaining moves
            pitch.reduceRemainingMoves();
        }

        // Postconditions
        rules.postcondition(to);

        status = rules.getStatus();
        statusText = rules.getStatusText();

        notifyObservers();
        return moved;
    }

    @Override
    public List<IFigure> getGoldFigures() {
        return pitch.getGoldPlayer().getFigures();
    }

    @Override
    public List<IFigure> getSilverFigures() {
        return pitch.getSilverPlayer().getFigures();
    }

    @Override
    public String getMoveHistoryText() {
        return undoManager.toString();
    }

    @Override
    public PLAYER_NAME getPlayerName(Position pos) {
        return pitch.getPlayerName(pos);
    }

    @Override
    public FIGURE_NAME getFigureName(Position pos) {
        return pitch.getFigureName(pos);
    }

    @Override
    public boolean isUndoListEmpty() {
        return undoManager.isUndoListEmpty();
    }

    @Override
    public boolean isRedoListEmpty() {
        return undoManager.isRedoListEmpty();
    }

    @Override
    public List<Position> getPossibleMoves(Position from) {
        PLAYER_NAME currenPlayerName = getCurrentPlayerName();
        return rules.getPossibleMoves(currenPlayerName, from);
    }

    @Override
    public boolean isChangePlayerEnable() {
        return changePlayerEable && !status.equals(GameStatus.PUSHFIGURE);
    }

    @Override
    public FIGURE_NAME getLastMoveFigureName() {
        return undoManager.getLastMoveFigureName();
    }

    @Override
    public Position getLastMoveFromPosition() {
        return undoManager.getLastMoveFromPosition();
    }

    @Override
    public void disableFigure(Position pos) {
        pitch.disableFigure(pos);
    }

    @Override
    public boolean noRabbits(PLAYER_NAME playerName) {
        return pitch.noRabbits(playerName);
    }
}