package Controller;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.scene.Node;

import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXPasswordField;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;


public class MainSceneController implements Initializable{

    private Stage stage;
    private Scene scene;
  ////////////////////////// Registration //////////////////////////

      @FXML
      private MFXButton Sign_in;
  
      @FXML
      private MFXPasswordField confirm_password;
  
      @FXML
      private Label hava_account;
  
      @FXML
      private MFXPasswordField password;
  
      @FXML
      private AnchorPane usename;
  
      @FXML
      private MFXTextField username;
  
      @FXML
      private Label warnning;
  
      @FXML
      public void signIn(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource(".././UI/Rooms.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

      }

      @Override
      public void initialize(URL location, ResourceBundle resources) {
        // TODO Auto-generated method stub
         }
  
  ////////////////////////////// Rooms //////////////////////////////
  

  
  
      @FXML
      private Button join1;
  
      @FXML
      private Button join2;
  
      @FXML
      private Button join3;
  
      @FXML
      private Button join4;
  
      @FXML
      private Button join5;
  
      @FXML
      private Button join6;
  
      @FXML
      private Label privateChat;
  
      @FXML
      private MFXTextField searchServerName;
  
      @FXML
      void join1(ActionEvent event) {
  
      }
  
      @FXML
      void join2(ActionEvent event) {
  
      }
  
      @FXML
      void join3(ActionEvent event) {
  
      }
  
      @FXML
      void join4(ActionEvent event) {
  
      }
  
      @FXML
      void join5(ActionEvent event) {
  
      }
  
      @FXML
      void join6(ActionEvent event) {
  
      }
  
      @FXML
      void privateChat(MouseEvent event) {
  
      }
  
      @FXML
      void searchServerName(ActionEvent event) {
  
      }
  
  
  
}
