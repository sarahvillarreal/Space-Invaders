package principal;

import javax.swing.JFrame;

import hilo.GameBoard;

public class Main {

	private static JFrame window;
	private static GameBoard board;
	
	public static void main(String[] args) {
		
		createFrame();
		createGameBoard();
	}
	
	/**Creates a JFrame object*/
	private static void createFrame() {
		
		window = new JFrame("Space Invaders");
		window.setVisible(true);
		window.setBounds(100, 100, Reference.windowWidth, Reference.windowHeight);
		window.setResizable(false);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	/**Creates a GameBoard object and adds it to the JFrame*/
	private static void createGameBoard() {
		
		board = new GameBoard();
		window.add(board);
		board.requestFocusInWindow();
	}
}
