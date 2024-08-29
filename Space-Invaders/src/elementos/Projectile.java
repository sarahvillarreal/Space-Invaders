package elementos;

import java.awt.Rectangle;

public class Projectile extends ScreenElement {
	
	private int yDir;
	
	//Crea un proyectil
	public Projectile(int posX, int posY, int yDir) {
		this.posX=posX;
		this.posY=posY;
		this.yDir=yDir;
	}
	
	//Mueve un proyectil en y
	public void move() {
		this.posY+=yDir;
	}
	
	//Mueve el alien kamikaze
	public void move2() {
		int movimiento=1;
		
		if(movimiento<=2)
		{
			this.posY+=yDir;
			this.posX+=5;
			movimiento++;
		}
		
		else if(movimiento>=3)
		{
			this.posY+=yDir;
			this.posX-=5;
			movimiento++;
		}
		
		else if(movimiento==5)
		{
			movimiento = 1;
		}
	}

	@Override
	public Rectangle getBoundingBox() {
		return new Rectangle(posX, posY, 2, 10);
	}
}
