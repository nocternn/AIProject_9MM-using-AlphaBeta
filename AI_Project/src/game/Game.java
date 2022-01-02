package game;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

import board.Board;
import board.BoardController;
import board.Piece;
import javafx.application.Platform;
import javafx.scene.paint.Color;
import main.Main;

public class Game {
	public enum GamePhase {Opening, Middle, Ending};
	public static final int MIN_PIECES_ON_BOARD = 3;
	public static int turnCounter = 0; //Count playing turns
	
	private static GamePhase currentPhase = GamePhase.Opening;
	private static Board board = Main.getBoard();
	private static FutureTask<Void> updateUITask;
	
	public static int getTurnCounter() {
		return turnCounter;
	}

	public static void setTurnCounter(int turnCounter) {
		Game.turnCounter = turnCounter;
	}


	public static GamePhase getCurrentPhase() {
		return currentPhase;
	}
	
	public static void updateGamePhase(Piece[] gameboard, Piece[] white, Piece[] black) {
		switch (currentPhase) {
		case Opening:
			updateUITask = new FutureTask<Void>(() -> {
				BoardController.maskBoard.toFront();
				BoardController.maskWhitePieces.toBack();
			}, null);
			if (board.checkAllPiecesOnBoard(white) && board.checkAllPiecesOnBoard(black)) {
				currentPhase = GamePhase.Middle;
				updateUITask = new FutureTask<Void>(() -> {
					BoardController.maskBoard.toBack();
				}, null);
			}
			break;
		case Middle:
			updateUITask = new FutureTask<Void>(() -> {
				BoardController.maskBoard.toBack();
			}, null);
			if (board.getNumberOfPiecesOnBoard(gameboard, Color.WHITE) == MIN_PIECES_ON_BOARD 
					|| board.getNumberOfPiecesOnBoard(gameboard, Color.BLACK) == MIN_PIECES_ON_BOARD)
				currentPhase = GamePhase.Ending;
			break;
		case Ending:
			updateUITask = new FutureTask<Void>(() -> {
				BoardController.maskBoard.toBack();
			}, null);
			if (board.getNumberOfPiecesOnBoard(gameboard, Color.WHITE) < MIN_PIECES_ON_BOARD
					|| board.getNumberOfPiecesOnBoard(gameboard, Color.BLACK) < MIN_PIECES_ON_BOARD
					|| Board.stepCnt >= 10) {
				updateUITask = new FutureTask<Void>(() -> {
					BoardController.maskBoard.toFront();
				}, null);
				gameOver(gameboard);
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
	
	public static boolean isGameOver(Piece[] gameboard) {
		if (currentPhase == GamePhase.Ending && (board.getNumberOfPiecesOnBoard(gameboard, Color.WHITE) < MIN_PIECES_ON_BOARD
			|| board.getNumberOfPiecesOnBoard(gameboard, Color.BLACK) < MIN_PIECES_ON_BOARD
			|| Board.stepCnt >= 10)) {
			BoardController.maskBoard.toFront();
			gameOver(gameboard);
			return true;
		}
		return false;
	}

	private static void gameOver(Piece[] gameboard) {
		// TODO Show winner/draw screen
        if(Board.stepCnt >= 10){
            BoardController.draw.setVisible(true);
        }
        else if(board.getNumberOfPiecesOnBoard(gameboard, Color.WHITE) < MIN_PIECES_ON_BOARD){
            BoardController.blackWin.setVisible(true);
        }
        else{
            BoardController.whiteWin.setVisible(true);
        }
        System.out.println("Game is over~");
	}
}
