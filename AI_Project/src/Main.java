import java.util.ArrayList;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

import board.*;

public class Main extends Application{
	private static Scene scene = null;
	private Board board;

	public void start(Stage primaryStage) throws Exception {
		Pane root = FXMLLoader.load(getClass().getResource("board/Board.fxml"));
		scene = new Scene(root, 1200, 800);
		
		primaryStage.setScene(scene);
		primaryStage.setTitle("Nine Men's Morris");
		primaryStage.setResizable(false);
		primaryStage.show();
		
		ArrayList<Circle> whitePieces = new ArrayList<Circle>();
		ArrayList<Circle> blackPieces = new ArrayList<Circle>();
		
		// Data bind game pieces
		board = new Board();
		whitePieces.addAll(FXCollections.observableList(board.whitePieces));
		blackPieces.addAll(FXCollections.observableList(board.blackPieces));
		
		Pane pane = (Pane) scene.lookup("#pane");
		pane.getChildren().addAll(board.blackPieces);
		pane.getChildren().addAll(board.whitePieces);
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