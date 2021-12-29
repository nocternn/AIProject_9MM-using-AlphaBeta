package board;


import java.util.ArrayList;

import game.Game;
import game.Game.GamePhase;
import helper.MoveListener;
import javafx.scene.paint.Color;

public class Board implements MoveListener {
	public ArrayList<Piece> whitePieces = new ArrayList<Piece>();
	public ArrayList<Piece> blackPieces = new ArrayList<Piece>();
	public static int stepCnt=0;
	
	private static Piece[] board = new Piece[24];
	private static final int[][] possibleMills = {
			{0, 1, 2}, {3, 4, 5}, {6, 7, 8}, {9, 10, 11}, {12, 13, 14}, {15, 16, 17}, {18, 19, 20}, {21, 22, 23},
			{0, 9, 21}, {3, 10, 18}, {6, 11, 15}, {1, 4, 7}, {16, 19, 22}, {8, 12, 17}, {5, 13, 20}, {2, 14, 23}
		};
	
	public Board() {
		stepCnt = 0;
		// Initialize black pieces.
		int blackIndex = 0;
		for (double centerX = 821.0; centerX < 821.0+14.0*9; centerX += 14.0) {
			this.blackPieces.add(new Piece(Color.BLACK, centerX, 118, Color.WHITESMOKE, blackIndex, this));
			blackIndex++;
		}
		
		// Initialize white pieces. Set drag and drop feature for white pieces.
		int whiteIndex = 0;
		for (double centerX = 218.0; centerX < 218.0+14.0*9; centerX += 14.0) {
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
		if(Game.getCurrentPhase() != GamePhase.Opening) stepCnt++;
		if (isMill(Color.WHITE, newPosition)) {
			// TODO Implement taking a black piece from board in UI
			stepCnt = 0;
			System.out.println("White formed a mill");
		}
		// Black's turn to make a move
		moveBlackPiece();
		// Update game phase
		Game.updateGamePhase();
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
	
	private void moveBlackPiece() {
		int movedToPosition;
		switch (Game.getCurrentPhase()) {
		case Opening:
			movedToPosition = putBlackPieceOnBoard();
			break;
		default:
			movedToPosition = slideBlackPieceOnBoard();
			stepCnt++;
		}
		if (isMill(Color.BLACK, movedToPosition)) {
			// TODO Implement taking a white piece from board
			System.out.println("Black formed a mill");
		}
	}
	
	private int blackI = 8;
	private int putBlackPieceOnBoard() {
		// Temporary test code
		int newPos = 0;
		for (int i = 0; i < 24; i++)
			if (board[i] == null) {
				this.blackPieces.get(this.blackI).setCenterX(BoardController.boardPosition.get(i).getCenterX());
				this.blackPieces.get(this.blackI).setCenterY(BoardController.boardPosition.get(i).getCenterY());
				board[i] = this.blackPieces.get(this.blackI);
				this.blackI--;
				newPos = i;
				break;
			}
		return newPos;
	}

	private int slideBlackPieceOnBoard() {
		System.out.println("Slide black piece");
		return 0;
	}

	private boolean isMill(Color player, int currentPosition) {
		for (int[] possibleMill : possibleMills) {
			// If one of the positions in evaluated possible mill is empty then skip
			if (board[possibleMill[0]] == null || board[possibleMill[1]] == null || board[possibleMill[2]] == null)
				continue;
			// If current position is not in the evaluated possible mill then skip
			if (!(currentPosition == possibleMill[0] || currentPosition == possibleMill[1] || currentPosition == possibleMill[2]))
				continue;
			// If the evaluated possible mill does not contain pieces of the same color then skip
			if (!(board[possibleMill[0]].getColor() == player && board[possibleMill[1]].getColor() == player && board[possibleMill[2]].getColor() == player))
				continue;
			return true; // Mill was found
		}
		return false; // No mill found
	}

	public static boolean isOccupied(int position) {
		if (board[position] == null)
			return false;
		return true;
	}
}