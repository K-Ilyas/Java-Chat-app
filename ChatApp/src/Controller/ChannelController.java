package Controller;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import model.UserInformation;

import java.io.IOException;
import java.net.URL;
import java.util.LinkedList;
import java.util.ResourceBundle;

import Client.ClientHandler;
import DAO.*;
import UI.ResourceLoader;
import connection.MySQLConnectSingleton;


public class ChannelController implements Initializable {
  public String roomname;
  public String image;
  @FXML
   Label room_name;
  private LinkedList<String> selectedFriends = new LinkedList<>();
  private UserInformation user,userOrigin;

  public void setRoomAndUser(String roomname, UserInformation user,String image) {
    this.roomname = roomname;
    this.user = user;
    this.userOrigin=user;
    this.image=image;
    room_name.setText("dsa");
}
    @FXML
    private ListView<HBox> listView;

    @FXML
    private Label friendSelected;
    LinkedList<UserInformation> useerSelected = new LinkedList<>();


    @FXML
    void Cancel(ActionEvent event) {
        // Implémentez ce que vous souhaitez faire lorsque le bouton "cancel" est cliqué
    }

    @FXML
    void Create(ActionEvent event) throws IOException {
      boolean cree=false;
      System.out.println("*****************."+userOrigin);
      if (!useerSelected.isEmpty()) {
        // Affichez la liste des amis sélectionnés dans la console
        System.out.println("Amis sélectionnés :");
        for (UserInformation friend : useerSelected) {
            System.out.println("- " + friend);
        }
             ConnectedDAO connected_dao = new ConnectedDAO(MySQLConnectSingleton.getInstance());
             for (UserInformation friend : useerSelected) {
               connected_dao.createFriendUser(friend, roomname);
             }
          
        
  //    cree=ClientHandler.getClientSocket().createRoomWithUsers(userOrigin,useerSelected, roomname,image);
        FXMLLoader loader = new FXMLLoader(ResourceLoader.loadURL("fxml/Rooms.fxml")); 
       Parent root = loader.load();
      RoomController controller = loader.getController();
      controller.setRomm(roomname, userOrigin, image);
      
    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
    Scene scene = new Scene(root);
    stage.setScene(scene);
    stage.show();
    } else {
        System.out.println("Aucun ami sélectionné.");
    }
      
    }

  
public void fillListWithUsers() {
    // Appel à ClientHandler pour récupérer la liste des utilisateurs
    ClientHandler.initSocker();
    System.out.println("User : "+user);
    user = ClientHandler.getClientSocket().login(user.getPseudo(), user.getPassword());
    LinkedList<UserInformation> users = ClientHandler.getClientSocket().getUsers();

    if (users != null) {
        for (UserInformation user : users) {
            CheckBox checkBox = new CheckBox();
            checkBox.selectedProperty().addListener((obs, oldVal, newVal) -> {
                if (newVal) {
                  useerSelected.add(user);
                    selectedFriends.add(user.getPseudo());
                } else {
                  useerSelected.remove(user);
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
      
      selectedFriends = new LinkedList<>();

        
    }
}