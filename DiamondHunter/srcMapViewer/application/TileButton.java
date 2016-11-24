package application;

import javafx.event.EventHandler;
import javafx.scene.effect.Effect;
import javafx.scene.effect.Glow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

public class TileButton extends ImageView{

	static final Effect selected = new Glow(10);
	
	public TileButton(Image icon) {
		setImage(icon);
		setOnMouseEntered(new EventHandler<MouseEvent>(){

			@Override
			public void handle(MouseEvent arg0) {
				// TODO Auto-generated method stub
				setEffect(selected);
			}
			
		});
		
		setOnMouseExited(new EventHandler<MouseEvent>(){

			@Override
			public void handle(MouseEvent event) {	
				setEffect(null);  
				}
			});
	}

}
