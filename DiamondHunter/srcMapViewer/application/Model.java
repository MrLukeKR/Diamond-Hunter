package application;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.imageio.ImageIO;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class Model {

	
	// map
	private int[][] map;
	private int tileSize = 16; //TODO: Make this variable
	private int numRows;
	private int numCols;
	
	private int currentX;
	private int currentY;
	
	// tileset
	private BufferedImage tileset;
	public int numTilesAcross;
	private Tile[][] tiles;
	public static BufferedImage[][] ITEMS = load("Resources/Sprites/items.gif", 16, 16);
	
	private int axeX =-1, axeY=-1, boatX=-1, boatY=-1;
	private int item =-1;
	
	public static int AXE = 1, BOAT = 0;


public void loadTiles(String s) {
		
		try {
			
			tileset = ImageIO.read(new File(s));
			numTilesAcross = tileset.getWidth() / tileSize;
			setTiles(new Tile[2][numTilesAcross]);
			
			BufferedImage subimage;
			for(int col = 0; col < numTilesAcross; col++) {
				subimage = tileset.getSubimage(
							col * tileSize,
							0,
							tileSize,
							tileSize
						);
				getTiles()[0][col] = new Tile(subimage, Tile.NORMAL);
				subimage = tileset.getSubimage(
							col * tileSize,
							tileSize,
							tileSize,
							tileSize
						);
				getTiles()[1][col] = new Tile(subimage, Tile.BLOCKED);
			}
			
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
	}

public void updateCoordinates(int x, int y){
	currentX = x;
	currentY = y;
}

public void loadDefaultMap(){
	loadTiles("Resources/Tilesets/testtileset.gif");
	loadMap("Resources/Maps/testmap.map");
}
	
public void loadMap(String s) {
	
	try {
		
		InputStream in = new FileInputStream(new File(s));
		BufferedReader br = new BufferedReader(
					new InputStreamReader(in)
				);
		
		setNumCols(Integer.parseInt(br.readLine()));
		setNumRows(Integer.parseInt(br.readLine()));
		setMap(new int[getNumRows()][getNumCols()]);
		
		String delims = "\\s+";
		for(int row = 0; row < getNumRows(); row++) {
			String line = br.readLine();
			String[] tokens = line.split(delims);
			for(int col = 0; col < getNumCols(); col++) {
				getMap()[row][col] = Integer.parseInt(tokens[col]);
			}
		}
		br.close();
	}
	catch(Exception e) {
		e.printStackTrace();
	}
	
}

public static BufferedImage[][] load(String s, int w, int h) {
	BufferedImage[][] ret;
	try {
		BufferedImage spritesheet = ImageIO.read(new File(s));
		int width = spritesheet.getWidth() / w;
		int height = spritesheet.getHeight() / h;
		ret = new BufferedImage[height][width];
		for(int i = 0; i < height; i++) {
			for(int j = 0; j < width; j++) {
				ret[i][j] = spritesheet.getSubimage(j * w, i * h, w, h);
			}
		}
		return ret;
	}
	catch(Exception e) {
		e.printStackTrace();
		System.out.println("Error loading graphics.");
		System.exit(0);
	}
	return null;
}

public int getNumRows() {
	return numRows;
}

public void setNumRows(int numRows) {
	this.numRows = numRows;
}

public int getNumCols() {
	return numCols;
}

public void setNumCols(int numCols) {
	this.numCols = numCols;
}

public Tile[][] getTiles() {
	return tiles;
}

public void setTiles(Tile[][] tiles) {
	this.tiles = tiles;
}

public int[][] getMap() {
	return map;
}

public void setMap(int[][] map) {
	this.map = map;
}

public int getCurrentX() {
	return currentX;
}

public void setCurrentX(int currentX) {
	this.currentX = currentX;
}

public int getCurrentY() {
	return currentY;
}

public void setCurrentY(int currentY) {
	this.currentY = currentY;
}

public void setItem(int itemID) {
	item = itemID;
}

public boolean placeItem(int xLoc, int yLoc, boolean blocked) {
	if(blocked && item != -1){
		Alert warning = new Alert(AlertType.WARNING);
		warning.setTitle("Blocked Tile");
		warning.setHeaderText("You're about to add an item to a blocked tile!");
		warning.setContentText("Please make sure the item can still be obtained somehow.\n\n(PRO TIP: You can get to blocked areas by cutting trees down with an Axe or crossing water with a Boat)");
		warning.showAndWait();
		}
	
	if(item == 0){
		boatX = xLoc;
		boatY = yLoc;
	}else if (item == 1){
		axeX = xLoc;
		axeY = yLoc;
	}else
		return false;
	
	return true;
}

public int getCurrentItem() {
	return item;
}

public boolean itemPlaced(int i) {
	if(i == AXE){
		if(axeX == -1 && axeY == -1)
			return false;
	}else if (i == BOAT)
		if(boatX == -1 && boatY == -1)
			return false;
	
		return true;
}

public int getItem(int xLoc, int yLoc) {
	if((xLoc == axeX && yLoc == axeY) && (xLoc == boatX && yLoc == boatY))
		return 2;
	else if(xLoc == axeX && yLoc == axeY)
		return AXE;
	else if(xLoc == boatX && yLoc == boatY)
		return BOAT;
	return -1;
}
}
