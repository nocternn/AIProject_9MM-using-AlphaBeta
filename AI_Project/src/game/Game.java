package game;

import board.Board;
import board.Piece;
import javafx.scene.paint.Color;
import main.Main;

public class Game {
	public enum GamePhase {Opening, Middle, Ending};
	public static final int MIN_PIECES_ON_BOARD = 3;
	public static int turnCounter = 0; //Count playing turns
	
	private static GamePhase currentPhase = GamePhase.Opening;
	private static Board board = Main.getBoard();
	
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
			if (board.checkAllPiecesOnBoard(white) && board.checkAllPiecesOnBoard(black))
				currentPhase = GamePhase.Middle;
			break;
		case Middle:
			if (board.getNumberOfPiecesOnBoard(gameboard, Color.WHITE) == MIN_PIECES_ON_BOARD 
					&& board.getNumberOfPiecesOnBoard(gameboard, Color.BLACK) == MIN_PIECES_ON_BOARD)
				currentPhase = GamePhase.Ending;
			else if (board.getNumberOfPiecesOnBoard(gameboard, Color.WHITE) < MIN_PIECES_ON_BOARD
					|| board.getNumberOfPiecesOnBoard(gameboard, Color.BLACK) < MIN_PIECES_ON_BOARD)
				gameOver();
			break;
		case Ending:
			if (board.getNumberOfPiecesOnBoard(gameboard, Color.WHITE) < MIN_PIECES_ON_BOARD
					|| board.getNumberOfPiecesOnBoard(gameboard, Color.BLACK) < MIN_PIECES_ON_BOARD)
				gameOver();
			break;
		}
	}

	public static void gameOver() {
		// TODO Show winner/draw screen
		System.out.println("Game is over~");
	}
}
