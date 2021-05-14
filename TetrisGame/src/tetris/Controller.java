package tetris;

import java.awt.event.KeyEvent;
import java.util.concurrent.BlockingQueue;

/**
 * Controller class takes KeyPressedMessages from the queue add performs
 * necessary actions based on the key that was pressed
 * 
 * @author nanarboursalian
 *
 */
public class Controller {

	/**
	 * BlockingQueue that holds all the KeyPressedMessages for when a user presses a
	 * key
	 */
	BlockingQueue<KeyPressedMessage> queue;

	/**
	 * theWindow or the JFrame the has the current game that the user is playing
	 */
	WindowGame theWindow;

	/**
	 * Constructor for controller initializing the current queue and WindowGame
	 * 
	 * @param queue     BlockingQueue holding KeyPressedMessages for when a user
	 *                  presses a key
	 * @param theWindow Current JFrame that the user is playing on
	 */
	public Controller(BlockingQueue<KeyPressedMessage> queue, WindowGame theWindow) {
		this.queue = queue;
		this.theWindow = theWindow;
	}

	/**
	 * Keeps track of all of the KeyPressedMessages in queue, taking appropriate
	 * action depending on the key that the user presses
	 * 
	 * Right arrow means the currentShape moves to the right. Left arrow means the
	 * currentShape moves to the left. Up arrow means the currentShape rotates.
	 */
	public void runningLoop() {
		Board theBoard = theWindow.getBoard();

		while (theBoard.isDisplayable()) {
			KeyPressedMessage theMessage = null;

			try {
				theMessage = queue.take();
			} catch (InterruptedException e) {

			}

			if (theMessage.e.getKeyCode() == KeyEvent.VK_RIGHT) {
				theBoard.getCurrentShape().moveRight();
			} else if (theMessage.e.getKeyCode() == KeyEvent.VK_LEFT) {
				theBoard.getCurrentShape().moveLeft();
			} else if (theMessage.e.getKeyCode() == KeyEvent.VK_UP) {
				theBoard.getCurrentShape().rotateShape();
			}

			if (theBoard.getState() == 1) {
				if (theMessage.e.getKeyCode() == KeyEvent.VK_SPACE) {
					// resets game -- cleans the board

					for (int row = 0; row < theBoard.getBoard().length; row++) {
						for (int col = 0; col < theBoard.getBoard()[row].length; col++) {
							theBoard.getBoard()[row][col] = null;
							theBoard.resetScore();
						}
					}
					theBoard.setCurrentShape();
					theBoard.setState(0);

				}
			}
		}
	}

}
