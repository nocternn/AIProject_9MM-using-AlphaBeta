package board;

import javafx.fxml.*;
import javafx.scene.layout.Pane;
import javafx.scene.control.ChoiceBox;
import javafx.scene.shape.Circle;
import javafx.collections.*;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.ResourceBundle;

public class BoardController implements Initializable{
    static ArrayList<Circle> boardPosition = new ArrayList<Circle>();
    @FXML
    private ChoiceBox<String> algoChoiceBox;
    @FXML
    private Circle pos1,pos2,pos3,pos4,pos5,pos6,pos7,pos8,pos9,pos10,pos11,pos12,pos13,pos14,pos15,pos16,pos17,pos18,pos19,pos20,pos21,pos22,pos23,pos24;
    @FXML
    private Pane pane;
    
    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
       ArrayList<String> st = new ArrayList<>();
        st.add("Alpha-Beta, 2 moves ahead");
        st.add("Alpha-Beta, 5 moves ahead");
        st.add("Alpha-Beta, 7 moves ahead");
        algoChoiceBox.setItems(FXCollections.observableArrayList(st));
        
        Collections.addAll(boardPosition,pos1,pos2,pos3,pos4,pos5,pos6,pos7,pos8,pos9,pos10,pos11,pos12,pos13,pos14,pos15,pos16,pos17,pos18,pos19,pos20,pos21,pos22,pos23,pos24);
    }    
}
