
package Controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import Client.ClientHandler;
import UI.ResourceLoader;
import fr.brouillard.oss.cssfx.CSSFX;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXPasswordField;
import io.github.palexdev.materialfx.controls.MFXTextField;
import io.github.palexdev.materialfx.css.themes.MFXThemeManager;
import io.github.palexdev.materialfx.css.themes.Themes;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import model.UserInformation;
import javafx.scene.Node;

public class LoginContoller extends Application {

  private Stage stage;
  private Scene scene;
  private Parent root;
  UserInformation user = null;
  @FXML
  private Label createAccount;

  @FXML
  private MFXButton login;

  @FXML
  private MFXPasswordField password;

  @FXML
  private MFXTextField username;

  @FXML
  private Label warning;

  public LoginContoller(Stage stage){
    this.stage =  stage;
  }

  @FXML
  public void Login(ActionEvent event) throws IOException {
    ClientHandler.initSocker();
    String usernameUser = username.getText();
    String passwordUser = password.getText();
    user = ClientHandler.getClientSocket().login(usernameUser, passwordUser);
    if (usernameUser.isEmpty() || passwordUser.isEmpty()) {
      warning.setText("Please fill all the fields");
      return;
    } else if (user != null) {
      ClientHandler.setLoggedInUser(user);
      CSSFX.start();
      Stage newStage = new Stage(); // Create a new stage
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
      warning.setText("Invalid username or password");
      return;
    }
  }

  @FXML
  void creatAccount(MouseEvent event) throws IOException {
    Parent root = FXMLLoader.load(getClass().getResource(".././UI/Register.fxml"));
    stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
    scene = new Scene(root);
    stage.setScene(scene);
    stage.show();
  }

  @FXML
  void initialize() {
    assert login != null : "fx:id=\"login\" was not injected: check your FXML file 'Login.fxml'.";
    assert username != null : "fx:id=\"usename\" was not injected: check your FXML file 'Login.fxml'.";
    createAccount.setOnMouseClicked(arg0 -> {
      try {
        creatAccount(arg0);
      } catch (IOException e) {
        e.printStackTrace();
      }
    });
  }

  @Override
  public void start(Stage arg0) throws Exception {
    throw new UnsupportedOperationException("Unimplemented method 'start'");
  }

}