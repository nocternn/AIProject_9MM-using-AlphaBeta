package game;
public class Move {
	//A move of a piece from point A to point B
	public int from;
	public int to;
	public int index; //Index of that piece
	
	public Move (int from, int to, int index) {
		this.from = from;
		this.to = to;
		this.index = index;
	}
}