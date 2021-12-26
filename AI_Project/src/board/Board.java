package board;


import java.util.ArrayList;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.StrokeType;
import javafx.scene.input.MouseEvent;


public class Board {
	public ArrayList<Circle> whitePieces = new ArrayList<Circle>();
	public ArrayList<Circle> blackPieces = new ArrayList<Circle>();
	
	public Board() {
		// Initialize black pieces,
		for (double layoutX = 821.0; layoutX <= 821.0+14.0*9; layoutX += 14.0) {
			Circle circle = new Circle();

			circle.setFill(Color.BLACK);
			circle.setLayoutX(layoutX);
            circle.setLayoutY(118);
			circle.setRadius(14.0);
			circle.setStroke(Color.WHITESMOKE);
			circle.setStrokeType(StrokeType.INSIDE);
			circle.setStrokeWidth(0.3);
			
			this.blackPieces.add(circle);
		}
		
		// Initialize white pieces. Set drag and drop feature for white pieces.
		for (double layoutX = 218.0; layoutX <= 218.0+14.0*9; layoutX += 14.0) {
			Circle circle = new Circle();
			
			circle.setFill(Color.WHITE);
			circle.setLayoutX(layoutX);
            circle.setLayoutY(118);
			circle.setRadius(14.0);
			circle.setStroke(Color.BLACK);
			circle.setStrokeType(StrokeType.INSIDE);
			circle.setStrokeWidth(0.3);
			
			circle.setOnMousePressed(evt -> handleMousePress(evt, circle));
            circle.setOnMouseDragged(evt -> handleMouseDrag(evt, circle));
            circle.setOnMouseReleased(evt -> handleMouseRelease(evt, circle));
			
			this.whitePieces.add(circle);
		}

		
	}

	//Drag and drop functions for white pieces
	public void handleMousePress(MouseEvent evt, Circle circle){
        circle.setFill(Color.YELLOW);
    }
    public void handleMouseDrag(MouseEvent evt, Circle circle){
        circle.setCenterX(evt.getX());
        circle.setCenterY(evt.getY());
        for(int i=0;i<BoardController.boardPosition.size();i++) BoardController.boardPosition.get(i).setFill(Color.rgb(84, 255, 135));
    }
    public void handleMouseRelease(MouseEvent evt, Circle circle){
		circle.setFill(Color.WHITE);
        double releaseX = evt.getX() + circle.getLayoutX();
        double releaseY = evt.getY() + circle.getLayoutY();
        System.out.println("Drop location: " + releaseX + " " + releaseY);
        for(int i=0;i<BoardController.boardPosition.size();i++){
            double tempX = BoardController.boardPosition.get(i).getLayoutX();
            double tempY = BoardController.boardPosition.get(i).getLayoutY();
            if(releaseX >= tempX-30 && releaseX <= tempX+30 && releaseY >= tempY-30 && releaseY <= tempY+30){
                circle.setLayoutX(tempX);
                circle.setLayoutY(tempY);
                System.out.println("Snapped to: " + i + " " + circle.getLayoutX() + " " +  circle.getLayoutY());
                for(int j=0;j<BoardController.boardPosition.size();j++) BoardController.boardPosition.get(j).setFill(Color.TRANSPARENT);
            }
        }
    }
}
