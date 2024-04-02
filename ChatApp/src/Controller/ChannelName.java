package Controller;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import DAO.RoomDAO;
import DAO.UserDAO;
import DAO.UserInformation;
import DbSingleton.MySQLConnectSingleton;
import Socket.ClientHandler;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class ChannelName implements Initializable {
    @FXML
    private StackPane imagePane;
    private Stage stage;
    private Scene scene;

    protected UserInformation user ;
    @FXML
    private MFXButton choose;

    @FXML
    private MFXButton back;

    @FXML
    private MFXButton create;

    @FXML
    private MFXTextField roomname;
    String lien;

    @FXML
    void Create(ActionEvent event) throws IOException {
        // Ajoutez ici la logique pour créer quelque chose avec l'image choisie
         RoomDAO room_dao = new RoomDAO(MySQLConnectSingleton.getInstance());
          room_dao.createRoom(user.getPseudo(), roomname.getText(), lien);
      //  ClientHandler.getClientSocket().createRoomWithUsers(user,null, roomname.getText(),lien);
      FXMLLoader loader = new FXMLLoader(getClass().getResource(".././UI/ChannelFreind.fxml"));
      Parent root = loader.load();
  
      // Obtenez le contrôleur de la nouvelle interface
      ChannelController controller = loader.getController();
      
      // Passez les données de roomname et user au contrôleur de la nouvelle interface
      controller.setRoomname(roomname.getText());
      controller.setUser(user);
  
  
    stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
    scene = new Scene(root);
    stage.setScene(scene);
    stage.show();
    }

    @FXML
    void back(ActionEvent event) {
        // Ajoutez ici la logique pour revenir en arrière
    }

    @FXML
    void ChooseImage(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose Image File");
    
        // Filtre pour afficher uniquement les fichiers image
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif", "*.bmp", "*.jpeg"));
    
        File selectedFile = fileChooser.showOpenDialog(null);
    
        if (selectedFile != null) {
            // Chargement de l'image sélectionnée
            Image image = new Image(selectedFile.toURI().toString());
            lien=selectedFile.toURI().toString();
        
    
        
            
            // Définir le rayon du cercle en fonction de la taille désirée de l'image
            double imageSize = 75; // Taille de l'image souhaitée

            Circle circle = new Circle(196, 26, imageSize, Color.TRANSPARENT);
            circle.setFill(new ImagePattern(image));
            circle.setEffect(new DropShadow(+19d, 0d, +2d, Color.DARKSEAGREEN));
    
            // Ajout du cercle à la pile d'images
            imagePane.getChildren().add(circle);
    
            // Rendre le bouton create visible une fois que l'image est choisie
            choose.setVisible(false);
        }
    }
    

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Masquer le bouton create au démarrage de l'application
        ClientHandler.initSocker();
        user = ClientHandler.getClientSocket().login("aziz", "aziz");
    }
}
