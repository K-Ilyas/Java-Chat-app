import javafx.application.Application;
import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import com.github.sarxos.webcam.Webcam;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.ByteBuffer;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.TargetDataLine;
import javax.swing.ImageIcon;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Client extends Application {

    private ImageView remoteVideoView;
    private Webcam webcam;
    private Stage screenCaptureStage;

    private void createScreenCaptureStage(Stage primaryStage) {
        screenCaptureStage = new Stage();
        screenCaptureStage.initOwner(primaryStage);
        screenCaptureStage.setOpacity(0); // Set opacity to 0 to make it invisible
        screenCaptureStage.setFullScreen(true);
        screenCaptureStage.setAlwaysOnTop(true);
        screenCaptureStage.show();
    }

    @Override
    public void start(Stage primaryStage) {
        remoteVideoView = new ImageView();
        VBox root = new VBox(remoteVideoView);
        Scene scene = new Scene(root, 640, 480);

        primaryStage.setScene(scene);
        primaryStage.setTitle("Video Chat App");
        primaryStage.setOnCloseRequest(event -> {
            // Clean up resources when closing the application
            if (webcam != null && webcam.isOpen()) {
                webcam.close();
            }
        });

        screenCaptureStage = new Stage();
        screenCaptureStage.initOwner(primaryStage);
        screenCaptureStage.initStyle(StageStyle.TRANSPARENT);
        screenCaptureStage.setOpacity(0);
        
        primaryStage.show();


        // // Start webcam

        createScreenCaptureStage(primaryStage);

        webcam = Webcam.getDefault();
        webcam.setViewSize(webcam.getViewSizes()[0]);
        webcam.open();

        // Start video server
        new Thread(() -> {
            try {
                connectToServer(primaryStage);
            } catch (AWTException e) {
                e.printStackTrace();
            }
        }).start();

    }

    private void connectToServer(Stage primaryStage) throws AWTException {

        try (Socket socket = new Socket("localhost", 8080);
                Socket socketAudi = new Socket("localhost", 8081);
                OutputStream outerAudio = socketAudi.getOutputStream();
                OutputStream outer = socket.getOutputStream();
                ObjectOutputStream out = new ObjectOutputStream(outer)) {
            ImageIcon ic;
            BufferedImage br;

            new SendAudio(outerAudio).start();
            Robot robot = new Robot();
            while (true) {
                // br = webcam.getImage();

                // Image fxImage = SwingFXUtils.toFXImage(br, null);

                // // Update the UI on the JavaFX Application Thread
                // Platform.runLater(() -> {
                // remoteVideoView.setImage(fxImage);
                // });

                // Sleep for a short interval to control the capture rate
                // Convert BufferedImage to byte array
                // ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                // ImageIO.write(br, "png", byteArrayOutputStream);
                // byte[] imageData = byteArrayOutputStream.toByteArray();

                // Send byte array to client

                // share screen

                Rectangle2D screenBounds = Screen.getPrimary().getBounds();
                BufferedImage screenImage = robot
                        .createScreenCapture(new Rectangle((int) screenBounds.getMinX(), (int) screenBounds.getMinY(),
                                (int) screenBounds.getWidth(), (int) screenBounds.getHeight()));

                double widthScaleFactor = primaryStage.getWidth() / screenImage.getWidth();
                double heightScaleFactor = primaryStage.getHeight() / screenImage.getHeight();
                double scaleFactor = Math.min(widthScaleFactor, heightScaleFactor);

                // Scale down the screen image if necessary
                if (scaleFactor < 1.0) {
                    int newWidth = (int) (screenImage.getWidth() * scaleFactor);
                    int newHeight = (int) (screenImage.getHeight() * scaleFactor);
                    BufferedImage scaledImage = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_ARGB);
                    Graphics2D g2d = scaledImage.createGraphics();
                    g2d.drawImage(screenImage, 0, 0, newWidth, newHeight, null);
                    g2d.dispose();
                    screenImage = scaledImage;
                }

                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                ImageIO.write(screenImage, "png", byteArrayOutputStream);
                byte[] imageData = byteArrayOutputStream.toByteArray();

                // Convert BufferedImage to JavaFX Image and display
                // Image fxImage2 = SwingFXUtils.toFXImage(screenImage, null);
                // Platform.runLater(() -> remoteVideoView.setImage(fxImage2));

                out.writeObject(imageData);
                out.flush();

                try {
                    Thread.sleep(100); // Adjust the sleep duration as needed
                } catch (InterruptedException e) {
                    e.printStackTrace(); // Handle interruption
                }

                // // // Receive video frame from server
                // // byte[] imageData = new byte[socket.getReceiveBufferSize()];
                // // int bytesRead = inputStream.read(imageData);

                // // if (bytesRead != -1) {
                // // // Display received video
                // // BufferedImage receivedImage = ImageIO.read(new
                // // ByteArrayInputStream(imageData));
                // // remoteVideoView.setImage(SwingFXUtils.toFXImage(receivedImage, null));
                // // }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void showVideoFrame(BufferedImage image) {
        // Display received video in JavaFX UI
        if (image != null) {
            javafx.application.Platform.runLater(() -> {
                remoteVideoView.setImage(SwingFXUtils.toFXImage(image, null));
            });
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
