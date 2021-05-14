package tetris;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * App class that runs the program, continuously running Controller through
 * WindowGame
 * 
 * @author nanarboursalian
 *
 */
public class TetrisGame {

	/**
	 * Main method that runs the entire program of the game
	 * @param args
	 */
	public static void main(String[] args) {

		BlockingQueue queue = new LinkedBlockingQueue<KeyPressedMessage>();

		WindowGame window = new WindowGame(queue);

		Controller controller = new Controller(queue, window);
		controller.runningLoop();

	}

}
