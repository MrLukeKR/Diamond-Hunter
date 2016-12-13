package application;

import java.awt.image.BufferedImage;
import java.io.File;

import com.neet.DiamondHunter.TileMap.Tile;

public interface IModel {
	void saveItemMap(String dir);
	void loadTiles(File f);
	void loadTiles(String s);
	void loadMap(File f);
	void loadMap(String s);
	BufferedImage[][] loadItems(String s, int w, int h);
	
	boolean placeItem(int xLoc, int yLoc, boolean blocked);
	boolean itemPlaced(int i);
	int getItemID(int xLoc, int yLoc);
	
	void setNumRows(int numRows);
	int getNumRows();
	void setNumCols(int numCols);
	int getNumCols();
	void setTiles(Tile[][] tiles);
	Tile[][] getTiles();
	void setMap(int[][] map);
	int[][] getMap();
	void setCurrentX(int x);
	int getCurrentX();
	void setCurrentY(int y);
	int getCurrentY();
	void setItem(int itemID);
	int getCurrentItem();
}
