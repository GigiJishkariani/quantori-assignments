package tetris;

import java.util.Random;

public class FigureFactory {

	private static final Move[] moves = new Move[] {
			new Move() { public int[][] move() { return O(); } },
			new Move() { public int[][] move() { return J(); } },
			new Move() { public int[][] move() { return T(); } },
			new Move() { public int[][] move() { return I(); } },
			new Move() { public int[][] move() { return Z(); } },
			new Move() { public int[][] move() { return L(); } },
			new Move() { public int[][] move() { return S(); } },
	};


	public static int[][] createNextFigure() {
		return moves[new Random().nextInt(moves.length)].move();
	}

	static int[][] O() {
		return new int[][] {
			{0, 1, 1, 0},
			{0, 1, 1, 0},
			{0, 0, 0, 0},
			{0, 0, 0, 0},
		};
	}

	static int[][] J() {
		return new int[][] {
			{0, 0, 2, 0},
			{0, 0, 2, 0},
			{0, 2, 2, 0},
			{0, 0, 0, 0},
		};
	}

	static int[][] T() {
		return new int[][] {
				{0, 0, 0, 0},
				{0, 0, 0, 0},
				{0, 0, 3, 0},
				{0, 3, 3, 3},
		};
	}

	static int[][] I() {
		return new int[][] {
				{0, 0, 0, 0},
				{4, 4, 4, 4},
				{0, 0, 0, 0},
				{0, 0, 0, 0},
		};
	}

	static int[][] Z() {
		return new int[][] {
				{0, 0, 0, 0},
				{5, 5, 0, 0},
				{0, 5, 5, 0},
				{0, 0, 0, 0},
		};
	}

	static int[][] L() {
		return new int[][] {
				{0, 6, 0, 0},
				{0, 6, 0, 0},
				{0, 6, 6, 0},
				{0, 0, 0, 0},
		};
	}

	static int[][] S() {
		return new int[][] {
				{0, 0, 0, 0},
				{0, 0, 7, 7},
				{0, 7, 7, 0},
				{0, 0, 0, 0},
		};
	}

	
}
