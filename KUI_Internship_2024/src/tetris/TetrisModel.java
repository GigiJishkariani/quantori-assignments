package tetris;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

public class TetrisModel implements GameEventsListener {

	public static final int DEFAULT_HEIGHT = 20;
	public static final int DEFAULT_WIDTH = 10;
	public static final int DEFAULT_COLORS_NUMBER = 7;
	public static boolean IS_LOST = false;

	public static int SPEED = 1000;

	public static int LEVEL = 0;
	public static int STARTING_LEVEL = 0;

	public static int ROWS_CLEARED_NUM = 0;
	public static int ROWS_CLEARED_CURRENTNUM = 0;
	public static int SCORE = 0;
	public static int STARTING_SCORE = 0;

	int maxColors;
	public int[][] field;
	public int[][] figure;
	public Pair position;
	public TetrisState state = new TetrisState();
	final List<ModelListener> listeners = new ArrayList<>();

	public void addListener(ModelListener listener) {
		listeners.add(listener);
	}

	public void removeListener(ModelListener listener) {
		listeners.remove(listener);
	}

	public TetrisModel(int width, int height, int maxColors) {
		this.state.width = width;
		this.state.height = height;
		this.maxColors = maxColors;
		state.field = new int[height][width];
		initFigure();
	}

	private void initFigure() {
		state.figure = FigureFactory.createNextFigure();
		state.position = new Pair(state.width / 2 - 2, 0);
	}

	public Pair size() {
		return new Pair(state.width, state.height);
	}

	@Override
	public void slideDown() {
		var newPosition = new Pair(state.position.x(), state.position.y() + 1);
		if (isNewFiguresPositionValid(newPosition)) {
			state.position = newPosition;
			notifyListeners();
		} else {
			pasteFigure();
			initFigure();
			notifyListeners();
			if (!isNewFiguresPositionValid(state.position)) {
				gameOver();
			}
		}
	}

	private void notifyListeners() {
		listeners.forEach(listener -> listener.onChange(this));
	}

	@Override
	public void gameOver() {
		System.out.println("Game Over");
		IS_LOST = true;
		listeners.forEach(ModelListener::gameOver);
	}


	public void restartGame(int width, int height, int colorsNumber) {
		this.field = new int[height][width];
		this.position = new Pair(0, 0); // Reset position
		SCORE = STARTING_SCORE;
		LEVEL = STARTING_LEVEL;
		initFigure();
	}

	@Override
	public void moveLeft() {
		var newPosition = new Pair(state.position.x() - 1, state.position.y());
		if (isNewFiguresPositionValid(newPosition)) {
			state.position = newPosition;
			notifyListeners();
		}
	}

	@Override
	public void moveRight() {
		var newPosition = new Pair(state.position.x() + 1, state.position.y());
		if (isNewFiguresPositionValid(newPosition)) {
			state.position = newPosition;
			notifyListeners();
		}
	}

	@Override
	public void rotate() {
		int length = state.figure.length;
		int[][] f = new int[length][length];
		for (int r = 0; r < state.figure.length; r++) {
			for (int c = 0; c < state.figure[r].length; c++) {
				f[c][3 - r] = state.figure[r][c];
			}
		}
		state.figure = f;
		notifyListeners();
	}

	@Override
	public void drop() {
		Pair newPosition = new Pair(state.position.x(), state.position.y() + 1);
		if (isNewFiguresPositionValid(newPosition)) {
			state.position = newPosition;
			notifyListeners();
		}
	}

	public boolean isNewFiguresPositionValid(Pair newPosition) {

		boolean[] result = new boolean[1];
		result[0] = true;

		walkThroughAllFigureCells(newPosition, (absPos, relPos) -> {
			if (result[0]) {
				result[0] = checkAbsPos(absPos);
			}
		});

		return result[0];
	}

	@Override
	public void removeRow(){
		for (int i = 0; i < state.field.length; i++) {
			boolean isFull = true;
			for (int j = 0; j < state.field[i].length; j++) {
				if (state.field[i][j] == 0){
					isFull = false;
					break;
				}
			}
			if(isFull){
				for (int k = i; k > 0; k--) {
					for (int j = 0; j < state.field[k].length; j++) {
						state.field[k][j] = state.field[k-1][j];
					}
				}
				for (int j = 0; j < state.field[0].length; j++) {
					state.field[0][j] = 0;
				}
				i--;
				ROWS_CLEARED_NUM += 1;
				ROWS_CLEARED_CURRENTNUM += 1;
				int currentLevel = checkLevel();
				System.out.println("LEVEL : " +LEVEL);
				System.out.println("SCORE : " + SCORE);
				getScore(ROWS_CLEARED_CURRENTNUM, currentLevel);
				ROWS_CLEARED_CURRENTNUM = 0;
			}
		}
		notifyListeners();
	}

	private void getScore(int rowsClearedCurrentnum, int currentLevel) {
		switch (rowsClearedCurrentnum){
			case 1:
				SCORE += 40 * (currentLevel + 1);
				break;
			case 2:
				SCORE += 100 * (currentLevel + 1);
				break;
			case 3:
				SCORE += 300 * (currentLevel + 1);
				break;
			case 4:
				SCORE += 1200 * (currentLevel + 1);
				break;

		}

		changeSpeed();
	}

	private void changeSpeed() {
		int newSpeed = SPEED;
		if(SCORE >= 1000){
			newSpeed += 3000;
		} else if (SCORE >= 500) {
			newSpeed += 2000;
		} else if (SCORE >= 250) {
			newSpeed += 1000;
		} else if (SCORE >= 125) {
			newSpeed += 500;
		} else {
			newSpeed = SPEED;
		}
		SPEED = newSpeed;

		System.out.println("SPEED : "+ SPEED);

	}

	public int checkLevel() {
		if(ROWS_CLEARED_NUM == 2){
			LEVEL += 1;
			ROWS_CLEARED_NUM = 0;

		}
		return LEVEL;
	}


	private void walkThroughAllFigureCells(Pair newPosition, BiConsumer<Pair, Pair> payload) {
		for (int row = 0; row < state.figure.length; row++) {
			for (int col = 0; col < state.figure[row].length; col++) {
				if (state.figure[row][col] == 0)
					continue;
				int absCol = newPosition.x() + col;
				int absRow = newPosition.y() + row;
				payload.accept(new Pair(absCol, absRow), new Pair(col, row));
			}
		}
	}

	private boolean checkAbsPos(Pair absPos) {
		var absCol = absPos.x();
		var absRow = absPos.y();
		if (isColumnPositionOutOfBoundaries(absCol))
			return false;
		if (isRowPositionOutOfBoundaries(absRow))
			return false;
		if (state.field[absRow][absCol] != 0)
			return false;
		return true;
	}

	private boolean isRowPositionOutOfBoundaries(int absRow) {
		return absRow < 0 || absRow >= state.height;
	}

	private boolean isColumnPositionOutOfBoundaries(int absCol) {
		return absCol < 0 || absCol >= state.width;
	}

	public void pasteFigure() {
		walkThroughAllFigureCells(state.position, (absPos, relPos) -> {
			state.field[absPos.y()][absPos.x()] = state.figure[relPos.y()][relPos.x()];
		});
	}

}
