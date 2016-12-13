package Interfaces;

import application.Model;

public interface ITileButton {

	/**
	 * Assigns an instantiated Model reference to the TileButton, for interaction between the Model and TileButton
	 * @param model - Instantiated object of type Model.java
	 */
	void setModel(Model model);

	/**
	 * Assigns an X / Y position in the Map to the TileButton
	 * @param x - X Co-ordinate of this TileButton
	 * @param y - Y Co-ordinate of this TileButton
	 */
	void setCoordinates(int x, int y);

	/**
	 * Set to true if the Tile disallows players walking across it, set to false if it allows players walking across it
	 * @param blocked - Whether or not the tile blocks players from walking on it
	 */
	void setIsBlocked(boolean blocked);

}