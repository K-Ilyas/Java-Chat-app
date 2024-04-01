package Client;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.Socket;

import Controller.MessagesContoller;
import model.FriendRequest;
import model.MessageTo;
import model.UserInformation;



public class ReciveNotification implements Runnable {

    private Socket soc = null;
    private boolean shouldRun = true;
    private MessagesContoller messagesContoller;


    public ReciveNotification(Socket soc) {
        this.soc = soc;
        this.messagesContoller  = null;
    }
    
    public void setMessagesContoller(MessagesContoller messagesContoller) {
        this.messagesContoller = messagesContoller;
    }

    public void run() {
        try (InputStream in = soc.getInputStream();
             ObjectInputStream ois = new ObjectInputStream(in);
             DataInputStream din = new DataInputStream(in)) {

            int status = 0;

            while (this.shouldRun) {
                System.out.println("Waiting for notification");
                status = din.readInt();
                if (status == 1) {
                    System.out.println("Notification received: message");
                    UserInformation sender = (UserInformation) ois.readObject();
                    System.out.println("Received message from " + sender.getUuid());
                    MessageTo message = (MessageTo) ois.readObject();
                    messagesContoller.setNotification(sender, message);
                    System.out.println("Received message: " + message);
                } else if (status == 2) {
                    System.out.println("Notification received: friend request");
                    UserInformation sender = (UserInformation) ois.readObject();
                    System.out.println("Received friend request from " + sender.getUuid());
                    FriendRequest friendRequest = (FriendRequest) ois.readObject();
                    System.out.println("Received friend request: " + friendRequest);
                }
            }

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void setShouldRun(boolean shouldRun) {
        synchronized (this) {
            this.shouldRun = shouldRun;
        }
    }




}