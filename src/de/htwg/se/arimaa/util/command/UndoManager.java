package de.htwg.se.arimaa.util.command;

import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;

import de.htwg.se.arimaa.controller.impl.MoveFigureCommand;
import de.htwg.se.arimaa.model.FIGURE_NAME;
import de.htwg.se.arimaa.model.PLAYER_NAME;
import de.htwg.se.arimaa.util.position.Position;

public class UndoManager {

	private Deque<UndoableMoveFigureCommand> undoStack;
	private Deque<UndoableMoveFigureCommand> redoStack;

	private UndoableMoveFigureCommand topCommand;

	public UndoManager() {
		undoStack = new LinkedList<UndoableMoveFigureCommand>();
		redoStack = new LinkedList<UndoableMoveFigureCommand>();
		topCommand = null;
	}

	public void doCommand(UndoableMoveFigureCommand newCommand) {
		newCommand.doCommand();
		undoStack.push(newCommand);
		redoStack = new LinkedList<UndoableMoveFigureCommand>(); // delete old branch
	}

	public void undoCommand() {
		if (!undoStack.isEmpty()) {
			topCommand = undoStack.pop();
			topCommand.undoCommand();
			redoStack.push(topCommand);
		}
	}

	public void redoCommand() {
		if (!redoStack.isEmpty()) {
			topCommand = redoStack.pop();
			topCommand.redoCommand();
			undoStack.push(topCommand); // switch forth
		}
	}

	public void reset() {
		undoStack.clear();
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();

		ArrayList<UndoableMoveFigureCommand> undoList = new ArrayList<>(undoStack);
		undoList = reverse(undoList);

		String currentPlayerName = "Silver";
		int row = 0;
		int collumn = 0;
		for (int i = 0; i < undoList.size(); i++) {
			String line[] = undoList.get(i).toString().split("#");
			if (!currentPlayerName.equals(line[0])) {
				currentPlayerName = line[0];

				if (currentPlayerName.equals(PLAYER_NAME.GOLD.toString()))
					row++;

				if (i > 0 && collumn < 4) {
					collumn = 0;
					sb.append("pass ");
				}

				if (i > 0)
					sb.append("\n");

				String playerNameNotation = currentPlayerName.substring(0, 1).toLowerCase();
				sb.append(row + playerNameNotation + " ");
			}

			sb.append(line[1] + " ");
			collumn++;
		}

		return sb.toString();
	}

	private ArrayList<UndoableMoveFigureCommand> reverse(ArrayList<UndoableMoveFigureCommand> undoList) {
		for (int i = 0, j = undoList.size() - 1; i < j; i++) {
			undoList.add(i, undoList.remove(j));
		}
		return undoList;
	}

	public boolean isUndoListEmpty() {
		return undoStack.isEmpty();
	}

	public boolean isRedoListEmpty() {
		return redoStack.isEmpty();
	}

	public FIGURE_NAME getLastMoveFigureName() {
		if (undoStack.isEmpty())
			return null;

		return undoStack.peek().getFigureName();
	}

	public Position getLastMoveFromPosition() {
		if (undoStack.isEmpty())
			return null;
		
		return undoStack.peek().getFromPosition();
	}

}
