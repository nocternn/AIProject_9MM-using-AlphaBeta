

import java.util.ArrayList;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.StrokeType;

public class Board {
	public ArrayList<Circle> whitePieces = new ArrayList<Circle>();
	public ArrayList<Circle> blackPieces = new ArrayList<Circle>();
	
	public Board() {
		// Initialize black pieces,
		for (double layoutX = 0; layoutX <= 112.0; layoutX += 14.0) {
			Circle circle = new Circle();

			circle.setFill(Color.BLACK);
			circle.setLayoutX(layoutX);
			circle.setRadius(14.0);
			circle.setStroke(Color.WHITE);
			circle.setStrokeType(StrokeType.INSIDE);
			circle.setStrokeWidth(0.3);
			
			this.blackPieces.add(circle);
		}
		
		// Initialize white pieces. Set drag and drop feature for white pieces.
		for (double layoutX = 0; layoutX <= 112.0; layoutX += 14.0) {
			Circle circle = new Circle();
			
			circle.setFill(Color.WHITE);
			circle.setLayoutX(layoutX);
			circle.setRadius(14.0);
			circle.setStroke(Color.BLACK);
			circle.setStrokeType(StrokeType.INSIDE);
			circle.setStrokeWidth(0.3);
			
			circle.setOnMouseClicked(event -> {
				System.out.println("Drag Done");
			});
			circle.setOnMouseDragged(event -> {
				circle.setCenterX(event.getX());
				circle.setCenterY(event.getY());
			});
			
			this.whitePieces.add(circle);
		}
	}
}