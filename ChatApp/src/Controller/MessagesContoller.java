package Controller;


import java.io.IOException;
import java.net.URL;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.ResourceBundle;

import Client.ClientHandler;
import Events.NotificationEvent;
import UI.ResourceLoader;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXListView;
import io.github.palexdev.materialfx.controls.MFXScrollPane;
import io.github.palexdev.materialfx.controls.MFXTextField;
import io.github.palexdev.materialfx.controls.cell.MFXListCell;
import io.github.palexdev.materialfx.utils.others.FunctionalStringConverter;
import io.github.palexdev.mfxcomponents.controls.base.MFXLabeled;
import io.github.palexdev.mfxresources.fonts.MFXFontIcon;
import io.github.palexdev.virtualizedfx.enums.ScrollPaneEnums.ScrollBarPolicy;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import model.MessageTo;
import model.UserInformation;
import bubble.*;

public class MessagesContoller implements Initializable
{
    private static MessagesContoller instance;
    

    @FXML 
    private VBox chatPane;

    @FXML 
    private GridPane rootPane;

    @FXML
    private MFXListView<UserInformation> userList;

    @FXML 
    private MFXTextField messageInput;

    @FXML 
    private HBox actions;
    
    @FXML 
    private MFXButton send;


    @FXML 
    private MFXScrollPane messageScroller;

    @FXML 
    private Label RcLabel;

    @FXML 
    private MFXButton voiceBtn;

    @FXML 
    private MFXButton videoBtn;


    @FXML 
    private MFXTextField searchUserTF;


    private static ObservableList<Node> speechBubbles = FXCollections.observableArrayList();

    private static UserInformation selectedUser;

    private Stage stage;


    private Stage emojiSelectorStage;
    MessagesContoller(Stage stage){
        this.stage = stage;
        instance = this;
    }
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {


        // Set the policy of the vertical scrollbar to always display
        // messageScroller.setVbarPolicy(MFXScrollPane.ScrollBarPolicy.ALWAYS);

        // Bind the height of the content to the height of the viewport
        // chatPane.prefHeightProperty().bind(messageScroller.heightProperty());

        Bindings.bindContentBidirectional(speechBubbles, chatPane.getChildren());
        speechBubbles.addListener((ListChangeListener<Node>) change -> {
            while (change.next()) {
                if (change.wasAdded()) {
                    // Scroll to the bottom
                    messageScroller.setVvalue(1.0);
                }
            }
        });


        ClientHandler.getClientSocket().getRecive().setMessagesContoller(this);
        Thread Notifications = new Thread(ClientHandler.getClientSocket().getRecive());
        
        Notifications.start();

        MFXFontIcon wrapper = new MFXFontIcon("fas-sliders", 24);
        MFXButton sendButton = new MFXButton();
        sendButton.setGraphic(wrapper);

        // actions.getChildren().add(actions);


        // [API] get the user from the ClientHandler API
        LinkedList<UserInformation> contactsListT = ClientHandler.getClientSocket().getUsers();

        // https://examples.javacodegeeks.com/java-development/desktop-java/javafx/listview-javafx/javafx-listview-example/

        ObservableList<UserInformation> users = FXCollections.<UserInformation>observableArrayList(contactsListT);
        StringConverter<UserInformation> converter = FunctionalStringConverter.to(person -> (person == null) ? "" : person.getPseudo());
        
        userList.setItems(users);
            
        userList.setConverter(converter);
        userList.setCellFactory(person -> new UserCellFactory(userList, person));
        userList.features().enableBounceEffect();
        userList.features().enableSmoothScrolling(0.5);
        

        // [Feature] add the right first page 


    FilteredList<UserInformation> filteredData = ListConverter.convertToFilteredList(users);

    //Set the filter Predicate whenever the filter changes.
    searchUserTF.textProperty().addListener((observable, oldValue, newValue) -> {
        filteredData.setPredicate(client ->{
            // If filter text is empty, display all persons.
            if(newValue == null || newValue.isEmpty()){
                return true;
            }

            // Compare first name and last name of every client with filter text.
            String lowerCaseFilter = newValue.toLowerCase();

            if(client.getPseudo().toLowerCase().contains(lowerCaseFilter)){
                return true; //filter matches first name
            }
            return false; //Does not match
        });
    });

    
    SortedList<UserInformation> sortedData = new SortedList<>(filteredData);


    userList.setItems(sortedData);

        
    }

    @FXML
    public void sendButtonAction(ActionEvent event) {
        String msg = messageInput.getText();
        if (messageInput != null && !messageInput.getText().isEmpty()) {
            // [API] here goes the send the send Function from ClientHandler
            ClientHandler.getClientSocket().sendMessage(msg, ClientHandler.getLoggedInUser(), selectedUser);
            messageInput.clear();
        }
    }
 

    private static class UserCellFactory extends MFXListCell<UserInformation> {
        private final MFXFontIcon userIcon;

        public UserCellFactory(MFXListView<UserInformation> listView, UserInformation data) {
            super(listView, data);

            userIcon = new MFXFontIcon("fas-user", 18);
            userIcon.getStyleClass().add("user-icon");
            render(data);
        }

        @Override
        protected void render(UserInformation data) {
            super.render(data);
            if (userIcon != null) getChildren().add(0, userIcon);
        }
    }


  



    private void updateChatPane(UserInformation selectedUser) {
        chatPane.getChildren().clear();
        MessagesContoller.selectedUser = selectedUser;
        LinkedList<MessageTo> messages = ClientHandler.getClientSocket().getMessages(ClientHandler.getLoggedInUser(), selectedUser);
        messages.sort(Comparator.comparing(MessageTo::getMessage_date));


        for (MessageTo message : messages){
            if (message.getUuid_sender().equals(MessagesContoller.selectedUser.getUuid()))
                speechBubbles.add(new SpeechBox(message, "RIGHT"));
            else speechBubbles.add(new SpeechBox(message, "LEFT"));        
        }

        RcLabel.setText(selectedUser.getPseudo());
    }

@FXML
private void handleUserClick(MouseEvent event) {
    UserInformation selectedUser = (UserInformation) userList.getSelectionModel().getSelectedValues().get(0);
    if (selectedUser != null) {
        updateChatPane(selectedUser);
    }
}


  public void setNotification(UserInformation sender, MessageTo msg){
    Platform.runLater(() -> {
        if (sender.getUuid().equals(selectedUser.getUuid()))
            speechBubbles.add(new SpeechBox(msg, "RIGHT"));
        else speechBubbles.add(new SpeechBox(msg, "LEFT"));
        // BubbledTextFlow otherBubbled = new BubbledTextFlow(EmojiDisplayer.createEmojiAndTextNode(content));
// 
    });
 }


 public MFXTextField getMessageInput() {
     return messageInput;
 }

 public static MessagesContoller getInstance(){
    return instance;
}


//  @FXML
//     private void emojiSelectorBtnAction(ActionEvent event) {
//         try {
//             // Load the FXML file for the new scene
//             FXMLLoader loader = new FXMLLoader(ResourceLoader.loadURL("fxml/EmojiSelectorUI.fxml"));
//             Parent root = loader.load();
            
//             // Get the controller for the new scene
//             EmojiSelectorController newSceneController = loader.getController();
            
//             // Create a new stage
//             Stage newStage = new Stage();
//             newStage.setTitle("Emojie Selector");
            
//             Scene scene = new Scene(root);
//             newStage.setScene(scene);
            
//             newStage.show();
//         } catch (IOException e) {
//             e.printStackTrace();
//         }
//     }

    @FXML
    private void emojiSelectorBtnAction(ActionEvent event) {
        if (emojiSelectorStage != null && emojiSelectorStage.isShowing()) {
            // Close the emoji selector
            emojiSelectorStage.close();
        } else {
            try {
                emojiSelectorStage = new Stage();
                FXMLLoader loader = new FXMLLoader(ResourceLoader.loadURL("fxml/EmojiSelectorUI.fxml"));
                loader.setControllerFactory(c -> new EmojiSelectorController(emojiSelectorStage ,stage));
                Parent root = loader.load();
                Scene scene = new Scene(root);
                emojiSelectorStage.setTitle("Emoji Piker");
                emojiSelectorStage.setScene(scene);
                emojiSelectorStage.setResizable(true);
                emojiSelectorStage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


}