package Controller;

import java.net.URL;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.Map;
import java.util.ResourceBundle;

import Client.ClientHandler;
import UI.ResourceLoader;
import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import model.FriendRequest;
import model.UserInformation;

public class NotificationContoller implements Initializable {

    @FXML
    private VBox Notification;

    private Stage stage;

    public NotificationContoller(Stage stage) {
        this.stage = stage;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        Hashtable<UserInformation, FriendRequest> contactsListT = ClientHandler.getClientSocket().getFriendRequests(ClientHandler.getLoggedInUser());
        if (contactsListT != null)
            populateNotificationList(contactsListT);
    }
    private void populateNotificationList(Hashtable<UserInformation, FriendRequest> contactsListT) {
        Notification.getChildren().clear();
        for (Map.Entry<UserInformation, FriendRequest> entry : contactsListT.entrySet()) {
            UserInformation user = entry.getKey();
            FriendRequest friendRequest = entry.getValue();
            HBox friendBox = createNotificationBox(user, friendRequest);
            Notification.getChildren().add(friendBox);
        }
    }

    private HBox createNotificationBox(UserInformation user, FriendRequest friendRequest) {
        HBox friendBox = new HBox(10);
        friendBox.setAlignment(Pos.CENTER_LEFT);

        ImageView imageView = new ImageView(new Image(ResourceLoader.load("Images/logo.png")));
        imageView.setFitWidth(50);
        imageView.setFitHeight(50);


        Text nameText = new Text(user.getPseudo());
        nameText.setFont(Font.font("Verdana", FontWeight.BOLD, 14));

        MFXButton acceptButton = new MFXButton("Accept");
        acceptButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                ClientHandler.getClientSocket().acceptInvitation(ClientHandler.getLoggedInUser(), friendRequest, "accepted");
            }
        });

        MFXButton rejectButton = new MFXButton("Reject");
        rejectButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                ClientHandler.getClientSocket().acceptInvitation(ClientHandler.getLoggedInUser(), friendRequest, "rejected");
            }
        });

        friendBox.getChildren().addAll(imageView, nameText, acceptButton, rejectButton);

        friendBox.setStyle("-fx-padding: 10px; -fx-border-color: black; -fx-border-width: 1px; -fx-border-radius: 5px;");
        return friendBox;
    }
}
