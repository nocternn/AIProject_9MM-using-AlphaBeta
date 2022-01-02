package game;

import board.Board;
import board.BoardController;
import board.Piece;
import javafx.scene.paint.Color;
import main.Main;

public class Algorithms {
	public static final int MAX_PIECES_ON_BOARD = 9;
	public static final int MIN_PIECES_ON_BOARD = 3;
	
	private static Board board = Main.getBoard();
	private int selectedDepth;
	private Move nextMove;
	
	public Move getMove(Piece[] originalBoard, Piece[] originalWhite, Piece[] originalBlack) {
		nextMove = null;
		selectedDepth = BoardController.getAlgorithmDepth();
		// Declare copy arrays
		Piece[] gameboard = new Piece[24];
		Piece[] white = new Piece[9];
		Piece[] black = new Piece[9];
		// Copy values
		for (int i = 0; i < 24; i++)
			if (originalBoard[i] != null)
				gameboard[i] = originalBoard[i].clone();
		for (int i = 0; i < 9; i++) {
			white[i] = originalWhite[i].clone();
			black[i] = originalBlack[i].clone();
		}
		AlphaBeta(gameboard, white, black, selectedDepth, Integer.MIN_VALUE, Integer.MAX_VALUE, Color.BLACK);
		return nextMove;
	}
	
	private int evaluateBoard(Piece[] gameboard, Color player) {
		int numMills = board.countMill(gameboard, player);
		if (numMills > 0) {
			if (player == Color.WHITE)
				return numMills;
			else
				return -numMills;
		} else {
			int numWhitePiece = board.getNumberOfPiecesOnBoard(gameboard, Color.WHITE);
			int numBlackPiece = board.getNumberOfPiecesOnBoard(gameboard, Color.BLACK);
			if (player == Color.WHITE)
				return numWhitePiece - numBlackPiece;
			else
				return numBlackPiece - numWhitePiece;
		}
	}
	
	private int AlphaBeta(Piece[] gameboard, Piece[] white, Piece[] black, int depth, int alpha, int beta, Color player) {
		if (depth == 0) {
			return evaluateBoard(gameboard, player);
		}
		
		if (player == Color.WHITE) {
			// HUMAN TURN: find MIN value of possible moves
			Color opponent = Color.BLACK;
			int maxEval = Integer.MIN_VALUE;
			for (Move move: board.getPossibleMoves(gameboard, white)) {
				// Move piece
				// Remove piece from old position
				if (move.piece.initialPosition >= 0)
					gameboard[move.piece.initialPosition] = null;
				// Set piece in new position
				white[move.piece.getIndex()].initialPosition = move.newPositionOnBoard;
				gameboard[move.newPositionOnBoard] = white[move.piece.getIndex()];
				if (board.isMill(gameboard, player, move.newPositionOnBoard)) {
					// Delete an opponent piece on board
					int[] almostMill = board.findAlmostMill(gameboard, opponent);
					if (almostMill != null) {
						// If there exists some almost mill then delete one of its pieces
						for (int position : almostMill) {
							if (gameboard[position] != null) {
								move.opponentPiece = gameboard[position];
								move.opponentPiece.initialPosition = position;
								black[move.opponentPiece.getIndex()].active = false;
								gameboard[position] = null;
								break;
							}
						}
					} else {
						// If there is no almost mill then delete the first piece found on board
						for (int position = 0; position < 24; position++) {
							if (gameboard[position] != null && !board.isMill(gameboard, opponent, position) && gameboard[position].getColor() == opponent) {
								move.opponentPiece = gameboard[position];
								move.opponentPiece.initialPosition = position;
								black[move.opponentPiece.getIndex()].active = false;
								gameboard[position] = null;
								break;
							}
						}
					}
				}
				// Opponent's turn
				int eval = AlphaBeta(gameboard.clone(), white.clone(), black.clone(), depth-1, alpha, beta, opponent);
				// Undo move
				// Remove piece from old position
				gameboard[move.newPositionOnBoard] = null;
				// Set piece in new position
				white[move.piece.getIndex()].initialPosition = move.piece.initialPosition;
				if (move.piece.initialPosition >= 0)
					gameboard[move.piece.initialPosition] = white[move.piece.getIndex()];
				// Undo delete
				if (move.opponentPiece != null) {
					black[move.opponentPiece.getIndex()].active = true;
					gameboard[move.opponentPiece.initialPosition] = move.opponentPiece;
				}
				// Update values
				maxEval = Math.max(maxEval, eval);
				alpha = Math.max(alpha, eval);
				if (beta <= alpha)
					break;
			}
			return maxEval;
		} else {
			// AI TURN: find MAX value of possible moves
			Color opponent = Color.WHITE;
			int minEval = Integer.MAX_VALUE;
			for (Move move: board.getPossibleMoves(gameboard, black)) {
				// Move piece
				// Remove piece from old position
				if (move.piece.initialPosition >= 0)
					gameboard[move.piece.initialPosition] = null;
				// Set piece in new position
				white[move.piece.getIndex()].initialPosition = move.newPositionOnBoard;
				gameboard[move.newPositionOnBoard] = white[move.piece.getIndex()];
				// Back-up data
				if (board.isMill(gameboard, player, move.newPositionOnBoard)) {
					// Delete an opponent piece on board
					int[] almostMill = board.findAlmostMill(gameboard, opponent);
					if (almostMill != null) {
						// If there exists some almost mill then delete one of its pieces
						for (int position : almostMill) {
							if (gameboard[position] != null) {
								move.opponentPiece = gameboard[position];
								black[move.opponentPiece.getIndex()].active = false;
								gameboard[position] = null;
								break;
							}
						}
					} else {
						// If there is no almost mill then delete the first piece found on board
						for (int position = 0; position < 24; position++) {
							if (gameboard[position] != null && !board.isMill(gameboard, opponent, position) && gameboard[position].getColor() == opponent) {
								move.opponentPiece = gameboard[position];
								black[move.opponentPiece.getIndex()].active = false;
								gameboard[position] = null;
								break;
							}
						}
					}
				}
				// Opponent's turn
				int eval = AlphaBeta(gameboard.clone(), white.clone(), black.clone(), depth-1, alpha, beta, opponent);
				if (depth == selectedDepth && eval < minEval) {
					this.nextMove = move;
				}
				// Undo move
				// Remove piece from old position
				gameboard[move.newPositionOnBoard] = null;
				// Set piece in new position
				white[move.piece.getIndex()].initialPosition = move.piece.initialPosition;
				if (move.piece.initialPosition >= 0)
					gameboard[move.piece.initialPosition] = white[move.piece.getIndex()];
				// Undo delete
				if (move.opponentPiece != null) {
					black[move.opponentPiece.getIndex()].active = true;
					gameboard[move.opponentPiece.initialPosition] = move.opponentPiece;
				}
				// Update values
				minEval = Math.min(minEval, eval);
				beta = Math.min(beta, eval);
				if (beta <= alpha)
					break;
			}
			return minEval;
		}
	}
	
	private int MiniMax(Piece[] gameboard, Piece[] white, Piece[] black, int depth, Color player) {
		if (depth == 0) {
			return evaluateBoard(gameboard, player);
		}
		
		if (player == Color.WHITE) {
			// HUMAN TURN: find MIN value of possible moves
			Color opponent = Color.BLACK;
			int maxEval = Integer.MIN_VALUE;
			for (Move move: board.getPossibleMoves(gameboard, white)) {
				// Move piece
				// Remove piece from old position
				if (move.piece.initialPosition >= 0)
					gameboard[move.piece.initialPosition] = null;
				// Set piece in new position
				white[move.piece.getIndex()].initialPosition = move.newPositionOnBoard;
				gameboard[move.newPositionOnBoard] = white[move.piece.getIndex()];
				if (board.isMill(gameboard, player, move.newPositionOnBoard)) {
					// Delete an opponent piece on board
					int[] almostMill = board.findAlmostMill(gameboard, opponent);
					if (almostMill != null) {
						// If there exists some almost mill then delete one of its pieces
						for (int position : almostMill) {
							if (gameboard[position] != null) {
								move.opponentPiece = gameboard[position];
								move.opponentPiece.initialPosition = position;
								black[move.opponentPiece.getIndex()].active = false;
								gameboard[position] = null;
								break;
							}
						}
					} else {
						// If there is no almost mill then delete the first piece found on board
						for (int position = 0; position < 24; position++) {
							if (gameboard[position] != null && !board.isMill(gameboard, opponent, position) && gameboard[position].getColor() == opponent) {
								move.opponentPiece = gameboard[position];
								move.opponentPiece.initialPosition = position;
								black[move.opponentPiece.getIndex()].active = false;
								gameboard[position] = null;
								break;
							}
						}
					}
				}
				// Opponent's turn
				maxEval = Math.max(maxEval, MiniMax(gameboard.clone(), white.clone(), black.clone(), depth-1, opponent));
				// Undo move
				// Remove piece from old position
				gameboard[move.newPositionOnBoard] = null;
				// Set piece in new position
				white[move.piece.getIndex()].initialPosition = move.piece.initialPosition;
				if (move.piece.initialPosition >= 0)
					gameboard[move.piece.initialPosition] = white[move.piece.getIndex()];
				// Undo delete
				if (move.opponentPiece != null) {
					black[move.opponentPiece.getIndex()].active = true;
					gameboard[move.opponentPiece.initialPosition] = move.opponentPiece;
				}
			}
			return maxEval;
		} else {
			// AI TURN: find MAX value of possible moves
			Color opponent = Color.WHITE;
			int minEval = Integer.MAX_VALUE;
			for (Move move: board.getPossibleMoves(gameboard, black)) {
				// Move piece
				// Remove piece from old position
				if (move.piece.initialPosition >= 0)
					gameboard[move.piece.initialPosition] = null;
				// Set piece in new position
				white[move.piece.getIndex()].initialPosition = move.newPositionOnBoard;
				gameboard[move.newPositionOnBoard] = white[move.piece.getIndex()];
				// Back-up data
				if (board.isMill(gameboard, player, move.newPositionOnBoard)) {
					// Delete an opponent piece on board
					int[] almostMill = board.findAlmostMill(gameboard, opponent);
					if (almostMill != null) {
						// If there exists some almost mill then delete one of its pieces
						for (int position : almostMill) {
							if (gameboard[position] != null) {
								move.opponentPiece = gameboard[position];
								black[move.opponentPiece.getIndex()].active = false;
								gameboard[position] = null;
								break;
							}
						}
					} else {
						// If there is no almost mill then delete the first piece found on board
						for (int position = 0; position < 24; position++) {
							if (gameboard[position] != null && !board.isMill(gameboard, opponent, position) && gameboard[position].getColor() == opponent) {
								move.opponentPiece = gameboard[position];
								black[move.opponentPiece.getIndex()].active = false;
								gameboard[position] = null;
								break;
							}
						}
					}
				}
				// Opponent's turn
				int eval = MiniMax(gameboard.clone(), white.clone(), black.clone(), depth-1, opponent);
				if (depth == selectedDepth && eval < minEval) {
					this.nextMove = move;
				}
				minEval = Math.min(minEval, eval);
				// Undo move
				// Remove piece from old position
				gameboard[move.newPositionOnBoard] = null;
				// Set piece in new position
				white[move.piece.getIndex()].initialPosition = move.piece.initialPosition;
				if (move.piece.initialPosition >= 0)
					gameboard[move.piece.initialPosition] = white[move.piece.getIndex()];
				// Undo delete
				if (move.opponentPiece != null) {
					black[move.opponentPiece.getIndex()].active = true;
					gameboard[move.opponentPiece.initialPosition] = move.opponentPiece;
				}
			}
			return minEval;
		}
	}
}