package application;

import javafx.event.EventHandler;
import javafx.scene.effect.Effect;
import javafx.scene.effect.Glow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

public class TileButton extends ImageView{

	static final Effect selected = new Glow(10);
	
	private MainController controller;
	private Model model;
	private int xLoc, yLoc;
	
	public TileButton(Image icon) {
		setImage(icon);
		setHandlers();
	}
	
	private void setHandlers(){
		setOnMouseEntered(new EventHandler<MouseEvent>(){

			@Override
			public void handle(MouseEvent arg0) {
				// TODO Auto-generated method stub
				setEffect(selected);
				model.updateCoordinates(xLoc, yLoc);
				controller.updateCoordinates();
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
	
	public void setCoordinates(int x, int y){
		xLoc = x;
		yLoc = y;
	}
}
