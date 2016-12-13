package application;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

import Interfaces.IController;
import Interfaces.IModel;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class Controller implements Initializable, IController {
	private static Stage view;
	private static Model model;
	
	//FXML Injected Variables
	
	@FXML private MenuItem loadTilesetButton;
	@FXML private MenuItem loadMapButton;
	@FXML private MenuItem saveMapButton;
	@FXML private MenuItem closeButton;
	@FXML private ToggleButton axeButton;
	@FXML private ToggleButton boatButton;
	@FXML private GridPane mapGrid;
	@FXML private Label coordLabel;
	@FXML private Label blockedLabel;
	private FileChooser tileFileChooser = new FileChooser();
	private FileChooser mapFileChooser = new FileChooser();
	private FileChooser itemFileChooser = new FileChooser();
	
	/**
	 * Contains the image used to represent an Axe item
	 */
	ImageView axeIcon = new ImageView();
	
	/**
	 * Contains the image used to represent a Boat item
	 */
	ImageView boatIcon = new ImageView();

	/**
	 * Allows a model to be passed into the controller, so Model methods can be called directly where necessary
	 * @param m - The model
	 */
	public static void setModel(Model m){ model = m; }
	
	/**
	 * Allows a JavaFX Stage to be passed in to act as a "Parent" window when using File Choosers
	 * @param newview - The JavaFX Stage
	 */
	public static void setView(Stage newview){ view = newview; }
	
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		model.setController(this);
		
		confirmSuccessfulLoad();

		initTileFileChooser();
		initSaveMapChooser();
		initMapFileChooser();

		loadDefaultMap();
		model.loadItems();
		
		initIcons();
	}
	
	//FXML Methods
	
	/**
	 * Opens a File Chooser and loads the tiles from the selected file
	 * @param event - FXML event that fires this method
	 */
	@FXML
	private void handleLoadingTileset(ActionEvent event){	
		File tileFile = tileFileChooser.showOpenDialog(view);
		if(tileFile != null){
			model.loadTiles(tileFile);
			loadMapButton.setDisable(false);
		}
	}
	
	/**
	 * Opens a File Chooser and saves the map to the selected file
	 * @param event - FXML event that fires this method
	 */
	@FXML
	private void saveItemMap(ActionEvent event){
		File itemFile = itemFileChooser.showSaveDialog(view);
		if(itemFile != null) model.saveItemMap(itemFile.getAbsolutePath());
	}
	
	/**
	 * Opens a File Chooser and loads the map from the selected file
	 * @param event - FXML event that fires this method
	 */
	@FXML
	private void handleLoadingMap(ActionEvent event){
		File mapFile = mapFileChooser.showOpenDialog(view);
		if(mapFile != null){
			model.loadMap(mapFile);	
			model.createMapGrid(mapGrid);
		}
	}
	
	/**
	 * Sets the models currently held item when the Axe ToggleButton has been pressed
	 * @param event - FXML event that fires this method
	 */
	@FXML private void axeToggled(ActionEvent event){
		if(axeButton.isSelected()){
			model.setItem(IModel.AXE);
		}else
			model.setItem(IModel.EMPTY);
	}

	/**
	 * Sets the models currently held item when the Boat ToggleButton has been pressed
	 * @param event - FXML event that fires this method
	 */
	@FXML private void boatToggled(ActionEvent event){
		if(boatButton.isSelected())
			model.setItem(IModel.BOAT);
		else
			model.setItem(IModel.EMPTY);
	}
	
	/**
	 * Exits the application when the close button has been pressed
	 * @param event - FXML event that fires this method
	 */
	@FXML private void exitApplication(ActionEvent event){ System.exit(0); }
	
	//Non-FXML Methods
	
	/**
	 * Checks that all of the FXML elements have been injected successfully and if not, throw an exception
	 */
	private void confirmSuccessfulLoad(){
		try {
			if(loadTilesetButton == null) 	throw new Exception("Load Tileset button was not injected!");
			if( loadMapButton == null) 		throw new Exception( "Load Map button was not injected!");
			if( saveMapButton == null) 		throw new Exception( "Save Map Button was not injected!");
			if( closeButton == null)		throw new Exception( "Close Button was not injected!");
			if( axeButton == null) 			throw new Exception( "Axe Button was not injected!");
			if( boatButton == null) 		throw new Exception( "Boat Button was not injected!");		
			if( mapGrid == null) 			throw new Exception( "Map Grid was not injected!");
			if( coordLabel  == null) 		throw new Exception( "Coordinate Label was not injected!");
			if( blockedLabel  == null) 		throw new Exception( "Blocked Label was not injected!");
			if( tileFileChooser  == null) 	throw new Exception( "Tile File Chooser was not created!");
			if( mapFileChooser  == null) 	throw new Exception( "Map File Chooser was not created!");
			if( itemFileChooser  == null) 	throw new Exception( "Item File Chooser was not created!");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void updateCoordinates(){
		coordLabel.setText("X: " + model.getCurrentX() + " Y: " + model.getCurrentY());
	}
	
	@Override
	public void loadDefaultMap(){
		model.loadDefaultMap();
		model.createMapGrid(mapGrid);
	}
	
	/**
	 * Setup of the Tile File Chooser including Title and File Filter
	 */
	private void initTileFileChooser(){
		tileFileChooser.setTitle("Open Tileset");
		tileFileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("GIF", "*.gif"));
	}
	
	/**
	 * Setup of the Save Map File Chooser including Title, File Name and File Filter
	 */
	private void initSaveMapChooser(){
		itemFileChooser.setTitle("Save Item Map");
		itemFileChooser.setInitialFileName("My Item Map");
		itemFileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Item Map", "*.itm"));
	}
	
	/**
	 * Setup of the Load Map File Chooser including Title and File Filter
	 */
	private void initMapFileChooser(){
		mapFileChooser.setTitle("Open Map");
		mapFileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Map", "*.map"));
	}

	@Override
	public void updateIsBlocked(boolean isBlocked) {
		if (isBlocked)  blockedLabel.setText("Blocked");
		else 			blockedLabel.setText("Unblocked");
	}
	
	/**
	 * Places the Axe and Boat Item icons in the relevant ToggleButtons - Ready to be moved to the appropriate X/Y location
	 */
	private void initIcons(){
		axeIcon.setImage(SwingFXUtils.toFXImage(Model.ITEMS[1][IModel.AXE], null));
		boatIcon.setImage(SwingFXUtils.toFXImage(Model.ITEMS[1][IModel.BOAT], null));
		
		axeButton.setGraphic(axeIcon);
		boatButton.setGraphic(boatIcon);
	}
	
	@Override
	public void updateHasItem(int item){
		switch(item){
			case IModel.EMPTY:blockedLabel.setText(blockedLabel.getText() + " - has no items"); 		break;
			case IModel.AXE:  blockedLabel.setText(blockedLabel.getText() + " - has Axe"); 				break;
			case IModel.BOAT: blockedLabel.setText(blockedLabel.getText() + " - has Boat"); 			break;
			case IModel.BOTH: blockedLabel.setText(blockedLabel.getText() + " - has Axe and Boat"); 	break;
		}
	}

	@Override
	public void displayItem(int xLoc, int yLoc) {
		if(model.getCurrentItem() == IModel.AXE){
			axeButton.setGraphic(null);
			if(model.itemPlaced(IModel.AXE))
				mapGrid.getChildren().removeAll(axeIcon);
			mapGrid.add(axeIcon, xLoc, yLoc);
		} else if(model.getCurrentItem() == IModel.BOAT){
			boatButton.setGraphic(null);
			if(model.itemPlaced(IModel.BOAT))
				mapGrid.getChildren().removeAll(boatIcon);
			mapGrid.add(boatIcon, xLoc, yLoc);
		}
	}
}
