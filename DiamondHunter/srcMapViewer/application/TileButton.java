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
	
	private MainController controller;
	private Model model;
	private int xLoc, yLoc;
	private boolean isBlocked;
	
	public TileButton(Image icon) {
		setImage(icon);
		setHandlers();
		
		xLoc = yLoc = -1;
	}
	
	private void setHandlers(){
		setOnMouseEntered(new EventHandler<MouseEvent>(){

			@Override
			public void handle(MouseEvent arg0) {
				// TODO Auto-generated method stub
				setEffect(selected);
				if(xLoc != -1 && yLoc != -1){
					model.updateCoordinates(xLoc, yLoc);
					controller.updateCoordinates();
					controller.updateIsBlocked(isBlocked);
				}
			}
			
		});
		
		setOnMouseExited(new EventHandler<MouseEvent>(){

			@Override
			public void handle(MouseEvent event) {	
				setEffect(null);  
				}
			});
	}

	public void setController (MainController mainController){
		controller = mainController;
	}
	
	public void setModel (Model model){
		this.model = model;
	}
	
	public void setIsBlocked(boolean blocked){
		isBlocked = blocked;
		if(blocked)
			selected = new InnerShadow(5, Color.RED);
		else
			selected = new InnerShadow(5, Color.BLACK);
	}
	
	public void setCoordinates(int x, int y){
		xLoc = x;
		yLoc = y;
	}
}
