package Controller;

import java.net.URL;
import java.util.LinkedList;
import java.util.ResourceBundle;

import Client.ClientHandler;
import UI.ResourceLoader;
import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import model.UserInformation;

public class FriendController implements Initializable {

    @FXML
    private VBox friendsList;

    private Stage stage;

    public FriendController(Stage stage) {
        this.stage = stage;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        populateFriendsList();
    }

    private void populateFriendsList() {
        friendsList.getChildren().clear();
        LinkedList<UserInformation> contactsListT = ClientHandler.getClientSocket().getNotFriends(ClientHandler.getLoggedInUser());
        if (contactsListT != null) {
            for (UserInformation user : contactsListT) {
                HBox friendBox = createFriendBox(user);
                friendsList.getChildren().add(friendBox);
            }
        } else {
            // Handle case where contactsListT is null
        }
    }

    private HBox createFriendBox(UserInformation user) {
        HBox friendBox = new HBox(10);
        friendBox.setAlignment(Pos.CENTER_LEFT);
        ImageView imageView = new ImageView(new Image(ResourceLoader.load("Images/logo.png")));
        imageView.setFitWidth(50);
        imageView.setFitHeight(50);

        Text nameText = new Text(user.getPseudo());
        nameText.setFont(Font.font("Verdana", FontWeight.BOLD, 14));

        MFXButton addButton = new MFXButton("Request Friend");
        addButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (ClientHandler.getLoggedInUser() != null) {
                    System.out.println("Selected = " + user);
                    System.out.println("Logged in  = " + ClientHandler.getLoggedInUser());
                    ClientHandler.getClientSocket().sentInvitation(ClientHandler.getLoggedInUser(), user);
                    populateFriendsList();
                } else {
                    
                }
            }
        });

        friendBox.getChildren().addAll(imageView, nameText, addButton);

        friendBox.setStyle("-fx-padding: 10px; -fx-border-color: black; -fx-border-width: 1px; -fx-border-radius: 5px;");
        return friendBox;
    }
}
