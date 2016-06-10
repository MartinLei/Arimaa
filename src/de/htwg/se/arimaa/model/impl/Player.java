package de.htwg.se.arimaa.model.impl;

import java.util.List;

import de.htwg.se.arimaa.model.IPlayer;
import de.htwg.se.arimaa.util.character.Position;

public class Player implements IPlayer {
	private List<Character> figures;
	private String playerName;

	public Player(String playerName, List<Character> figures) {
		this.playerName = playerName;
		this.figures = figures;
	}



	public CHARAKTER_NAME getFigur(Position pos) {
		for (Character cr : figures) {
			if (pos.equals(cr.getPosition()))
				return cr.getName();
		}
		return null;
	}

	public String getPlayerName() {
		return playerName;
	}

	public boolean setFigureChangePositon(Position start, Position end) {
		for (Character cr : figures) {
			if (cr.getPosition().equals(start)) {
				cr.setPosition(end);
				return true;
			}
		}
		return false;
	}
}
