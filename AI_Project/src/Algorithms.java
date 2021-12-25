public class Algorithms {
	Board board = Main.board;
	private int numberOfWhitesOnBoard = board.whitePieces.size();
	private int numberOfBlacksOnBoard = board.blackPieces.size();
	boolean blackIsPlaying = true;	
	
	public int Evaluate() {
		return numberOfWhitesOnBoard - numberOfBlacksOnBoard;
	}
	
	public int MiniMax(int depth) {
		if (depth == 0) {
			return Evaluate();
		}
		
		if (blackIsPlaying == true) {
			int minEval = Integer.MAX_VALUE;
			return minEval;
		} else {
			int maxEval = Integer.MIN_VALUE;
			return maxEval;
		}
	}
	
}