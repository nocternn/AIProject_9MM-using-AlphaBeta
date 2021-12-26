package board;
import javafx.fxml.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.control.ChoiceBox;
import javafx.scene.shape.Circle;
import javafx.collections.*;
import javafx.scene.paint.Color;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class BoardController implements Initializable{
    @FXML
    private ChoiceBox<String> algoChoiceBox;
    @FXML
    private Circle pos1,pos2,pos3,pos4,pos5,pos6,pos7,pos8,pos9,pos10,pos11,pos12,pos13,pos14,pos15,pos16,pos17,pos18,pos19,pos20,pos21,pos22,pos23,pos24;
    @FXML
    private Pane pane;

    private ArrayList<Piece> boardPosition = new ArrayList<Piece>();
    
    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
       ArrayList<String> st = new ArrayList<>();
        st.add("Alpha-Beta, 2 moves ahead");
        st.add("Alpha-Beta, 5 moves ahead");
        st.add("Alpha-Beta, 7 moves a head");
        algoChoiceBox.setItems(FXCollections.observableArrayList(st));
        
        boardPosition.add(new Piece(pos1.getLayoutX(), pos1.getLayoutY(), pos1.getRadius(), pos1));
        boardPosition.add(new Piece(pos2.getLayoutX(), pos2.getLayoutY(), pos2.getRadius(), pos2));
        boardPosition.add(new Piece(pos3.getLayoutX(), pos3.getLayoutY(), pos3.getRadius(), pos3));
        boardPosition.add(new Piece(pos4.getLayoutX(), pos4.getLayoutY(), pos4.getRadius(), pos4));
        boardPosition.add(new Piece(pos5.getLayoutX(), pos5.getLayoutY(), pos5.getRadius(), pos5));
        boardPosition.add(new Piece(pos6.getLayoutX(), pos6.getLayoutY(), pos6.getRadius(), pos6));
        boardPosition.add(new Piece(pos7.getLayoutX(), pos7.getLayoutY(), pos7.getRadius(), pos7));
        boardPosition.add(new Piece(pos8.getLayoutX(), pos8.getLayoutY(), pos8.getRadius(), pos8));
        boardPosition.add(new Piece(pos9.getLayoutX(), pos9.getLayoutY(), pos9.getRadius(), pos9));
        boardPosition.add(new Piece(pos10.getLayoutX(), pos10.getLayoutY(), pos10.getRadius(), pos10));
        boardPosition.add(new Piece(pos11.getLayoutX(), pos11.getLayoutY(), pos11.getRadius(), pos11));
        boardPosition.add(new Piece(pos12.getLayoutX(), pos12.getLayoutY(), pos12.getRadius(), pos12));
        boardPosition.add(new Piece(pos13.getLayoutX(), pos13.getLayoutY(), pos13.getRadius(), pos13));
        boardPosition.add(new Piece(pos14.getLayoutX(), pos14.getLayoutY(), pos14.getRadius(), pos14));

        for(int i=0;i<9;i++){
            Circle piece = new Circle();
            piece.setFill(Color.WHITE);
            piece.setStroke(Color.BLACK);
            piece.setStrokeWidth(0.3);
            piece.setRadius(14);
            piece.setLayoutX(232 + 14*i);
            piece.setLayoutY(132);
            piece.setOnMousePressed(evt -> handleMousePress(evt,piece));
            piece.setOnMouseDragged(evt -> handleMouseDrag(evt, piece));
            piece.setOnMouseReleased(evt -> handleMouseRelease(evt, piece));
            pane.getChildren().add(piece);

            Circle blackPiece = new Circle();
            blackPiece.setFill(Color.BLACK);
            blackPiece.setStroke(Color.WHITESMOKE);
            blackPiece.setStrokeWidth(0.3);
            blackPiece.setRadius(14);
            blackPiece.setLayoutX(835 + 14*i);
            blackPiece.setLayoutY(132);
            pane.getChildren().add(blackPiece);
        }
    }
    
    public void handleMousePress(MouseEvent evt, Circle circle){
        circle.setFill(Color.YELLOW);
    }
    public void handleMouseDrag(MouseEvent evt, Circle circle){
        circle.setCenterX(evt.getX());
        circle.setCenterY(evt.getY());
        for(int i=0;i<boardPosition.size();i++) boardPosition.get(i).setFill(Color.rgb(84, 255, 135));
    }
    public void handleMouseRelease(MouseEvent evt, Circle circle){
        double releaseX = evt.getX() + circle.getLayoutX();
        double releaseY = evt.getY() + circle.getLayoutY();
        System.out.println(releaseX + " " + releaseY);
        for(int i=0;i<boardPosition.size();i++){
            double tempX = boardPosition.get(i).getX();
            double tempY = boardPosition.get(i).getY();
            System.out.println(i + ": " + tempX + " " + tempY);
            if(releaseX >= tempX-30 && releaseX <= tempX+30 && releaseY >= tempY-30 && releaseY <= tempY+30){
                circle.setLayoutX(tempX);
                circle.setLayoutY(tempY);
                System.out.println("New pos: " + circle.getLayoutX() + " " +  circle.getLayoutY());
                for(int j=0;j<boardPosition.size();j++) boardPosition.get(j).setFill(Color.TRANSPARENT);
            }
        }
    }
}
/*  public void handleDragDone(DragEvent evt){
        System.out.println("onDragDone");
        evt.consume();
    }
    public void handleDragOver(DragEvent evt){
        System.out.println("OnDragOver");
        evt.consume();
    }
    public void handleDragEnter(DragEvent evt){
        System.out.println("onDragEntered");
        pos1.setFill(Color.GREEN);
        evt.consume();
    }
    public void handleDragExit(DragEvent evt){
        pos1.setFill(Color.TRANSPARENT);
        evt.consume();
    }
    public void handleDragDrop(DragEvent evt){
        System.out.println("OnDragDrop");
        evt.setDropCompleted(true);
        evt.consume();
    } 
    public void handleDragDetect(MouseEvent evt){
        System.out.println("onDragDetected");
                
        Dragboard db = testPiece.startDragAndDrop(TransferMode.ANY);
        ClipboardContent content = new ClipboardContent();
                content.putString("DRAGGING");
                db.setContent(content);
        //testPiece.setCenterX(evt.getX());
		//testPiece.setCenterY(evt.getY());
        evt.consume();
    }*/
