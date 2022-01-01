package game;

import board.Board;
import javafx.scene.paint.Color;

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
			if (Board.getNumberWhiteOnBoard() == MAX_PIECES_ON_BOARD && Board.getNumberBlackOnBoard() == MAX_PIECES_ON_BOARD) {
				currentPhase = GamePhase.Middle;
				for (int i=0; i<24; i++) {
					if (Board.isOccupied(i) && Board.board[i].getColor() == Color.WHITE) 
						Board.board[i].toFront();			
				}
			}
			break;
		case Middle:
			if (Board.getNumberWhiteOnBoard() == MIN_PIECES_ON_BOARD && Board.getNumberBlackOnBoard() == MIN_PIECES_ON_BOARD)
				currentPhase = GamePhase.Ending;
			break;
		case Ending:
			if (Board.getNumberWhiteOnBoard() < MIN_PIECES_ON_BOARD || Board.getNumberBlackOnBoard() < MIN_PIECES_ON_BOARD)
				gameOver();
			break;
		}
	}

	private static void gameOver() {
		// TODO Show winner/draw screen
		System.out.println("Game is over~");
	}
}
