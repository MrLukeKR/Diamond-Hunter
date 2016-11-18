package application;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

public class MainController implements Initializable {

	@FXML private Button loadTilesetButton;
	@FXML private Button loadMapButton;
	@FXML private TextField tilesetDirectory;
	@FXML private TextField mapDirectory;
	@FXML private HBox tilesetLibrary;
	
	@FXML
	private void handleLoadingTiles(ActionEvent event){
		String directory = tilesetDirectory.getText();

		if(validateDirectory(directory)){
			File imageFile = new File(directory);
			Image tilesetImage = new Image(imageFile.toURI().toString());

			ImageView viewer;
			for(int i = 0; i < 10; i ++){
				viewer = new ImageView();
				viewer.setImage(tilesetImage);
				viewer.setPreserveRatio(false);
				viewer.setFitHeight(60);
				viewer.setFitWidth(60);
				tilesetLibrary.getChildren().add(viewer);
			}
		} else {
			System.out.println("Invalid directory entered for Map Directory");
		//TODO: JavaFX warning notification
		}
	}
	
	@FXML
	private void handleLoadingMap(ActionEvent event){
		String directory = mapDirectory.getText();

		if(validateDirectory(directory)){
			
		} else {
			System.out.println("Invalid directory entered for Map Directory");
			//TODO: JavaFX warning notification
		}
	}
	
	
	
	private boolean validateDirectory(String directory){
		return Pattern.matches("^(.+)/([^/]+)$", directory);
	}
	
	private void checkInput(TextField fieldToCheck, Button buttonToAlter){
		if(fieldToCheck.getText().isEmpty())
			buttonToAlter.setDisable(true);
		else
			buttonToAlter.setDisable(false);
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		assert loadTilesetButton != null : "Load Tileset button was not injected!";
		assert loadMapButton != null : "Load Map button was not injected!";
		assert tilesetDirectory != null : "Tileset Directory field was not injected!";
		assert mapDirectory != null : "Map Directory field was not injected!";
		
		tilesetDirectory.textProperty().addListener(new ChangeListener<String>(){

			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				checkInput(tilesetDirectory, loadTilesetButton);	
			}
		});
		
		mapDirectory.textProperty().addListener(new ChangeListener<String>(){

			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				checkInput(mapDirectory, loadMapButton);	
			}
		});
	}
}
