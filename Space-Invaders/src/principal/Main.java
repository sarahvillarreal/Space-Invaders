package principal;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

import acciones.ResourceManager;
import hilo.GameBoard;

public class Main {

	private static JFrame window;
	private static GameBoard board;
	
	public static void main(String[] args) {
		
		createFrame();
		createGameBoard();
	}
	
	//Crea JFrame
	private static void createFrame() {
		
		window = new JFrame("Space-Invaders");
		window.setVisible(true);
		window.setBounds(100, 100, Reference.windowWidth, Reference.windowHeight);
		window.setResizable(false);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		ImageIcon icon = new ImageIcon(Main.class.getResource("/imagenes/logo.png"));
        window.setIconImage(icon.getImage());
	}
	
	//Crea el tablero de juego y lo agrega al JFrame
	private static void createGameBoard() {
		
		board = new GameBoard();
		window.add(board);
		board.requestFocusInWindow();
	}
}
