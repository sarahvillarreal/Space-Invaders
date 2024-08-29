package hilo;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.Line2D;
import java.util.ArrayList;

import javax.swing.JPanel;
import javax.swing.Timer;

import principal.Reference;
import elementos.Alien;
import elementos.Player;
import elementos.Projectile;
import elementos.Shield;
import elementos.Shield.Type;
import acciones.GameState;
import acciones.GameTasks;
import acciones.MapGenerator;
import acciones.ResourceManager;

public class GameBoard extends JPanel implements ActionListener, KeyListener {

	public static GameState state;
	private Timer timer;
	private int timerDelay = 20;
	
	private int playerDeathDelay = 200;
	
	private static int score = 0;
	private static int aliensKilled = 0;
	
	//Crea el tablero de juego
	public GameBoard() {
		addKeyListener(this);
		this.setFocusable(true);
		
		//inicializa jugador
		Reference.player = new Player();
		
		//inicializa aliens
		GameTasks.initAliens();
		
		//inicializa lista de proyectiles
		Reference.projectiles = new ArrayList<Projectile>();
		
		//inicializa generador de mapa
		Reference.map = new MapGenerator();
		
		state = GameState.MAIN_MENU;
		
		//inicializa timer
		timer = new Timer(timerDelay, this);
		timer.start();
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		repaint(); 
		revalidate();
		
		//fondo
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, Reference.windowWidth, Reference.windowHeight);
		
		if(state == GameState.NIVEL_UNO || state == GameState.NIVEL_DOS || state == GameState.NIVEL_TRES) {
			graphicRunning(g);
		}
		else if(state == GameState.NIVEL_CUATRO) {
			graphicRunningNivel4(g);
		}
		else if(state == GameState.NIVEL_CINCO) {
			graphicRunningNivel5(g);
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

	//Aumenta el puntaje segun el valor del alien
	public static void addScore(int value) {score+=value;}
	
	@Override
	public void keyPressed(KeyEvent arg0) {
		
		if(state == GameState.NIVEL_UNO || state == GameState.NIVEL_DOS || state == GameState.NIVEL_TRES || state == GameState.NIVEL_CUATRO || state == GameState.NIVEL_CINCO) {
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
				
			case KeyEvent.VK_4:
				
				//inicializa shields
				Reference.shields = new Shield[] {
						new Shield(70, 400, Type.TRIANGLE1),
						new Shield(100, 400, Type.SQUARE),
						new Shield(130, 400, Type.SQUARE),
						new Shield(160, 400, Type.TRIANGLE2),
						new Shield(70, 430, Type.SQUARE),
						new Shield(100, 430, Type.TRIANGLE3),
						new Shield(130, 430, Type.TRIANGLE4),
						new Shield(160, 430, Type.SQUARE),
						
						new Shield(270, 400, Type.TRIANGLE1),
						new Shield(300, 400, Type.SQUARE),
						new Shield(330, 400, Type.SQUARE),
						new Shield(360, 400, Type.TRIANGLE2),
						new Shield(270, 430, Type.SQUARE),
						new Shield(300, 430, Type.TRIANGLE3),
						new Shield(330, 430, Type.TRIANGLE4),
						new Shield(360, 430, Type.SQUARE),
						
						new Shield(470, 400, Type.TRIANGLE1),
						new Shield(500, 400, Type.SQUARE),
						new Shield(530, 400, Type.SQUARE),
						new Shield(560, 400, Type.TRIANGLE2),
						new Shield(470, 430, Type.SQUARE),
						new Shield(500, 430, Type.TRIANGLE3),
						new Shield(530, 430, Type.TRIANGLE4),
						new Shield(560, 430, Type.SQUARE),
						
						new Shield(670, 400, Type.TRIANGLE1),
						new Shield(700, 400, Type.SQUARE),
						new Shield(730, 400, Type.SQUARE),
						new Shield(760, 400, Type.TRIANGLE2),
						new Shield(670, 430, Type.SQUARE),
						new Shield(700, 430, Type.TRIANGLE3),
						new Shield(730, 430, Type.TRIANGLE4),
						new Shield(760, 430, Type.SQUARE)
				};
				
				state = GameState.NIVEL_CUATRO;
				break;
				
			case KeyEvent.VK_5:
				state = GameState.NIVEL_CINCO;
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
			//Cuando el jugador esta vivo
			if(!Reference.player.isDead()) {
				//Cambian los delays
				GameTasks.changeDelays();
				
				//Se mueven los aliens
				GameTasks.moveAllAliens();
				
				//Se mueven los proyectiles
				GameTasks.movePrjs();
				
				//Se chequea la colision
				if(GameTasks.checkCollisionAlienPrj()) {
					aliensKilled++;
				}
				
				//Si el jugador es disparado
				if(GameTasks.checkCollisionPlayerPrj()) {
					Reference.player.setDead(true);
				}
				
				//Chequea si los aliens llegaron al "planeta"
				if(GameTasks.checkAliensLanded())
					state = GameState.GAME_OVER;
			}
			
			//Cuando el jugador esta muerto
			if(Reference.player.isDead()) {
				playerDeathDelay--;
				
				if(playerDeathDelay == 0) {
					playerDeathDelay = 200;
					//Respawnear al jugador si todavia tiene vidas
					if(Reference.player.getLives()>0)
						Reference.player.setDead(false);
					else
						state = GameState.GAME_OVER;
				}
			}
			
			//Cuenta la cantidad de aliens que mato el jugador
			if(aliensKilled == Reference.alienColumns*Reference.alienRows) {
				aliensKilled = 0;
				GameTasks.initAliens();
				Alien.decreaseMotionDelay();
				Reference.player.addLife();
			}
		}
		
		if(state == GameState.NIVEL_DOS) {
			
			if(!Reference.player.isDead()) {
				
				GameTasks.changeDelays();
				
				GameTasks.moveAllAliens();
				
				GameTasks.alienShoot();
				
				GameTasks.movePrjs();
				
				if(GameTasks.checkCollisionAlienPrj()) {
					aliensKilled++;
				}
				
				if(GameTasks.checkCollisionPlayerPrj()) {
					Reference.player.setDead(true);
				}
				
				if(GameTasks.checkAliensLanded())
					state = GameState.GAME_OVER;
			}
			
			if(Reference.player.isDead()) {
				playerDeathDelay--;
				
				if(playerDeathDelay == 0) {
					playerDeathDelay = 200;
					
					if(Reference.player.getLives()>0)
						Reference.player.setDead(false);
					else
						state = GameState.GAME_OVER;
				}
			}
			
			if(aliensKilled == Reference.alienColumns*Reference.alienRows) {
				aliensKilled = 0;
				GameTasks.initAliens();
				Alien.decreaseMotionDelay();
				Reference.player.addLife();
			}
		}
		
		if(state == GameState.NIVEL_TRES) {
			
			if(!Reference.player.isDead()) {
				
				GameTasks.changeDelays();
				
				GameTasks.moveAllAliens();
				
				GameTasks.alienShoot();
				
				//Hace que el alien entre en modo kamikaze
				GameTasks.alienKamikaze();
				
				GameTasks.movePrjs();
				
				if(GameTasks.activateMove2() == true) {
					GameTasks.movePrjs2();
				}
				
				if(GameTasks.checkCollisionAlienPrj()) {
					aliensKilled++;
				}
				
				if(GameTasks.checkCollisionPlayerPrj()) {
					Reference.player.setDead(true);
				}
				
				if(GameTasks.checkAliensLanded())
					state = GameState.GAME_OVER;
				
			}
			
			if(Reference.player.isDead()) {
				playerDeathDelay--;
				
				if(playerDeathDelay == 0) {
					playerDeathDelay = 200;
					if(Reference.player.getLives()>0)
						Reference.player.setDead(false);
					else
						state = GameState.GAME_OVER;
				}
			}
			
			if(aliensKilled == Reference.alienColumns*Reference.alienRows) {
				aliensKilled = 0;
				GameTasks.initAliens();
				Alien.decreaseMotionDelay();
				Reference.player.addLife();
			}
		}
		
		if(state == GameState.NIVEL_CUATRO) {
			
			
			if(!Reference.player.isDead()) {
				
				GameTasks.changeDelays();
				
				GameTasks.moveAllAliens();
				
				GameTasks.alienShoot();
				
				GameTasks.movePrjs();
				
				if(GameTasks.checkCollisionAlienPrj()) {
					aliensKilled++;
				}
				
				GameTasks.checkCollisionShieldPrj();
				
				if(GameTasks.checkCollisionPlayerPrj()) {
					Reference.player.setDead(true);
				}
				
				if(GameTasks.checkAliensLanded())
					state = GameState.GAME_OVER;
				
				//Crea la nave roja
				GameTasks.redShipFlight();
				
				//Chequea la colision de la nave roja
				GameTasks.checkCollisionRedshipPrj();
			}
			
			if(Reference.player.isDead()) {
				playerDeathDelay--;
				
				if(playerDeathDelay == 0) {
					playerDeathDelay = 200;
					
					if(Reference.player.getLives()>0)
						Reference.player.setDead(false);
					else
						state = GameState.GAME_OVER;
				}
			}
			
			if(aliensKilled == Reference.alienColumns*Reference.alienRows) {
				aliensKilled = 0;
				GameTasks.initAliens();
				Alien.decreaseMotionDelay();
				Reference.player.addLife();
			}
		}
		
		if(state == GameState.NIVEL_CINCO) {
			
			if(!Reference.player.isDead()) {
				
				GameTasks.changeDelays();
				
				GameTasks.moveAllAliens();
				
				GameTasks.alienShoot();
				
				GameTasks.movePrjs();
				
				if(GameTasks.checkCollisionAlienPrj()) {
					aliensKilled++;
				}
				
				if(GameTasks.checkCollisionPlayerPrj()) {
					Reference.player.setDead(true);
				}
				
				if(GameTasks.checkAliensLanded())
					state = GameState.GAME_OVER;
				
				GameTasks.redShipFlight();
				
				GameTasks.checkCollisionRedshipPrj();
			}
			
			if(Reference.player.isDead()) {
				playerDeathDelay--;
				
				if(playerDeathDelay == 0) {
					playerDeathDelay = 200;
					
					if(Reference.player.getLives()>0)
						Reference.player.setDead(false);
					else
						state = GameState.GAME_OVER;
				}
			}
			
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
		
		Graphics2D g2d = (Graphics2D) g;

		//jugador	//posX, posY, width, height
		if(!Reference.player.isDead()) {
			g.drawImage(ResourceManager.player, Reference.player.getPosX(), Reference.player.getPosY(), ResourceManager.player.getWidth()*3, ResourceManager.player.getHeight()*3, null);
		}
		else {
			g.drawImage(ResourceManager.deadPlayer, Reference.player.getPosX(), Reference.player.getPosY(), ResourceManager.redPlayer.getWidth()*3, ResourceManager.redPlayer.getHeight()*3, null);
		}
			
		//texto
		g.setFont(new Font("Arial", Font.BOLD, 20));
		g.setColor(Color.WHITE);
		g.drawString("Score: ", 500, 560);
		g.drawString(""+score, 700, 560);
		g.drawString("Lives: ", 40, 560);
		
		//vidas
		for(int i=0; i<Reference.player.getLives(); i++) {
			g.drawImage(ResourceManager.player, i*40+110, 550, ResourceManager.player.getWidth()*2, ResourceManager.player.getHeight()*2, null);
		}
		
		//linea verde
		g.setColor(Color.GREEN);
		g.fillRect(5, 530, 880, 5);
		
		//dibujar proyectiles
		g.setColor(Color.WHITE);
		for(int i=0; i<Reference.projectiles.size(); i++) {
			g.fillRect(Reference.projectiles.get(i).getPosX(), Reference.projectiles.get(i).getPosY(), 2, 10);
		}
		
		//dibujar aliens
		Reference.map.draw((Graphics2D) g);

		//linea planeta
		float[] dashPattern = {10, 10}; //linea punteada
        g2d.setStroke(new BasicStroke(2, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10, dashPattern, 0));
        g.setColor(Color.WHITE);
        g2d.draw(new Line2D.Float(0, 458, 1000, 458));
	}
	
	private static void graphicRunningNivel4(Graphics g) {
		
		Graphics2D g2d = (Graphics2D) g;
		
		//jugador	//posX, posY, width, height
		if(!Reference.player.isDead() && !Reference.player.hasSuperShot()) {
			g.drawImage(ResourceManager.player, Reference.player.getPosX(), Reference.player.getPosY(), ResourceManager.player.getWidth()*3, ResourceManager.player.getHeight()*3, null);
		}
		else if(!Reference.player.isDead() && Reference.player.hasSuperShot()) {
			g.drawImage(ResourceManager.redPlayer, Reference.player.getPosX(), Reference.player.getPosY(), ResourceManager.deadPlayer.getWidth()*3, ResourceManager.deadPlayer.getHeight()*3, null);
		}
		else {
			g.drawImage(ResourceManager.deadPlayer, Reference.player.getPosX(), Reference.player.getPosY(), ResourceManager.redPlayer.getWidth()*3, ResourceManager.redPlayer.getHeight()*3, null);
		}
			
		//texto
		g.setFont(new Font("Arial", Font.BOLD, 20));
		g.setColor(Color.WHITE);
		g.drawString("Score: ", 500, 560);
		g.drawString(""+score, 700, 560);
		g.drawString("Lives: ", 40, 560);
		
		//vidas
		for(int i=0; i<Reference.player.getLives(); i++) {
			g.drawImage(ResourceManager.player, i*40+110, 550, ResourceManager.player.getWidth()*2, ResourceManager.player.getHeight()*2, null);
		}
		
		//linea verde
		g.setColor(Color.GREEN);
		g.fillRect(5, 530, 880, 5);
		
		//dibujar proyectiles
		g.setColor(Color.WHITE);
		for(int i=0; i<Reference.projectiles.size(); i++) {
			g.fillRect(Reference.projectiles.get(i).getPosX(), Reference.projectiles.get(i).getPosY(), 2, 10);
		}
		
		//dibujar escudos
			g.setColor(Color.GREEN);
			for(int i=0; i<Reference.shields.length; i++) {
				if(Reference.shields[i].getType() == Type.SQUARE) {
					switch(Reference.shields[i].getDamage()) {
					case 1:
						g.drawImage(ResourceManager.shieldSquareDmg2, Reference.shields[i].getPosX(), Reference.shields[i].getPosY(), ResourceManager.shieldSquareDmg2.getWidth()*3, ResourceManager.shieldSquareDmg2.getHeight()*3, null); break;
					case 2:
						g.drawImage(ResourceManager.shieldSquareDmg1, Reference.shields[i].getPosX(), Reference.shields[i].getPosY(), ResourceManager.shieldSquareDmg1.getWidth()*3, ResourceManager.shieldSquareDmg1.getHeight()*3, null); break;
					case 3:
						g.drawImage(ResourceManager.shieldSquareDmg0, Reference.shields[i].getPosX(), Reference.shields[i].getPosY(), ResourceManager.shieldSquareDmg0.getWidth()*3, ResourceManager.shieldSquareDmg0.getHeight()*3, null); break;
					default:
						continue;
					}
				}
				
				if(Reference.shields[i].getType() == Type.TRIANGLE1) {
					switch(Reference.shields[i].getDamage()) {
					case 1:
						g.drawImage(ResourceManager.shieldTriangle1dmg2, Reference.shields[i].getPosX(), Reference.shields[i].getPosY(), ResourceManager.shieldTriangle1dmg2.getWidth()*3, ResourceManager.shieldTriangle1dmg2.getHeight()*3, null); break;
					case 2:
						g.drawImage(ResourceManager.shieldTriangle1dmg1, Reference.shields[i].getPosX(), Reference.shields[i].getPosY(), ResourceManager.shieldTriangle1dmg1.getWidth()*3, ResourceManager.shieldTriangle1dmg1.getHeight()*3, null); break;
					case 3:
						g.drawImage(ResourceManager.shieldTriangle1dmg0, Reference.shields[i].getPosX(), Reference.shields[i].getPosY(), ResourceManager.shieldTriangle1dmg0.getWidth()*3, ResourceManager.shieldTriangle1dmg0.getHeight()*3, null); break;
					default:
						continue;
					}
				}
				
				if(Reference.shields[i].getType() == Type.TRIANGLE2) {
					switch(Reference.shields[i].getDamage()) {
					case 1:
						g.drawImage(ResourceManager.shieldTriangle2dmg2, Reference.shields[i].getPosX(), Reference.shields[i].getPosY(), ResourceManager.shieldTriangle2dmg2.getWidth()*3, ResourceManager.shieldTriangle2dmg2.getHeight()*3, null); break;
					case 2:
						g.drawImage(ResourceManager.shieldTriangle2dmg1, Reference.shields[i].getPosX(), Reference.shields[i].getPosY(), ResourceManager.shieldTriangle2dmg1.getWidth()*3, ResourceManager.shieldTriangle2dmg1.getHeight()*3, null); break;
					case 3:
						g.drawImage(ResourceManager.shieldTriangle2dmg0, Reference.shields[i].getPosX(), Reference.shields[i].getPosY(), ResourceManager.shieldTriangle2dmg0.getWidth()*3, ResourceManager.shieldTriangle2dmg0.getHeight()*3, null); break;
					default:
						continue;
					}
				}
				
				if(Reference.shields[i].getType() == Type.TRIANGLE3) {
					switch(Reference.shields[i].getDamage()) {
					case 1:
						g.drawImage(ResourceManager.shieldTriangle3dmg2, Reference.shields[i].getPosX(), Reference.shields[i].getPosY(), ResourceManager.shieldTriangle3dmg2.getWidth()*3, ResourceManager.shieldTriangle3dmg2.getHeight()*3, null); break;
					case 2:
						g.drawImage(ResourceManager.shieldTriangle3dmg1, Reference.shields[i].getPosX(), Reference.shields[i].getPosY(), ResourceManager.shieldTriangle3dmg1.getWidth()*3, ResourceManager.shieldTriangle3dmg1.getHeight()*3, null); break;
					case 3:
						g.drawImage(ResourceManager.shieldTriangle3dmg0, Reference.shields[i].getPosX(), Reference.shields[i].getPosY(), ResourceManager.shieldTriangle3dmg0.getWidth()*3, ResourceManager.shieldTriangle3dmg0.getHeight()*3, null); break;
					default:
						continue;
					}
				}
				
				if(Reference.shields[i].getType() == Type.TRIANGLE4) {
					switch(Reference.shields[i].getDamage()) {
					case 1:
						g.drawImage(ResourceManager.shieldTriangle4dmg2, Reference.shields[i].getPosX(), Reference.shields[i].getPosY(), ResourceManager.shieldTriangle4dmg2.getWidth()*3, ResourceManager.shieldTriangle4dmg2.getHeight()*3, null); break;
					case 2:
						g.drawImage(ResourceManager.shieldTriangle4dmg1, Reference.shields[i].getPosX(), Reference.shields[i].getPosY(), ResourceManager.shieldTriangle4dmg1.getWidth()*3, ResourceManager.shieldTriangle4dmg1.getHeight()*3, null); break;
					case 3:
						g.drawImage(ResourceManager.shieldTriangle4dmg0, Reference.shields[i].getPosX(), Reference.shields[i].getPosY(), ResourceManager.shieldTriangle4dmg0.getWidth()*3, ResourceManager.shieldTriangle4dmg0.getHeight()*3, null); break;
					default:
						continue;
					}
				}
					
			}
			
		//dibujar nave roja
		if(Reference.redShip != null)
			g.drawImage(ResourceManager.redShip, Reference.redShip.getPosX(), Reference.redShip.getPosY(), ResourceManager.redShip.getWidth()*3, ResourceManager.redShip.getHeight()*3, null);
		
		//dibujar aliens
		Reference.map.draw((Graphics2D) g);

		//linea planeta
		float[] dashPattern = {10, 10}; //linea punteada
        g2d.setStroke(new BasicStroke(2, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10, dashPattern, 0));
        g2d.setColor(Color.WHITE);
        g2d.draw(new Line2D.Float(0, 376, 1000, 376));
	}
	
	private static void graphicRunningNivel5(Graphics g) {
		
		Graphics2D g2d = (Graphics2D) g;
		
		//jugador	//posX, posY, width, height
		if(!Reference.player.isDead() && !Reference.player.hasSuperShot()) {
			g.drawImage(ResourceManager.player, Reference.player.getPosX(), Reference.player.getPosY(), ResourceManager.player.getWidth()*3, ResourceManager.player.getHeight()*3, null);
		}
		else if(!Reference.player.isDead() && Reference.player.hasSuperShot()) {
			g.drawImage(ResourceManager.redPlayer, Reference.player.getPosX(), Reference.player.getPosY(), ResourceManager.deadPlayer.getWidth()*3, ResourceManager.deadPlayer.getHeight()*3, null);
		}
		else {
			g.drawImage(ResourceManager.deadPlayer, Reference.player.getPosX(), Reference.player.getPosY(), ResourceManager.redPlayer.getWidth()*3, ResourceManager.redPlayer.getHeight()*3, null);
		}
			
		//texto
		g.setFont(new Font("Arial", Font.BOLD, 20));
		g.setColor(Color.WHITE);
		g.drawString("Score: ", 500, 560);
		g.drawString(""+score, 700, 560);
		g.drawString("Lives: ", 40, 560);
		
		//vidas
		for(int i=0; i<Reference.player.getLives(); i++) {
			g.drawImage(ResourceManager.player, i*40+110, 550, ResourceManager.player.getWidth()*2, ResourceManager.player.getHeight()*2, null);
		}
		
		//linea verde
		g.setColor(Color.GREEN);
		g.fillRect(5, 530, 880, 5);
		
		//dibujar proyectiles
		g.setColor(Color.WHITE);
		for(int i=0; i<Reference.projectiles.size(); i++) {
			g.fillRect(Reference.projectiles.get(i).getPosX(), Reference.projectiles.get(i).getPosY(), 2, 10);
		}
			
		//dibujar nave roja
		if(Reference.redShip != null)
			g.drawImage(ResourceManager.redShip, Reference.redShip.getPosX(), Reference.redShip.getPosY(), ResourceManager.redShip.getWidth()*3, ResourceManager.redShip.getHeight()*3, null);
		
		//dibujar aliens
		Reference.map.draw((Graphics2D) g);
		
		//linea planeta
		float[] dashPattern = {10, 10}; //linea punteada
        g2d.setStroke(new BasicStroke(2, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10, dashPattern, 0));
        g2d.setColor(Color.WHITE);
        g2d.draw(new Line2D.Float(0, 376, 1000, 376));
	}
	
	private static void graphicMainMenu(Graphics g) {
		//titulo
		g.setColor(Color.WHITE);
		g.setFont(new Font("Arial", Font.BOLD, 90));
		g.setColor(Color.WHITE);
	
		g.drawImage(ResourceManager.logo, 78, 40, ResourceManager.logo.getWidth()*3, ResourceManager.logo.getHeight()*3, null);

		
		//puntos aliens
		g.drawImage(ResourceManager.alien0, 367, 230, ResourceManager.alien0.getWidth()*3, ResourceManager.alien0.getHeight()*3, null);
		g.drawImage(ResourceManager.alien1, 367, 260, ResourceManager.alien1.getWidth()*3, ResourceManager.alien1.getHeight()*3, null);
		g.drawImage(ResourceManager.alien2, 372, 290, ResourceManager.alien2.getWidth()*3, ResourceManager.alien2.getHeight()*3, null);
		
		g.setFont(new Font("Arial", Font.PLAIN, 20));
		g.drawString("= 10 pts", 417, 250);
		g.drawString("= 20 pts", 417, 280);
		g.drawString("= 30 pts", 417, 310);
		
		//botones
		g.drawString("---- Jugar ----", 375, 360);
		g.drawString("[1] Nivel 1", 390, 390);
		g.drawString("[2] Nivel 2", 390, 420);
		g.drawString("[3] Nivel 3", 390, 450);
		g.drawString("[4] Nivel 4", 390, 480);
		g.drawString("[5] Nivel 5", 390, 510);
		g.drawString("[Space] Highscores", 350, 550);
	}
	
	private static void graphicGameOver(Graphics g) {
		g.setColor(Color.RED);
		g.setFont(new Font("Arial", Font.BOLD, 50));
		g.drawString("GAME OVER", 300, 150);
		
		
		g.setColor(Color.WHITE);
		g.setFont(new Font("Arial", Font.PLAIN, 20));
		g.drawString("Your score: "+score, 390, 200);
		g.drawString("Presione cualquier tecla para regresar al menu", 252, 250);
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
		
		g.drawString("Presione cualquier tecla para regresar al menú", 240, 500);
	}
	
}
