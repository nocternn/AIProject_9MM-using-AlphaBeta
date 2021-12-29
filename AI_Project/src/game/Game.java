package game;

import board.Board;
import board.BoardController;

import javafx.scene.text.*;

public class Game {
	public enum GamePhase {Opening, Middle, Ending};
	public static final int MAX_PIECES_ON_BOARD = 9;
	public static final int MIN_PIECES_ON_BOARD = 3;
	
	private static GamePhase currentPhase = GamePhase.Opening;

	public static GamePhase getCurrentPhase() {
		return currentPhase;
	}
	
	public static void updateGamePhase() {
		switch (currentPhase) {
		case Opening:
			if (Board.getNumberWhiteOnBoard() == MAX_PIECES_ON_BOARD && Board.getNumberBlackOnBoard() == MAX_PIECES_ON_BOARD)
				currentPhase = GamePhase.Middle;
			break;
		case Middle:
			if (Board.getNumberWhiteOnBoard() == MIN_PIECES_ON_BOARD && Board.getNumberBlackOnBoard() == MIN_PIECES_ON_BOARD)
				currentPhase = GamePhase.Ending;
			break;
		case Ending:
			if (Board.getNumberWhiteOnBoard() < MIN_PIECES_ON_BOARD || Board.getNumberBlackOnBoard() < MIN_PIECES_ON_BOARD || Board.stepCnt >= 10)
				gameOver();
			break;
		}
	}

	private static void gameOver() {
		if(Board.stepCnt >= 10){
			BoardController.gameResult = new Text(525, 470, "DRAW");
			BoardController.gameResult.setFont(Font.font("Georgia", FontWeight.BOLD, 36));
			BoardController.gameResult.setVisible(true);
		}
		else if(Board.getNumberWhiteOnBoard() < MIN_PIECES_ON_BOARD){
			BoardController.gameResult = new Text(522, 440, "BLACK\n  WIN!");
			BoardController.gameResult.setFont(Font.font("Georgia", FontWeight.BOLD, 36));
			BoardController.gameResult.setVisible(true);
		}
		else{
			BoardController.gameResult = new Text(519, 440, "WHITE\n   WIN!");
			BoardController.gameResult.setFont(Font.font("Georgia", FontWeight.BOLD, 36));
			BoardController.gameResult.setVisible(true);
		}
		System.out.println("Game is over~");
	}
}
