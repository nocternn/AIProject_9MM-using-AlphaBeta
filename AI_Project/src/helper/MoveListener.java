package helper;

public interface MoveListener {
	void movedWhitePiece(int pieceIndex, int initialPosition, int newPosition);
	void removedBlackPiece(int piecePosition);
}
