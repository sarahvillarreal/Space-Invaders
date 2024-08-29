package elementos;

import java.awt.Rectangle;

public abstract class ScreenElement {

	protected int posX;
	protected int posY;
	
	protected boolean died;
	
	public int getPosX() {return posX;}
	
	public int getPosY() {return posY;}
	
	public boolean isDead() {return died;}
	
	public void setDead(boolean died) {this.died=died;}
	
	//La hitbox del elemento
	public abstract Rectangle getBoundingBox();
}
