package application;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.imageio.ImageIO;

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
}
