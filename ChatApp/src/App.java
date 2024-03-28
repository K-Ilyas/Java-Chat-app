import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class App extends Application {

    @Override
    public void start(Stage stage) throws Exception {
      stage.getIcons().add(new Image("./images/icon.png"));
        // Parent root = FXMLLoader.load(getClass().getResource("src/UI/Rooms.fxml"));
        // Scene scene = new Scene(root);
        // stage.setTitle("Chat Registration");
        // stage.setScene(scene);
        // stage.setResizable(true);
        // stage.show();
        Parent root = FXMLLoader.load(getClass().getResource("./UI/Login.fxml"));
        Scene scene = new Scene(root);
        stage.setTitle("Chat Rooms");
        stage.setScene(scene);
        stage.setResizable(true);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}