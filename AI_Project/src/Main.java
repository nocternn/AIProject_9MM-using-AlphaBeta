import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.shape.*;
import javafx.stage.Stage;

public class Main extends Application{
	private static Scene scene = null;

	public void start(Stage primaryStage) throws Exception {
		Pane root = FXMLLoader.load(getClass().getResource("Board.fxml"));
		scene = new Scene(root, 800, 800);
		
		primaryStage.setScene(scene);
		primaryStage.setTitle("Nine Men's Morris - by J7Defender");
		primaryStage.setResizable(false);
		primaryStage.show();
		
		//Test Drag and Drop, WORKED but with that 1 object only
		Circle circle = (Circle) scene.lookup("#white1");
		circle.setOnMouseClicked(event -> {
			System.out.println("Drag Done");
			
		});
		circle.setOnMouseDragged(event -> {
			circle.setCenterX(event.getX());
			circle.setCenterY(event.getY());
		});
	}
	
	private void OpeningPhase() {
		
	}
	
	private void MidGamePhase() {
		
	}
	
	private void EndGamePhase() {
		
	}
	
	private void GameEnd() {
		
	}
	
	public static void main(String[] args) {
		launch(args);
	}
	
}