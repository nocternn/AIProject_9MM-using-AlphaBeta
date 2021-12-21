import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class Main extends Application{
	private static Scene scene = null;

	public void start(Stage primaryStage) throws Exception {
		// TODO Auto-generated method stub
		Pane root = FXMLLoader.load(getClass().getResource("Board.fxml"));
		scene = new Scene(root, 800, 800);
		
		primaryStage.setScene(scene);
		primaryStage.setTitle("Nine Men's Morris - by J7Defender");
		primaryStage.setResizable(false);
		primaryStage.show();
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		launch(args);
	}
}