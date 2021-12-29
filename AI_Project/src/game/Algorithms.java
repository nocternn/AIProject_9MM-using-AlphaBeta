package game;

import java.util.ArrayList;

import board.Board;
import board.Piece;
import game.Game.GamePhase;
import javafx.scene.paint.Color;
import main.Main;

public class Algorithms {
	public Piece[] whitePieces = board.getWhitePieces();
	public Piece[] blackPieces = board.getBlackPieces();

	private static GamePhase currentGamePhase = Game.getCurrentPhase();
	private static Board board = Main.getBoard();
	private static int Depth = 5; //EXPERIMENT: Algorithm only search to this constant depth
	private static int nextMaxMoveIndex = -2; // Index of the piece that AI will move
	private static int nextMaxMoveFrom = -2; // Index of play position that the AI will move piece from. FINAL RESULT
	private static int nextMaxMoveTo = -2; // Index of play position that the AI will move piece to. FINAL RESULT
	
	public int MiniMax (int depth, boolean blackIsPlaying) { // currentPosition: Current position of playing that we choose
		ArrayList<Move> possibleMoves = new ArrayList<Move>();
		int eval = 0;
		if (depth == 0) {
			return board.getNumberWhiteOnBoard() - board.getNumberBlackOnBoard();
			// After <depth> moves, check number of white vs black 
		}
		
		if (blackIsPlaying == false) {
			// HUMAN TURN: find MIN value of possible moves 
			int minEval = Integer.MAX_VALUE;
			for (int i = 0; i < 9; i++) {
				if (whitePieces[i].getActive())
					possibleMoves = GetPossibleMoves(whitePieces[i], i);
			}
			for (Move move: possibleMoves) {
				// Mark if an opponent piece has been deleted
				boolean opponentPieceDeleted = false;
				// Mark deleted position
				int deletedPosition = 0;
				// Data of the deleted position for restoration
				Piece deletedPieceOnBoard = new Piece();
				// Move the piece to a possibleMove in the algorithm but not update the display
				MoveGhostPiece(Color.WHITE, move.index, move.from, move.to);
				if (board.isMill(Color.WHITE, move.to)) {
					// Delete an opponent piece on board
					for (int[] possibleMill : Board.possibleMills) {
						if ((CheckAlmostMill(possibleMill, Color.BLACK)) && (opponentPieceDeleted == false)) {
							for (int pieceIndex: possibleMill) {
								if (Board.board[pieceIndex] != null) {
									// Set active status of the deleted piece = deactivated
									whitePieces[Board.board[pieceIndex].getIndex()].setActive(false);
									// Backup data of the deleted piece
									deletedPieceOnBoard = Board.board[pieceIndex];
									// Set that play position as nothing was there
									Board.board[pieceIndex] = null;
									// Mark as deleted
									opponentPieceDeleted = true;
									break;
								}
							}
						}
					}
					//If none AlmostMill
				}
				// Go to next step of another player (in algorithm only, not real player's turn)
				minEval = Math.min(minEval, MiniMax(depth-1, !blackIsPlaying));
				// Move it back
				MoveGhostPiece(Color.WHITE, move.index, move.to, move.from);
				// If a piece was deleted, restore it
				if (opponentPieceDeleted == true) {
					// Restore active status
					whitePieces[deletedPieceOnBoard.getIndex()].setActive(true);
					// Restore data on to board
					Board.board[deletedPosition] = deletedPieceOnBoard;
				}
			}
			return minEval;
		} else {
			// AI TURN: find MAX value of possible moves
			int maxEval = Integer.MIN_VALUE;
			for (int i = 0; i < 9; i++) {
				if (blackPieces[i].getActive())
					possibleMoves = GetPossibleMoves(whitePieces[i], i);
			}
			for (Move move: possibleMoves) {
				// Mark if an opponent piece has been deleted
				boolean opponentPieceDeleted = false;
				// Mark deleted position
				int deletedPosition = 0;
				// Data of the deleted position for restoration
				Piece deletedPieceOnBoard = new Piece();
				// Move the piece to a possibleMove in the algorithm but not update the display
				MoveGhostPiece(Color.BLACK, move.index, move.from, move.to);
				if (board.isMill(Color.BLACK, move.to)) {
					// Delete an opponent piece on board
					for (int[] possibleMill : Board.possibleMills) {
						if ((CheckAlmostMill(possibleMill, Color.WHITE)) && (opponentPieceDeleted == false)) {
							for (int pieceIndex: possibleMill) {
								if (Board.board[pieceIndex] != null) {
									// Set active status of the deleted piece = deactivated
									blackPieces[Board.board[pieceIndex].getIndex()].setActive(false);
									// Backup data of the deleted piece
									deletedPieceOnBoard = Board.board[pieceIndex];
									// Set that play position as nothing was there
									Board.board[pieceIndex] = null;
									// Mark as deleted
									opponentPieceDeleted = true;
									break;
								}
							}
						}
					}
				}
				eval = MiniMax(depth-1, !blackIsPlaying);
				if (eval > maxEval) {
					maxEval = eval;
					if (depth == Depth) { // EXPERIMENT: Constant depth for checking only
						// Record the final result: Next move for AI
						nextMaxMoveIndex = move.index;
						nextMaxMoveFrom = move.from;
						nextMaxMoveTo = move.to;
						System.out.println("Get: " + move.index + ": " + move.from + " -> " + move.to);
						System.out.println("Get: " + nextMaxMoveIndex + ": " + nextMaxMoveFrom + " -> " + nextMaxMoveTo);
					}
				}
				// Move it back
				MoveGhostPiece(Color.BLACK, move.index, move.from, move.to);
				// If a piece was deleted, restore it
				if (opponentPieceDeleted == true) {
					// Restore active status
					whitePieces[deletedPieceOnBoard.getIndex()].setActive(true);
					// Restore data on to board
					Board.board[deletedPosition] = deletedPieceOnBoard;
				}
			}
			return maxEval;
		}
	}
	
	public ArrayList<Move> GetPossibleMoves(Piece piece, int index) {
		// Get all of the possible next moves
		ArrayList<Move> possibleMoves = new ArrayList<Move>();
		switch (currentGamePhase) {
			case Opening:
				// In opening phase, player can move a piece from outside play-board to anywhere that is not occupied
				if (piece.getPosition() < 0) {
					for (int i = 0; i < 24; i++) {
						if (!Board.isOccupied(i)) {							
							possibleMoves.add(new Move(piece.getPosition(), i, index));
							System.out.println(index + ": " + piece.getPosition() + " -> " + i);
						}
					}
					System.out.println("_________________________________");
				}
				break;
			case Middle:
				// In middle phases, player can only move to adjacent points of a piece
				for (int pos: Board.possibleSlides[index]) {
					if (!Board.isOccupied(Board.possibleSlides[index][pos])) {
						possibleMoves.add(new Move(piece.getPosition(), Board.possibleSlides[index][pos], index));
					}
				}
				break;
				
			case Ending:
				// In end phase, player can move a piece to anywhere that is not occupied
				for (int i = 0; i < 24; i++) {
					if (!Board.isOccupied(i))
						possibleMoves.add(new Move(piece.getPosition(), i, index));
				}
				break;
		}
		return possibleMoves;
	}
	
	public void MoveGhostPiece(Color color, int index, int from, int to) {
		// Move the piece but in Algorithm only
		if (color == Color.WHITE) {
			if (to >= 0)
				Board.board[to] = whitePieces[index];
			if (from >= 0)
				Board.board[from] = null;
			whitePieces[index].setPosition(to);
		} else {
			Board.board[to] = blackPieces[index];
			if (from >= 0)
				Board.board[from] = null;
			blackPieces[index].setPosition(to);
		}
	}
	
	public boolean CheckAlmostMill(int[] possibleMill, Color color) {
		// Check if the 3 play pos can have an almost Mill
		ArrayList<Piece> possibleNotNullMillPositions = new ArrayList<Piece>(); // List of not null Mill positions 
		for (int i = 0; i <= 2; i++) {
			if (Board.board[possibleMill[i]] != null)
				// If the position on board is not null, add to ArrayList
				possibleNotNullMillPositions.add(Board.board[possibleMill[i]]);
		}
		if (possibleNotNullMillPositions.size() == 2 && possibleNotNullMillPositions.get(0).getColor() == possibleNotNullMillPositions.get(1).getColor())
			// If there are 2 piece on this AlmostMillPosition and they are the same color, it is an AlmostMill situation
			return true;
		return false;
	}
	
	public static void UpdateGamePhase(GamePhase gamePhase) {
		currentGamePhase = gamePhase;
	}
	
	public Move GetMaxMove() { 
		// Get the next move for AI
		MiniMax(Depth, true); // Algorithm only search for this constant of depth
		Move finalMove = new Move(nextMaxMoveFrom, nextMaxMoveTo, nextMaxMoveIndex);
		return finalMove;
	}
}