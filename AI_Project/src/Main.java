import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class Main extends Application{
	private static Scene scene = null;
	private Board board;

	public void start(Stage primaryStage) throws Exception {
		Pane root = FXMLLoader.load(getClass().getResource("Board.fxml"));
		scene = new Scene(root, 800, 800);
		
		primaryStage.setScene(scene);
		primaryStage.setTitle("Nine Men's Morris - by J7Defender");
		primaryStage.setResizable(false);
		primaryStage.show();
		
		Group whitePieces = (Group) scene.lookup("#whitePieces");
		Group blackPieces = (Group) scene.lookup("#blackPieces");
		
		// Data bind game pieces
		board = new Board();
		whitePieces.getChildren().setAll(FXCollections.observableList(board.whitePieces));
		blackPieces.getChildren().setAll(FXCollections.observableList(board.blackPieces));
	}
	
	public static void main(String[] args) {
		launch(args);
		
		// One thread for each game phase
		Thread openingThread = new Thread(new Runnable() {
			@Override
			public void run() {
				Game.OpeningPhase();
			}});
		Thread middleThread = new Thread(new Runnable() {
			@Override
			public void run() {
				Game.MiddlePhase();
			}});
		Thread endingThread = new Thread(new Runnable() {
			@Override
			public void run() {
				Game.EndingPhase();
			}});
		
		try {
			// First, start game opening phase
			openingThread.start();
			openingThread.join();
			
			// When opening phase is finished, start middle phase
			middleThread.start();
			middleThread.join();
			
			// When middle phase is finished, start ending phase
			endingThread.start();
			endingThread.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}