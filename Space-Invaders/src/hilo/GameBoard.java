package hilo;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

import javax.swing.JPanel;
import javax.swing.Timer;

import principal.Reference;
import elementos.Alien;
import elementos.Player;
import elementos.Projectile;
import acciones.GameState;
import acciones.GameTasks;
import acciones.MapGenerator;
import acciones.ResourceManager;

public class GameBoard extends JPanel implements ActionListener, KeyListener {

	private GameState state;
	private Timer timer;
	private int timerDelay = 20;
	
	private int playerDeathDelay = 200;
	
	private static int score = 0;
	private static int aliensKilled = 0;
	
	/**Creates GameBoard*/
	public GameBoard() {
		addKeyListener(this);
		this.setFocusable(true);
		
		//init player
		Reference.player = new Player();
		
		//init aliens
		GameTasks.initAliens();
		
		//init projectiles list
		Reference.projectiles = new ArrayList<Projectile>();
		
		
		
		//init map gen
		Reference.map = new MapGenerator();
		
		state = GameState.MAIN_MENU;
		
		//init timer
		timer = new Timer(timerDelay, this);
		timer.start();
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		repaint(); revalidate();
		
		//background
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, Reference.windowWidth, Reference.windowHeight);
		
		if(state == GameState.NIVEL_UNO) {
			graphicRunning(g);
		}
		else if(state == GameState.NIVEL_DOS) {
			graphicRunning(g);
		}
		else if(state == GameState.NIVEL_TRES) {
			graphicRunning(g);
		}
		else if(state == GameState.MAIN_MENU) {
			graphicMainMenu(g);
		}
		else if(state == GameState.GAME_OVER) {
			graphicGameOver(g);
		}
		else if(state == GameState.HIGHSCORES) {
			graphicHighscores(g);
		}
	}

	/**Increases the score of a certain amount
	 * @param value - The amount */
	public static void addScore(int value) {score+=value;}
	
	@Override
	public void keyPressed(KeyEvent arg0) {
		
		if(state == GameState.NIVEL_UNO || state == GameState.NIVEL_DOS || state == GameState.NIVEL_TRES) {
			if(!Reference.player.isDead()) {
				switch(arg0.getKeyCode()) {
				
				case KeyEvent.VK_LEFT:
					if(Reference.player.getPosX()>10) Reference.player.move(-5);
					break;
					
				case KeyEvent.VK_RIGHT:
					if(Reference.player.getPosX()<810) Reference.player.move(5);
					break;
				
				case KeyEvent.VK_SPACE:
					if(GameTasks.getPlayerDelay() > Reference.player.shotDelay || Reference.player.hasSuperShot()) {
						Reference.projectiles.add(new Projectile(Reference.player.getPosX()+19, Reference.player.getPosY()-10, -10));
						GameTasks.resetPlayerDelay();
					}
					break;
				}
			}
		}
		else if(state == GameState.MAIN_MENU) {
			switch(arg0.getKeyCode()) {
			
			case KeyEvent.VK_1:
				state = GameState.NIVEL_UNO;
				break;
			
			case KeyEvent.VK_2:
				state = GameState.NIVEL_DOS;
				break;
				
			case KeyEvent.VK_3:
				state = GameState.NIVEL_TRES;
				break;
			
			case KeyEvent.VK_SPACE:
				state = GameState.HIGHSCORES;
				break;
			}
		}
		else if(state == GameState.GAME_OVER) {
			state = GameState.MAIN_MENU;
			
			if(score > Reference.highscores[9]) {
				Reference.highscores[9] = score;
				GameTasks.sortHighscores();
				ResourceManager.writeHighscores();
			}
			
			score = 0;
			GameTasks.restart();
		}
		else if(state == GameState.HIGHSCORES) {
			state = GameState.MAIN_MENU;
		}
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		
		if(state == GameState.NIVEL_UNO) {
			//When player is alive
			if(!Reference.player.isDead()) {
				//Changes delays
				GameTasks.changeDelays();
				
				//Moves all aliens
				GameTasks.moveAllAliens();
				
				//Moves projectiles
				GameTasks.movePrjs();
				
				//Checks collision
				if(GameTasks.checkCollisionAlienPrj()) {
					aliensKilled++;
				}
				
				//Checks if the aliens have landed on the planet
				if(GameTasks.checkAliensLanded())
					state = GameState.GAME_OVER;
				
				//Creates the red ship on the screen
				GameTasks.redShipFlight();
				
				//Checks collision with red ship
				GameTasks.checkCollisionRedshipPrj();
			}
			
			//When player is dead
			if(Reference.player.isDead()) {
				playerDeathDelay--;
				
				if(playerDeathDelay == 0) {
					playerDeathDelay = 200;
					//Respawn the player if he still has lives
					if(Reference.player.getLives()>0)
						Reference.player.setDead(false);
					else
						state = GameState.GAME_OVER;
				}
			}
			
			//Count how many aliens has the player killed
			if(aliensKilled == Reference.alienColumns*Reference.alienRows) {
				aliensKilled = 0;
				GameTasks.initAliens();
				Alien.decreaseMotionDelay();
				Reference.player.addLife();
			}
		}
		
		if(state == GameState.NIVEL_DOS) {
			//When player is alive
			if(!Reference.player.isDead()) {
				//Changes delays
				GameTasks.changeDelays();
				
				//Moves all aliens
				GameTasks.moveAllAliens();
				
				//Makes aliens shoot
				GameTasks.alienShoot();
				
				//Moves projectiles
				GameTasks.movePrjs();
				
				//Checks collision
				if(GameTasks.checkCollisionAlienPrj()) {
					aliensKilled++;
				}
				
				
				//If player is shot
				if(GameTasks.checkCollisionPlayerPrj()) {
					Reference.player.setDead(true);
				}
				
				//Checks if the aliens have landed on the planet
				if(GameTasks.checkAliensLanded())
					state = GameState.GAME_OVER;
				
				//Creates the red ship on the screen
				GameTasks.redShipFlight();
				
				//Checks collision with red ship
				GameTasks.checkCollisionRedshipPrj();
			}
			
			//When player is dead
			if(Reference.player.isDead()) {
				playerDeathDelay--;
				
				if(playerDeathDelay == 0) {
					playerDeathDelay = 200;
					//Respawn the player if he still has lives
					if(Reference.player.getLives()>0)
						Reference.player.setDead(false);
					else
						state = GameState.GAME_OVER;
				}
			}
			
			//Count how many aliens has the player killed
			if(aliensKilled == Reference.alienColumns*Reference.alienRows) {
				aliensKilled = 0;
				GameTasks.initAliens();
				Alien.decreaseMotionDelay();
				Reference.player.addLife();
			}
		}
		
		if(state == GameState.NIVEL_TRES) {
			//When player is alive
			if(!Reference.player.isDead()) {
				//Changes delays
				GameTasks.changeDelays();
				
				//Moves all aliens
				GameTasks.moveAllAliens();
				
				//Makes aliens shoot
				GameTasks.alienShoot();
				
				//Makes aliens shoot
				GameTasks.alienKamikaze();
				
				//Moves projectiles
				GameTasks.movePrjs();
				
				//Checks collision
				if(GameTasks.checkCollisionAlienPrj()) {
					aliensKilled++;
				}
				
				
				//If player is shot
				if(GameTasks.checkCollisionPlayerPrj()) {
					Reference.player.setDead(true);
				}
				
				//Checks if the aliens have landed on the planet
				if(GameTasks.checkAliensLanded())
					state = GameState.GAME_OVER;
				
				//Creates the red ship on the screen
				GameTasks.redShipFlight();
				
				//Checks collision with red ship
				GameTasks.checkCollisionRedshipPrj();
			}
			
			//When player is dead
			if(Reference.player.isDead()) {
				playerDeathDelay--;
				
				if(playerDeathDelay == 0) {
					playerDeathDelay = 200;
					//Respawn the player if he still has lives
					if(Reference.player.getLives()>0)
						Reference.player.setDead(false);
					else
						state = GameState.GAME_OVER;
				}
			}
			
			//Count how many aliens has the player killed
			if(aliensKilled == Reference.alienColumns*Reference.alienRows) {
				aliensKilled = 0;
				GameTasks.initAliens();
				Alien.decreaseMotionDelay();
				Reference.player.addLife();
			}
		}
		
		repaint(); 
		revalidate();
	}
	
	@Override
	public void keyReleased(KeyEvent arg0) {}
	
	@Override
	public void keyTyped(KeyEvent arg0) {}
	
	private static void graphicRunning(Graphics g) {
		//player	//posX, posY, width, height
		if(!Reference.player.isDead() && !Reference.player.hasSuperShot()) {
			g.drawImage(ResourceManager.player, Reference.player.getPosX(), Reference.player.getPosY(), ResourceManager.player.getWidth()*3, ResourceManager.player.getHeight()*3, null);
		}
		else if(!Reference.player.isDead() && Reference.player.hasSuperShot()) {
			g.drawImage(ResourceManager.redPlayer, Reference.player.getPosX(), Reference.player.getPosY(), ResourceManager.deadPlayer.getWidth()*3, ResourceManager.deadPlayer.getHeight()*3, null);
		}
		else {
			g.drawImage(ResourceManager.deadPlayer, Reference.player.getPosX(), Reference.player.getPosY(), ResourceManager.redPlayer.getWidth()*3, ResourceManager.redPlayer.getHeight()*3, null);
		}
			
		//strings
		g.setFont(new Font("Arial", Font.BOLD, 20));
		g.setColor(Color.WHITE);
		g.drawString("Score: ", 500, 560);
		g.drawString(""+score, 700, 560);
		g.drawString("Lives: ", 40, 560);
		
		//lives
		for(int i=0; i<Reference.player.getLives(); i++) {
			g.drawImage(ResourceManager.player, i*40+110, 550, ResourceManager.player.getWidth()*2, ResourceManager.player.getHeight()*2, null);
		}
		
		//line
		g.setColor(Color.GREEN);
		g.fillRect(5, 530, 880, 5);
		
		//draws projectiles
		g.setColor(Color.WHITE);
		for(int i=0; i<Reference.projectiles.size(); i++) {
			g.fillRect(Reference.projectiles.get(i).getPosX(), Reference.projectiles.get(i).getPosY(), 2, 10);
		}
		
		
		//draw aliens
		Reference.map.draw((Graphics2D) g);
		
		//draw red ship
		if(Reference.redShip != null)
			g.drawImage(ResourceManager.redShip, Reference.redShip.getPosX(), Reference.redShip.getPosY(), ResourceManager.redShip.getWidth()*3, ResourceManager.redShip.getHeight()*3, null);
	}
	
	private static void graphicMainMenu(Graphics g) {
		//title
		g.setColor(Color.WHITE);
		g.setFont(new Font("Arial", Font.BOLD, 90));
		g.drawString("SPACE", 50, 150);
		g.setColor(Color.GREEN);
		g.drawString("INVADERS", 400, 150);
		g.setColor(Color.WHITE);
		
		//points
		g.drawImage(ResourceManager.alien0, 350, 200, ResourceManager.alien0.getWidth()*3, ResourceManager.alien0.getHeight()*3, null);
		g.drawImage(ResourceManager.alien1, 350, 240, ResourceManager.alien1.getWidth()*3, ResourceManager.alien1.getHeight()*3, null);
		g.drawImage(ResourceManager.alien2, 350, 280, ResourceManager.alien2.getWidth()*3, ResourceManager.alien2.getHeight()*3, null);
		g.drawImage(ResourceManager.redShip, 340, 320, ResourceManager.redShip.getWidth()*3, ResourceManager.redShip.getHeight()*3, null);
		
		g.setFont(new Font("Arial", Font.PLAIN, 20));
		g.drawString("= 10 pts", 400, 220);
		g.drawString("= 20 pts", 400, 260);
		g.drawString("= 30 pts", 400, 300);
		g.drawString("= poder", 410, 340);
		
		//buttons
		g.drawString("[1] Nivel 1", 330, 400);
		g.drawString("[2] Nivel 2", 330, 430);
		g.drawString("[3] Nivel 3", 330, 460);
		g.drawString("[Space] Highscores", 330, 490);
	}
	
	private static void graphicGameOver(Graphics g) {
		g.setColor(Color.RED);
		g.setFont(new Font("Arial", Font.BOLD, 50));
		g.drawString("GAME OVER", 300, 150);
		
		g.setColor(Color.WHITE);
		g.setFont(new Font("Arial", Font.PLAIN, 20));
		g.drawString("Your score: "+score, 350, 200);
		g.drawString("Press any button to return to main menu", 300, 250);
	}
	
	private static void graphicHighscores(Graphics g) {
		g.setColor(Color.WHITE);
		g.setFont(new Font("Arial", Font.BOLD, 40));
		g.drawString("HIGHSCORES", 300, 100);
		
		g.setFont(new Font("Arial", Font.PLAIN, 20));
		for(int i=0; i<Reference.highscores.length; i++) {
			g.drawString(""+(i+1)+" -", 320, 140+i*30);
			g.drawString(""+Reference.highscores[i], 540, 140+i*30);
		}
		
		g.drawString("Press any button to return to main menu", 300, 500);
	}
	
}
