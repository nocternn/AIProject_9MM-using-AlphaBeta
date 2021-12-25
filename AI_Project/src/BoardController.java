import javafx.fxml.*;
import javafx.scene.*;
import javafx.event.*;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.ChoiceBox;
import javafx.scene.shape.Circle;
import javafx.util.*;
import javafx.animation.*;
import javafx.collections.*;


import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.input.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;


import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class BoardController implements Initializable{
    @FXML
    private Group whitePieces;
    @FXML
    private Group blackPieces;
    @FXML
    private ChoiceBox<String> algoChoiceBox;
    @FXML
    private Group boardPos;
    @FXML
    private Circle testPiece;
    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        ArrayList<String> st = new ArrayList<>();
        st.add("Alpha-Beta, 2 moves ahead");
        st.add("Alpha-Beta, 5 moves ahead");
        st.add("Alpha-Beta, 7 moves a head");
        algoChoiceBox.setItems(FXCollections.observableArrayList(st));
    }
    public void handleDragDetect(MouseEvent evt){
        System.out.println("onDragDetected");

        Dragboard db = testPiece.startDragAndDrop(TransferMode.ANY);
                
        ClipboardContent content = new ClipboardContent();
        content.putString("DRAG");
        db.setContent(content);
        evt.consume();
    }
    public void handleDragDone(MouseEvent evt){
        System.out.println("onDragDone");
        evt.consume();
    }
}
