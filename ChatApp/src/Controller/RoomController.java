package Controller;

import java.net.URL;
import java.util.ResourceBundle;

import DAO.UserInformation;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;


public class RoomController extends Application implements Initializable {

    private Stage stage;
    private Scene scene;
    public String roomname;
    public String imageRoom;
    @FXML
     private  ImageView imageView;
     @FXML
      private Label nameRoom;

    public void setRomm(String roomname,UserInformation user, String imageRoom) {
        this.roomname = roomname;
        this.imageRoom = imageRoom;
        nameRoom.setText(roomname);
        Image newImage = new Image(imageRoom);
        imageView.setImage(newImage);
    }

      @Override
      public void initialize(URL location, ResourceBundle resources) {
        // TODO Auto-generated method stub
         }
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

      @Override
      public void start(Stage arg0) throws Exception {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'start'");
      }
  
  
  
}
