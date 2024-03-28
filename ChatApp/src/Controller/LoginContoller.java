
package Controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.scene.Node;


public class LoginContoller {


    private Stage stage;
    private Scene scene;
    private Parent root;

     @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private MFXButton login;

    @FXML
    private AnchorPane usename;

    @FXML

    public void Login(ActionEvent event) throws IOException{
        Parent root = FXMLLoader.load(getClass().getResource("chat.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    } 

    @FXML
    void initialize() {
        assert login != null : "fx:id=\"login\" was not injected: check your FXML file 'Login.fxml'.";
        assert usename != null : "fx:id=\"usename\" was not injected: check your FXML file 'Login.fxml'.";

    }



}