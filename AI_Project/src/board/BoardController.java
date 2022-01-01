package board;

import javafx.fxml.*;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.Group;
import javafx.scene.control.ChoiceBox;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import main.Main;
import javafx.collections.*;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.ResourceBundle;

public class BoardController implements Initializable{
    static ArrayList<Circle> boardPosition = new ArrayList<Circle>();
    static Text millStatus;
    static ArrayList<Group> crossPosition = new ArrayList<Group>();
    @FXML
    private ChoiceBox<String> algoChoiceBox;
    @FXML
    private Circle pos1,pos2,pos3,pos4,pos5,pos6,pos7,pos8,pos9,pos10,pos11,pos12,pos13,pos14,pos15,pos16,pos17,pos18,pos19,pos20,pos21,pos22,pos23,pos24;
    @FXML
    private Pane pane;
    @FXML
    private Group x1,x2,x3,x4,x5,x6,x7,x8,x9,x10,x11,x12,x13,x14,x15,x16,x17,x18,x19,x20,x21,x22,x23,x24; // crosses to mark erasable pieces
    
    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
       ArrayList<String> st = new ArrayList<>();
        st.add("Alpha-Beta, 2 moves ahead");
        st.add("Alpha-Beta, 5 moves ahead");
        st.add("Alpha-Beta, 7 moves ahead");
        algoChoiceBox.setItems(FXCollections.observableArrayList(st));
        
        Collections.addAll(boardPosition,pos1,pos2,pos3,pos4,pos5,pos6,pos7,pos8,pos9,pos10,pos11,pos12,pos13,pos14,pos15,pos16,pos17,pos18,pos19,pos20,pos21,pos22,pos23,pos24);
        Collections.addAll(crossPosition,x1,x2,x3,x4,x5,x6,x7,x8,x9,x10,x11,x12,x13,x14,x15,x16,x17,x18,x19,x20,x21,x22,x23,x24);
        for (int i=0; i<24; i++) {
        	crossPosition.get(i).setVisible(false);
        }
        
        millStatus = new Text(550, 466, "Mill!");
        millStatus.setFont(Font.font("Georgia", FontWeight.BOLD, 30));
        millStatus.setVisible(false);
        pane.getChildren().add(millStatus);
    }
    
	public static void markBlackPiece(Piece[] board) {
		// Mark erasable pieces
		for (int i=0; i<24; i++) {
			if (Board.isOccupied(i) && board[i].getColor() == Color.BLACK && !Main.getBoard().isMill(board, Color.BLACK, i)) {
				BoardController.crossPosition.get(i).setVisible(true);
				BoardController.crossPosition.get(i).toFront();
				board[i].marked = true;
			}				
		}
	}
	public static void unmarkBlackPiece(Piece[] board) {
    	// Unmark the pieces after delete
		for (int i=0; i<24; i++) {
			BoardController.crossPosition.get(i).setVisible(false);
			if (board[i] != null && board[i].getColor() == Color.BLACK)
					board[i].marked = false;
		}
	}
}
