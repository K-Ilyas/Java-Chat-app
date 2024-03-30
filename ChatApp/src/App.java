import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import fr.brouillard.oss.cssfx.CSSFX;
import Controller.MainContoller;
import UI.ResourceLoader;
import io.github.palexdev.materialfx.css.themes.MFXThemeManager;
import io.github.palexdev.materialfx.css.themes.Themes;

public class App extends Application {

    // @Override
    // public void start(Stage stage) throws Exception {
    // //   stage.getIcons().add(new Image("./images/icon.png"));
    //     // Parent root = FXMLLoader.load(getClass().getResource("src/UI/Rooms.fxml"));
    //     // Scene scene = new Scene(root);
    //     // stage.setTitle("Chat Registration");
    //     // stage.setScene(scene);
    //     // stage.setResizable(true);
    //     // stage.show();
    //     Parent root = FXMLLoader.load(getClass().getResource("./UI/login.fxml"));
    //     Scene scene = new Scene(root);
    //     stage.setTitle("Chat Rooms");
    //     stage.setScene(scene);
    //     stage.setResizable(true);
    //     stage.show();
    // }


    @Override
	public void start(Stage primaryStage) throws Exception {
		CSSFX.start();

		FXMLLoader loader = new FXMLLoader(ResourceLoader.loadURL("Main.fxml"));
		loader.setControllerFactory(c -> new MainContoller(primaryStage));
		Parent root = loader.load();
		Scene scene = new Scene(root);
        System.out.println(Themes.DEFAULT);
        System.out.println(Themes.LEGACY);
		MFXThemeManager.addOn(scene, Themes.DEFAULT, Themes.LEGACY);
		scene.setFill(Color.TRANSPARENT);
		primaryStage.initStyle(StageStyle.TRANSPARENT);
		primaryStage.setScene(scene);
		primaryStage.setTitle("ChatApp");
		primaryStage.show();

		//ScenicView.show(scene);
	}

    // @Override
    // public void start(Stage stage) throws Exception {
    //     Parent root = FXMLLoader.load(getClass().getResource("login.fxml"));
    //     Scene scene = new Scene(root);
    //     stage.setScene(scene);
    //     stage.show();
    // }
        

    public static void main(String[] args) {
        launch(args);
    }
}