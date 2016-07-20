package de.htwg.se.arimaa.arimaa;

import java.util.Scanner;

import com.google.inject.Guice;
import com.google.inject.Injector;

import de.htwg.se.arimaa.controller.IArimaaController;
import de.htwg.se.arimaa.view.TextUI;
import de.htwg.se.arimaa.view.gui.ArimaaFrame;

public class Arimaa {

	private static Scanner scanner;
	private TextUI tui;
	private ArimaaFrame gui;
	protected IArimaaController controller;
	private static Arimaa instance = null;
	
	private Arimaa() {
		Injector injector = Guice.createInjector(new ArimaaModule());
		
		controller = injector.getInstance(IArimaaController.class);
		tui = new TextUI(controller);
		gui = new ArimaaFrame(controller);
	}

	public TextUI getTui() {
		return tui;
	}

	public ArimaaFrame getGui() {
		return gui;
	}
   
	public static Arimaa getInstance(){
		if(instance == null){
			instance = new Arimaa();	
		}
		return instance;
	}
	public static void main(final String[] args) {
		Arimaa game = Arimaa.getInstance();

		game.tui.showPitch();
		boolean continu = true;
		scanner = new Scanner(System.in);
		while (continu) {
			System.out.println("Bitte um Eingabe: "); // TODO refactor
			continu = game.tui.processInputLine(scanner.next());
		}

	}

}
