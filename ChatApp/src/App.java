import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import Controller.LoginContoller;
import UI.ResourceLoader;


public class App extends Application {

    @Override
    public void start(Stage stage) throws Exception {
		FXMLLoader loader = new FXMLLoader(ResourceLoader.loadURL("./Login.fxml"));
		loader.setControllerFactory(c -> new LoginContoller(stage));
		Parent root = loader.load();
        Scene scene = new Scene(root);
        stage.setTitle("Login");
        stage.setScene(scene);
        stage.setResizable(true);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}