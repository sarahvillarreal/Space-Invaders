package elementos;

import java.awt.Rectangle;

import acciones.ResourceManager;

public class Alien extends ScreenElement {
    
    private static int xDir = 10;
    private static int motionDelay = 1;
    private static int motionDelay2 = 1;
    private static int frame = 0;
    
    private int deathDelay = 20;
    
    //Crea un alien
    public Alien(int posX, int posY) {
        this.posX = posX;
        this.posY = posY;
        this.died = false;
    }
    
    //Mueve un alien dependiendo de su dirección hacia los costados
    public void moveSideways() {
        posX += xDir;
    }
    
    //Mueve un alien hacia abajo
    public void moveDownwards() {
        posY += 20;
    }
    
    //Chequea si el alien está en el final de la pantalla en x y cambia su dirección
    public boolean checkDir() {
        if (posX < 20) {
            xDir = 10;
            return true;
        } else if (posX > 810) {
            xDir = -10;
            return true;
        }
        return false;
    }
    
    public int getDir() {
        return xDir;
    }

    public static int getMotionDelay() {
        return motionDelay;
    }

    public static int getMotionDelay2() {
        return motionDelay2;
    }
    
    //Baja la velocidad de los aliens de dos en dos
    public static void decreaseMotionDelay() {
        if (motionDelay > 0) motionDelay = motionDelay-2;
        
        if (motionDelay2 > 0) motionDelay2 = motionDelay2-2;
    }
    
    //Para que cambien las fotos de los aliens por frame
    public static void changeFrame() {
        if (frame == 0) frame = 1;
        else if (frame == 1) frame = 0;
    }
    
    public static int getFrame() {
        return frame;
    }
    
    //Baja el delay de muerte de uno en uno
    public void decreaseDeathDelay() {
        deathDelay--;
    }

    public int getDeathDelay() {
        return deathDelay;
    }
    
    public void setPosX(int posX) {
        this.posX = posX;
    }
    
    public void setPosY(int posY) {
        this.posY = posY;
    }
    
    @Override
    public Rectangle getBoundingBox() {
        return new Rectangle(posX, posY, ResourceManager.alien1.getWidth() * 3, ResourceManager.alien1.getHeight() * 3);
    }
}
