package application;
	
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;

public class MapViewer extends Application {

	private Model model = new Model();
	
	@Override
	public void start(Stage primaryStage) {
		try {
			Controller.setModel(model);
			Controller.setStage(primaryStage);
			
			BorderPane root = new BorderPane();
			Scene scene = new Scene(root,650,795);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			Parent content = FXMLLoader.load(getClass().getClassLoader().getResource("View.fxml"));
			root.setCenter(content);
			
			primaryStage.setScene(scene);
			primaryStage.setResizable(false);
			primaryStage.setTitle("Diamond Hunter Map Editor");
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
