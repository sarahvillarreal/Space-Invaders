package principal;

import java.util.ArrayList;

import elementos.Alien;
import elementos.Player;
import elementos.Projectile;
import elementos.AlienShip;
import elementos.Shield;
import acciones.MapGenerator;
import acciones.ResourceManager;

public class Reference {
	
	public static final int windowWidth = 900;
	public static final int windowHeight = 600;
	
	public static Player player;
	public static Alien aliens[][];
	public static MapGenerator map;
	public static ArrayList<Projectile> projectiles;
	public static Shield shields[];
	public static AlienShip redShip;
	
	public static final int alienRows = 5;
	public static final int alienColumns = 11;
	
	public static int[] highscores = ResourceManager.readHighscores();
}
