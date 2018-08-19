package aview.gui;

import com.google.inject.Inject;
import controller.GameStatus;
import controller.IArimaaController;
import util.observer.Event;
import util.observer.IObserver;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class ArimaaFrame extends JFrame implements IObserver {

	private static final int DEFAULT_Y = 520;
	private static final int DEFAULT_X = 720;

	private InfoPanel infoPanel;
	private PitchPanel pitchPanel;

	private ButtonPanel buttonPanel;
	private StatusPanel statusPanel;

	private MoveHistoryPanel moveStatusPanel;

	private Container pane;
	private IArimaaController controller;

	@Inject
	public ArimaaFrame(final IArimaaController controller) {
		this.controller = controller;
		controller.addObserver(this);

		this.setTitle("HTWG Arimaa");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(DEFAULT_X, DEFAULT_Y);
		this.setJMenuBar(new ArimaaMenuBar(controller));
		pane = getContentPane();
		pane.setLayout(new BorderLayout());

		constructArimaaPane(controller);

		// Closing window handler
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent we) {
				controller.quitGame();
			}
		});
	}

	private void constructArimaaPane(IArimaaController controller) {
		JPanel centerPanel = new JPanel();
		pane.add(centerPanel, BorderLayout.CENTER);

		JPanel leftPanel = new JPanel(new GridLayout(1, 0));

		leftPanel.setPreferredSize(new Dimension(440, 440));
		centerPanel.add(leftPanel);

		pitchPanel = new PitchPanel(controller);
		leftPanel.add(pitchPanel);

		JPanel rightPanel = new JPanel();
		rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.PAGE_AXIS));
		centerPanel.add(rightPanel);

		infoPanel = new InfoPanel(controller);
		rightPanel.add(infoPanel);

		moveStatusPanel = new MoveHistoryPanel(controller);
		rightPanel.add(moveStatusPanel);

		buttonPanel = new ButtonPanel(controller);
		rightPanel.add(buttonPanel);

		statusPanel = new StatusPanel();
		pane.add(statusPanel, BorderLayout.SOUTH);

		setVisible(true);
		repaint();

	}

	@Override
	public void update(Event e) {
		statusPanel.setText(controller.getGameStatus(), controller.getStatusText());

		GameStatus gs = controller.getGameStatus();
		if (gs.equals(GameStatus.EXIT)) {
			this.setVisible(false);
			this.dispose();
			return;
		}

		repaint();
	}

}