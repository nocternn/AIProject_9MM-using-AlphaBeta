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
	private int Depth = 5; //constant depth (during development only)
	private int nextMaxMove = -2; //Index of play position that will be the next move for the AI. FINAL RESULT
	
	public int MiniMax (int depth, boolean blackIsPlaying, int currentPosition) { //currentPosition: Current position of playing that we choose
		ArrayList<Move> possibleMoves = new ArrayList<Move>();
		int eval = 0;
		if (depth == 0) {
			return board.getNumberWhiteOnBoard() - board.getNumberBlackOnBoard();
			//After <depth> moves, check number of white vs black 
		}
		
		if (blackIsPlaying == false) {
			//If it is human turn, we find min value of possible moves 
			int minEval = Integer.MAX_VALUE;
			for (int i = 0; i < 9; i++) {
				if (whitePieces[i].getActive())
					possibleMoves = getPossibleMoves(whitePieces[i], i);
			}
			for (Move move: possibleMoves) {
				//Move the piece to a possibleMove in the algorithm but not update the display
				moveGhostPiece(Color.WHITE, move.index, move.from, move.to);
				if (board.isMill(Color.WHITE, move.to)) {
					//TODO Delete an opponent piece on board and update number of black pieces
				}
				minEval = Math.min(minEval, MiniMax(depth-1, !blackIsPlaying, move.to));
				//Move it back
				moveGhostPiece(Color.WHITE, move.index, move.to, move.from);
			}
			return minEval;
		} else {
			//If it is AI turn, we find max value of possible moves
			int maxEval = Integer.MIN_VALUE;
			for (int i = 0; i < 9; i++) {
				if (blackPieces[i].getActive())
					possibleMoves = getPossibleMoves(whitePieces[i], i);
			}
			for (Move move: possibleMoves) {
				//Move the piece to a possibleMove in the algorithm but not update the display
				moveGhostPiece(Color.BLACK, move.index, move.from, move.to);
				if (board.isMill(Color.BLACK, move.to)) {
					//TODO Delete an opponent piece on board and update number of white pieces
				}
				eval = MiniMax(depth-1, !blackIsPlaying, move.to);
				if (eval > maxEval) {
					maxEval = eval;
					if (depth == Depth)
						nextMaxMove = move.to; //Record the final result: Next move for AI
				}
				//Move it back
				moveGhostPiece(Color.BLACK, move.index, move.to, move.from);
			}
			return maxEval;
		}
	}
	
	public ArrayList<Move> getPossibleMoves(Piece piece, int index) {
		//Get all of the possible next moves
		ArrayList<Move> possibleMoves = new ArrayList<Move>();
		switch (currentGamePhase) {
			case Middle:
				//Can only move to adjacent points of a piece
				for (int pos: Board.possibleSlides[index]) {
					if (!Board.isOccupied(Board.possibleSlides[index][pos])) {
						possibleMoves.add(new Move(piece.getPosition(), Board.possibleSlides[index][pos], index));
					}
				}
				break;
				
			default:
				//In opening and end phase, player can move a piece to anywhere that is not occupied
				for (int i = 0; i < 24; i++) {
					if (!Board.isOccupied(i))
						possibleMoves.add(new Move(piece.getPosition(), i, index));
				}
				break;
		}
		return possibleMoves;
	}
	
	public void moveGhostPiece(Color color, int index, int from, int to) {
		//Move the piece but in Algorithm only
		Board.board[to] = Board.board[from];
		Board.board[from] = null;
	}
	
	public static void updateGamePhase(GamePhase gamePhase) {
		currentGamePhase = gamePhase;
	}
	
	public int getMaxMove() {
		return nextMaxMove;
	}
}