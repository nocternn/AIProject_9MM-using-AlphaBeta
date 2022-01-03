package game;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

import board.Board;
import board.BoardController;
import board.Piece;
import javafx.application.Platform;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import main.Main;

public class Game {
	public enum GamePhase {Opening, Middle, Ending};
	public static final int MIN_PIECES_ON_BOARD = 3;
	public static int turnCounter = 0; //Count playing turns
	public static GamePhase currentPhase = GamePhase.Opening;
	
	private static FutureTask<Void> updateUITask;
	
	public static int getTurnCounter() {
		return turnCounter;
	}

	public static void setTurnCounter(int turnCounter) {
		Game.turnCounter = turnCounter;
	}
	
	public static void updateGamePhase(Board board) {
		switch (currentPhase) {
		case Opening:
			updateUITask = new FutureTask<Void>(() -> {
				BoardController.setMaskVisivility(true, false);
			}, null);
			if (board.checkAllPiecesOnBoard(Color.WHITE) && board.checkAllPiecesOnBoard(Color.BLACK)) {
				currentPhase = GamePhase.Middle;
				updateUITask = new FutureTask<Void>(() -> {
					BoardController.setMaskVisivility(false, false);
				}, null);
			}
			break;
		case Middle:
			updateUITask = new FutureTask<Void>(() -> {
				BoardController.setMaskVisivility(false, false);
			}, null);
			if (board.getNumberOfPiecesOnBoard(Color.WHITE) == MIN_PIECES_ON_BOARD 
					|| board.getNumberOfPiecesOnBoard(Color.BLACK) == MIN_PIECES_ON_BOARD)
				currentPhase = GamePhase.Ending;
			break;
		case Ending:
			updateUITask = new FutureTask<Void>(() -> {
				BoardController.setMaskVisivility(false, false);
			}, null);
			if (board.getNumberOfPiecesOnBoard(Color.WHITE) < MIN_PIECES_ON_BOARD
					|| board.getNumberOfPiecesOnBoard(Color.BLACK) < MIN_PIECES_ON_BOARD
					|| Board.stepCnt >= 10) {
				updateUITask = new FutureTask<Void>(() -> {
					BoardController.setMaskVisivility(true, false);
				}, null);
				gameOver(board);
			}
			break;
		default:
			break;
		}
		
	    // Block thread until UI is updated
	    try {
	    	Platform.runLater(updateUITask);
			updateUITask.get();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static boolean isGameOver(Board board) {
		if (currentPhase == GamePhase.Ending && (board.getNumberOfPiecesOnBoard(Color.WHITE) < MIN_PIECES_ON_BOARD
			|| board.getNumberOfPiecesOnBoard(Color.BLACK) < MIN_PIECES_ON_BOARD
			|| Board.stepCnt >= 10)) {
			BoardController.setMaskVisivility(false, false);
			gameOver(board);
			return true;
		}
		return false;
	}

	private static void gameOver(Board board) {
		// Show winner/draw screen
        if(Board.stepCnt >= 10){
			BoardController.setGameResultVisibility(true, false, false);
        }
        else if(board.getNumberOfPiecesOnBoard(Color.WHITE) < MIN_PIECES_ON_BOARD){
			BoardController.setGameResultVisibility(false, false, true);
        }
        else{
			BoardController.setGameResultVisibility(false, true, false);
        }
        System.out.println("Game is over~");
	}
}
