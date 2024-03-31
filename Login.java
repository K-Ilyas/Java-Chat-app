import java.util.LinkedList;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import javafx.scene.layout.GridPane;

import javafx.stage.Stage;


import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.LinkedList;
import java.util.Scanner;

import classes.UserInformation;
import connection.MySQLConnectSingleton;
import dao.UserDAO;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

public class Login extends Application{
    UserDAO user_dao = new UserDAO(MySQLConnectSingleton.getInstance());
    UserInformation user = null;
   
    Home chatApp = new Home();
    @Override
    public void start(Stage primaryStage) {
        ClientHandler.initSocker();

        primaryStage.setTitle("Login Panel");
        
        


        // Create a grid pane for the login form
        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(10));
        gridPane.setVgap(8);
        gridPane.setHgap(10);
        
        // Add username label and text field
        Label usernameLabel = new Label("Username:");
        GridPane.setConstraints(usernameLabel, 0, 0);
        TextField usernameInput = new TextField();
        GridPane.setConstraints(usernameInput, 1, 0);
        
        // Add password label and password field
        Label passwordLabel = new Label("Password:");
        GridPane.setConstraints(passwordLabel, 0, 1);
        PasswordField passwordInput = new PasswordField();
        GridPane.setConstraints(passwordInput, 1, 1);
        
        // Add login button
        Button loginButton = new Button("Login");
        GridPane.setConstraints(loginButton, 1, 2);
        
        // Add event handler to handle login button click


        loginButton.setOnAction(e -> {
            String username = usernameInput.getText();
            String password = passwordInput.getText();
            user = ClientHandler.getClientSocket().login(username, password);
            System.out.println(user);
            if (user != null) {
                ClientHandler.setLoggedInUser(user);
                chatApp.start(primaryStage);
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Login Failed");
                alert.setHeaderText(null);
                alert.setContentText("Invalid username or password");
                alert.showAndWait();
            }
        });

        Button registerButton = new Button("Register");
        // place register button on the right to login button
        GridPane.setConstraints(registerButton, 0, 2);

        registerButton.setOnAction(e -> {
            Register register = new Register();
            register.start(primaryStage);
        });
        

      gridPane.getChildren().addAll(usernameLabel, usernameInput, passwordLabel, passwordInput, loginButton, registerButton);

      Scene scene = new Scene(gridPane, 800, 600);
      primaryStage.setScene(scene);
      primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }


}