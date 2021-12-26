package board;


import java.util.ArrayList;

import game.Game;
import helper.MoveListener;
import javafx.scene.paint.Color;

public class Board implements MoveListener {
	public ArrayList<Piece> whitePieces = new ArrayList<Piece>();
	public ArrayList<Piece> blackPieces = new ArrayList<Piece>();
	
	private static Piece[] board = new Piece[24];
	
	public Board() {
		// Initialize black pieces.
		int blackIndex = 0;
		for (double centerX = 821.0; centerX <= 821.0+14.0*9; centerX += 14.0) {
			this.blackPieces.add(new Piece(Color.BLACK, centerX, 118, Color.WHITESMOKE, blackIndex, this));
			blackIndex++;
		}
		
		// Initialize white pieces. Set drag and drop feature for white pieces.
		int whiteIndex = 0;
		for (double centerX = 218.0; centerX <= 218.0+14.0*9; centerX += 14.0) {
			this.whitePieces.add(new Piece(Color.WHITE, centerX, 118, Color.BLACK, whiteIndex, this));
			whiteIndex++;
		}
	}

	@Override
	public void movedWhitePiece(int pieceIndex, int initialPosition, int newPosition) {
		// Delete piece at old position
		if (initialPosition >= 0) {
			board[initialPosition] = null;
		}
		// Add piece at new position
		board[newPosition] = this.whitePieces.get(pieceIndex);
		// Black's turn to make a move
		switch (Game.getCurrentPhase()) {
		case Opening:
			putBlackPieceOnBoard();
			break;
		default:
			slideBlackPieceOnBoard();
		}
		// Update game phase
		Game.updateGamePhase();
	}
	
	private void putBlackPieceOnBoard() {
		System.out.println("Put black piece");
	}

	private void slideBlackPieceOnBoard() {
		System.out.println("Slide black piece");
	}

	public static int getNumberWhiteOnBoard() {
		int count = 0;
		for (Piece piece : board) {
			if (piece != null && piece.getColor() == Color.WHITE)
				count++;
		}
		return count;
	}

	public static int getNumberBlackOnBoard() {
		int count = 0;
		for (Piece piece : board) {
			if (piece != null && piece.getColor() == Color.BLACK)
				count++;
		}
		return count;
	}

}