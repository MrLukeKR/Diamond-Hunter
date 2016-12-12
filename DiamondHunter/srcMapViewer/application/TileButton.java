package application;

import javafx.event.EventHandler;
import javafx.scene.effect.Effect;
import javafx.scene.effect.InnerShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

public class TileButton extends ImageView{

	private Effect selected;

	private Model model;
	private int xLoc = Model.EMPTY, yLoc = Model.EMPTY;
	private boolean isBlocked;
	
	public TileButton(Image icon) {
		setImage(icon);
		setHandlers();
	}
	
	public void setModel (Model model){ this.model = model; }
	public void setCoordinates(int x, int y){ xLoc = x; yLoc = y; }
	
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

	public void setIsBlocked(boolean blocked){
		isBlocked = blocked;
		
		if(blocked) selected = new InnerShadow(5, Color.RED);
		else 		selected = new InnerShadow(5, Color.BLACK);
	}
}
