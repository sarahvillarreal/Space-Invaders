package elementos;

import java.awt.Rectangle;

import principal.Reference;
import acciones.ResourceManager;

public class Player extends ScreenElement {

	public final int shotDelay = 12;
	private boolean superShot;
	
	private int lives;
	
	//Crea un jugador en su respectiva posición
	public Player() {
		posX = Reference.windowWidth/2;
		posY = Reference.windowHeight-100;
		lives = 3;
		died = false;
		superShot = false;
	}

	//Muebe el jugador en x
	public void move(int xDir) {
		posX+=xDir;
	}
	
	
	public int getLives() {return lives;}
	
	//Aumenta las vidas de a una, el máximo es 5
	public void addLife() {
		if(lives < 5) lives++;
	}
	
	//Disminuye las vidas de a una
	public void takeLife() {lives--;}
	
	

	public void setSuperShot(boolean superShot) {this.superShot=superShot;}
	
	public boolean hasSuperShot() {return superShot;}

	
	@Override
	public Rectangle getBoundingBox() {
		return new Rectangle(posX, posY, ResourceManager.player.getWidth()*3, ResourceManager.player.getHeight()*3);
	}

	
}