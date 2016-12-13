package Interfaces;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.InputStream;

import com.neet.DiamondHunter.TileMap.Tile;

import javafx.scene.layout.GridPane;

public interface IModel {

	/**
	 * Constant value that represents an EMPTY item
	 */
	int EMPTY = -1;
	/**
	 * Constant value that represents an AXE item
	 */
	int AXE = 1;
	/**
	 * Constant value that represents a BOAT item
	 */
	int BOAT = 0;
	/**
	 * Constant value that represents a BOAT and an AXE item
	 */
	int BOTH = 2;

	/**
	 * Loads items from an item spritesheet into the item buffer
	 */
	void loadItems();

	/**
	 * Gives the Model a reference to a Controller so that it may access functions that update the user interface
	 * @param mainController - The controller to be passed in
	 */
	void setController(IController mainController);

	/**
	 * Saves the item coordinates in a .itm (Item Map) file to a location chosen by the user
	 * @param dir - The location (file path) to store the file in
	 */
	void saveItemMap(String dir);

	/**
	 * Generates the graphical representation of the Map
	 * @param mapGrid - The GridPane from the UI that displays the map
	 */
	void createMapGrid(GridPane mapGrid);

	/**
	 * Loads Tile images from an external file
	 * @param f - Image file that contains Tile icons
	 */
	void loadTiles(File f);

	/**
	 * Loads Tile images from an internal Resource
	 * @param s - The Resource's file path
	 */
	void loadTiles(String s);

	/**
	 * Separates a Tileset into individual Tiles
	 * @param tileset - The BufferedImage representation of the entire TileSet
	 */
	void processTiles(BufferedImage tileset);

	/**
	 * Informs the controller that the currently selected coordinates have changed and updates the UI
	 * @param x - The currently selected x coordinate
	 * @param y - The currently selected y coordinate
	 * @param isBlocked - Whether or not the Tile allows player access
	 */
	void updateCoordinates(int x, int y, boolean isBlocked);

	/**
	 * Loads the original Map when first starting up, to save time loading the files from scratch
	 */
	void loadDefaultMap();

	/**
	 * Loads a Map from an external file
	 * @param f - The file path to the .map (Map) File
	 */
	void loadMap(File f);

	/**
	 * Loads a Map from an internal Resource
	 * @param s - The file path to the .map (Map) Resource
	 */
	void loadMap(String s);

	/**
	 * Reads the Map data from the passed in InputStream and stores it in a buffer
	 * @param in - The InputStream from loadMap(String s) or loadMap(File f)
	 */
	void processMap(InputStream in);

	/**
	 * Loads the icons required for in-game collectible Items
	 * @param s - The file path to the icon file
	 * @param w - The width of the icons
	 * @param h - The height of the icons
	 * @return A buffer containing the separate item icons
	 */
	BufferedImage[][] loadItems(String s, int w, int h);

	/**
	 * Sets the number of rows in the map
	 * @param numRows - Number of rows in the map
	 */
	void setNumRows(int numRows);

	/**
	 * Returns the number of rows in the map
	 * @return The number of rows in the map
	 */
	int getNumRows();

	/**
	 * Sets the number of columns in the map
	 * @param numCols - Number of columns in the map
	 */
	void setNumCols(int numCols);

	/**
	 * Returns the number of columns in the map
	 * @return The number of columns in the map
	 */
	int getNumCols();

	/**
	 * Sets which tiles to use in the map
	 * @param tiles - 2D array of Tiles (Blocked and Unblocked)
	 */
	void setTiles(Tile[][] tiles);

	/**
	 * Returns the currently used Tile Set
	 * @return Currently assigned Tile buffer
	 */
	Tile[][] getTiles();

	/**
	 * Sets the map up, where each array element is a Tile index
	 * @param map - 2D array of integers that represent Tile indexes
	 */
	void setMap(int[][] map);

	/**
	 * Returns the currently assigned map
	 * @return A 2D array of integers that represent Tile indexes
	 */
	int[][] getMap();

	/**
	 * Sets the currently selected x coordinate
	 * @param x - Currently selected x coordinate
	 */
	void setCurrentX(int x);

	/**
	 * Gets the currently selected x coordinate
	 * @return Currently selected x coordinate
	 */
	int getCurrentX();

	/**
	 * Sets the currently selected y coordinate
	 * @param y - Currently selected y coordinate
	 */
	void setCurrentY(int y);

	/**
	 * Gets the currently selected y coordinate
	 * @return Currently selected y coordinate
	 */
	int getCurrentY();

	/**
	 * Informs the Model of which Item is currently held by the user for placement on the Map
	 * @param itemID - The item type (Axe, Boat, etc.)
	 */
	void setItem(int itemID);

	/**
	 * Gets the Item currently held by the user for placement on the Map
	 * @return The item type (Axe, Boat, etc.)
	 */
	int getCurrentItem();

	/**
	 * Sets the location of the currently selected item (from getCurrentItem())
	 * @param xLoc - The x coordinate of the item
	 * @param yLoc - The y coordinate of the item
	 * @param blocked - Whether or not the item blocks user access
	 * @return True if successful, False if the selected Item doesn't exist or isn't implemented
	 */
	boolean placeItem(int xLoc, int yLoc, boolean blocked);

	/**
	 * Gets whether or not an item exists on the map
	 * @param i - The Item type (Axe, Boat, etc.)
	 * @return True if the item is on the map, False if not
	 */
	boolean itemPlaced(int i);

	/**
	 * Gets the Item at a given X/Y coordinate
	 * @param xLoc - x coordinate to check for an Item
	 * @param yLoc - y coordinate to check for an Item
	 * @return The Item number (Axe, Boat, Both or Empty)
	 */
	int getItemID(int xLoc, int yLoc);

}