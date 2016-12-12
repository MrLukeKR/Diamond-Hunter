package application;

//Imports

import javafx.event.EventHandler;
import javafx.scene.effect.Effect;
import javafx.scene.effect.InnerShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

/**
 * 
 * @author Luke Rose <psylr5@nottingham.ac.uk>
 * @version 1.0
 *
 */

public class TileButton extends ImageView{

	private Effect selected;

	/**
	 * Allows non-static access to the model class
	 */
	private Model model;
	
	/**
	 * X / Y location of the current TileButton on the Map
	 */
	private int xLoc = Model.EMPTY, yLoc = Model.EMPTY;
	
	/**
	 * Flag to show the TileButton disallows Player access (can't walk on the tile)
	 */
	private boolean isBlocked;
	
	/**
	 * TileButton constructor
	 * @param icon - TileSet image assigned to this tile
	 */
	public TileButton(Image icon) {
		setImage(icon);
		setHandlers();
	}
	
	/**
	 * Assigns an instantiated Model reference to the TileButton, for interaction between the Model and TileButton
	 * @param model - Instantiated object of type Model.java
	 */
	public void setModel (Model model){ this.model = model; }
	
	/**
	 * Assigns an X / Y position in the Map to the TileButton
	 * @param x - X Co-ordinate of this TileButton
	 * @param y - Y Co-ordinate of this TileButton
	 */
	public void setCoordinates(int x, int y){ xLoc = x; yLoc = y; }
	
	/**
	 * Set to true if the Tile disallows players walking across it, set to false if it allows players walking across it
	 * @param blocked - Whether or not the tile blocks players from walking on it
	 */
	public void setIsBlocked(boolean blocked){
		isBlocked = blocked;
		
		if(blocked) selected = new InnerShadow(5, Color.RED);
		else 		selected = new InnerShadow(5, Color.BLACK);
	}
	
	
	/**
	 * Initialises event listeners to allow for Item placement on clicking and TileButton highlighting on MouseOver
	 */
	private void setHandlers(){
		setOnMouseEntered(new EventHandler<MouseEvent>(){
			@Override
			public void handle(MouseEvent arg0) {
				setEffect(selected);
				if(xLoc != Model.EMPTY && yLoc != Model.EMPTY)
					model.updateCoordinates(xLoc, yLoc, isBlocked);
			}
		});

		setOnMouseClicked(new EventHandler<MouseEvent>(){
			@Override
			public void handle(MouseEvent arg0) {
				model.placeItem(xLoc, yLoc, isBlocked);
				model.updateCoordinates(xLoc, yLoc, isBlocked);
			}
		});
		
		setOnMouseExited(new EventHandler<MouseEvent>(){
			@Override
			public void handle(MouseEvent event) { setEffect(null); }
			});
	}

	
}
