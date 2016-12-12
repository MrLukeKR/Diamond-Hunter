package application;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import javax.imageio.ImageIO;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;

public class Model {	
	public static final int EMPTY= - 1, AXE = 1, BOAT = 0;

	
	private MainController controller;
	
	// map
	private int[][] map;
	private int tileSize = 16; //TODO: Make this variable
	private int numRows;
	private int numCols;
	
	private int currentX;
	private int currentY;
	
	// tileset
	private BufferedImage tileset;
	private static BufferedImage spritesheet;
	public int numTilesAcross;
	private Tile[][] tiles;
	public static BufferedImage[][] ITEMS; 
	
	private int axeX = EMPTY, axeY = EMPTY, boatX = EMPTY, boatY = EMPTY;
	private int item = EMPTY;

	public void loadItems(){
		ITEMS = load("/Sprites/items.gif", 16, 16);
	}
	
	public void saveItemMap(String dir){
		try {
			PrintWriter itemMapFile = new PrintWriter(dir,"UTF-8");
			itemMapFile.println(AXE + "," + axeX + "," + axeY);
			itemMapFile.println(BOAT + "," + boatX + "," + boatY);
			itemMapFile.close();
		} catch (FileNotFoundException | UnsupportedEncodingException e) {
			e.printStackTrace();
		}	
	}
	
	private TileButton createTileButton(int type, int index){
		Image icon = SwingFXUtils.toFXImage(getTiles()[type][index].image, null);
		TileButton newButton = new TileButton(icon);
		if(type == 1)
			newButton.setIsBlocked(true);
		else
			newButton.setIsBlocked(false);
		return newButton;		
	}
	
	private TileButton createTileButton(int mapValue){
		if(mapValue >= numTilesAcross)
			return createTileButton(1, mapValue - numTilesAcross);
		else
			return createTileButton(0, mapValue);
	}
	
	public void createMapGrid(GridPane mapGrid){
		int cols = getNumCols();
		int rows = getNumRows();
		int [][] map = getMap();
		
		for(int r = 0; r < rows; r++)
			for(int c = 0; c < cols; c++){		
				TileButton tempButton = createTileButton(map[r][c]);
				tempButton.setModel(this);
				tempButton.setCoordinates(c, r);
				mapGrid.add(tempButton, c, r);
		}
		
	}
	 
	public void loadTiles(File f){
		try {
			tileset = ImageIO.read(f);
			processTiles(tileset);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
public void loadTiles(String s) {
		
		try {	
			tileset = ImageIO.read(getClass().getResourceAsStream(s));
			processTiles(tileset);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
	}

public void processTiles(BufferedImage tileset){
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

public void updateCoordinates(int x, int y, boolean isBlocked){
	currentX = x; currentY = y;
	controller.updateCoordinates();
	controller.updateIsBlocked(isBlocked);
	controller.updateHasItem(getItem(x, y));
	
}

public void loadDefaultMap(){
	loadTiles("/Tilesets/testtileset.gif");
	loadMap("/Maps/testmap.map");
}
	
public void loadMap(File f){
	try {
		processMap(new FileInputStream(f));
	} catch (FileNotFoundException e) {
		e.printStackTrace();
	}
}

public void loadMap(String s) {
	try { 
		processMap(getClass().getResourceAsStream(s));
	} catch(Exception e) 
	{ 
		e.printStackTrace(); 
	}
}

public void processMap(InputStream in){
	try {
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

public BufferedImage[][] load(String s, int w, int h) {
	BufferedImage[][] ret;
	try {
	    spritesheet = ImageIO.read(getClass().getResourceAsStream(s));
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
	if(blocked && item != EMPTY){
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
	
	controller.displayItem(xLoc, yLoc);
	
	return true;
}

public int getCurrentItem() {
	return item;
}

public boolean itemPlaced(int i) {
	if(i == AXE){
		if(axeX == EMPTY && axeY == EMPTY)
			return false;
	}else if (i == BOAT)
		if(boatX == EMPTY && boatY == EMPTY)
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
	return EMPTY;
}

	public void setController(MainController mainController) {
		controller = mainController;
	}
}
