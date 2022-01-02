package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import board.*;
import game.Algorithms;

public class Main extends Application{
	private static Scene scene = null;
	private static Board board;
	private static Algorithms algorithms;

	public void start(Stage primaryStage) throws Exception {
		Pane root = FXMLLoader.load(getClass().getResource("../board/Board.fxml"));
		scene = new Scene(root, 1200, 800);
		
		primaryStage.setScene(scene);
		primaryStage.setTitle("Nine Men's Morris");
		primaryStage.setResizable(false);
		primaryStage.show();
		
		// Initialize game board
		board = new Board();
		algorithms = new Algorithms();
		
		// Data bind game pieces
		Pane pane = (Pane) scene.lookup("#pane");
		pane.getChildren().addAll(board.getBlackPieces());
		pane.getChildren().addAll(board.getWhitePieces());
		
		BoardController.restartButton.setOnAction(evt -> {
			// Remove current game
			pane.getChildren().removeAll(board.getBlackPieces());
			pane.getChildren().removeAll(board.getWhitePieces());
			// Initialize new game board
			board = new Board();
			algorithms = new Algorithms();
			pane.getChildren().addAll(board.getBlackPieces());
			pane.getChildren().addAll(board.getWhitePieces());
        	System.out.println("Game restarted");});
	}
	
	public static Board getBoard() {
		return board;
	}

	public static Algorithms getAlgorithms() {
		return algorithms;
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}