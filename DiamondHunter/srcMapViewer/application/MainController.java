package application;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class MainController implements Initializable {

	
	private static Stage stage;
	@FXML private MenuItem loadTilesetButton;
	@FXML private MenuItem loadMapButton;
	@FXML private MenuItem saveTilesetButton;
	@FXML private MenuItem saveMapButton;
	@FXML private MenuItem closeButton;
	@FXML private HBox normalTiles;
	@FXML private HBox blockedTiles;
	@FXML private GridPane mapGrid;
	@FXML private ImageView tilesetImageViewer;
	private FileChooser fileChooser = new FileChooser();
	
	private Model mapEditorModel = new Model();
	
	public static void setStage(Stage newStage){
		stage = newStage;
	}
	
	@FXML
	private void handleLoadingTileset(ActionEvent event){	
		//TODO: Restrict file types to BufferedImage supported images
		fileChooser.setTitle("Open Tileset");
		String directory = fileChooser.showOpenDialog(stage).getAbsolutePath(); //TODO: Add dialog escape handling

		if(validateDirectory(directory)){
			File imageFile = new File(directory);
			Image tilesetImage = new Image(imageFile.toURI().toString());

			tilesetImageViewer.setImage(tilesetImage);
		
			mapEditorModel.loadTiles(directory);

			for(int i = 0; i < mapEditorModel.numTilesAcross; i++){
				normalTiles.getChildren().add(createTileButton(0, i));				
				blockedTiles.getChildren().add(createTileButton(1, i));
			}
			
			loadMapButton.setDisable(false);
		} else {
			System.out.println("Invalid directory entered for Tileset Directory");
		//TODO: JavaFX warning notification
		}
	}
	
	private TileButton createTileButton(int type, int index){
		Image icon = SwingFXUtils.toFXImage(mapEditorModel.getTiles()[type][index].image, null);
		TileButton newButton = new TileButton(icon);
		return newButton;		
	}
	
	private TileButton createTileButton(int mapValue){
		int tilesPerRow = mapEditorModel.numTilesAcross;

		if(mapValue >= tilesPerRow)
			return createTileButton(1, mapValue - tilesPerRow);
		else
			return createTileButton(0, mapValue);
	}
	
	@FXML
	private void handleLoadingMap(ActionEvent event){
		fileChooser.setTitle("Open Map");
		String directory = fileChooser.showOpenDialog(stage).getAbsolutePath();

		if(validateDirectory(directory)){
			mapEditorModel.loadMap(directory);	
			createMapGrid(mapEditorModel.getNumCols(),mapEditorModel.getNumRows(),mapEditorModel.getMap());

		} else {
			System.out.println("Invalid directory entered for Map Directory");
			//TODO: JavaFX warning notification
		}
	}
	
	private boolean validateDirectory(String directory){
		return true; //TODO: Fix the regex pattern
		//return Pattern.matches("^(.+)/([^/]+)$", directory);
	}
	
	private void createMapGrid(int cols, int rows, int[][] map){
		for(int r = 0; r < rows; r++)
			for(int c = 0; c < cols; c++){		
				mapGrid.add(createTileButton(mapEditorModel.getMap()[r][c]), c, r);
		}
		
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		assert loadTilesetButton != null : "Load Tileset button was not injected!";
		assert loadMapButton != null : "Load Map button was not injected!";
		assert mapGrid != null: "Map Grid was not injected!";
	}
}
