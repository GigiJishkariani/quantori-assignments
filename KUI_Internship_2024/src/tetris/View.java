package tetris;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Graphics;


public class View extends JPanel {

	static final int BOX_SIZE = 30;
	private boolean gameOver;
	private final TetrisModel model;

	public View(TetrisModel model) {
		this.model = model;
		setPreferredSize(new Dimension(500, 700));
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D graphics = (Graphics2D) g;

		if (gameOver) {
			drawGameOver(graphics);
		} else {
			drawBoard(graphics, model);
			drawFigure(graphics, model);
		}
	}

	public void setGameOver(boolean gameOver) {
		this.gameOver = gameOver;
		repaint();
	}

	public boolean isGameOver() {
		return gameOver;
	}

	private void drawGameOver(Graphics2D graphics) {
		graphics.setColor(Color.white);
		graphics.fillRect(0, 0, getWidth(), getHeight());

		graphics.setColor(Color.BLACK);
		graphics.setFont(new Font("Arial", Font.BOLD, 50));
		String message = "GAME OVER";
		int messageWidth = graphics.getFontMetrics().stringWidth(message);
		int messageHeight = graphics.getFontMetrics().getAscent();
		graphics.drawString(message, (getWidth() - messageWidth) / 2, (getHeight() + messageHeight) / 2);
	}

	private void drawBoard(Graphics2D graphics, TetrisModel model) {
		int[][] field = model.state.field;
		for (int row = 0; row < field.length; row++) {
			for (int col = 0; col < field[row].length; col++) {
				int colorIndex = field[row][col];
				if (colorIndex != 0) {
					graphics.setColor(Tetris.COLORS[colorIndex]);
				} else {
					graphics.setColor(Color.BLACK);
				}
				graphics.fillRect(20 + col * BOX_SIZE, 30 + row * BOX_SIZE, BOX_SIZE, BOX_SIZE);
				graphics.drawRect(20 + col * BOX_SIZE, 30 + row * BOX_SIZE, BOX_SIZE, BOX_SIZE);
			}
		}
	}

	private void drawFigure(Graphics2D graphics, TetrisModel model) {
		int[][] figure = model.state.figure;
		Pair position = model.state.position;
		for (int row = 0; row < figure.length; row++) {
			for (int col = 0; col < figure[row].length; col++) {
				if (figure[row][col] != 0) {
					graphics.setColor(Tetris.COLORS[figure[row][col]]);
					graphics.fillRect(20 + (position.x() + col) * BOX_SIZE, 30 + (position.y() + row) * BOX_SIZE, BOX_SIZE, BOX_SIZE);
				}
			}
		}
	}

	public JPanel getGameOverPanel() {
		JPanel gameOverPanel = new JPanel(new BorderLayout());

		JLabel gameOverLabel = new JLabel("GAME OVER", SwingConstants.CENTER);
		gameOverLabel.setFont(new Font("Arial", Font.BOLD, 50));
		gameOverPanel.add(gameOverLabel, BorderLayout.CENTER);

		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new GridLayout(1, 1, 100, 100)); // Vertical layout with spacing

		JButton quitButton = new JButton("Quit");
		quitButton.setFont(new Font("Arial", Font.BOLD, 30));
		quitButton.setPreferredSize(new Dimension(200, 50));
		buttonPanel.add(quitButton);

		gameOverPanel.add(buttonPanel, BorderLayout.SOUTH);

		quitButton.addActionListener(e -> System.exit(0));

		return gameOverPanel;
	}
}
