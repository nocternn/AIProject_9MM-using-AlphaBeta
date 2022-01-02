package board;

import java.util.ArrayList;

import game.Game;
import game.Game.GamePhase;
import game.Move;
import helper.MoveListener;
import javafx.scene.paint.Color;
import main.Main;

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
	public static int stepCnt=0;
	
	private static Piece[] whitePieces = new Piece[9];
	private static Piece[] blackPieces = new Piece[9];
	private static Piece[] board = new Piece[24];
	
	public Board() {
		initBoard();
	}
	
	public Piece[] getBoard() {
		return board;
	}

	public Piece[] getWhitePieces() {
		return whitePieces;
	}

	public Piece[] getBlackPieces() {
		return blackPieces;
	}

	public int getNumberOfPiecesOnBoard(Piece[] board, Color player) {
		int count = 0;
		for (Piece piece : board) {
			if (piece != null && piece.getColor() == player)
				count++;
		}
		return count;
	}
	
	public ArrayList<Move> getPossibleMoves(Piece[] board, Piece[] pieces) {
		// Get all of the possible next moves
		ArrayList<Move> possibleMoves = new ArrayList<Move>();
		GamePhase currentPhase = Game.getCurrentPhase();
		
		for (Piece piece : pieces) {
			if (!piece.active)
				continue;
			switch (currentPhase) {
				case Opening:
					// In opening phase, player can move a piece from outside play-board to anywhere that is not occupied
					if (piece.initialPosition < 0) {
						for (int i = 0; i < 24; i++) {
							if (!isOccupied(board, i)) {							
								possibleMoves.add(new Move(piece, i));
							}
						}
					}
					break;
				case Middle:
					// In middle phases, player can only move to adjacent points of a piece
					if (piece.initialPosition >= 0)
						for (int pos: possibleSlides[piece.initialPosition]) {
							if (!isOccupied(board, pos)) {
								possibleMoves.add(new Move(piece, pos));
							}
						}
					break;
				case Ending:
					// In end phase, player can move a piece to anywhere that is not occupied
					for (int i = 0; i < 24; i++) {
						if (!isOccupied(board, i))
							possibleMoves.add(new Move(piece, i));
					}
					break;
			}
		}
		return possibleMoves;
	}
	
	public boolean isMill(Piece[] board, Color player, int currentPosition) {
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
	
	public static boolean isAdjacent(int initialPosition, int newPosition) {
		for (int i: possibleSlides[initialPosition]) {
			if (newPosition == i) return true;
		}
		return false;
	}

	public static boolean isOccupied(int position) {
		if (board[position] == null)
			return false;
		return true;
	}
	public boolean isOccupied(Piece[] board, int position) {
		if (board[position] == null)
			return false;
		return true;
	}

	public int countMill(Piece[] gameboard, Color player) {
		int count = 0;
		for (int[] mill : possibleMills) {
			if (isMill(gameboard, player, mill[0])) {
				count++;
			}
		}
		return count;
	}

	public void movePiece(Piece[] gameboard, Piece[] pieces, Move move) {
		// Remove piece from old position
		if (move.piece.initialPosition >= 0)
			gameboard[move.piece.initialPosition] = null;
		// Set piece in new position
		pieces[move.piece.getIndex()].initialPosition = move.newPositionOnBoard;
		gameboard[move.newPositionOnBoard] = pieces[move.piece.getIndex()];
	}
	
	public int[] findAlmostMill(Piece[] board, Color player) {
		for (int[] mill : possibleMills) {
			int count = 0;
			for (int position : mill) {
				if (board[position] != null && board[position].getColor() == player)
					count++;
			}
			if (count == 2)
				return mill;
		}
		return null;
	}

	public boolean checkAllPiecesOnBoard(Piece[] pieces) {
		for (Piece piece : pieces)
			if (piece.active && piece.initialPosition == -1)
				return false;
		return true;
	}
	
	@Override
	public void movedWhitePiece(int pieceIndex, int initialPosition, int newPosition) {
		// Delete piece at old position
		if (initialPosition >= 0) {
			board[initialPosition] = null;
		}
		// Add piece at new position
		whitePieces[pieceIndex].initialPosition = newPosition;
		board[newPosition] = whitePieces[pieceIndex];
		// Count turn
		if(Game.getCurrentPhase() != GamePhase.Opening)
			stepCnt++;
		// Check for mill
		BoardController.maskWhitePieces.toFront();
		BoardController.maskBoard.toFront();
		if (isMill(board, Color.WHITE, newPosition)) {
			BoardController.millStatus.setVisible(true);
			BoardController.bringPiecesToFront(board, Color.BLACK);
			BoardController.markBlackPiece(board);
			stepCnt = 0;
			return;
		}
		blackTurn();
	}
	
	@Override
	public void removedBlackPiece(int positionOnBoard) {
		// Remove piece from board memory
		board[positionOnBoard] = null;
		// Unmark pieces
		BoardController.unmarkBlackPiece(board);
		BoardController.millStatus.setVisible(false);
		BoardController.maskBoard.toBack();
		blackTurn();
	}
	
	private void blackTurn() {
		// If white won, stop the game
		if (Game.isGameOver(board))
			return;
		// Else it's black's turn to move
		BoardController.setTurnVisibility(false, true);
		new Thread(() -> {
			moveBlackPiece(board, whitePieces, blackPieces);
			Game.updateGamePhase(board, whitePieces, blackPieces);
		}).start();
	}
	
	private void initBoard() {
		// Initialize black pieces.
		int blackIndex = 0;
		for (double centerX = 821.0; centerX < 821.0+14.0*9; centerX += 14.0) {
			blackPieces[blackIndex] = new Piece(Color.BLACK, centerX, 118, Color.WHITESMOKE, blackIndex, this);
			blackIndex++;
		}
		// Initialize white pieces.
		int whiteIndex = 0;
		for (double centerX = 218.0; centerX < 218.0+14.0*9; centerX += 14.0) {
			whitePieces[whiteIndex] = new Piece(Color.WHITE, centerX, 118, Color.BLACK, whiteIndex, this);
			whiteIndex++;
		}
		//Set all positions on board to null, set visibility of all unrelated components to false
		for (int i=0; i<24; i++) {
			board[i] = null;
			BoardController.crossPosition.get(i).setVisible(false);
		}
		BoardController.millStatus.setVisible(false);
		BoardController.whiteWin.setVisible(false);
		BoardController.blackWin.setVisible(false);
		BoardController.draw.setVisible(false);
	}
	
	private void moveBlackPiece(Piece[] board, Piece[] white, Piece[] black) {
		// Get next move of AI
		Move nextMove = Main.getAlgorithms().getMove(board, white, black);
		// Get move data
		int index = nextMove.piece.getIndex();
		int oldPosition = nextMove.piece.initialPosition;
		int newPosition = nextMove.newPositionOnBoard;
		// Execute move
		black[index].setCenterX(BoardController.boardPosition.get(newPosition).getCenterX());
		black[index].setCenterY(BoardController.boardPosition.get(newPosition).getCenterY());
		black[index].initialPosition = newPosition;
		board[newPosition] = blackPieces[index];
		if (oldPosition >= 0)
			board[oldPosition] = null;
		// Check for new mill
		if (isMill(board, Color.BLACK, newPosition)) {
			if (nextMove.opponentPiece != null) {
				// Remove same piece as decided in minimax
				board[nextMove.opponentPiece.initialPosition] = null;
				white[nextMove.opponentPiece.getIndex()].deletePiece();
			} else {
				// Delete an opponent piece on board
				int[] almostMill = findAlmostMill(board, Color.WHITE);
				if (almostMill != null) {
					// If there exists some almost mill then delete one of its pieces
					for (int position : almostMill) {
						if (board[position] != null && !isMill(board, Color.WHITE, position)) {
							deletePiece(white, board[position].getIndex());
							board[position] = null;
							break;
						}
					}
				} else {
					// If there is no almost mill then delete the first piece found on board
					for (int position = 0; position < 24; position++) {
						if (board[position] != null && !isMill(board, Color.WHITE, position) && board[position].getColor() == Color.WHITE) {
							deletePiece(white, board[position].getIndex());
							board[position] = null;
							break;
						}
					}
				}
			}
		}
		BoardController.setTurnVisibility(true, false);
	}
	
	private void deletePiece(Piece[] pieces, int index) {
		pieces[index].marked = true;
		pieces[index].deletePiece();
		pieces[index].initialPosition = -1;
		pieces[index].marked = false;
	}
}