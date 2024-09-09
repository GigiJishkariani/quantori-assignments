package tetris;

public interface GameEventsListener {
	
	void slideDown();

	void gameOver();

	void moveLeft();
	void moveRight();
	void rotate();
	void drop();
	void removeRow();
}
