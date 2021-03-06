package main;

import aview.gui.ArimaaFrame;
import aview.tui.TextUI;
import com.google.inject.Guice;
import com.google.inject.Injector;
import controller.GameStatus;
import controller.IArimaaController;

import java.util.Scanner;

class Arimaa {
    private final TextUI tui;
    private final ArimaaFrame gui;
    private final IArimaaController controller;
    private static Arimaa instance = null;

    private Arimaa() {
        Injector injector = Guice.createInjector(new ArimaaModule());

        controller = injector.getInstance(IArimaaController.class);
        tui = new TextUI(controller);
        gui = new ArimaaFrame(controller);

        // init new game
        controller.createNewGame();


    }

    private static Arimaa getInstance() {
        if (instance == null) {
            instance = new Arimaa();
        }
        return instance;
    }

    private boolean exit() {
        return controller.getGameStatus().equals(GameStatus.EXIT);
    }

    public static void main(final String[] args) {
        Arimaa game = Arimaa.getInstance();

        Scanner scanner = new Scanner(System.in);
        while (!game.exit()) {
            game.tui.processInputLine(scanner.next());
        }

    }

}
