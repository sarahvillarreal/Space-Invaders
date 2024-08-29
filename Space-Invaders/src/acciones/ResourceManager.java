package acciones;

import java.awt.image.BufferedImage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

import javax.imageio.ImageIO;

import principal.Reference;

public class ResourceManager {

	public static final BufferedImage player = readImage("player");
	public static final BufferedImage logo = readImage("logo");
	public static final BufferedImage alien0 = readImage("alien0");
	public static final BufferedImage alien0_1 = readImage("alien0_1");
	public static final BufferedImage alien1 = readImage("alien1");
	public static final BufferedImage alien1_1 = readImage("alien1_1");
	public static final BufferedImage alien2 = readImage("alien2");
	public static final BufferedImage deadPlayer = readImage("deadPlayer");
	public static final BufferedImage redPlayer = readImage("redPlayer");
	public static final BufferedImage shieldSquareDmg0 = readImage("shield_square_dmg0");
	public static final BufferedImage shieldSquareDmg1 = readImage("shield_square_dmg1");
	public static final BufferedImage shieldSquareDmg2 = readImage("shield_square_dmg2");
	public static final BufferedImage shieldTriangle1dmg0 = readImage("shield_triangle1_dmg0");
	public static final BufferedImage shieldTriangle1dmg1 = readImage("shield_triangle1_dmg1");
	public static final BufferedImage shieldTriangle1dmg2 = readImage("shield_triangle1_dmg2");
	public static final BufferedImage shieldTriangle2dmg0 = readImage("shield_triangle2_dmg0");
	public static final BufferedImage shieldTriangle2dmg1 = readImage("shield_triangle2_dmg1");
	public static final BufferedImage shieldTriangle2dmg2 = readImage("shield_triangle2_dmg2");
	public static final BufferedImage shieldTriangle3dmg0 = readImage("shield_triangle3_dmg0");
	public static final BufferedImage shieldTriangle3dmg1 = readImage("shield_triangle3_dmg1");
	public static final BufferedImage shieldTriangle3dmg2 = readImage("shield_triangle3_dmg2");
	public static final BufferedImage shieldTriangle4dmg0 = readImage("shield_triangle4_dmg0");
	public static final BufferedImage shieldTriangle4dmg1 = readImage("shield_triangle4_dmg1");
	public static final BufferedImage shieldTriangle4dmg2 = readImage("shield_triangle4_dmg2");
	public static final BufferedImage explosion = readImage("explosion");
	public static final BufferedImage redShip = readImage("redShip");
	
	private static BufferedReader reader;
	private static PrintWriter writer;
	
	//Lee las imagenes
	private static BufferedImage readImage(String fileName) {
		
		BufferedImage img = null;
		
		try {
			img = ImageIO.read(new File("src/imagenes/"+fileName+".png"));
		} catch (IOException e) {
		}
		
		return img;
	}

	//Intento de que lea el .txt con los puntajes
	public static int[] readHighscores() {
		
		int[] array = new int[10];
		
		try {
			reader = new BufferedReader(new FileReader("src/archivos/highscores.txt"));
		} catch (FileNotFoundException e1) {
		}
		
		try {
			String str = reader.readLine();
			while(str!=null) {
				String[] strarray = str.split("-");
				array[Integer.parseInt(strarray[0])] = Integer.parseInt(strarray[1]);
				
				str = reader.readLine();
			}
		} catch (IOException e) {
		}
		
		return array;
	}
	
	//Intento de que escriba lo que tiene guardado Reference.highscores[] en highscores.txt
	public static void writeHighscores() {
		
		try {
			writer = new PrintWriter(new File("src/archivos/highscores.txt"));
		} catch (FileNotFoundException e) {
		}
		
		for(int i=0; i<Reference.highscores.length; i++) {
			writer.println(i+"-"+Reference.highscores[i]);
		}
		writer.close();
	}
}
