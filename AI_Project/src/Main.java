import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import board.*;

public class Main extends Application{
	private static Scene scene = null;

	public void start(Stage primaryStage) throws Exception {
		Pane root = FXMLLoader.load(getClass().getResource("board/Board.fxml"));
		scene = new Scene(root, 1200, 800);
		
		primaryStage.setScene(scene);
		primaryStage.setTitle("Nine Men's Morris");
		primaryStage.setResizable(false);
		primaryStage.show();
		
		// Data bind game pieces
		
		Pane pane = (Pane) scene.lookup("#pane");
		pane.getChildren().addAll(Board.blackPieces);
		pane.getChildren().addAll(Board.whitePieces);
	}
	
	public static void main(String[] args) {
		launch(args);
	}
	
}