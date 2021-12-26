package board;

import java.util.ArrayList;

import helper.MoveListener;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.StrokeType;

public class Piece extends Circle {
    private ArrayList<MoveListener> listeners = new ArrayList<MoveListener>();
    private double initialX, initialY;
    private int initialPosition = -1;
    private int index;
    
    public Piece(Color fill, double x, double y, Color stroke, int index, MoveListener listener) {
		this.setFill(fill);
		this.setCenterX(x);
		this.setCenterY(y);
		this.setRadius(14.0);
		this.setStroke(stroke);
		this.setStrokeType(StrokeType.INSIDE);
		this.setStrokeWidth(0.3);
		
		this.initialX = x;
		this.initialY = y;
		this.index = index;
		this.listeners.add(listener);
		
		if (fill == Color.WHITE) {
			this.setOnMousePressed(evt -> handleMousePress(evt));
			this.setOnMouseDragged(evt -> handleMouseDrag(evt));
			this.setOnMouseReleased(evt -> handleMouseRelease(evt));
		}
    }
    
    public int getIndex() {
    	return this.index;
    }
    
    public Color getColor() {
    	return (Color) this.getFill();
    }
    
	//Drag and drop functions for white pieces
	public void handleMousePress(MouseEvent evt){
        this.setFill(Color.YELLOW);
    }
    public void handleMouseDrag(MouseEvent evt){
    	this.setCenterX(evt.getX());
    	this.setCenterY(evt.getY());
        for(int i=0;i<BoardController.boardPosition.size();i++) BoardController.boardPosition.get(i).setFill(Color.rgb(84, 255, 135));
    }
    public void handleMouseRelease(MouseEvent evt){
    	this.setFill(Color.WHITE);
        double releaseX = evt.getSceneX();
        double releaseY = evt.getSceneY();
        System.out.println("Drop location: " + releaseX + " " + releaseY);
        for(int i=0; i<BoardController.boardPosition.size();i++){
            double tempX = BoardController.boardPosition.get(i).getCenterX();
            double tempY = BoardController.boardPosition.get(i).getCenterY();
            // If the piece was release close enough to a valid position then snap it to that position
            if(releaseX >= tempX-50 && releaseX <= tempX+50 && releaseY >= tempY-50 && releaseY <= tempY+50) {
                System.out.println("Snapped to: " + i + " " + tempX + " " +  tempY);
                for(int j=0;j<BoardController.boardPosition.size();j++) BoardController.boardPosition.get(j).setFill(Color.TRANSPARENT);
                // Notify listeners that a white piece has been moved
                for (MoveListener listener : listeners)
                	listener.movedWhitePiece(this.index, this.initialPosition, i);
                // Set new piece origin
                this.initialX = tempX;
                this.initialY = tempY;
                this.initialPosition = i;
            }
        }
    	this.setCenterX(initialX);
    	this.setCenterY(initialY);
    }
}
