package board;

import game.Algorithms;
import game.Game;
import game.Move;
import helper.MoveListener;
import javafx.scene.paint.Color;

public class Board implements MoveListener {
	public static final int[][] possibleMills = {
			{0, 1, 2}, {3, 4, 5}, {6, 7, 8}, {9, 10, 11}, {12, 13, 14}, {15, 16, 17}, {18, 19, 20}, {21, 22, 23}, 
			{0, 9, 21}, {3, 10, 18}, {6, 11, 15}, {1, 4, 7}, {16, 19, 22}, {8, 12, 17}, {5, 13, 20}, {2, 14, 23}
	};
	public static final int[][] possibleSlides = {
			{1, 9}, {0, 2, 4}, {1, 14}, {4, 10}, {1, 3, 5, 7}, {4, 13}, {7, 11}, {4, 6 ,8}, {7, 12}, {0, 10, 21},
			{3, 9, 11, 18}, {6, 10, 15}, {8, 13, 17}, {5, 12, 14, 20}, {2, 13, 23}, {11, 16}, {15, 17, 19}, {12, 16},
			{10, 19}, {16, 18, 20, 22}, {13, 19}, {9, 22}, {19, 21, 23}, {14 ,22}
	}; //Adjacent play positions of piece[i]
	
	private static Piece[] whitePieces = new Piece[9];
	private static Piece[] blackPieces = new Piece[9];
	public static Piece[] board = new Piece[27];
	
	public Board() {
		initBoard();
	}
	
	public Piece[] getBoard() {
		return board;
	}
	
	private void initBoard() {
		// Initialize black pieces.
		int blackIndex = 0;
		for (double centerX = 821.0; centerX < 821.0+14.0*9; centerX += 14.0) {
			blackPieces[blackIndex] = new Piece(Color.BLACK, centerX, 118, Color.WHITESMOKE, blackIndex, this);
			blackIndex++;
		}
		
		// Initialize white pieces. Set drag and drop feature for white pieces.
		int whiteIndex = 0;
		for (double centerX = 218.0; centerX < 218.0+14.0*9; centerX += 14.0) {
			whitePieces[whiteIndex] = new Piece(Color.WHITE, centerX, 118, Color.BLACK, whiteIndex, this);
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
		board[newPosition] = whitePieces[pieceIndex];
		if (isMill(Color.WHITE, newPosition)) {
			// Implement taking a black piece from board in UI
			System.out.println("White formed a mill");
		}
		
		switch (Game.getCurrentPhase()) {
		case Opening:
			// TODO In opening phase, player can move a piece from outside play-board to anywhere that is not occupied
			break;
			
		case Middle:
            // TODO In middle phases, player can only move to adjacent points of a piece
			break;
			
		case Ending:
			// TODO In end phase, player can move a piece to anywhere that is not occupied
			break;
			
	}
		
		Algorithms algo = new Algorithms();
		// Get next move of AI
		Move nextMove = algo.GetMaxMove();
		// Black's turn to make a move
		moveBlackPiece(nextMove);
		// Update game phase
		Game.updateGamePhase();
	}

	public Piece[] getWhitePieces() {
		return whitePieces;
	}

	public Piece[] getBlackPieces() {
		return blackPieces;
	}

	public int getNumberWhiteOnBoard() {
		int count = 0;
		for (Piece piece : board) {
			if (piece != null && piece.getColor() == Color.WHITE)
				count++;
		}
		return count;
	}

	public int getNumberBlackOnBoard() {
		int count = 0;
		for (Piece piece : board) {
			if (piece != null && piece.getColor() == Color.BLACK)
				count++;
		}
		return count;
	}
	
	private void moveBlackPiece(Move move) {
		// Move black piece
		board[move.to] = blackPieces[move.index];
		if (move.from >= 0)
			board[move.from] = null;
		blackPieces[move.index].setPosition(move.to);
		if (isMill(Color.BLACK, move.to)) {
			// Implement taking a white piece from board
			System.out.println("Black formed a mill");
		}
		// Set location for pieces on UI
		Board.blackPieces[move.index].setCenterX(BoardController.boardPosition.get(move.to).getCenterX());
		Board.blackPieces[move.index].setCenterY(BoardController.boardPosition.get(move.to).getCenterY());
	}

	public boolean isMill(Color player, int currentPosition) {
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