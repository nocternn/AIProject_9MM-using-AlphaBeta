package game;

import board.Board;

public class Game {
	public enum GamePhase {Opening, Middle, Ending};
	public static final int MAX_PIECES_ON_BOARD = 9;
	public static final int MIN_PIECES_ON_BOARD = 3;
	public static int turnCounter = 0; //Count playing turns
	private static GamePhase currentPhase = GamePhase.Opening;
	
	public static int getTurnCounter() {
		return turnCounter;
	}

	public static void setTurnCounter(int turnCounter) {
		Game.turnCounter = turnCounter;
	}


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
			if (Board.getNumberWhiteOnBoard() < MIN_PIECES_ON_BOARD || Board.getNumberBlackOnBoard() < MIN_PIECES_ON_BOARD)
				gameOver();
			break;
		}
		
		Algorithms.updateGamePhase(currentPhase);
	}

	private static void gameOver() {
		// TODO Show winner/draw screen
		System.out.println("Game is over~");
	}
}
