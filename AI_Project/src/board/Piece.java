package board;

import java.util.ArrayList;

import game.Game;
import game.Game.GamePhase;
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
    private boolean active = true; //Check if that piece is still available to play
    
    public Piece() {
    	// Empty constructor
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
		
		if (fill == Color.WHITE) {
			this.setOnMousePressed(evt -> handleMousePress(evt));
			this.setOnMouseDragged(evt -> handleMouseDrag(evt));
			this.setOnMouseReleased(evt -> handleMouseRelease(evt));
		}
		
		else this.setOnMouseClicked(evt -> handleMouseClicked(evt));
    }
    
    public int getIndex() {
    	return this.index;
    }
    
    public int getPosition() {
    	return this.initialPosition;
    }
    
    public boolean getActive() {
    	return active;
    } 
    
    public Color getColor() {
    	return (Color) this.getFill();
    }
    public void deletePiece() {
    	this.active = false;
    	this.setVisible(false);
    }
    
	//Drag and drop functions for white pieces
	public void handleMousePress(MouseEvent evt){
        this.setFill(Color.YELLOW);
    }
    public void handleMouseDrag(MouseEvent evt){
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
    public void handleMouseRelease(MouseEvent evt){
    	this.setFill(Color.WHITE);
        double releaseX = evt.getSceneX();
        double releaseY = evt.getSceneY();
        System.out.println("Drop location: " + releaseX + " " + releaseY);
        for(int i=0; i<24;i++){
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
                System.out.println("Snapped to: " + i + " " + tempX + " " +  tempY);
                for(int j=0;j<BoardController.boardPosition.size();j++) BoardController.boardPosition.get(j).setFill(Color.TRANSPARENT);
                // Notify listeners that a white piece has been moved
                for (MoveListener listener : listeners)
                	listener.moveWhitePiece(this.index, this.initialPosition, i);
                // Set new piece origin
                this.initialX = tempX;
                this.initialY = tempY;
                this.initialPosition = i;
            }
            // If the position of the piece doesn't change after drag
            else for(int j=0;j<BoardController.boardPosition.size();j++) BoardController.boardPosition.get(j).setFill(Color.TRANSPARENT);
        }
    	this.setCenterX(initialX);
    	this.setCenterY(initialY);
    }
    
    //Click to delete a black piece
    public void handleMouseClicked(MouseEvent evt) {
    	this.deletePiece();
    	System.out.println("Deleted a black piece");
    	BoardController.stackPane.toFront();
    	// Unmark the pieces after delete
		for (int i=0; i<24; i++) {
			BoardController.crossPosition.get(i).setVisible(false);
		}
		
		if (Game.getCurrentPhase() == GamePhase.Middle || Game.getCurrentPhase() == GamePhase.Ending) {
			for (int i=0; i<24; i++) {
				if (Board.isOccupied(i) && Board.board[i].getColor() == Color.WHITE) 
					Board.board[i].toFront();			
			}
		}
    }
}
