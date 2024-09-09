package tetris;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import javax.swing.*;

public class Tetris {

	private static ScheduledExecutorService service;
	static final Color[] COLORS = { Color.BLACK, Color.BLUE, Color.RED, Color.GREEN, Color.CYAN, Color.MAGENTA,
			Color.ORANGE, Color.YELLOW };

	public static void main(String[] args) {

		JFrame frame = new JFrame("Tetris");
		TetrisModel model = new TetrisModel(TetrisModel.DEFAULT_WIDTH, TetrisModel.DEFAULT_HEIGHT,
				TetrisModel.DEFAULT_COLORS_NUMBER);
		View view = new View(model);

		JPanel sidePanel = new JPanel(new GridBagLayout());
		sidePanel.setPreferredSize(new Dimension(150, 700));

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.insets = new Insets(10, 10, 10, 40);
		gbc.anchor = GridBagConstraints.WEST;

		JLabel scoreLabel = new JLabel("Score: " + TetrisModel.SCORE);
		JLabel levelLabel = new JLabel("Level: " + TetrisModel.LEVEL);
		scoreLabel.setFont(new Font("Arial", Font.BOLD, 20));
		levelLabel.setFont(new Font("Arial", Font.BOLD, 20));

		sidePanel.add(scoreLabel, gbc);
		gbc.gridy = 1;
		sidePanel.add(levelLabel, gbc);

		frame.setLayout(new BorderLayout());
		frame.add(view, BorderLayout.CENTER);
		frame.add(sidePanel, BorderLayout.EAST);

		frame.pack();
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		frame.setVisible(true);

		Controller controller = new Controller(model, view);

		model.addListener(new ModelListener() {
			@Override
			public void onChange(TetrisModel model) {
				scoreLabel.setText("Score: " + TetrisModel.SCORE);
				levelLabel.setText("Level: " + TetrisModel.LEVEL);
				view.repaint();
			}

			@Override
			public void gameOver() {
				view.setGameOver(true);
				frame.add(view.getGameOverPanel(), BorderLayout.CENTER);
				frame.revalidate();
				view.repaint();
				service.shutdown();
			}
		});

		frame.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				switch (e.getKeyCode()) {
					case KeyEvent.VK_LEFT -> {
						controller.moveLeft();
						controller.removeRow();
					}
					case KeyEvent.VK_RIGHT -> {
						controller.moveRight();
						controller.removeRow();
					}
					case KeyEvent.VK_UP -> {
						controller.rotate();
						controller.removeRow();
					}
					case KeyEvent.VK_DOWN -> {
						controller.drop();
						controller.removeRow();
					}
				}
			}
		});

		service = Executors.newSingleThreadScheduledExecutor();
		int speed = TetrisModel.SPEED;
		service.scheduleAtFixedRate(controller::slideDown, 0, speed, TimeUnit.MILLISECONDS);
	}
}
