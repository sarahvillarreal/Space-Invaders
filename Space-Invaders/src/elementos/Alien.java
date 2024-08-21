package elementos;

import java.awt.Rectangle;

import acciones.ResourceManager;

public class Alien extends ScreenElement {
    
    private static int xDir = 10;
    private static int motionDelay = 20;
    private static int frame = 0;
    
    private int deathDelay = 20;
    
    /** Creates one alien object */
    public Alien(int posX, int posY) {
        this.posX = posX;
        this.posY = posY;
        this.died = false;
    }
    
    /** Moves one alien depending on its direction */
    public void moveSideways() {
        posX += xDir;
    }
    
    /** Moves one alien down of 10 pixels */
    public void moveDownwards() {
        posY += 10;
    }
    
    /** Checks if this alien is at the end of the screen and changes its direction
     * @return true if the direction has changed, false if not */
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
    
    /** Getter Method */
    public int getDir() {
        return xDir;
    }
    
    /** Getter Method */
    public static int getMotionDelay() {
        return motionDelay;
    }
    
    /** Decreases motionDelay by 1 */
    public static void decreaseMotionDelay() {
        if (motionDelay > 0) motionDelay--;
    }
    
    /** Changes frame from 0 to 1 or vice versa */
    public static void changeFrame() {
        if (frame == 0) frame = 1;
        else if (frame == 1) frame = 0;
    }
    
    /** Getter Method */
    public static int getFrame() {
        return frame;
    }
    
    /** Decreases death delay by 1 */
    public void decreaseDeathDelay() {
        deathDelay--;
    }
    
    /** Getter Method */
    public int getDeathDelay() {
        return deathDelay;
    }
    
    /** Setter Method for posX */
    public void setPosX(int posX) {
        this.posX = posX;
    }
    
    /** Setter Method for posY */
    public void setPosY(int posY) {
        this.posY = posY;
    }
    
    @Override
    public Rectangle getBoundingBox() {
        return new Rectangle(posX, posY, ResourceManager.alien1.getWidth() * 3, ResourceManager.alien1.getHeight() * 3);
    }
}
