package tetris;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


public class TetrisTests {

	private TetrisModel model;
	
	@BeforeEach
	public void setup() {
		model = new TetrisModel(TetrisModel.DEFAULT_WIDTH, TetrisModel.DEFAULT_HEIGHT, TetrisModel.DEFAULT_COLORS_NUMBER);
	}

	@Test
	public void testCreationOfModel() {
		Pair p = model.size();
		assertEquals(TetrisModel.DEFAULT_WIDTH, p.x());
		assertEquals(TetrisModel.DEFAULT_HEIGHT, p.y());
		testFieldExsistence();
	}

	@Test
	public void testFieldExsistence() {
		int[][] field = model.state.field;
		assertNotNull(field);
		assertEquals(TetrisModel.DEFAULT_HEIGHT, field.length);
	}
	
	@Test
	public void testColorsRange() throws Exception {
		assertEquals(TetrisModel.DEFAULT_COLORS_NUMBER, model.maxColors);
	}

	@Test
	public void testFigureCreated() throws Exception {
		int[][] figure = model.state.figure;
		assertNotNull(figure);
	}
	
	@Test
	public void positionExists() throws Exception {
		Pair p = model.state.position;
		assertNotNull(p);
		assertEquals(0, p.y());
		assertEquals(model.size().x() / 2 - 2, p.x());
	}
	
	@Test
	public void testGameEventsListener() throws Exception {
		assertTrue(GameEventsListener.class.isAssignableFrom(model.getClass()));
	}
	
	@Test
	public void testSlideDown() throws Exception {
		int old = model.state.position.y();
		model.slideDown();
		assertEquals(old + 1, model.state.position.y());
	}
	

	
	@Test
	public void testMoveLeft() throws Exception {
		var oldPos = model.state.position;
		model.moveLeft();
		assertEquals(oldPos.x() - 1, model.state.position.x());
	}
	
	@Test
	public void testMoveRight() throws Exception {
		var oldPos = model.state.position;
		model.moveRight();
		assertEquals(oldPos.x() + 1, model.state.position.x());
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
