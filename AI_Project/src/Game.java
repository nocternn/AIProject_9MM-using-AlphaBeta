public class Game {
	public enum Player {White, Black};
	public static Player currentPlayer = Player.White;

	public static void OpeningPhase() {
		if (currentPlayer == Player.Black) {
			// TODO Implement board.PutBlackPiece();
			currentPlayer = Player.White;
		}
		
		/* TODO Check if the most recent move formed a mill
		 * Should have an attribute in Board to hold the most recent piece that was moved in order to check for mill.
		 * Implement board.IsMill();
		 * Implement board.TakeWhitePiece();
		 * Implement board.TakeBlackPiece();
		 */
	}
	
	public static void MiddlePhase() {
		
	}
	
	public static void EndingPhase() {
		
	}
}
