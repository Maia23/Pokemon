package view;

import java.awt.Dimension;

import javax.swing.*;

/**
 * Main game Window, hosts all other components belonging to the game's interface
 */
public final class GameWindow extends JFrame {

	private static final long serialVersionUID = 1L;
	
	private static GameWindow gameWindow = new GameWindow();
    
    public static GameWindow getInstance() {   
    	return gameWindow;
    }
	
	/**
	 * Initializes mainWindow with predefined params
	 * @param width
	 * @param height
	 */
	public GameWindow(int width, int height) {
		
		setTitle("Pokemon");
		setSize(width,height);
		setMinimumSize(new Dimension(900, 900));
		setMaximumSize(new Dimension(800,800));
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
	
	public GameWindow() {
		this(1200, 1200);
	}

}

