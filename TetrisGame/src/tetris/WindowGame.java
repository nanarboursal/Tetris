package tetris;

import java.util.concurrent.BlockingQueue;

import javax.swing.JFrame;

/**
 * Window Game class creates the JFrame where the Board of the game resides.
 * 
 * @author nanarboursalian
 *
 */
public class WindowGame {
	/**
	 * Size of the JFrame
	 */
	public static final int WIDTH = 445, HEIGHT = 630;

	/**
	 * Board to be applied to the Window, JFrame that composes the WindowGame, and
	 * the queue that holds KeyPressedMessages
	 */
	private Board board;
	private JFrame window;
	BlockingQueue<KeyPressedMessage> queue;

	/**
	 * Constructor for WindowGame that ensures that queue is made to hold the
	 * messages for KeyEvents
	 * 
	 * @param queue stored in WindowGame to pass to Controller
	 */
	public WindowGame(BlockingQueue<KeyPressedMessage> queue) {

		this.queue = queue;

		// Frame Settings
		window = new JFrame("Tetris");
		window.setSize(WIDTH, HEIGHT);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setLocationRelativeTo(null); // puts the window in the center of the screen
		window.setResizable(false);

		board = new Board(queue);
		window.add(board);
		window.addKeyListener(board);
		window.setVisible(true);
	}

	/**
	 * Returns the Board that is a part of the WindowGame
	 * 
	 * @return board
	 */
	public Board getBoard() {
		return this.board;
	}

}
