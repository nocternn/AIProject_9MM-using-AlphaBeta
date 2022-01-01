package game;

import board.Piece;

public class Move {
	//A move of a piece from point A to point B
	public Piece piece;
	public Piece opponentPiece;
	public int newPositionOnBoard;
	
	public Move (Piece piece, int to) {
		this.piece = piece;
		this.newPositionOnBoard = to;
	}
}