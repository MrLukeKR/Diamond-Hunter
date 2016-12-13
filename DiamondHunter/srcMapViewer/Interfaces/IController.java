package Interfaces;

public interface IController {

	/**
	 * Updates the text in the info ToolBar of the UI
	 */
	void updateCoordinates();

	/**
	 * Loads the original map and tiles
	 */
	void loadDefaultMap();

	/**
	 * Updates the info in the ToolBar to show whether or not the currently selected Tile is blocked or not
	 * @param isBlocked - Whether or not the Tile allows player access
	 */
	void updateIsBlocked(boolean isBlocked);

	/**
	 * Updates the info in the ToolBar to show what items are at the current location
	 * @param item
	 */
	void updateHasItem(int item);

	/**
	 * Places the relevant Item icon at the X/Y coordinates given
	 * @param xLoc - x location of the item
	 * @param yLoc - y location of the item
	 */
	void displayItem(int xLoc, int yLoc);

}