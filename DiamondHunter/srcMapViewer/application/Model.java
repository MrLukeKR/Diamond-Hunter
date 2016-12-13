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

public class Model implements IModel {	
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
	
	/**
	 * Represents the coordinates of an item
	 */
	private int axeX = EMPTY, axeY = EMPTY, boatX = EMPTY, boatY = EMPTY;
	/**
	 * Represents the currently selected item from the "Item Toolbar"
	 */
	private int item = EMPTY;

	/**
	 * Loads items from an item spritesheet into the item buffer
	 */
	public void loadItems(){ ITEMS = loadItems("/Sprites/items.gif", 16, 16); }
	
	/**
	 * Gives the Model a reference to a Controller so that it may access functions that update the user interface
	 * @param mainController - The controller to be passed in
	 */
	public void setController(Controller mainController) { controller = mainController; }
	
	
	/**
	 * Saves the item coordinates in a .itm (Item Map) file to a location chosen by the user
	 * @param dir - The location (file path) to store the file in
	 */
	public void saveItemMap(String dir){
		try {
			PrintWriter itemMapFile = new PrintWriter(dir,"UTF-8");
			itemMapFile.println(AXE + "," + axeX + "," + axeY);
			itemMapFile.println(BOAT + "," + boatX + "," + boatY);
			itemMapFile.close();
		} 
		catch (FileNotFoundException | UnsupportedEncodingException e) { e.printStackTrace(); }	
	}
	
	/**
	 * Creates a new TileButton and returns it
	 * @param type - Whether or not the Tile is a blocking tile or not
	 * @param index - The position of the Tile in the Tileset buffer
	 * @param x - The x coordinate of the tile
	 * @param y - the y coordinate of the tile
	 * @return A new fully initialised TileButton
	 */
	private TileButton createTileButton(int type, int index, int x, int y){
		Image icon = SwingFXUtils.toFXImage(tiles[type][index].getImage(), null);
		TileButton newButton = new TileButton(icon);
		
		if(type == 1)	newButton.setIsBlocked(true);
		else			newButton.setIsBlocked(false);
	
		newButton.setCoordinates(x, y);
		newButton.setModel(this);
		
		return newButton;		
	}
	
	/**
	 * Creates a new TileButton based only on its Map representation and returns it.
	 * This is helpful when you may not know if it's blocking or not, as this function calculates its blocking status.
	 * @param mapValue - The number stored in the .map (Map File) that represents this tile
	 * @param x - The x coordinate of the Tile
	 * @param y - The y coordinate of the Tile
	 * @return A new fully initialised TileButton
	 */
	private TileButton createTileButton(int mapValue, int x, int y){
		if(mapValue >= numTilesAcross)	return createTileButton(1, mapValue - numTilesAcross, x, y);
		else 							return createTileButton(0, mapValue, x, y);
	}
	
	/**
	 * Generates the graphical representation of the Map
	 * @param mapGrid - The GridPane from the UI that displays the map
	 */
	public void createMapGrid(GridPane mapGrid){	
		for(int y = 0; y < numRows; y++)
			for(int x = 0; x < numCols; x++)
				mapGrid.add(createTileButton(map[y][x], x, y), x, y);
	}
	 
	/**
	 * Loads Tile images from an external file
	 * @param f - Image file that contains Tile icons
	 */
	public void loadTiles(File f){
		try {
			tileset = ImageIO.read(f);
			processTiles(tileset);
		} 
		catch (FileNotFoundException e) { e.printStackTrace(); } 
		catch (IOException e) { e.printStackTrace(); }
	}
	
	/**
	 * Loads Tile images from an internal Resource
	 * @param s - The Resource's file path
	 */
	public void loadTiles(String s) {
		try {	
			tileset = ImageIO.read(getClass().getResourceAsStream(s));
			processTiles(tileset);
		} 
		catch(Exception e) { e.printStackTrace(); }
	}

	/**
	 * Separates a Tileset into individual Tiles
	 * @param tileset - The BufferedImage representation of the entire TileSet
	 */
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

	/**
	 * Informs the controller that the currently selected coordinates have changed and updates the UI
	 * @param x - The currently selected x coordinate
	 * @param y - The currently selected y coordinate
	 * @param isBlocked - Whether or not the Tile allows player access
	 */
	public void updateCoordinates(int x, int y, boolean isBlocked){
		currentX = x; currentY = y;
		controller.updateCoordinates();
		controller.updateIsBlocked(isBlocked);
		controller.updateHasItem(getItemID(x, y));
	}

	/**
	 * Loads the original Map when first starting up, to save time loading the files from scratch
	 */
	public void loadDefaultMap(){
		loadTiles("/Tilesets/testtileset.gif");
		loadMap("/Maps/testmap.map");
	}
	
	/**
	 * Loads a Map from an external file
	 * @param f - The file path to the .map (Map) File
	 */
	public void loadMap(File f){
		try { processMap(new FileInputStream(f)); } 
		catch (FileNotFoundException e) { e.printStackTrace(); }
	}

	/**
	 * Loads a Map from an internal Resource
	 * @param s - The file path to the .map (Map) Resource
	 */
	public void loadMap(String s) {
		try { processMap(getClass().getResourceAsStream(s)); } 
		catch(Exception e)  { e.printStackTrace(); }
	}

	/**
	 * Reads the Map data from the passed in InputStream and stores it in a buffer
	 * @param in - The InputStream from loadMap(String s) or loadMap(File f)
	 */
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

	/**
	 * Loads the icons required for in-game collectible Items
	 * @param s - The file path to the icon file
	 * @param w - The width of the icons
	 * @param h - The height of the icons
	 * @return A buffer containing the separate item icons
	 */
	public BufferedImage[][] loadItems(String s, int w, int h) {
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

	
	/**
	 * Sets the number of rows in the map
	 * @param numRows - Number of rows in the map
	 */
	public void setNumRows(int numRows) { this.numRows = numRows; }
	
	/**
	 * Returns the number of rows in the map
	 * @return The number of rows in the map
	 */
	public int getNumRows() 			{ return numRows; }

	/**
	 * Sets the number of columns in the map
	 * @param numCols - Number of columns in the map
	 */
	public void setNumCols(int numCols) { this.numCols = numCols; }
	
	/**
	 * Returns the number of columns in the map
	 * @return The number of columns in the map
	 */
	public int getNumCols() 			{ return numCols; }

	/**
	 * Sets which tiles to use in the map
	 * @param tiles - 2D array of Tiles (Blocked and Unblocked)
	 */
	public void setTiles(Tile[][] tiles){ this.tiles = tiles; }
	
	/**
	 * Returns the currently used Tile Set
	 * @return Currently assigned Tile buffer
	 */
	public Tile[][] getTiles() 			{ return tiles; }

	/**
	 * Sets the map up, where each array element is a Tile index
	 * @param map - 2D array of integers that represent Tile indexes
	 */
	public void setMap(int[][] map)		{ this.map = map; }
	
	/**
	 * Returns the currently assigned map
	 * @return A 2D array of integers that represent Tile indexes
	 */
	public int[][] getMap() 			{ return map; }

	/**
	 * Sets the currently selected x coordinate
	 * @param x - Currently selected x coordinate
	 */
	public void setCurrentX(int x)		{ currentX = x; }
	
	/**
	 * Gets the currently selected x coordinate
	 * @return Currently selected x coordinate
	 */
	public int getCurrentX() 			{ return currentX; }

	/**
	 * Sets the currently selected y coordinate
	 * @param y - Currently selected y coordinate
	 */
	public void setCurrentY(int y) 		{ currentY = y; }
	
	/**
	 * Gets the currently selected y coordinate
	 * @return Currently selected y coordinate
	 */
	public int getCurrentY() 			{ return currentY; }

	/**
	 * Informs the Model of which Item is currently held by the user for placement on the Map
	 * @param itemID - The item type (Axe, Boat, etc.)
	 */
	public void setItem(int itemID) { item = itemID; }

	/**
	 * Gets the Item currently held by the user for placement on the Map
	 * @return The item type (Axe, Boat, etc.)
	 */
	public int getCurrentItem() { return item; }

	/**
	 * Sets the location of the currently selected item (from getCurrentItem())
	 * @param xLoc - The x coordinate of the item
	 * @param yLoc - The y coordinate of the item
	 * @param blocked - Whether or not the item blocks user access
	 * @return True if successful, False if the selected Item doesn't exist or isn't implemented
	 */
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

	/**
	 * Gets whether or not an item exists on the map
	 * @param i - The Item type (Axe, Boat, etc.)
	 * @return True if the item is on the map, False if not
	 */
	public boolean itemPlaced(int i) {
		boolean axeExists  = (axeX  != EMPTY && axeY  != EMPTY);
		boolean boatExists = (boatX != EMPTY && boatY != EMPTY);
	
		if(i == AXE){
			if(!axeExists) return false;
		}else if (i == BOAT)
			if(!boatExists) return false;
	
		return true;
	}

	/**
	 * Gets the Item at a given X/Y coordinate
	 * @param xLoc - x coordinate to check for an Item
	 * @param yLoc - y coordinate to check for an Item
	 * @return The Item number (Axe, Boat, Both or Empty)
	 */
	public int getItemID(int xLoc, int yLoc) {
		boolean axePlaced  = (xLoc == axeX && yLoc == axeY);
		boolean boatPlaced = (xLoc == boatX && yLoc == boatY);
		
		if(axePlaced && boatPlaced) return BOTH;
		else if(axePlaced)			return AXE;
		else if(boatPlaced)			return BOAT;
		
		return EMPTY;
	}
}