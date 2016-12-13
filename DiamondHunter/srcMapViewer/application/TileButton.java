package application;

import Interfaces.IModel;
import Interfaces.ITileButton;

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

public class TileButton extends ImageView implements ITileButton{

	/**
	 * The ImageView effect used with this TileButton - will show a red border if the TileButton is blocked, black border if not
	 */
	private Effect selected;

	/**
	 * Allows non-static access to the model class
	 */
	private Model model;
	
	/**
	 * X / Y location of the current TileButton on the Map
	 */
	private int xLoc = IModel.EMPTY, yLoc = IModel.EMPTY;
	
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
	
	@Override
	public void setModel (Model model){ this.model = model; }
	
	@Override
	public void setCoordinates(int x, int y){ xLoc = x; yLoc = y; }
	
	@Override
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
				if(xLoc != IModel.EMPTY && yLoc != IModel.EMPTY)
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
