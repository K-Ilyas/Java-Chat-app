package Controller;


import java.net.URL;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import DAO.UserInformation;
import Socket.ClientHandler;
import UI.ResourceLoader;
import bubble.BubbleSpec;
import bubble.BubbledLabel;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXListView;
import io.github.palexdev.materialfx.controls.MFXTextField;
import io.github.palexdev.materialfx.controls.cell.MFXListCell;
import io.github.palexdev.materialfx.utils.others.FunctionalStringConverter;
import io.github.palexdev.mfxresources.fonts.MFXFontIcon;
import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.util.StringConverter;

public class MessagesContoller implements Initializable
{
    private UserInformation friend;

    @FXML 
    private MFXListView<HBox> chatPane;

    @FXML
    private MFXListView<UserInformation> userList;

    @FXML 
    private MFXTextField messageInput;

    @FXML 
    private HBox actions;
    
    @FXML 
    private MFXButton send;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        HBox bottomBar = new HBox();
        bottomBar.setSpacing(0);
        bottomBar.setAlignment(Pos.BOTTOM_LEFT);
        VBox vbox = new VBox();
        vbox.setPadding(new Insets(0, 0, 10, 0));

        MFXFontIcon wrapper = new MFXFontIcon("fas-sliders", 24);
        MFXButton sendButton = new MFXButton();
        sendButton.setGraphic(wrapper);

        // actions.getChildren().add(actions);

        // [API] get the user from the ClientHandler API
        LinkedList<UserInformation> contactsListT = new LinkedList<>();
        contactsListT.add(new UserInformation("ahmed", "woow")); 
        // https://examples.javacodegeeks.com/java-development/desktop-java/javafx/listview-javafx/javafx-listview-example/

        ObservableList<UserInformation> users = FXCollections.<UserInformation>observableArrayList(contactsListT);
        StringConverter<UserInformation> converter = FunctionalStringConverter.to(person -> (person == null) ? "" : person.getPseudo());
        
        userList.setItems(users);
            
        userList.setConverter(converter);
        userList.setCellFactory(person -> new UserCellFactory(userList, person));
        userList.features().enableBounceEffect();
        userList.features().enableSmoothScrolling(0.5);
        
        // [API] here goes the old messages
        chatPane.getItems().add(BuildMsg(new MessageTo("123","1234322","Hello from the ", "12/12/12", false), Color.LIGHTBLUE));
        chatPane.getItems().add(BuildMsg(new MessageTo("123","1234322","Hello from the ", "12/12/12", false) , Color.LIGHTGREEN));
        chatPane.getItems().add(BuildMsg(new MessageTo("123","1234322","Hello from the ", "12/12/12", false) , Color.LIGHTBLUE));
        chatPane.getItems().add(BuildMsg(new MessageTo("123","1234322","Hello from the ", "12/12/12", false) , Color.LIGHTGREEN));
        chatPane.getItems().add(BuildMsg(new MessageTo("123","1234322","Hello from the ", "12/12/12", false) , Color.LIGHTBLUE));

         ObjectProperty<UserInformation> selectedUserProperty = new SimpleObjectProperty<>();
        ObjectProperty<MessageTo> selectedMessageProperty = new SimpleObjectProperty<>();

        userList.setCellFactory(lv -> new MFXListCell<UserInformation>(userList, lv) {
            @Override
            protected void updateItem(UserInformation user, boolean empty) {
                updateItem(user, empty);
                if (empty || user == null) {
                    setText(null);
                } else {
                    setText(user.getPseudo());
                    setStyle("-fx-background-color: #f0f2f5; -fx-padding: 5;");
                    setOnMouseClicked(event -> {
                        selectedUserProperty.set(user);
                        friend = user;
                        List<MessageTo> messages = new ArrayList<>();
                        messages.add(new MessageTo("123","1234322","Hello from the ", "12/12/12", false));
                        messages.add(new MessageTo("123","1234322","Hello from thfdsfe fs", "12/12/16", false));
                        messages.add(new MessageTo("123","1234322","Hello from thdfsfdsfe ", "12/12/17", false));
                        messages.add(new MessageTo("123","1234322","Hello from the gfdgfgd", "12/12/18", false));
                        // messageCount.setText("Message Count: " + messages.size());
                        messages.sort(Comparator.comparing(MessageTo::getMessage_date));
                        chatPane;
                        for(MessageTo message : messages)
                            chatPane.getItems().add(BuildMsg(message, Color.LIGHTBLUE));
                        
                    });
                }
            }
        });

        
    }

    @FXML
    public void sendButtonAction(ActionEvent event) {
        String msg = messageInput.getText();
        if (messageInput != null && !messageInput.getText().isEmpty()) {
            // [API] here goes the send the send Function from ClientHandler
            addToChat(new MessageTo("123", "1234322", msg, "12/12/12", false));
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



    public synchronized void addToChat(MessageTo msg) {

        Task<HBox> othersMessages = new Task<HBox>() {
            @Override
            public HBox call() throws Exception {
                Image image = new Image(ResourceLoader.load("Images/logo.png").toString());
                ImageView profileImage = new ImageView(image);
                profileImage.setFitHeight(32);
                profileImage.setFitWidth(32);
                BubbledLabel bl6 = new BubbledLabel();
                bl6.setText(msg.getUuid_sender() + ": " + msg.getMessage());
                bl6.setBackground(new Background(new BackgroundFill(Color.rgb(96, 28, 190), CornerRadii.EMPTY, Insets.EMPTY)));  
                HBox x = new HBox();
                bl6.setBubbleSpec(BubbleSpec.FACE_LEFT_CENTER);
                x.getChildren().addAll(profileImage, bl6);
                return x;
            }
        };

        othersMessages.setOnSucceeded(event -> {
            chatPane.getItems().add(othersMessages.getValue());
        });

        Task<HBox> yourMessages = new Task<HBox>() {
            @Override
            public HBox call() throws Exception {
                Image image = new Image(ResourceLoader.load("Images/logo.png").toString());
                ImageView profileImage = new ImageView(image);
                profileImage.setFitHeight(32);
                profileImage.setFitWidth(32);

                BubbledLabel bl6 = new BubbledLabel();
                bl6.setText(msg.getMessage());
                
                bl6.setBackground(new Background(new BackgroundFill(Color.LIGHTGREEN,
                        null, null)));

                HBox x = new HBox();
                x.setMaxWidth(chatPane.getWidth() - 20);
                x.setAlignment(Pos.TOP_RIGHT);
                bl6.setBubbleSpec(BubbleSpec.FACE_RIGHT_CENTER);
                x.getChildren().addAll(bl6, profileImage);

                return x;
            }
        };
        yourMessages.setOnSucceeded(event -> chatPane.getItems().add(yourMessages.getValue()));

        if (msg.getUuid_sender().equals("1235255455")) {
            Thread t2 = new Thread(yourMessages);
            t2.setDaemon(true);
            t2.start();
        } else {
            Thread t = new Thread(othersMessages);
            t.setDaemon(true);
            t.start();
        }
    }

    public HBox BuildMsg(MessageTo msg, Color cl){
        Image image = new Image(ResourceLoader.load("Images/logo.png").toString());
        ImageView profileImage = new ImageView(image);
        profileImage.setFitHeight(32);
        profileImage.setFitWidth(32);
        BubbledLabel bl6 = new BubbledLabel();
        bl6.setText(msg.getUuid_sender() + ": " + msg.getMessage());
        bl6.setBackground(new Background(new BackgroundFill(cl, CornerRadii.EMPTY, Insets.EMPTY)));  
        HBox x = new HBox();
        bl6.setBubbleSpec(BubbleSpec.FACE_LEFT_CENTER);
        x.getChildren().addAll(profileImage, bl6);
        return x;
    }
}
