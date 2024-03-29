

import javafx.application.Application;
import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import javax.imageio.ImageIO;

import java.awt.image.BufferedImage;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Video extends Application {

    private ImageView remoteVideoView;

    @Override
    public void start(Stage primaryStage) {
        remoteVideoView = new ImageView();
        VBox root = new VBox(remoteVideoView);
        Scene scene = new Scene(root, 640, 480);

        primaryStage.setScene(scene);
        primaryStage.setTitle("Video Chat App");
        primaryStage.show();

        // Start video server
        new Thread(() -> {
            try {
                startVideoServer();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }).start();
    }

    private void startVideoServer() throws ClassNotFoundException {
        try (ServerSocket serverSocket = new ServerSocket(8080);ServerSocket serverSocketAudio = new ServerSocket(8081) ) {

            System.out.println("Waiting for client connection...");
            System.out.println("wait...");

            Socket clientSocket = serverSocket.accept();
            Socket clientSocketAudio = serverSocketAudio.accept();

            System.out.println("Client connected!");
             

            try (InputStream inner = clientSocket.getInputStream();InputStream innerAudio = clientSocketAudio.getInputStream(); ObjectInputStream in = new ObjectInputStream(inner)) {

                new ListenAudio(innerAudio).start();

                while (true) {
                    // Capture video frame from webcam
                    // BufferedImage image = webcam.getImage();
                    byte[] imageData = (byte[]) in.readObject();

                    // Convert byte array back to BufferedImage
                    ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(imageData);
                    BufferedImage image = ImageIO.read(byteArrayInputStream);
                    

                    // Convert BufferedImage to JavaFX Image and display
                    Image fxImage = SwingFXUtils.toFXImage(image, null);
                    Platform.runLater(() -> {
                        remoteVideoView.setImage(fxImage);
                    });
                    
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}