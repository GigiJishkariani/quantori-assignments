package tetris;

public class Controller implements ModelListener, GameEventsListener {

	private TetrisModel model;
	private View view;

	public Controller(TetrisModel model, View view) {
		this.model = model;
		model.addListener(this);
		this.view = view;
	}

	@Override
	public void onChange(TetrisModel model) {
		view.repaint();
	}

	@Override
	public void slideDown() {
		model.slideDown();
		view.repaint();
	}

	@Override
	public void moveLeft() {
		model.moveLeft();
		view.repaint();
	}

	@Override
	public void moveRight() {
		model.moveRight();
		view.repaint();
	}

	@Override
	public void rotate() {
		model.rotate();
		view.repaint();
	}

	@Override
	public void drop() {
		model.drop();
		view.repaint();
	}

	@Override
	public void gameOver() {
		view.setGameOver(true);
		view.repaint();
	}

	@Override
	public void removeRow() {
		model.removeRow();
		view.repaint();
	}
}
