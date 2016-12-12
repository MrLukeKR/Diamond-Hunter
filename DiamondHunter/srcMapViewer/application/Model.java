package application;

//Imports

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
import com.neet.DiamondHunter.TileMap.Tile;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;

/**
 * 
 * @author Luke Rose <psylr5@nottingham.ac.uk>
 * @version 1.0
 *
 */

public class Model {	
	/**
	 * Constant value that represents an EMPTY item
	 */
	public static final int EMPTY= - 1;
	
	/**
	 * Constant value that represents an AXE item
	 */
	public static final int AXE = 1;
	
	/**
	 * Constant value that represents a BOAT item
	 */
	public static final int BOAT = 0;
	
	/**
	 * Constant value that represents a BOAT and an AXE item
	 */
	public static final int BOTH = 2;
	
	private Controller controller;
	
	// map
	private int[][] map;
	private int tileSize = 16;
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

	public void loadItems(){ ITEMS = load("/Sprites/items.gif", 16, 16); }
	public void setController(Controller mainController) { controller = mainController; }
	
	public void saveItemMap(String dir){
		try {
			PrintWriter itemMapFile = new PrintWriter(dir,"UTF-8");
			itemMapFile.println(AXE + "," + axeX + "," + axeY);
			itemMapFile.println(BOAT + "," + boatX + "," + boatY);
			itemMapFile.close();
		} 
		catch (FileNotFoundException | UnsupportedEncodingException e) { e.printStackTrace(); }	
	}
	
	private TileButton createTileButton(int type, int index, int x, int y){
		Image icon = SwingFXUtils.toFXImage(tiles[type][index].getImage(), null);
		TileButton newButton = new TileButton(icon);
		
		if(type == 1)	newButton.setIsBlocked(true);
		else			newButton.setIsBlocked(false);
	
		newButton.setCoordinates(x, y);
		newButton.setModel(this);
		
		return newButton;		
	}
	
	private TileButton createTileButton(int mapValue, int x, int y){
		if(mapValue >= numTilesAcross)	return createTileButton(1, mapValue - numTilesAcross, x, y);
		else 							return createTileButton(0, mapValue, x, y);
	}
	
	public void createMapGrid(GridPane mapGrid){	
		for(int y = 0; y < numRows; y++)
			for(int x = 0; x < numCols; x++)
				mapGrid.add(createTileButton(map[y][x], x, y), x, y);
	}
	 
	public void loadTiles(File f){
		try {
			tileset = ImageIO.read(f);
			processTiles(tileset);
		} 
		catch (FileNotFoundException e) { e.printStackTrace(); } 
		catch (IOException e) { e.printStackTrace(); }
	}
	
	public void loadTiles(String s) {
		
		try {	
			tileset = ImageIO.read(getClass().getResourceAsStream(s));
			processTiles(tileset);
		} 
		catch(Exception e) { e.printStackTrace(); }
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
		controller.updateHasItem(getItemID(x, y));
	}

	public void loadDefaultMap(){
		loadTiles("/Tilesets/testtileset.gif");
		loadMap("/Maps/testmap.map");
	}
	
	public void loadMap(File f){
		try { processMap(new FileInputStream(f)); } 
		catch (FileNotFoundException e) { e.printStackTrace(); }
	}

	public void loadMap(String s) {
		try { processMap(getClass().getResourceAsStream(s)); } 
		catch(Exception e)  { e.printStackTrace(); }
	}

	public void processMap(InputStream in){
		try {
			BufferedReader br = new BufferedReader( new InputStreamReader(in) );
	
			setNumCols(Integer.parseInt(br.readLine()));
			setNumRows(Integer.parseInt(br.readLine()));
			setMap(new int[getNumRows()][getNumCols()]);
	
			String delims = "\\s+";
			for(int row = 0; row < getNumRows(); row++) {
				String line = br.readLine();
				String[] tokens = line.split(delims);
				for(int col = 0; col < getNumCols(); col++)
					getMap()[row][col] = Integer.parseInt(tokens[col]);
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
			for(int i = 0; i < height; i++) 
				for(int j = 0; j < width; j++) 
					ret[i][j] = spritesheet.getSubimage(j * w, i * h, w, h);
			
			return ret;
		}
		catch(Exception e) {
			e.printStackTrace();
			System.out.println("Error loading graphics.");
			System.exit(0);
		}
		return null;
	}

	public void setNumRows(int numRows) { this.numRows = numRows; }
	public int getNumRows() 			{ return numRows; }

	public void setNumCols(int numCols) { this.numCols = numCols; }
	public int getNumCols() 			{ return numCols; }

	public void setTiles(Tile[][] tiles){ this.tiles = tiles; }
	public Tile[][] getTiles() 			{ return tiles; }

	public void setMap(int[][] map)		{ this.map = map; }
	public int[][] getMap() 			{ return map; }

	public void setCurrentX(int x)		{ currentX = x; }
	public int getCurrentX() 			{ return currentX; }

	public void setCurrentY(int y) 		{ currentY = y; }
	public int getCurrentY() 			{ return currentY; }

	public void setItem(int itemID) { item = itemID; }

	public int getCurrentItem() { return item; }

	public boolean placeItem(int xLoc, int yLoc, boolean blocked) {
		if(blocked && item != EMPTY){
			Alert warning = new Alert(AlertType.WARNING);
			warning.setTitle("Blocked Tile");
			warning.setHeaderText("You're about to add an item to a blocked tile!");
			warning.setContentText("Please make sure the item can still be obtained somehow.\n\n(PRO TIP: You can get to blocked areas by cutting trees down with an Axe or crossing water with a Boat)");
			warning.showAndWait();
		}
	
		if		 (item == Model.BOAT){	boatX = xLoc; boatY = yLoc; }
		else if  (item == Model.AXE) {	axeX = xLoc;  axeY = yLoc;  }
		else     return false;
	
		controller.displayItem(xLoc, yLoc);
	
		return true;
	}

	public boolean itemPlaced(int i) {
		boolean axeExists  = (axeX  != EMPTY && axeY  != EMPTY);
		boolean boatExists = (boatX != EMPTY && boatY != EMPTY);
	
		if(i == AXE){
			if(!axeExists) return false;
		}else if (i == BOAT)
			if(!boatExists) return false;
	
		return true;
	}

	public int getItemID(int xLoc, int yLoc) {
		boolean axePlaced  = (xLoc == axeX && yLoc == axeY);
		boolean boatPlaced = (xLoc == boatX && yLoc == boatY);
		
		if(axePlaced && boatPlaced) return BOTH;
		else if(axePlaced)			return AXE;
		else if(boatPlaced)			return BOAT;
		
		return EMPTY;
	}
}