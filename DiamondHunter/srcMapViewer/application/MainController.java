package application;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class MainController implements Initializable {

	@FXML private Button loadTilesetButton;
	@FXML private Button loadMapButton;
	@FXML private TextField tilesetDirectory;
	@FXML private TextField mapDirectory;
	@FXML private ImageView tilesetViewer;
	private Image tilesetImage;
	
	@FXML
	private void handleLoadingTiles(ActionEvent event){
		String directory = tilesetDirectory.getText();

		if(validateDirectory(directory)){
			File imageFile = new File(directory);
			
			tilesetImage = new Image(imageFile.toURI().toString());
			tilesetViewer.setImage(tilesetImage);
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
		//TODO: REGEX of directory structure
		
		return true;
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
		assert tilesetViewer != null : "Tilset Image Viewer was not injected!";
		
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
