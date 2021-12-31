package game;

import java.util.ArrayList;

import board.Board;
import board.Piece;
import game.Game.GamePhase;
import javafx.scene.paint.Color;
import main.Main;

public class Algorithms {
	public static final int MAX_PIECES_ON_BOARD = 9;
	public static final int MIN_PIECES_ON_BOARD = 3;
	
	private static Board board = Main.getBoard();
	private Piece[] gameBoard = board.getBoard().clone();
	private Piece[] whitePieces = board.getWhitePieces().clone();
	private Piece[] blackPieces = board.getBlackPieces().clone();

	private static GamePhase currentGamePhase = Game.getCurrentPhase();
	private static int Depth = 3; //EXPERIMENT: Algorithm only search to this constant depth
	private static int nextMaxMoveIndex = -2; // Index of the piece that AI will move. FINAL RESULT
	private static int nextMaxMoveFrom = -2; // Index of play position that the AI will move piece from. FINAL RESULT
	private static int nextMaxMoveTo = -2; // Index of play position that the AI will move piece to. FINAL RESULT
	
	private int MiniMax (int depth, boolean blackIsPlaying) { // currentPosition: Current position of playing that we choose
		ArrayList<Move> possibleMoves = new ArrayList<Move>();
		int eval = 0;
		if (depth == 0) {
			return getNumberBlackOnBoard() - getNumberWhiteOnBoard();
			// After <depth> moves, check number of black vs white
		}
		
		if (blackIsPlaying == false) {
			// HUMAN TURN: find MIN value of possible moves
			int minEval = Integer.MAX_VALUE;
			for (int i = 0; i < 9; i++) {
				if (whitePieces[i].getActive())
					possibleMoves.addAll(GetPossibleMoves(whitePieces[i], i));
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
				if (isMill(Color.WHITE, move.to)) {
					// Delete an opponent piece on board
					for (int[] possibleMill : Board.possibleMills) {
						if ((CheckAlmostMill(possibleMill, Color.BLACK)) && (opponentPieceDeleted == false)) {
							for (int pieceIndex: possibleMill) {
								if (gameBoard[pieceIndex] != null) {
									// Set active status of the deleted piece = deactivated
									blackPieces[gameBoard[pieceIndex].getIndex()].setActive(false);
									// Backup data of the deleted piece
									deletedPieceOnBoard = gameBoard[pieceIndex];
									// Set that play position as nothing was there
									gameBoard[pieceIndex] = null;
									// Mark as deleted
									opponentPieceDeleted = true;
									break;
								}
							}
						}
					}
					//If none AlmostMill
				}
				// Check current gamePhase if it's needed to be updated, then update
				boolean gamePhaseUpdated = false;
				if (currentGamePhase != UpdateGamePhase()) {
					currentGamePhase = UpdateGamePhase();
					gamePhaseUpdated = true;
				}
				// Go to next step of another player (in algorithm only, not real player's turn)
				minEval = Math.min(minEval, MiniMax(depth-1, !blackIsPlaying));
				// Move it back
				MoveGhostPiece(Color.WHITE, move.index, move.to, move.from);
				// Roll back currentGamePhase if it has been updated
				if (gamePhaseUpdated)
					if (currentGamePhase == GamePhase.Middle)
						currentGamePhase = GamePhase.Opening;
					else if (currentGamePhase == GamePhase.Ending)
						currentGamePhase = GamePhase.Middle;
				// If a piece was deleted, restore it
				if (opponentPieceDeleted == true) {
					// Restore active status
					blackPieces[deletedPieceOnBoard.getIndex()].setActive(true);
					// Restore data on to board
					gameBoard[deletedPosition] = deletedPieceOnBoard;
				}
			}
			return minEval;
		} else {
			// AI TURN: find MAX value of possible moves
			int maxEval = Integer.MIN_VALUE;
			for (int i = 0; i < 9; i++) {
				if (blackPieces[i].getActive())
					possibleMoves.addAll(GetPossibleMoves(blackPieces[i], i));
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
				if (isMill(Color.BLACK, move.to)) {
					// Delete an opponent piece on board
					for (int[] possibleMill : Board.possibleMills) {
						if ((CheckAlmostMill(possibleMill, Color.WHITE)) && (opponentPieceDeleted == false)) {
							for (int pieceIndex: possibleMill) {
								if (gameBoard[pieceIndex] != null) {
									// Set active status of the deleted piece = deactivated
									whitePieces[gameBoard[pieceIndex].getIndex()].setActive(false);
									// Backup data of the deleted piece
									deletedPieceOnBoard = gameBoard[pieceIndex];
									// Set that play position as nothing was there
									gameBoard[pieceIndex] = null;
									// Mark as deleted
									opponentPieceDeleted = true;
									break;
								}
							}
						}
					}
				}
				// Check current gamePhase if it's needed to be updated, then update
				boolean gamePhaseUpdated = false;
				if (currentGamePhase != UpdateGamePhase()) {
					currentGamePhase = UpdateGamePhase();
					gamePhaseUpdated = true;
				}
				eval = MiniMax(depth-1, !blackIsPlaying);
				if (eval > maxEval) {
					maxEval = eval;
					if (depth == Depth) { // EXPERIMENT: Constant depth for checking only
						// Record the final result: Next move for AI
						nextMaxMoveIndex = move.index;
						nextMaxMoveFrom = move.from;
						nextMaxMoveTo = move.to;
						System.out.println("Get: " + nextMaxMoveIndex + ": " + nextMaxMoveFrom + " -> " + nextMaxMoveTo);
					}
				}
				// Move it back
				MoveGhostPiece(Color.BLACK, move.index, move.to, move.from);
				// Roll back currentGamePhase if it has been updated
				if (gamePhaseUpdated)
					if (currentGamePhase == GamePhase.Middle)
						currentGamePhase = GamePhase.Opening;
					else if (currentGamePhase == GamePhase.Ending)
						currentGamePhase = GamePhase.Middle;
				// If a piece was deleted, restore it
				if (opponentPieceDeleted == true) {
					// Restore active status
					whitePieces[deletedPieceOnBoard.getIndex()].setActive(true);
					// Restore data on to board
					gameBoard[deletedPosition] = deletedPieceOnBoard;
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
						if (!isOccupied(i)) {							
							possibleMoves.add(new Move(piece.getPosition(), i, index));
						}
					}
				}
				break;
				
			case Middle:
                // In middle phases, player can only move to adjacent points of a piece
				System.out.println(piece.getIndex() + " " + piece.initialPosition);
                for (int pos: Board.possibleSlides[piece.initialPosition]) {
                    if (!isOccupied(pos)) {
                        possibleMoves.add(new Move(piece.initialPosition, pos, index));
                    }
                }
                break;	
				
			case Ending:
				// In end phase, player can move a piece to anywhere that is not occupied
				for (int i = 0; i < 24; i++) {
					if (!isOccupied(i))
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
				gameBoard[to] = whitePieces[index];
			if (from >= 0)
				gameBoard[from] = null;
			whitePieces[index].setPosition(to);
		} else {
			if (to >= 0)
				gameBoard[to] = blackPieces[index];
			if (from >= 0)
				gameBoard[from] = null;
			blackPieces[index].setPosition(to);
		}
	}
	
	public boolean CheckAlmostMill(int[] possibleMill, Color color) {
		// Check if the 3 play pos can have an almost Mill
		ArrayList<Piece> possibleNotNullMillPositions = new ArrayList<Piece>(); // List of not null Mill positions 
		for (int i = 0; i <= 2; i++) {
			if (gameBoard[possibleMill[i]] != null)
				// If the position on board is not null, add to ArrayList
				possibleNotNullMillPositions.add(gameBoard[possibleMill[i]]);
		}
		if (possibleNotNullMillPositions.size() == 2 && possibleNotNullMillPositions.get(0).getColor() == possibleNotNullMillPositions.get(1).getColor() && 
				possibleNotNullMillPositions.get(0).getColor() == color)
			// If there are 2 piece on this AlmostMillPosition and they are the same color, it is an AlmostMill situation
			return true;
		return false;
	}
	
	public GamePhase UpdateGamePhase() {
		// Change game phase between phases, forward
		int numberWhite = getNumberWhiteOnBoard();
		int numberBlack = getNumberBlackOnBoard();
		
		GamePhase tempGamePhase = currentGamePhase;
		
		switch (currentGamePhase) {
		case Opening:
			if (numberWhite == MAX_PIECES_ON_BOARD && numberBlack == MAX_PIECES_ON_BOARD)
				tempGamePhase = GamePhase.Middle;
			break;
		case Middle:
			if (numberWhite == MIN_PIECES_ON_BOARD && numberBlack == MIN_PIECES_ON_BOARD)
				tempGamePhase = GamePhase.Ending;
			break;
		case Ending:
			if (numberWhite < MIN_PIECES_ON_BOARD || numberBlack < MIN_PIECES_ON_BOARD)
				Game.gameOver();
			break;
		}
		
		return tempGamePhase;
	}
	
	public boolean isMill(Color player, int currentPosition) {
		for (int[] possibleMill : Board.possibleMills) {
			// If one of the positions in evaluated possible mill is empty then skip
			if (gameBoard[possibleMill[0]] == null || gameBoard[possibleMill[1]] == null || gameBoard[possibleMill[2]] == null)
				continue;
			// If current position is not in the evaluated possible mill then skip
			if (!(currentPosition == possibleMill[0] || currentPosition == possibleMill[1] || currentPosition == possibleMill[2]))
				continue;
			// If the evaluated possible mill does not contain pieces of the same color then skip
			if (!(gameBoard[possibleMill[0]].getColor() == player && gameBoard[possibleMill[1]].getColor() == player && gameBoard[possibleMill[2]].getColor() == player))
				continue;
			return true; // Mill was found
		}
		return false; // No mill found
	}

	public boolean isOccupied(int position) {
		if (gameBoard[position] == null)
			return false;
		return true;
	}
	
	public int getNumberWhiteOnBoard() {
		int count = 0;
		for (Piece piece : gameBoard) {
			if (piece != null && piece.getColor() == Color.WHITE)
				count++;
		}
		return count;
	}

	public int getNumberBlackOnBoard() {
		int count = 0;
		for (Piece piece : gameBoard) {
			if (piece != null && piece.getColor() == Color.BLACK)
				count++;
		}
		return count;
	}
	
	public Move GetMaxMove() {
		// Get the next move for AI
		MiniMax(Depth, true); // Algorithm only search for this constant of depth
		Move finalMove = new Move(nextMaxMoveFrom, nextMaxMoveTo, nextMaxMoveIndex);
		return finalMove;
	}
}