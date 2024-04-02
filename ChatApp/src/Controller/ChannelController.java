package Controller;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

import java.net.URL;
import java.util.LinkedList;
import java.util.ResourceBundle;

import DAO.UserInformation;
import Socket.ClientHandler;

public class ChannelController implements Initializable {
  private String roomname;
    private UserInformation user1;

    public void setRoomname(String roomname) {
        this.roomname = roomname;
    }

    public void setUser(UserInformation user) {
        this.user1 = user;
    }
    @FXML
    private ListView<HBox> listView;

    @FXML
    private Label friendSelected;

    private LinkedList<UserInformation> users = new LinkedList<>();
    private LinkedList<String> selectedFriends = new LinkedList<>();
    private UserInformation user;
    
    @FXML
    void Cancel(ActionEvent event) {
        // Implémentez ce que vous souhaitez faire lorsque le bouton "cancel" est cliqué
    }

    @FXML
    void Create(ActionEvent event) {
      if (!selectedFriends.isEmpty()) {
        // Affichez la liste des amis sélectionnés dans la console
        System.out.println("Amis sélectionnés :");
        for (String friend : selectedFriends) {
            System.out.println("- " + friend);
        }
    } else {
        System.out.println("Aucun ami sélectionné.");
    }
        user.setIsadmin(true);
    }

  
public void fillListWithUsers() {
    // Appel à ClientHandler pour récupérer la liste des utilisateurs
    ClientHandler.initSocker();
    user = ClientHandler.getClientSocket().login("aziz", "aziz");
    LinkedList<UserInformation> users = ClientHandler.getClientSocket().getUsers();

    if (users != null) {
        for (UserInformation user : users) {
            CheckBox checkBox = new CheckBox();
            checkBox.selectedProperty().addListener((obs, oldVal, newVal) -> {
                if (newVal) {
                    selectedFriends.add(user.getPseudo());
                } else {
                    selectedFriends.remove(user.getPseudo());
                }
                friendSelected.setText(String.valueOf(selectedFriends.size()));
            });
        
            // Chargement de l'image à partir du chemin fourni par l'utilisateur
            Image image = new Image(user.getImage());
            ImageView imageView = new ImageView(image);
            imageView.setFitWidth(40); // Largeur souhaitée de l'image
            imageView.setFitHeight(40); // Hauteur souhaitée de l'image

            Label nameLabel = new Label(user.getPseudo());
            HBox hbox = new HBox(10,checkBox, imageView,nameLabel);
            listView.getItems().add(hbox);
            
        }
    } else {
        System.out.println("La liste d'utilisateurs est null.");
    }

}
    @Override
    public void initialize(URL location, ResourceBundle resources) {
      

        fillListWithUsers();
        
    }
}
