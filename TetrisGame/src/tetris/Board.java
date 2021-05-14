package tetris;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Random;

import javax.swing.JPanel;
import javax.swing.Timer;
import java.util.concurrent.BlockingQueue;

/**
 * Board class is the JPanel that holds all the view components of the program
 * 
 * @author nanarboursalian
 *
 */
public class Board extends JPanel implements KeyListener {

	/**
	 * Tells when the game is over or when the user is still playing
	 * 
	 * Initial state is still playing
	 */
	public static int STATE_GAME_PLAY = 0;
	public static int STATE_GAME_OVER = 1;
	private int state = STATE_GAME_PLAY;

	/**
	 * Frames per second and the delay of a Shape as it moves down a Board
	 */
	private static int FPS = 60; // frames per second
	private static int delay = FPS / 1000;

	/**
	 * Users score, incremented every time a user lands another shape without the
	 * game ending
	 */
	private int score = 0;

	/**
	 * Size of the Board and each individual block
	 */
	public static final int BOARD_WIDTH = 10, BOARD_HEIGHT = 20, BLOCK_SIZE = 30;

	/**
	 * Timer for Shape traversing down the game page
	 */
	private Timer looper;

	/**
	 * Holds all the Shapes in the game and their respective filled in/not filled in
	 * blocks
	 */
	private Color[][] board = new Color[BOARD_HEIGHT][BOARD_WIDTH];

	/**
	 * Randomizer for deciding what Shape should traverse next
	 */
	private Random random;

	/**
	 * Array holding all of the possible Shape types for the game
	 */
	private Shape[] shapes = new Shape[7];
	/**
	 * Possible colors for all of the Shapes in the game
	 */
	private Color[] colors = { Color.decode("#ed1c24"), Color.decode("#ff7f27"), Color.decode("#fff200"),
			Color.decode("#22b14c"), Color.decode("#00a2e8"), Color.decode("#a349a4"), Color.decode("#3f48cc") };

	/**
	 * Shape currently traversing down the screen
	 */
	private Shape currentShape;

	/**
	 * BlockingQueue holding all of the KeyPressedMessage instances to deduce
	 * appropriate action in Controller based on the key pressed
	 */
	BlockingQueue<KeyPressedMessage> queue;

	/**
	 * Constructor initializes the queue holding KeyPressedMessages, initializes the
	 * randomizer, creates 7 unique Shapes and adds them to the shapes array,
	 * initializes currentShape, and begins Timer in updating and painting the
	 * components
	 * 
	 * @param queue
	 */
	public Board(BlockingQueue<KeyPressedMessage> queue) {

		this.queue = queue;

		random = new Random();

		shapes[0] = new Shape(new int[][] { { 1, 1, 1, 1 } // I shape;
		}, this, colors[0]);

		shapes[1] = new Shape(new int[][] { { 1, 1, 1 }, { 0, 1, 0 }, // T shape;
		}, this, colors[1]);

		shapes[2] = new Shape(new int[][] { { 1, 1, 1 }, { 1, 0, 0 }, // L shape;
		}, this, colors[2]);

		shapes[3] = new Shape(new int[][] { { 1, 1, 1 }, { 0, 0, 1 }, // J shape;
		}, this, colors[3]);

		shapes[4] = new Shape(new int[][] { { 0, 1, 1 }, { 1, 1, 0 }, // S shape;
		}, this, colors[4]);

		shapes[5] = new Shape(new int[][] { { 1, 1, 0 }, { 0, 1, 1 }, // Z shape;
		}, this, colors[5]);

		shapes[6] = new Shape(new int[][] { { 1, 1 }, { 1, 1 }, // O shape;
		}, this, colors[6]);

		currentShape = shapes[0];

		looper = new Timer(delay, new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				update();
				repaint(); // paintComponent gets called again
			}
		});
		looper.start();
	}

	/**
	 * Updates Shape if game in play
	 */
	private void update() {
		if (state == STATE_GAME_PLAY) {
			currentShape.update();
		}
	}

	/**
	 * Randomly chooses a shape from the array of shapes and sets initial location
	 */
	public void setCurrentShape() {
		currentShape = shapes[random.nextInt(shapes.length)];
		currentShape.reset();
		checkOverGame();
	}

	/**
	 * Checks whether or not the game is over by seeing if a block at the top of the
	 * grid is filled in, or the coordinate value is 1
	 */
	private void checkOverGame() {
		int[][] coords = currentShape.getCoords();
		for (int row = 0; row < coords.length; row++) {
			for (int col = 0; col < coords[0].length; col++) {
				if (coords[row][col] != 0) {
					if (board[row + currentShape.getY()][col + currentShape.getX()] != null) {
						state = STATE_GAME_OVER;
					}
				}
			}
		}
	}

	/**
	 * Paints the background, grid, and shape
	 * 
	 * Includes the score on the screen for the user to see
	 * 
	 * If game is over, final score and instructions to restart are shown
	 * 
	 * @param g
	 */
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		// makes window background black
		g.setColor(Color.black);
		g.fillRect(0, 0, getWidth(), getHeight());

		currentShape.render(g);

		for (int row = 0; row < board.length; row++) {
			for (int col = 0; col < board[row].length; col++) {
				if (board[row][col] != null) {
					g.setColor(board[row][col]);
					g.fillRect(col * BLOCK_SIZE, row * BLOCK_SIZE, BLOCK_SIZE, BLOCK_SIZE);
				}
			}
		}
		g.setColor(Color.white);
		g.setFont(new Font("Georgia", Font.BOLD, 20));

		g.drawString("SCORE", WindowGame.WIDTH - 125, WindowGame.HEIGHT / 2);
		g.drawString(score + "", WindowGame.WIDTH - 125, WindowGame.HEIGHT / 2 + 30);

		// draw the Board
		g.setColor(Color.white);
		for (int row = 0; row < BOARD_HEIGHT; row++) {
			g.drawLine(0, BLOCK_SIZE * row, BLOCK_SIZE * BOARD_WIDTH, BLOCK_SIZE * row);
		}

		for (int col = 0; col <= BOARD_WIDTH; col++) {
			g.drawLine(col * BLOCK_SIZE, 0, col * BLOCK_SIZE, BLOCK_SIZE * BOARD_HEIGHT);
		}

		if (state == STATE_GAME_OVER) {
			g.setColor(Color.white);
			g.drawString("GAME", WindowGame.WIDTH - 125, WindowGame.HEIGHT / 4);
			g.drawString("OVER", WindowGame.WIDTH - 125, WindowGame.HEIGHT / 4 + 30);

			g.setFont(new Font("Georgia", Font.PLAIN, 20));
			g.drawString("Press", WindowGame.WIDTH - 125, WindowGame.HEIGHT / 2 + 90);
			g.drawString("SPACE", WindowGame.WIDTH - 125, WindowGame.HEIGHT / 2 + 110);
			g.drawString("to", WindowGame.WIDTH - 125, WindowGame.HEIGHT / 2 + 130);
			g.drawString("restart.", WindowGame.WIDTH - 125, WindowGame.HEIGHT / 2 + 150);
		}

	}

	/**
	 * Returns that Color board that holds the Shapes and their respective locations
	 * 
	 * @return board
	 */
	public Color[][] getBoard() {
		return this.board;
	}

	@Override
	public void keyTyped(KeyEvent e) {

	}

	/**
	 * Creates a KeyPressedMessage and sends the KeyEvent
	 * 
	 * Action to be determined in Controller depending on e
	 */
	@Override
	public void keyPressed(KeyEvent e) {

		try {
			this.queue.put(new KeyPressedMessage(e));
		} catch (InterruptedException er) {
			er.printStackTrace();
		}

	}

	@Override
	public void keyReleased(KeyEvent e) {
	}

	/**
	 * Increments score by 1
	 */
	public void addScore() {
		score++;
	}

	/**
	 * Returns Shape currently traversing down the board
	 * 
	 * @return currentShape
	 */
	public Shape getCurrentShape() {
		return this.currentShape;
	}

	/**
	 * Returns whether game is playing or game is over
	 * 
	 * @return state
	 */
	public int getState() {
		return this.state;
	}

	/**
	 * Sets game playing/game over to specified state
	 * 
	 * @param ste
	 */
	public void setState(int ste) {
		this.state = ste;
	}

	/**
	 * Resets score to 0
	 */
	public void resetScore() {
		this.score = 0;
	}

}
