package board;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class Piece {
    private double X;
    private double Y;
    private double radius;
    private Circle c;
    
    public Piece(double X, double Y, double radius, Circle c){
        this.X = X;
        this.Y = Y;
        this.radius = radius;
        this.c = c;
    }
    public double getX(){
        return X;
    }
    public double getY(){
        return Y;
    }
    public double getRadius(){
        return radius;
    }
    public void setFill(Color color){
        c.setFill(color);
    }
}
