package elementos;

import java.awt.Rectangle;

import acciones.ResourceManager;

public class AlienShip extends ScreenElement {

	//Crea una nave roja a la derecha de la pantalla
	public AlienShip() {
		this.posX = 900;
		this.posY = 20;
	}
	
	//Mueve la nave hacia la izquierda
	public void move() {
		this.posX-=10;
	}
	
	@Override
	public Rectangle getBoundingBox() {
		return new Rectangle(posX, posY, ResourceManager.redShip.getWidth()*3, ResourceManager.redShip.getHeight()*3);
	}

}
