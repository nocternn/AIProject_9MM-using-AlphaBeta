package board;

import javafx.fxml.*;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.Group;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ProgressBar;
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
	public static ArrayList<Circle> boardPosition = new ArrayList<Circle>();
    public static Text millStatus, gameResult, whiteWin, blackWin, draw;
    public static ArrayList<Group> crossPosition = new ArrayList<Group>();
    public static StackPane maskWhitePieces, maskBoard;
    public static Group s_whiteTurn, s_blackTurn;
    public static ChoiceBox<String> s_algoChoiceBox;
    public static ProgressBar s_blackProgress;
    @FXML
    private ChoiceBox<String> algoChoiceBox;
    @FXML
    private Circle pos1,pos2,pos3,pos4,pos5,pos6,pos7,pos8,pos9,pos10,pos11,pos12,pos13,pos14,pos15,pos16,pos17,pos18,pos19,pos20,pos21,pos22,pos23,pos24;
    @FXML
    private Pane pane;
    @FXML
    private Group whiteTurn, blackTurn;
    @FXML
    private Group x1,x2,x3,x4,x5,x6,x7,x8,x9,x10,x11,x12,x13,x14,x15,x16,x17,x18,x19,x20,x21,x22,x23,x24; // crosses to mark erasable pieces
    @FXML
    private ProgressBar blackProgress;

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
    	ArrayList<String> st = new ArrayList<>();
        st.add("Alpha-Beta, 2 moves ahead");
        st.add("Alpha-Beta, 4 moves ahead");
        st.add("Alpha-Beta, 6 moves ahead");
        st.add("Alpha-Beta, 8 moves ahead");
        st.add("Alpha-Beta, 10 moves ahead");
        algoChoiceBox.setItems(FXCollections.observableArrayList(st));
        algoChoiceBox.getSelectionModel().selectFirst();
        s_algoChoiceBox = algoChoiceBox;
        
        Collections.addAll(boardPosition,pos1,pos2,pos3,pos4,pos5,pos6,pos7,pos8,pos9,pos10,pos11,pos12,pos13,pos14,pos15,pos16,pos17,pos18,pos19,pos20,pos21,pos22,pos23,pos24);
        Collections.addAll(crossPosition,x1,x2,x3,x4,x5,x6,x7,x8,x9,x10,x11,x12,x13,x14,x15,x16,x17,x18,x19,x20,x21,x22,x23,x24);
        for (int i=0; i<24; i++) {
        	crossPosition.get(i).setVisible(false);
        }
        
        s_whiteTurn = whiteTurn;
        s_blackTurn = blackTurn;
        
        millStatus = new Text(550, 466, "Mill!");
        millStatus.setFont(Font.font("Georgia", FontWeight.BOLD, 30));
        millStatus.setVisible(false);
        pane.getChildren().add(millStatus);
        
        maskWhitePieces = new StackPane();
        maskWhitePieces.setPrefSize(300, 50);
        maskWhitePieces.setLayoutX(138);
        maskWhitePieces.setLayoutY(107);
        maskWhitePieces.setVisible(true);
        pane.getChildren().add(maskWhitePieces);
        
        maskBoard = new StackPane();
        maskBoard.setPrefSize(663, 640);
        maskBoard.setLayoutX(256);
        maskBoard.setLayoutY(137);
        maskBoard.setVisible(true);
        pane.getChildren().add(maskBoard);
        
        draw = new Text(524, 470, "DRAW");
        draw.setFont(Font.font("Georgia", FontWeight.BOLD, 36));
        draw.setVisible(false);
        pane.getChildren().add(draw);
        blackWin = new Text(522, 448, "BLACK\n  WIN!");
        blackWin.setFont(Font.font("Georgia", FontWeight.BOLD, 36));
        blackWin.setVisible(false);
        pane.getChildren().add(blackWin);
        whiteWin = new Text(516, 448, "WHITE\n   WIN!");
        whiteWin.setFont(Font.font("Georgia", FontWeight.BOLD, 36));
        whiteWin.setVisible(false);
        pane.getChildren().add(whiteWin);

        s_blackProgress = blackProgress;   
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
	
	public static void bringPiecesToFront(Piece[] board, Color color) {
		for (int i=0; i<24; i++) {
			if (Board.isOccupied(i) && board[i].getColor() == color) 
				board[i].toFront();
		}
	}

	public static void setTurnVisibility(boolean white, boolean black) {
		s_whiteTurn.setVisible(white);
		s_blackTurn.setVisible(black);
        s_blackProgress.setProgress(ProgressBar.INDETERMINATE_PROGRESS);
        if(black){
            s_blackProgress.setStyle("-fx-accent: black;");
        }
        else{
            s_blackProgress.setStyle("-fx-accent: white;");
        }
	}

    public static void controlProgress(boolean Active){
        if(Active){
            s_blackProgress.setStyle("-fx-accent: black;");
        }
        else{
            s_blackProgress.setStyle("-fx-accent: rgba(255,255,255,0);");
        }
    }
	
	public static int getAlgorithmDepth() {
		switch(s_algoChoiceBox.getSelectionModel().getSelectedIndex()) {
		case 1:
			return 4;
		case 2:
			return 6;
		case 3:
			return 8;
		case 4:
			return 10;
		default:
			return 2;
		}
	}
}