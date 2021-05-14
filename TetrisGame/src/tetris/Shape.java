package tetris;

import java.awt.Color;
import java.awt.Graphics;

/**
 * Shape class holds coordinates and shape of the class for rendering
 * 
 * @author nanarboursalian
 *
 */
public class Shape {

	/**
	 * Size of the Board and the each individual square block
	 */
	public static final int BOARD_WIDTH = 10, BOARD_HEIGHT = 20, BLOCK_SIZE = 30;

	/**
	 * Initial location of the shape
	 */
	private int x = 4, y = 0;

	/**
	 * Instance variables for tracking the speed of the downward movement of the
	 * Shape
	 */
	private int normal = 400;
	private int delayTimeForMovement = normal;
	private long beginTime;

	/**
	 * Handles left/right movement of a shape
	 */
	private int deltaX = 0;

	/**
	 * Determines if a shape has encountered into another shape or the outer walls
	 */
	private boolean collision = false;

	/**
	 * Coordinates of the shape that tell which block is filled in with the
	 * specified Color color
	 */
	private int[][] coords;
	private Color color;

	/**
	 * Board on which the Shape resides/moves on
	 */
	private Board board;

	/**
	 * Constructor that initializes the coords, board, and color of a Shape object
	 * 
	 * @param coords Tells which block is filled in
	 * @param board  Tells which Board the Shape is traversing
	 * @param color  Tells what color fills in the coords of the Shape
	 */
	public Shape(int[][] coords, Board board, Color color) {
		this.coords = coords;
		this.board = board;
		this.color = color;
		delayTimeForMovement = normal;
	}

	/**
	 * Set the x location of the Shape
	 * 
	 * @param x Location to be set
	 */
	public void setX(int x) {
		this.x = x;
	}

	/**
	 * Set the y location of the Shape
	 * 
	 * @param y Location to be set
	 */
	public void setY(int y) {
		this.y = y;
	}

	/**
	 * Resets the location of the shape to its original location
	 */
	public void reset() {
		this.x = 4;
		this.y = 0;
		collision = false;
	}

	/**
	 * Fills in the color for the board. Checks movement in the horizontal to ensure
	 * no collision. Checks movement in the vertical to ensure no collision.
	 */
	public void update() {
		if (collision) {
			// Fill the color for the board
			for (int row = 0; row < coords.length; row++) {
				for (int col = 0; col < coords[0].length; col++) {
					if (coords[row][col] != 0) {
						board.getBoard()[y + row][x + col] = color;
					}
				}
			}

			// Checks the lines of the board if there is a collision
			checkLine();
			board.addScore();
			// set current shape
			board.setCurrentShape();

			return;
		}

		// check horizontal movement
		boolean moveX = true;
		if (!((x + deltaX + coords[0].length) > 10) && !((x + deltaX) < 0)) {
			for (int row = 0; row < coords.length; row++) {
				for (int col = 0; col < coords[row].length; col++) {
					if (coords[row][col] != 0) {
						if (board.getBoard()[y + row][x + deltaX + col] != null) {
							moveX = false;
						}
					}
				}
			}
			if (moveX) {
				x += deltaX;
			}
		}
		deltaX = 0;

		if (System.currentTimeMillis() - beginTime > delayTimeForMovement) {
			// vertical movement
			if (!(y + 1 + coords.length > BOARD_HEIGHT)) {
				for (int row = 0; row < coords.length; row++) {
					for (int col = 0; col < coords[row].length; col++) {
						if (coords[row][col] != 0) {
							if (board.getBoard()[y + 1 + row][x + deltaX + col] != null) {
								collision = true;
							}
						}
					}
				}
				if (!collision) {

					y++; // moves block to the bottom of the board
				}
			} else {
				collision = true; // collides with the bottom
			}
			beginTime = System.currentTimeMillis();
		}
	}

	/**
	 * Counts the number of values in a row that are 1, and if the count is the
	 * amount of columns the grid has, the bottom line moves up one and the full
	 * line disappears
	 */
	private void checkLine() {
		int bottomLine = board.getBoard().length - 1;
		for (int topLine = board.getBoard().length - 1; topLine > 0; topLine--) {
			int count = 0; // use for counting the cells
			for (int col = 0; col < board.getBoard()[0].length; col++) {
				if (board.getBoard()[topLine][col] != null) {
					count++;
				}
				board.getBoard()[bottomLine][col] = board.getBoard()[topLine][col];
			}
			if (count < board.getBoard()[0].length) {
				bottomLine--; // move bottom line up one unit

			}
		}
	}

	/**
	 * Transposes and reverses the coords of a Shape and checks if rotation will
	 * collide with walls or another shape
	 * 
	 * Sets new Shape coords
	 */
	public void rotateShape() {
		int[][] rotatedShape = transposeMatrix(coords);
		reverseRows(rotatedShape);

		// check for the right side and the bottom
		if ((x + rotatedShape[0].length > BOARD_WIDTH) || (y + rotatedShape.length > BOARD_HEIGHT)) {
			return;
		}

		// check for collision with other shapes before rotating
		for (int row = 0; row < rotatedShape.length; row++) {
			for (int col = 0; col < rotatedShape[row].length; col++) {
				if (rotatedShape[row][col] != 0) {
					if (board.getBoard()[y + row][x + col] != null) {
						return;
					}
				}
			}
		}

		coords = rotatedShape;

	}

	/**
	 * Flips Shape's coords over on its diagonal
	 * 
	 * @param matrix Shape coords
	 * @return Flipped coords
	 */
	private int[][] transposeMatrix(int[][] matrix) {
		int[][] temp = new int[matrix[0].length][matrix.length];
		for (int row = 0; row < matrix.length; row++) {
			for (int col = 0; col < matrix[0].length; col++) {
				temp[col][row] = matrix[row][col];
			}
		}
		return temp;
	}

	/**
	 * Reverses the rows of the coords values of a Shape
	 * 
	 * @param matrix Shape coords
	 */
	private void reverseRows(int[][] matrix) {
		int middle = matrix.length / 2;
		for (int row = 0; row < middle; row++) {
			int[] temp = matrix[row];
			matrix[row] = matrix[matrix.length - row - 1];
			matrix[matrix.length - row - 1] = temp;
		}
	}

	/**
	 * Uses Graphics to fill in the slots from the coords where the value is 1 with
	 * the Color initialized in the constructor
	 * 
	 * @param g Handles the graphics and filling in the Shape
	 */
	public void render(Graphics g) {
		// draw the Shape
		for (int row = 0; row < coords.length; row++) {
			for (int col = 0; col < coords[0].length; col++) {
				if (coords[row][col] != 0) {
					g.setColor(color);
					g.fillRect(col * BLOCK_SIZE + x * BLOCK_SIZE, row * BLOCK_SIZE + y * BLOCK_SIZE, BLOCK_SIZE,
							BLOCK_SIZE);
				}
			}
		}
	}

	/**
	 * Increments x location by one to move Shape to the right
	 */
	public void moveRight() {
		deltaX = 1;
	}

	/**
	 * Decrements x location by one to move Shape to the left
	 */
	public void moveLeft() {
		deltaX = -1;
	}

	/**
	 * Returns the y location of the Shape
	 * 
	 * @return y
	 */
	public int getY() {
		return y;
	}

	/**
	 * Returns the x location of the Shape
	 * 
	 * @return x
	 */
	public int getX() {
		return x;
	}

	/**
	 * Returns the coordinates of the Shape
	 * 
	 * @return coords
	 */
	public int[][] getCoords() {
		return coords;
	}

}
