package board;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import game.Game;
import game.Game.GamePhase;
import helper.MoveListener;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.StrokeType;

public class Piece extends Circle implements Cloneable {
    boolean marked = false; // Piece is marked for deletion
    
    public int initialPosition = -1; // Current position on game board [0,23]
    public boolean active = true; //Check if that piece is still available to play
    
    private ArrayList<MoveListener> listeners = new ArrayList<MoveListener>();
    private double initialX, initialY;
    private int index; // Index when generate pieces, stay constant for entire game

    public Piece clone() {
    	Piece piece = this;
    	try {
			piece = (Piece) super.clone();
		} catch (CloneNotSupportedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
    	return piece;
    }
    
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
		

		this.setOnMousePressed(evt -> handleMousePress(evt));
		if (fill == Color.WHITE) {
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
    
    public boolean deletePiece() {
    	if (marked) {
	    	this.active = false;
	    	this.setVisible(false);
    	}
    	return marked;
    }
    
	// Mouse interactions
    private void handleMousePress(MouseEvent evt){
		if ((Color) this.getFill() == Color.WHITE) {
			this.setFill(Color.YELLOW);
		} else {
	    	if (deletePiece()) {
	    		this.marked = false;
                for (MoveListener listener : listeners)
                	listener.removedBlackPiece(this.initialPosition);
                this.initialPosition = -1;
	    	}
		}
    }
	
    private void handleMouseDrag(MouseEvent evt){
    	this.setCenterX(evt.getX());
    	this.setCenterY(evt.getY());
    	
    	if (Game.getCurrentPhase() == GamePhase.Opening || Game.getCurrentPhase() == GamePhase.Ending) {
    		for(int i=0;i<24;i++) 
    			BoardController.boardPosition.get(i).setFill(Color.rgb(84, 255, 135));
    	}
    	else {
    		for (int i=0; i<24; i++) {
    			if (Board.isAdjacent(this.initialPosition,i) == true || i == this.initialPosition)
    				BoardController.boardPosition.get(i).setFill(Color.rgb(84, 255, 135));
    		}
    	}
    }
    
    private void handleMouseRelease(MouseEvent evt){
    	this.setFill(Color.WHITE);
        double releaseX = evt.getSceneX();
        double releaseY = evt.getSceneY();
        System.out.println("Drop location: " + releaseX + " " + releaseY);
        for(int i=0; i<BoardController.boardPosition.size();i++){
            double tempX = BoardController.boardPosition.get(i).getCenterX();
            double tempY = BoardController.boardPosition.get(i).getCenterY();
            // If the piece was release close enough to a valid position then snap it to that position
            if(releaseX >= tempX-50 && releaseX <= tempX+50 && releaseY >= tempY-50 && releaseY <= tempY+50) {
            	// If the closest position already holds a piece then skip
            	if (Board.isOccupied(i))
            		continue;
            	// In mid-game, if the closest position is not adjacent to the old position then skip
            	else if (Game.getCurrentPhase() == GamePhase.Middle && Board.isAdjacent(this.initialPosition,i) == false)
            		continue;
                System.out.println("Snapped " + this.index + " to: " + i + " " + tempX + " " +  tempY);
                for(int j=0;j<BoardController.boardPosition.size();j++) BoardController.boardPosition.get(j).setFill(Color.TRANSPARENT);
                // Notify listeners that a white piece has been moved
                for (MoveListener listener : listeners)
                	listener.movedWhitePiece(this.index, this.initialPosition, i);
                // Set new piece origin
                this.initialX = tempX;
                this.initialY = tempY;
                this.initialPosition = i;
                break;
            }
        }
    	this.setCenterX(initialX);
    	this.setCenterY(initialY);
    }
}
