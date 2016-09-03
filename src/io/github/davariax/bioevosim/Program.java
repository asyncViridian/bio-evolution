package io.github.davariax.bioevosim;

import java.util.ArrayList;

public class Program {
	static int dimCreatSquare;

	public static void main(String[] args) throws InterruptedException {
		ScreenInput.initInput(400, 300);
		// Scanner console = new Scanner(System.in);
		// System.out.println("Starting board X?");
		// int sizeX = console.nextInt();
		int sizeX = ScreenInput.queryInt("Starting board X?");
		// System.out.println("Starting board Y?");
		// int sizeY = console.nextInt();
		int sizeY = 5;
		// System.out.println("Starting board predator/prey count EACH?");
		// int countEach = console.nextInt();
		int countEach = 8;
		// System.out.println("Starting stat lower bound?");
		// int lowerBound = console.nextInt();
		int lowerBound = 2;
		// System.out.println("Starting stat upper bound?");
		// int upperBound = console.nextInt();
		int upperBound = 8;
		// System.out.println("How many iterations to run?");
		// int iterations = console.nextInt();
		int iterations = 100;
		dimCreatSquare = 4;
		// console.close();
		// TODO fix number input system
		Board board = new Board(sizeX, sizeY, countEach, lowerBound, upperBound);
		initBoard(board);
		for (int i = 0; i < iterations; i++) {
			board.randomize();
			updateBoard(board);
			board.sortByStat();
			board.feast();
			board.checkSurvivors();
			board.reproduce();
			while (!StdDraw.mousePressed() && !ScreenInput.spaceKeyPressed()) {
				// Wait until mouse click or keypress to move on
				Thread.sleep(100);
			}
		}
	}

	public static void initBoard(Board board) {
		int dim = Math.min(1000, Math.max(board.sizeX, board.sizeY) * 150);
		StdDraw.enableDoubleBuffering();
		StdDraw.setPenColor();
		StdDraw.setCanvasSize(dim, dim);
		StdDraw.setScale(-.05, 1.05);
		StdDraw.setPenRadius(0.005);
	}

	public static void updateBoard(Board board) {
		StdDraw.clear();
		StdDraw.setPenColor();
		drawCardsGrid(board.sizeX, board.sizeY);
		StdDraw.textRight(1, -0.025, "Click or Space Key To Advance");
		for (int posX = 0; posX < board.sizeX; posX++) {
			for (int posY = 0; posY < board.sizeY; posY++) {
				drawCellContents(board, posX, posY);
			}
		}
		StdDraw.show();
	}

	public static void drawCreatureStat(Creature creature, double posX, double posY, double size) {
		if (creature instanceof Predator) {
			StdDraw.setPenColor(StdDraw.RED);
		} else {
			StdDraw.setPenColor(StdDraw.GREEN);
		}
		StdDraw.circle(posX, posY, size);
		StdDraw.text(posX, posY, "" + creature.stat);
	}

	public static void drawCellContents(Board board, int coordX, int coordY) {
		ArrayList<Creature> cellcontents = board.getCellContents(coordX, coordY);
		double cellWidth = cellDimension(board.sizeX);
		double cellHeight = cellDimension(board.sizeY);
		double posTop = cellHeight * (coordY + 1);
		double posBot = cellHeight * coordY;
		double posLeft = cellWidth * coordX;
		double posRight = cellWidth * (coordX + 1);
		double intervalUD = (posTop - posBot) / (dimCreatSquare + 1);
		double intervalLR = (posRight - posLeft) / (dimCreatSquare + 1);
		int creatureNum = 0;
		for (int i = 0; i < ((double) cellcontents.size()) / dimCreatSquare; i++) {
			for (int j = 0; j < dimCreatSquare && creatureNum < cellcontents.size(); j++) {
				drawCreatureStat(cellcontents.get(creatureNum), posLeft + intervalLR * (j + 1),
						posBot + intervalUD * (i + 1), Math.min(cellWidth, cellHeight) / (2 * (dimCreatSquare + 2)));
				creatureNum++;
			}
		}
	}

	public static void drawCardsGrid(int cellsX, int cellsY) {
		double cellWidth = cellDimension(cellsX);
		double cellHeight = cellDimension(cellsY);
		for (int i = 0; i <= cellsX; i++) {
			StdDraw.line(i * cellWidth, 0, i * cellWidth, 1);
		}
		for (int i = 0; i <= cellsY; i++) {
			StdDraw.line(0, i * cellHeight, 1, i * cellHeight);
		}
	}

	public static double cellDimension(int dimension) {
		return 1. / dimension;
	}
}