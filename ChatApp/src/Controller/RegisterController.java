package Controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import Client.ClientHandler;
import UI.ResourceLoader;
import connection.MySQLConnectSingleton;
import fr.brouillard.oss.cssfx.CSSFX;
import javafx.scene.Node;

import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXPasswordField;
import io.github.palexdev.materialfx.controls.MFXTextField;
import io.github.palexdev.materialfx.css.themes.MFXThemeManager;
import io.github.palexdev.materialfx.css.themes.Themes;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import model.UserInformation;

public class RegisterController extends Application implements Initializable {

  private Stage stage;
  private Scene scene;
  ////////////////////////// Registration //////////////////////////

  @FXML
  private MFXButton Sign_in;

  @FXML
  private MFXPasswordField confirmPassword;

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
  
  UserInformation userInformation = null;

  @FXML
  public void signIn(ActionEvent event) throws IOException {
    ClientHandler.initSocker();

    String uuid = "undefined";
    String username = this.username.getText();
    String confirmPassword = this.confirmPassword.getText();
    String password = this.password.getText();
    String profile_picture = "https://robohash.org/" + username + ".png";

    if (username.isEmpty() || confirmPassword.isEmpty() || password.isEmpty()) {
      warnning.setText("Please fill all the fields");
      return;
    } else if (password.equals(confirmPassword)) {
      userInformation = ClientHandler.getClientSocket().singIn(username, password);
      ClientHandler.setLoggedInUser(userInformation);

      if (userInformation.getUuid() != "") {
        CSSFX.start();
        Stage newStage = new Stage(); 
        FXMLLoader loader = new FXMLLoader(ResourceLoader.loadURL("Main.fxml"));
        loader.setControllerFactory(c -> new MainContoller(newStage)); 
        Parent root = loader.load();
        Scene scene = new Scene(root);
        MFXThemeManager.addOn(scene, Themes.DEFAULT, Themes.LEGACY);
        scene.setFill(Color.TRANSPARENT);
        newStage.initStyle(StageStyle.TRANSPARENT);
        newStage.setScene(scene);
        newStage.setTitle("ChatApp");
        newStage.show();
      } else {
        warnning.setText("User already exists");
      }
    }
    else {
      warnning.setText("Your confirm password is not compatible with your password");
    }
  }

  @FXML
  void havaAnAccount(MouseEvent event) throws IOException {
    Parent root = FXMLLoader.load(getClass().getResource(".././UI/Login.fxml"));
    stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
    scene = new Scene(root);
    stage.setScene(scene);
    stage.show();
  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    // TODO Auto-generated method stub
    hava_account.setOnMouseClicked(arg0 -> {
      try {
        havaAnAccount(arg0);
      } catch (IOException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
    });
  }

  @Override
  public void start(Stage arg0) throws Exception {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'start'");

  }

}
