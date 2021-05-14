package tetris;

import java.awt.event.KeyEvent;

/**
 * KeyPressed Message class that stores the KeyEvent, passes to Controller
 * through BlockingQueue
 * 
 * @author nanarboursalian
 *
 */
public class KeyPressedMessage {
	
	/**
	 * Key that the user presses (either up, left, right)
	 */
	KeyEvent e;

	/**
	 * Constructor initializes the KeyEvent, or the key that the user presses
	 * @param e
	 */
	public KeyPressedMessage(KeyEvent e) {
		this.e = e;
	}
}
