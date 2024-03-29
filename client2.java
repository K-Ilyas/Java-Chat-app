import javafx.application.Application;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import com.github.sarxos.webcam.Webcam;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.nio.ByteBuffer;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

public class Client2 extends Application {

    private ImageView remoteVideoView;
    private Webcam webcam;

    @Override
    public void start(Stage primaryStage) {
        remoteVideoView = new ImageView();
        VBox root = new VBox(remoteVideoView);
        Scene scene = new Scene(root, 640, 480);

        primaryStage.setScene(scene);
        primaryStage.setTitle("Video Chat Client");
        primaryStage.show();

        primaryStage.setOnCloseRequest(event -> {
            // Clean up resources when closing the application
            if (webcam != null && webcam.isOpen()) {
                webcam.close();
            }
        });
        primaryStage.show();

        // // Start webcam
        webcam = Webcam.getDefault();
        webcam.setViewSize(webcam.getViewSizes()[0]);
        webcam.open();
        // Connect to server and start receiving video
        connectToServer();
    }

    private void connectToServer() {

      
        try (Socket socket = new Socket("localhost", 8080);
        ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream())) {
        ImageIcon ic;
        BufferedImage br;

        Webcam cam = Webcam.getDefault();
        cam.open();

        while (true) {

        br = cam.getImage();
        showVideoFrame(br);
        BufferedImage image = br; // Your method to capture the image
        // Convert BufferedImage to byte array
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ImageIO.write(image, "png", byteArrayOutputStream);
        byte[] imageData = byteArrayOutputStream.toByteArray();

        // Send byte array to client
        out.writeObject(imageData);
        out.flush();

        // // // Receive video frame from server
        // // byte[] imageData = new byte[socket.getReceiveBufferSize()];
        // // int bytesRead = inputStream.read(imageData);

        // // if (bytesRead != -1) {
        // // // Display received video
        // // BufferedImage receivedImage = ImageIO.read(new
        // // ByteArrayInputStream(imageData));
        // // remoteVideoView.setImage(SwingFXUtils.toFXImage(receivedImage, null));
        // // }

        // }
        }catch  (IOException e) {
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


import com.github.sarxos.webcam.Webcam;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import java.awt.image.BufferedImage;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Video extends Application {

    private ImageView remoteVideoView;
    private Webcam webcam;

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
        primaryStage.show();

        // // Start webcam
        // webcam = Webcam.getDefault();
        // webcam.setViewSize(webcam.getViewSizes()[0]);
        // webcam.open();

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
        try (ServerSocket serverSocket = new ServerSocket(8080)) {
            System.out.println("Waiting for client connection...");
            System.out.println("wait...");

            Socket clientSocket = serverSocket.accept();
            System.out.println("Client connected!");

            ImageIcon ic;

            try (ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream())) {
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
                    // // Send video frame to client
                    // ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    // ImageIO.write(image, "jpg", byteArrayOutputStream);
                    // byte[] imageData = byteArrayOutputStream.toByteArray();
                    // remoteVideoView.setImage(SwingFXUtils.toFXImage(image, null));
                    // outputStream.write(imageData);
                    // outputStream.flush();

                    // Display received video from client
                    // BufferedImage receivedImage = ImageIO.read(inputStream);
                    // Platform.runLater(() -> {
                    // remoteVideoView.setImage(SwingFXUtils.toFXImage(receivedImage, null));
                    // });
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


Rectangle screenRect = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
Robot robot = new Robot();
BufferedImage screenImage = robot.createScreenCapture(screenRect);


ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
ImageIO.write(screenImage, "png", byteArrayOutputStream);
byte[] imageData = byteArrayOutputStream.toByteArray();

// Convert BufferedImage to JavaFX Image and display
int maxWidth = 800; // Maximum width of the displayed screen content
int maxHeight = 600; // Maximum height of the displayed screen content
double scaleX = 1.0;
double scaleY = 1.0;
// ...
if (screenImage.getWidth() > maxWidth || screenImage.getHeight() > maxHeight) {
    scaleX = (double) maxWidth / screenImage.getWidth();
    scaleY = (double) maxHeight / screenImage.getHeight();
}

int newWidth = (int) (screenImage.getWidth() * scaleX);
int newHeight = (int) (screenImage.getHeight() * scaleY);

Image fxImage2 = SwingFXUtils.toFXImage( (BufferedImage) screenImage.getScaledInstance(newWidth, newHeight, java.awt.Image.SCALE_SMOOTH), null);
Platform.runLater(() -> remoteVideoView.setImage(fxImage2));
out.writeObject(imageData);
out.flush();
