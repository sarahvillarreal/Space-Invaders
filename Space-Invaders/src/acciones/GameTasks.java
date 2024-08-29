package acciones;

import java.util.ArrayList;
import java.util.Random;

import principal.Reference;
import elementos.Alien;
import elementos.Player;
import elementos.Projectile;
import elementos.Shield;
import hilo.GameBoard;
import elementos.Shield.Type;
import elementos.AlienShip;

public class GameTasks {

	private static int alienTimeDelay = 0;
	private static int playerTimeDelay = 0;
	private static int superShotDelay = 0;
	
	static boolean activate = false;
	
	//Controla la duracion de movimiento de los aliens y el jugador, junto al tiempo de super disparo del mismo
	public static void changeDelays() {
		alienTimeDelay++;
		playerTimeDelay++;
		if(Reference.player.hasSuperShot()) superShotDelay++;
	}

	public static int getAlienDelay() {return alienTimeDelay;}
	public static int getPlayerDelay() {return playerTimeDelay;}
	
	public static void resetPlayerDelay() {playerTimeDelay=0;}
	
	
	//Inicializa los aliens en sus respectivos lugares
	public static void initAliens() {
		Reference.aliens = new Alien[Reference.alienColumns][Reference.alienRows];
		for(int i=0; i<Reference.alienColumns; i++) {
			for(int j=0; j<Reference.alienRows; j++) {
				Reference.aliens[i][j] = new Alien(200+i*45, 50+j*45);
			}
		}
		alienTimeDelay = 0;
	}

	//Hace que se muevan todos los aliens
	public static void moveAllAliens() {
		
		//Mueve a todos los aliens en una direccion en x (lento)
		if(GameBoard.state == GameState.NIVEL_UNO || GameBoard.state == GameState.NIVEL_DOS || GameBoard.state == GameState.NIVEL_TRES) {
			
			if(alienTimeDelay==Alien.getMotionDelay()) {
				moveAllAliensSideways();
				alienTimeDelay=0;
			}
		}
		
		//Mueve a todos los aliens en una direccion en x (rapido)
				if(GameBoard.state == GameState.NIVEL_CUATRO || GameBoard.state == GameState.NIVEL_CINCO) {
					
					if(alienTimeDelay==Alien.getMotionDelay2()) {
						moveAllAliensSideways();
						alienTimeDelay=0;
					}
				}
		
		//Chequea si el ultimo alien toca el borde para cambiar de direccion
		if(Reference.aliens[0][0].getDir()>0) {
			if(Reference.aliens[Reference.alienColumns-1][Reference.alienRows-1].checkDir())
				moveAllAliensDown();
		}
		else {
			if(Reference.aliens[0][0].checkDir())
				moveAllAliensDown();
		}
	}
	
	//Hace que todos y cada uno de los aliens se muevan en x mediante alien[i][j].moveSideways()
	private static void moveAllAliensSideways() {
		for(int i=0; i<Reference.alienColumns; i++) {
			for(int j=0; j<Reference.alienRows; j++) {
				try {
					Reference.aliens[i][j].moveSideways();
				} catch (NullPointerException e) {
					continue;
				}
			}
		}
		Alien.changeFrame();
	}
	
	//Hace que todos y cada uno de los aliens se muevan en y mediante alien[i][j].moveDownwards()
	private static void moveAllAliensDown() {
		for(int i=0; i<Reference.alienColumns; i++) {
			for(int j=0; j<Reference.alienRows; j++) {
				try {
					Reference.aliens[i][j].moveDownwards();
				} catch (NullPointerException e) {
					continue;
				}
			}
		}
	}
	
	//Mueve todos los proyectiles
	public static void movePrjs() {
		for(int i=0; i<Reference.projectiles.size(); i++) {
			Reference.projectiles.get(i).move();
			
			//Borra los proyectiles que se van de la pantalla
			if(Reference.projectiles.get(i).getPosY()<0 || Reference.projectiles.get(i).getPosY()>Reference.windowHeight-80) {
				Reference.projectiles.remove(i);
			}
		}
	}
	
	//Intento para el nivel 3... no salio
	public static void movePrjs2() {
		for(int i=0; i<Reference.projectiles.size(); i++) {
			Reference.projectiles.get(i).move2();
			
			//Borra los proyectiles que se van de la pantalla
			if(Reference.projectiles.get(i).getPosY()<0 || Reference.projectiles.get(i).getPosY()>Reference.windowHeight-80) {
				Reference.projectiles.remove(i);
			}
		}
	}
	
	//Elige un alien random y, si es valido, dispara
	public static void alienShoot() {
		Random rand = new Random();
		int col = rand.nextInt(Reference.alienColumns*4);
		int row = rand.nextInt(Reference.alienRows*4);
		
		//Sale si el alien elegido esta muerto o si la opcion no es valida
		if(col>=Reference.alienColumns || row>=Reference.alienRows) return;
		if(Reference.aliens[col][row].isDead()) return;
		
		//Si el alien esta en la ultima fila, dispara
		if(row == 4) {
			Reference.projectiles.add(new Projectile(Reference.aliens[col][row].getPosX()+15, Reference.aliens[col][row].getPosY()+25, 2));
			return;
		}
		
		//Si el alien no tiene otro alien debajo de el, dispara
		if(Reference.aliens[col][row+1].isDead()) {
			Reference.projectiles.add(new Projectile(Reference.aliens[col][row].getPosX()+15, Reference.aliens[col][row].getPosY()+25, 2));
		}
	}
	
	public static void alienKamikaze() {
		Random rand = new Random();
		int col = rand.nextInt(Reference.alienColumns*4);
		int row = rand.nextInt(Reference.alienRows*4);
		
		//Sale si el alien elegido esta muerto o si la opcion no es valida
		if(col>=Reference.alienColumns || row>=Reference.alienRows) return;
		if(Reference.aliens[col][row].isDead()) return;
		
		//Si el alien esta en la ultima fila, dispara
		if(row == 4) {
			
			activate = true;
			
			//Crear el nuevo proyectil
		    Projectile newProjectile = new Projectile(Reference.aliens[col][row].getPosX()+15, Reference.aliens[col][row].getPosY()+25, 2);
		    movePrjs2();
		    return;
		}
		
		//Si el alien no tiene otro alien debajo de el, entra en modo kamikaze
		if(Reference.aliens[col][row+1].isDead()) {
			
			activate = true;
			
			//Crear el nuevo proyectil
		    Projectile newProjectile = new Projectile(Reference.aliens[col][row].getPosX()+15, Reference.aliens[col][row].getPosY()+25, 2);
		    movePrjs2();
		    return;
		}
	}

	public static boolean activateMove2() {return activate;}
	
	
	//Chequea la colision entre alien y proyectil
	public static boolean checkCollisionAlienPrj() {
		for(int i=0; i<Reference.alienColumns; i++) {
			for(int j=0; j<Reference.alienRows; j++) {
				for(int k=0; k<Reference.projectiles.size(); k++) {
					
					if(Reference.projectiles.get(k).getBoundingBox().intersects(Reference.aliens[i][j].getBoundingBox()) && !Reference.aliens[i][j].isDead()) {
						
						Reference.aliens[i][j].setDead(true);
						Reference.projectiles.remove(k);
						
						switch(Reference.map.getAlienType(i, j)) {
						case 0:
							GameBoard.addScore(10); break;
						case 1:
							GameBoard.addScore(20); break;
						case 2:
							GameBoard.addScore(30); break;
						}
						return true;
					}
				}
			}
		}
		return false;
	}
	
	//Chequea la colision entre jugador y proyectil
	public static boolean checkCollisionPlayerPrj() {
		for(int i=0; i<Reference.projectiles.size(); i++) {
			if(Reference.projectiles.get(i).getBoundingBox().intersects(Reference.player.getBoundingBox())) {
				Reference.projectiles.remove(i);
				Reference.player.takeLife();
				return true;
			}
		}
		return false;
	}
	
	//Chequea la colision entre escudo y proyectil
	public static void checkCollisionShieldPrj() {
		for(int i=0; i<Reference.shields.length; i++) {
			for(int j=0; j<Reference.projectiles.size(); j++) {
				if(Reference.projectiles.get(j).getBoundingBox().intersects(Reference.shields[i].getBoundingBox()) && Reference.shields[i].getDamage()>0) {
					Reference.shields[i].damage();
					Reference.projectiles.remove(j);
				}
			}
		}
	}
	
	////Chequea si la fila mas baja de aliens llego al "planeta"
	public static boolean checkAliensLanded() {
		for(int i=0; i<Reference.aliens.length; i++) {
			for(int j=0; j<Reference.aliens[i].length; j++) {
				if(GameBoard.state == GameState.NIVEL_UNO || GameBoard.state == GameState.NIVEL_DOS || GameBoard.state == GameState.NIVEL_TRES) {
					
					if(Reference.aliens[i][j].getPosY() > 430 && !Reference.aliens[i][j].isDead()) {
						return true;
					}
				}
				
				if(GameBoard.state == GameState.NIVEL_CUATRO || GameBoard.state == GameState.NIVEL_CINCO) {
					
					if(Reference.aliens[i][j].getPosY() > 350 && !Reference.aliens[i][j].isDead()) {
						return true;
					}
				}
			}
		}
		return false;
	}
	
	//Genera un numero random y si es 0 genera una nave roja
	public static void redShipFlight() {
		Random rand = new Random();
		int r = rand.nextInt(750);
		
		if(Reference.redShip == null && r == 0) {
			Reference.redShip = new AlienShip();
		}
			
		if(Reference.redShip != null) {
			Reference.redShip.move();
			if(Reference.redShip.getPosX() < 0)
				Reference.redShip = null;
		}
	}
	
	public static void checkCollisionRedshipPrj() {
		for(int i=0; i<Reference.projectiles.size(); i++) {
			if(Reference.redShip !=null) {
				if(Reference.projectiles.get(i).getBoundingBox().intersects(Reference.redShip.getBoundingBox())) {
					Reference.redShip = null;
					Reference.player.setSuperShot(true);
				}
			}
		}
		if(superShotDelay == 350) Reference.player.setSuperShot(false);
	}

	//Reinicializa algunos de los objetos (escudos y nave roja estan reinicializados en sus respectivos niveles)
	public static void restart() {
		
		//inicializa jugador
		Reference.player = new Player();
				
		//inicializa aliens
		GameTasks.initAliens();
		
		//inicializa la lista de proyectiles
		Reference.projectiles = new ArrayList<Projectile>();
	}

	//Guardar los puntajes
	public static void sortHighscores() {
		int appo;
		
		for(int i=0; i<Reference.highscores.length-1; i++) {
			for(int j=i+1; j<Reference.highscores.length; j++) {
				if(Reference.highscores[j]>Reference.highscores[i]) {
					appo = Reference.highscores[i];
					Reference.highscores[i] = Reference.highscores[j];
					Reference.highscores[j] = appo;
				}
			}
		}
		
	}
}
