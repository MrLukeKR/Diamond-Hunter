package application;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class MainController implements Initializable {

	
	private static Stage stage;
	private static Model mapEditorModel;
	
	@FXML private MenuItem loadTilesetButton;
	@FXML private MenuItem loadMapButton;
	@FXML private MenuItem saveTilesetButton;
	@FXML private MenuItem saveMapButton;
	@FXML private MenuItem closeButton;
	@FXML private ToggleButton axeButton;
	@FXML private ToggleButton boatButton;
	@FXML private GridPane mapGrid;
	@FXML private Label coordLabel;
	@FXML private Label blockedLabel;
	private FileChooser fileChooser = new FileChooser();
	
	public static void setModel(Model model){
		mapEditorModel = model;
	}
	
	public static void setStage(Stage newStage){
		stage = newStage;
	}
	
	@FXML
	private void handleLoadingTileset(ActionEvent event){	
		//TODO: Restrict file types to BufferedImage supported images
		fileChooser.setTitle("Open Tileset");
		String directory = fileChooser.showOpenDialog(stage).getAbsolutePath(); //TODO: Add dialog escape handling

		if(validateDirectory(directory)){
			mapEditorModel.loadTiles(directory);
		
			loadMapButton.setDisable(false);
		} else {
			System.out.println("Invalid directory entered for Tileset Directory");
		//TODO: JavaFX warning notification
		}
	}
	
	private TileButton createTileButton(int type, int index){
		Image icon = SwingFXUtils.toFXImage(mapEditorModel.getTiles()[type][index].image, null);
		TileButton newButton = new TileButton(icon);
		if(type == 1)
			newButton.setIsBlocked(true);
		else
			newButton.setIsBlocked(false);
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
			createMapGrid();

		} else {
			System.out.println("Invalid directory entered for Map Directory");
			//TODO: JavaFX warning notification
		}
	}

	void updateCoordinates(){
		coordLabel.setText("X: " + mapEditorModel.getCurrentX() + " Y: " + mapEditorModel.getCurrentY());
	}
	
	private boolean validateDirectory(String directory){
		return true; //TODO: Fix the regex pattern
		//return Pattern.matches("^(.+)/([^/]+)$", directory);
	}
	
	public void loadDefaultMap(){
		mapEditorModel.loadDefaultMap();
		createMapGrid();
	}
	
	private void createMapGrid(){
		int cols = mapEditorModel.getNumCols();
		int rows = mapEditorModel.getNumRows();
		int [][] map = mapEditorModel.getMap();
		
		for(int r = 0; r < rows; r++)
			for(int c = 0; c < cols; c++){		
				TileButton tempButton = createTileButton(map[r][c]);
				tempButton.setController(this);
				tempButton.setModel(mapEditorModel);
				tempButton.setCoordinates(c, r);
				mapGrid.add(tempButton, c, r);
				
		}
		
	}
	
	@FXML private void exitApplication(ActionEvent event){
		System.exit(0);
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		assert loadTilesetButton != null : "Load Tileset button was not injected!";
		assert loadMapButton != null : "Load Map button was not injected!";
		assert mapGrid != null: "Map Grid was not injected!";
		assert boatButton != null: "Boat Button was not injected!";
		loadDefaultMap();

		ImageView axeIcon = new ImageView();
		ImageView boatIcon = new ImageView();

		axeIcon.setImage(SwingFXUtils.toFXImage(Model.ITEMS[1][1], null));
		boatIcon.setImage(SwingFXUtils.toFXImage(Model.ITEMS[1][0], null));
		
		axeButton.setGraphic(axeIcon);
		boatButton.setGraphic(boatIcon);
	}

	public void updateIsBlocked(boolean isBlocked) {
		if (isBlocked)
		blockedLabel.setText("Blocked");
		else
			blockedLabel.setText("Unblocked");
	}
}
