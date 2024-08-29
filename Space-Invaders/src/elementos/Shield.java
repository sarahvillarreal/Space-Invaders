package elementos;

import java.awt.Rectangle;

import acciones.ResourceManager;

public class Shield extends ScreenElement {

	private int damageCount;
	private Type type;
	
	//Crea un escudo
	public Shield(int posX, int posY, Type type) {
		this.posX=posX;
		this.posY=posY;
		damageCount = 3;
		this.type=type; //la forma
	}
	
	public Type getType() {return type;}

	public int getDamage() {return damageCount;}
	
	//Disminuye su "vida" cuando recibe un golpe
	public void damage() {damageCount--;}
	
	@Override
	public Rectangle getBoundingBox() {
		return new Rectangle(posX, posY, ResourceManager.shieldSquareDmg0.getWidth()*3, ResourceManager.shieldSquareDmg0.getHeight()*3);
	}
	
	public enum Type{
		SQUARE,
		TRIANGLE1,
		TRIANGLE2,
		TRIANGLE3,
		TRIANGLE4
	}
}
